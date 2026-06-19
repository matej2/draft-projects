# This is a sample Python script.
import atexit
import os
import time

import gpxpy
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, UploadFile, File, HTTPException
from starlette.responses import RedirectResponse
from stravalib import Client

from model.Point import Point
from service.CacheService import CacheService

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

BASE_URL = "http://0.0.0.0:5000"

load_dotenv()

MONGO_URI = str(os.getenv("MONGO_URI"))
MONGO_BASE_URL = str(os.getenv("MONGO_BASE_URL"))
app = FastAPI(title="GPX Client", description=f"[Login to strava]({BASE_URL}/strava/login)")
STRAVA_CLIENT_ID     = int(str(os.getenv("STRAVA_CLIENT_ID")))
STRAVA_CLIENT_SECRET = str(os.getenv("STRAVA_CLIENT_SECRET"))
STRAVA_ACCESS_TOKEN  = str(os.getenv("STRAVA_ACCESS_TOKEN"))
STRAVA_REDIRECT_URI  = f"{BASE_URL}/strava/callback"
STRAVA_TOKEN_FILE    = "../strava_tokens.json"

cache = CacheService(MONGO_URI)

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


def get_strava_token(code: str):
    client = Client()
    token_response = client.exchange_code_for_token(client_id=STRAVA_CLIENT_ID,
                                                    client_secret=STRAVA_CLIENT_SECRET,
                                                    code=code)
    cache.save_access_info(token_response)

@app.get("/strava/login", include_in_schema=False)
def strava_login():
    tokens = cache.load_access_info()
    if tokens and tokens.get("expires_at", 0) > time.time():
        return RedirectResponse("/docs")

    client = Client()
    url = client.authorization_url(
        client_id=STRAVA_CLIENT_ID,
        redirect_uri=f"{BASE_URL}/strava/callback",
        scope=["activity:read_all"]
    )
    return RedirectResponse(url)


@app.get("/strava/callback", include_in_schema=False)
def strava_callback(code: str):
    get_strava_token(code)
    return RedirectResponse("/docs")

@app.get("/strava/activities")
def strava_activities():
    acc_info = cache.load_access_info()
    if not acc_info:
        raise HTTPException(status_code=403, detail="Unauthorized. Call /strava/login")

    client = Client(
        access_token=acc_info.get("access_token"),
        refresh_token=acc_info.get("refresh_token"),
        token_expires=acc_info.get("expires_at", 0))
    return [a for a in client.get_activities()]


def exit_handler():
    print('My application is ending!')
    cache.client.close()


atexit.register(exit_handler)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

