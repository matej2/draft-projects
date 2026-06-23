import os
from urllib import parse

import requests
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI
from starlette.responses import RedirectResponse

load_dotenv()

CLIENT_ID = os.getenv("CLIENT_ID")
CLIENT_SECRET = os.getenv("CLIENT_SECRET")
AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
TOKEN_URN = "https://oauth2.googleapis.com/token"
SCOPES = ["https://www.google.com/m8/feeds/"]
REDIRECT_URL = "http://localhost:5000/code/callback"
PROMPT = "consent"
STATE = "shfjoifspeirf"
RESPONSE_TYPE = "code"
app = FastAPI()

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
        "prompt": PROMPT,
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

    print(response.text)


if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=5000)


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
