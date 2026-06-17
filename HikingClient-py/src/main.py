import os

import httpx
import msal
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException

load_dotenv()

app = FastAPI(title="OneDrive FastAPI")

# --- Config ---
CLIENT_ID  = os.getenv("CLIENT_ID")
TENANT_ID  = "consumers"
AUTHORITY  = f"https://login.microsoftonline.com/{TENANT_ID}"
SCOPES     = ["Files.Read"]
CACHE_FILE = "token_cache.bin"
API_BASE   = "https://graph.microsoft.com/v1.0"


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

def get_msal_app(cache: msal.SerializableTokenCache) -> msal.PublicClientApplication:
    return msal.PublicClientApplication(
        CLIENT_ID,
        authority=AUTHORITY,
        token_cache=cache,
    )


# --- Token ---
def get_token_silent(cache, msal_app) -> str | None:
    accounts = msal_app.get_accounts()
    if not accounts:
        return None

    result = msal_app.acquire_token_silent(scopes=SCOPES, account=accounts[0])
    if result and "access_token" in result:
        save_cache(cache)
        return result["access_token"]

    return None


# --- Endpoints ---

@app.get("/login")
def login():
    """
    Call this first. Follow the printed instructions in the terminal
    to complete device code login. Only needed once — cache handles the rest.
    """
    cache    = load_cache()
    msal_app = get_msal_app(cache)

    # Return cached token if available
    token = get_token_silent(cache, msal_app)
    if token:
        return {"message": "✅ Already authenticated via cache", "access_token": token}

    # Start Device Code Flow
    flow = msal_app.initiate_device_flow(scopes=SCOPES)
    if "message" not in flow:
        raise HTTPException(status_code=500, detail="Failed to initiate device flow")

    print("\n" + flow["message"] + "\n", flush=True)  # Print code to terminal

    result = msal_app.acquire_token_by_device_flow(flow)  # Blocks until user logs in

    if "access_token" not in result:
        raise HTTPException(
            status_code=401,
            detail=f"Auth failed: {result.get('error')} — {result.get('error_description')}"
        )

    save_cache(cache)
    return {"message": "✅ Login successful", "access_token": result["access_token"]}


@app.get("/token")
def get_token():
    """Returns a valid token from cache, no user interaction."""
    cache    = load_cache()
    msal_app = get_msal_app(cache)

    token = get_token_silent(cache, msal_app)
    if not token:
        raise HTTPException(
            status_code=401,
            detail="No cached token. Call /login first."
        )

    return {"access_token": token}


@app.get("/images")
async def get_images():
    """Fetch all images from a OneDrive album (folder) by name."""
    cache    = load_cache()
    msal_app = get_msal_app(cache)

    token = get_token_silent(cache, msal_app)
    if not token:
        raise HTTPException(
            status_code=401,
            detail="Not authenticated. Call /login first."
        )

    headers = {"Authorization": f"Bearer {token}"}
    url     = f"{API_BASE}/me/drive/root:/Pictures/_Hiking:/children"

    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers)

        if not response.is_success:
            raise HTTPException(
                status_code=response.status_code,
                detail=f"Graph API error: {response.text}"
            )

        items  = response.json().get("value", [])
        images = [
            {
                "name":         item["name"],
                "id":           item["id"],
                "download_url": item.get("@microsoft.graph.downloadUrl"),
                "location": item["location"]
            }
            for item in items
            if item.get("file", {}).get("mimeType", "").startswith("image/")
        ]

    return { "count": len(images), "images": images}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)