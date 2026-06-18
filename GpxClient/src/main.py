# This is a sample Python script.
import json
import os
import time

import gpxpy
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, UploadFile, File
from starlette.responses import RedirectResponse
from stravalib import Client
from stravalib.protocol import AccessInfo

from model.Point import Point

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

load_dotenv()

app = FastAPI(title="GPX Client", description="[Login to strava](http://127.0.0.1:8000/strava/login)")
STRAVA_CLIENT_ID     = int(str(os.getenv("STRAVA_CLIENT_ID")))
STRAVA_CLIENT_SECRET = str(os.getenv("STRAVA_CLIENT_SECRET"))
STRAVA_ACCESS_TOKEN  = str(os.getenv("STRAVA_ACCESS_TOKEN"))
STRAVA_REDIRECT_URI  = "http://localhost:8000/strava/callback"
STRAVA_TOKEN_FILE    = "../strava_tokens.json"


@app.post("/gpx/process")
async def process_gpx(file: UploadFile = File(...)):
    contents = await file.read()
    gpx = gpxpy.parse(contents)

    for track in gpx.tracks:
        for segment in track.segments:
            return [
                Point(point.latitude, point.longitude, point.elevation)
                for point in segment.points
            ]
    return []


def load_strava_access_info() -> AccessInfo:
    json_content = {}
    if os.path.exists(STRAVA_TOKEN_FILE):
        with open(STRAVA_TOKEN_FILE, "r") as f:
            json_content = json.load(f)
    return AccessInfo(**json_content)

def save_strava_access_info(access_info: AccessInfo):
    with open(STRAVA_TOKEN_FILE, "w") as f:
        json.dump(access_info, f)

def get_strava_token(code: str):
    client = Client()
    token_response = client.exchange_code_for_token(client_id=STRAVA_CLIENT_ID,
                                                    client_secret=STRAVA_CLIENT_SECRET,
                                                    code=code)
    save_strava_access_info(token_response)

@app.get("/strava/login", include_in_schema=False)
def strava_login():
    """Redirect to Strava login — only needed once."""
    tokens = load_strava_access_info()
    if tokens and tokens.get("expires_at", 0) > time.time():
        return RedirectResponse("/docs")

    client = Client()
    url = client.authorization_url(client_id=STRAVA_CLIENT_ID,
                             redirect_uri="http://127.0.0.1:8000/strava/callback")
    return RedirectResponse(url)


@app.get("/strava/callback", include_in_schema=False)
def strava_callback(code: str):
    get_strava_token(code)
    return RedirectResponse("/docs")

@app.get("/strava/activities")
def strava_activities():
    acc_info = load_strava_access_info()
    client = Client(access_token=acc_info.get("access_token"), refresh_token=acc_info.get("refresh_token"), token_expires=acc_info.get("expires_at", 0))
    return [a for a in client.get_activities()]

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)

