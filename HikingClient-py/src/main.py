import os

import httpx
import msal
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.responses import RedirectResponse
from starlette.requests import Request

app = FastAPI(title="OneDrive FastAPI Integracija")
load_dotenv()

CLIENT_ID = os.getenv("CLIENT_ID")
CLIENT_SECRET = os.getenv("CLIENT_SECRET")
TENANT_ID = "common"
REDIRECT_URI = "http://localhost:8000/callback"

AUTHORITY = f"https://login.microsoftonline.com/{TENANT_ID}"
SCOPES = ["Files.Read"]
STATE = "Test"
CODE_CHALLENGE = "Challenge"

TOKEN_CACHE = {}


global auth_flow

def get_msal_app():
    return msal.ConfidentialClientApplication(
        client_id=CLIENT_ID,
        client_credential=CLIENT_SECRET,
        authority=AUTHORITY
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


@app.get("/images")
async def get_onedrive_images(folder_name: str = "Igre"):
    token = TOKEN_CACHE.get("access_token")
    if not token:
        return RedirectResponse(url="/login")

    headers = {"Authorization": f"Bearer {token}"}

    async with httpx.AsyncClient() as client:
        url = f"https://microsoft.com/{folder_name}:/children"

        response = await client.get(url, headers=headers)

        if response.status_code != 200:
            raise HTTPException(status_code=response.status_code, detail=response.json())

        data = response.json()
        items = data.get("value", [])

        images = []
        for item in items:
            if "image" in item:  # Graph API označi slikovne datoteke z objektom "image"
                images.append({
                    "id": item.get("id"),
                    "name": item.get("name"),
                    "download_url": item.get("@microsoft.graph.downloadUrl"),  # Direktna povezava do slike
                    "created_date": item.get("createdDateTime")
                })

        return {"folder": folder_name, "images_count": len(images), "images": images}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)