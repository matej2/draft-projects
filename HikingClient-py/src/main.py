import json
import os
from typing import Annotated

import httpx
import jwt
import msal
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException, Header
from fastapi.responses import RedirectResponse
from starlette.requests import Request

app = FastAPI(title="OneDrive FastAPI Integracija")
load_dotenv()

CLIENT_ID = os.getenv("CLIENT_ID")
CLIENT_SECRET = os.getenv("CLIENT_SECRET")
TENANT_ID = "consumers"
REDIRECT_URI = "http://localhost:8000/callback"

AUTHORITY = f"https://login.microsoftonline.com/{TENANT_ID}"
SCOPES = ["Files.Read"]
MSAL_SCOPE = ["https://graph.microsoft.com/.default"]
STATE = "Test"
CODE_CHALLENGE = "Challenge"
CACHE_FILE    = "token_cache.bin"
API_BASE_URL = "https://graph.microsoft.com/v1.0"
TOKEN_CACHE = {}
USER = "matej.jurko@gmail.com"


global auth_flow

def load_cache():
    cache = msal.SerializableTokenCache()
    if os.path.exists(CACHE_FILE):
        with open(CACHE_FILE, "r") as f:
            cache.deserialize(f.read())
    return cache

def save_cache(cache):
    #if not cache.has_state_changed:
    with open(CACHE_FILE, "w") as f:
        f.write(cache.serialize())

def get_msal_app(cache):
    return msal.PublicClientApplication(
        CLIENT_ID,
        authority=AUTHORITY,
        token_cache=cache
    )

@app.get("/login")
def login():
    msal_app = get_msal_app()
    flow = msal_app.initiate_auth_code_flow(
        scopes=SCOPES,
        redirect_uri=REDIRECT_URI
    )
    global auth_flow
    auth_flow = flow

    #auth_url = msal_app.get_authorization_request_url(SCOPES, redirect_uri=REDIRECT_URI, state=STATE)
    return RedirectResponse(flow["auth_uri"])


@app.get("/callback")
async def callback(request: Request, code: str = None, error: str = None, ):
    if error:
        raise HTTPException(status_code=400, detail=f"Prijava preklicana: {error}")

    if not code:
        raise HTTPException(status_code=400, detail="Avtorizacijska koda manjka.")

    msal_app = get_msal_app()

    global auth_flow

    auth_result = msal_app.acquire_token_by_auth_code_flow(
        auth_flow,
        {**request.query_params}
    )

    if "error" in auth_result:
        raise HTTPException(status_code=400, detail=auth_result.get("error_description"))

    TOKEN_CACHE["access_token"] = auth_result.get("access_token")

    return {"message": "Avtentikacija uspešna! Sedaj lahko pokličete /images"}



@app.get("/cache")
async def cache():
    cache = load_cache()

    msal_app = get_msal_app(cache)

    accounts = msal_app.get_accounts()

    if accounts:
        result = msal_app.acquire_token_silent(
            scopes=MSAL_SCOPE,
            account=accounts[0]
        )

        if result and "access_token" in result:
            save_cache(cache)
            print("✅ Authenticated silently from cache")
            return result["access_token"]

    print("⚠️  No cached token found. Starting Device Code Flow...")
    flow = msal_app.initiate_device_flow(scopes=SCOPES)
    print(flow["message"], flush=True)
    result = msal_app.acquire_token_by_device_flow(flow)

    if "access_token" in result:
        access_token = result["access_token"]
    else:
        raise Exception(
            f"Auth failed: {result.get('error')} — {result.get('error_description')}"
        )
    save_cache(cache)
    return result["access_token"]


def extract_token_safe(header: str) -> str:
    if not header:
        raise ValueError("Glava Authorization je prazna ali manjka.")

    parts = header.split(' ', 1)

    # PREVERJANJE: Če split vrne samo 1 element, pomeni, da ni presledka
    if len(parts) < 2:
        raise ValueError("Napačen format. Manjka tip avtentikacije (npr. 'Bearer') ali žeton.")

    token_type, token = parts

    if token_type.lower() != 'bearer':
        raise ValueError("Dovoljen je le 'Bearer' tip avtentikacije.")

    return token

def debug_token(token: str):
    # Decode without verification just to inspect claims
    decoded = jwt.decode(token, options={"verify_signature": False})
    print("Token scopes:", decoded.get("scp"))
    print("Token audience:", decoded.get("aud"))
    print("Token user:", decoded.get("upn") or decoded.get("preferred_username"))
    print("Token expiry:", decoded.get("exp"))

@app.get("/images")
async def get_onedrive_images(authorization: Annotated[str | None, Header()] = None):
    folder_name = "Documents"
    token = extract_token_safe(authorization)
    debug_token(token)

    if not authorization:
        raise Exception("Token missing")

    headers = {"Authorization": authorization}

    async with httpx.AsyncClient() as client:
        #url = f"https://graph.microsoft.com/v1.0/me/drive/root:/{folder_name}:/children?token={authorization}"
        #url = f"{API_BASE_URL}/me/drive/root/children"
        url = f"{API_BASE_URL}/me/drive/root:/Pictures:/children"
        #url = f"{API_BASE_URL}/drives/CDA366F995D7070B/root/children"
        #url = f"{API_BASE_URL}/me/albums"

        response = await client.get(url, headers=headers)
        print(json.dumps(json.loads(response.text), indent=2))
        response.raise_for_status()

        items = response.json().get("value", [])

        images = []
        for item in items:
            mime = item.get("file", {}).get("mimeType", "")
            if mime.startswith("image/"):
                images.append({
                    "name": item["name"],
                    "id": item["id"],
                    "download_url": item.get("@microsoft.graph.downloadUrl"),
                    "size": item.get("size"),
                    "mime_type": mime,
                })

        return images


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)