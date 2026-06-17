import os

import httpx
import msal
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import RedirectResponse, HTMLResponse
from fastapi.security import OAuth2AuthorizationCodeBearer

from model.Photo import Photo

load_dotenv()

CLIENT_ID     = os.getenv("CLIENT_ID")
CLIENT_SECRET = os.getenv("CLIENT_SECRET")  # Needed for auth code flow
TENANT_ID     = "consumers"
AUTHORITY     = f"https://login.microsoftonline.com/{TENANT_ID}"
REDIRECT_URI  = "http://localhost:8000/callback"
SCOPES        = ["Files.Read"]
CACHE_FILE    = "token_cache.bin"
API_BASE      = "https://graph.microsoft.com/v1.0"

app = FastAPI(
    title="OneDrive API",
    swagger_ui_init_oauth={
        "clientId": CLIENT_ID,
        "scopes": "Files.Read",
        "usePkceWithAuthorizationCodeGrant": True,
    }
)

# OAuth2 scheme — this is what makes Swagger show the Authorize button
oauth2_scheme = OAuth2AuthorizationCodeBearer(
    authorizationUrl=f"https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize",
    tokenUrl=f"https://login.microsoftonline.com/consumers/oauth2/v2.0/token",
    scopes={"Files.Read": "Read OneDrive files"},
)

# --- Cache ---
def load_cache() -> msal.SerializableTokenCache:
    cache = msal.SerializableTokenCache()
    if os.path.exists(CACHE_FILE):
        with open(CACHE_FILE, "r") as f:
            cache.deserialize(f.read())
    return cache

def save_cache(cache: msal.SerializableTokenCache):
    with open(CACHE_FILE, "w") as f:
        f.write(cache.serialize())

def get_msal_app(cache: msal.SerializableTokenCache) -> msal.ConfidentialClientApplication:
    return msal.ConfidentialClientApplication(
        CLIENT_ID,
        authority=AUTHORITY,
        client_credential=CLIENT_SECRET,
        token_cache=cache,
    )

# --- Auth flow storage (in-memory, fine for single user) ---
auth_flow_store = {}

@app.get("/login")
def login():
    cache    = load_cache()
    msal_app = get_msal_app(cache)
    accounts = msal_app.get_accounts()

    # ✅ Return cached token if available, skip login entirely
    if accounts:
        result = msal_app.acquire_token_silent(scopes=SCOPES, account=accounts[0])
        if result and "access_token" in result:
            save_cache(cache)
            return result.get("access_token")

    # No cache — start auth code flow
    flow = msal_app.initiate_auth_code_flow(
        scopes=SCOPES,
        redirect_uri=REDIRECT_URI,
    )
    auth_flow_store["flow"] = flow
    return RedirectResponse(flow["auth_uri"])

@app.get("/callback")
async def callback(request: Request):
    """Microsoft redirects here after login. Saves token to cache."""
    cache    = load_cache()
    msal_app = get_msal_app(cache)

    flow = auth_flow_store.get("flow")
    if not flow:
        raise HTTPException(status_code=400, detail="No auth flow found. Call /login first.")

    result = msal_app.acquire_token_by_auth_code_flow(
        flow,
        dict(request.query_params)
    )

    if "access_token" not in result:
        raise HTTPException(
            status_code=401,
            detail=f"Auth failed: {result.get('error_description')}"
        )
    save_cache(cache)

    return result["access_token"]


def get_token_from_cache() -> str:
    """Get valid token from cache, raise if missing."""
    cache    = load_cache()
    msal_app = get_msal_app(cache)
    accounts = msal_app.get_accounts()

    if not accounts:
        raise HTTPException(status_code=401, detail="Not authenticated. Visit /login first.")

    result = msal_app.acquire_token_silent(scopes=SCOPES, account=accounts[0])
    if not result or "access_token" not in result:
        raise HTTPException(status_code=401, detail="Token expired. Visit /login again.")

    save_cache(cache)
    return result["access_token"]


@app.get("/images")
async def get_album_images():
    token   = get_token_from_cache()
    headers = {"Authorization": f"Bearer {token}"}
    url     = f"{API_BASE}/me/drive/root:/Pictures/_Hiking:/children"

    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers)
        if not response.is_success:
            raise HTTPException(status_code=response.status_code, detail=f"Graph API error: {response.text}")

        items = response.json().get("value", [])
        return {
            "count": len(items),
            "images": [
                Photo(
                    item["id"],
                    item.get("createdBy", {}).get("user", {}).get("displayName"),
                    item.get("location", {}).get("latitude"),
                    item.get("location", {}).get("longitude")
                )
                for item in items
                if item.get("file", {}).get("mimeType", "").startswith("image/")
            ]
        }


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)