import json
import os
from typing import Annotated
from urllib import parse

import requests
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException, Depends
from fastapi.security import OAuth2AuthorizationCodeBearer
from starlette import status
from starlette.responses import RedirectResponse

load_dotenv()

CLIENT_ID = os.getenv("CLIENT_ID")
CLIENT_SECRET = os.getenv("CLIENT_SECRET")
API_KEY = os.getenv("API_KEY")
AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
TOKEN_URN = "https://oauth2.googleapis.com/token"
SCOPES = ["https://www.google.com/m8/feeds/"]
REDIRECT_URL = "http://localhost:5000/code/callback"
PROMPT = "consent"
STATE = "shfjoifspeirf"
RESPONSE_TYPE = "code"

API_URL = "https://people.googleapis.com/v1/people/me/connections"
app = FastAPI(
    title="Google Auth v Swaggerju",
    # Nastavitev Googlovih avtorizacijskih URL-jev za Swagger UI
    swagger_ui_oauth2_redirect_url="/docs/oauth2-redirect",
    swagger_ui_init_oauth={
        "clientId": CLIENT_ID,
        "clientSecret": CLIENT_SECRET,
        "appName": "Moja FastAPI Aplikacija",
        "scopes": SCOPES,
        "usePkceWithAuthorizationCodeGrant": True
    },
    swagger_ui_parameters={"tryItOutEnabled": True}
)



oauth2_scheme = OAuth2AuthorizationCodeBearer(
    authorizationUrl=AUTH_URL,
    tokenUrl=TOKEN_URN,
    scopes={
        SCOPES[0]: SCOPES[0],
    }
)

# Pomožna funkcija (Dependency), ki bo varovala vaše endpointe
async def get_google_token(token: Annotated[str, Depends(oauth2_scheme)]):
    if not token:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Žeton manjka ali je neveljaven",
        )
    return token


@app.get("/login")
def process_gpx():
    # https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground&prompt=consent&response_type=code&client_id=407408718192.apps.googleusercontent.com&scope=https%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F&access_type=offline

    # https://accounts.google.com/o/oauth2/v2/auth?client_id=407408718192.apps.googleusercontent.com&scopes=https%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F&redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground&response_type=code&state=shfjoifspeirf&prompt=consent&access_type=offline
    params = {
        "client_id": CLIENT_ID,
        "scope": " ".join(SCOPES),
        "redirect_uri": REDIRECT_URL,
        "response_type": RESPONSE_TYPE,
        "state": STATE,
        "access_type": "offline"
    }

    url = f"{AUTH_URL}?{parse.urlencode(params)}"
    print(url)

    return RedirectResponse(url)


@app.get("/code/callback")
def process_gpx(code: str):

    params = {
        "client_id": CLIENT_ID,
        "client_secret": CLIENT_SECRET,
        "code": code,
        "grant_type": "authorization_code",
        "redirect_uri": REDIRECT_URL,
    }

    url = f"{TOKEN_URN}?{parse.urlencode(params)}"
    print(url)

    response = requests.post(url)
    return response.json()

def get_token_direct():
    params = {
        "client_id": CLIENT_ID,
        "client_secret": CLIENT_SECRET,
        "response_type": "token",
    }

    url = f"{TOKEN_URN}?{parse.urlencode(params)}"
    response = requests.post(url)
    print(response.json())


@app.get("/people")
def get_people(token: Annotated[str, Depends(get_google_token)]):

    params = {
        "personFields": "names",

    }

    url = f"{API_URL}?{parse.urlencode(params)}"

    return requests.get(url, headers={"Authorization": f"Bearer {token}"}).json()

@app.get("/calendar")
def get_people():

    params = {
        "key": API_KEY,
    }

    url = f"https://www.googleapis.com/calendar/v3/calendars/mtj510@gmail.com/events?{parse.urlencode(params)}"

    return requests.get(url).json()



if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=5000)


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
