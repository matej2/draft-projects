from datetime import timedelta, datetime, UTC
from typing import Annotated

import bcrypt
from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordRequestForm
from jose import jwt, JWTError
from sqlalchemy.orm import Session
from starlette import status

from auth.auth import CreateUserRequest, get_db, bcrypt_context, Token, authenticate_user, SECRET_KEY, ALGORITHM, \
    oauth2_bearer
from models import User

router = APIRouter(
    prefix="/auth",
    tags=["auth"]
)
db_dependency = Annotated[Session, Depends(get_db)]

@router.post("/", status_code=status.HTTP_201_CREATED)
async def create_user(
        db: db_dependency,
        create_user_request: CreateUserRequest):
    create_user_request = User(
        username = create_user_request.username,
        hashed_password = bcrypt_context.hash(create_user_request.password[:72]),
    )

    db.add(create_user_request)
    db.commit()


def create_access_token(username: str, user_id: str, expires_after: timedelta):
    encode = {"sub": username, "id": user_id}
    expires_after = datetime.now(UTC) + expires_after
    encode.update({"exp": str(int(expires_after.timestamp()))})
    return jwt.encode(encode, SECRET_KEY, algorithm=ALGORITHM)

async def get_current_user(token: Annotated[str, Depends(oauth2_bearer)]):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub", "")
        user_id: str = payload.get("id", "")

        if username == "" or user_id == "":
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Could not validate user"
            )
        return {
            "username": username,
            "id": user_id
        }
    except JWTError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Could not validate jwt",
        )



@router.post("/token", response_model=Token)
async def login_for_access_token(
        form_data: Annotated[OAuth2PasswordRequestForm, Depends()],
        db: db_dependency):
    user = authenticate_user(form_data.username, form_data.password, db)

    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Incorrect username or password")
    token = create_access_token(user.username, user.id, timedelta(minutes=20))

    return {
        "access_token": token,
        "token_type": "bearer",
    }

    