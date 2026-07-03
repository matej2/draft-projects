from datetime import timedelta
from typing import Annotated

from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from starlette import status

from config.auth import bcrypt_context
from domain.dto.Auth import CreateUserRequest, Token
from domain.model.User import User
from service.DatabaseService import get_db
from util.Auth import authenticate_user, create_access_token

router = APIRouter(
    prefix="/auth",
    tags=["auth"]
)
db_dependency = Annotated[Session, Depends(get_db)]
form_dependency = Annotated[OAuth2PasswordRequestForm, Depends()]

@router.post("/", status_code=status.HTTP_201_CREATED)
async def create_user(
        db: db_dependency,
        create_user_request: CreateUserRequest):
    create_user_request = User(
        username = create_user_request.username,
        hashed_password=bcrypt_context.hash(create_user_request.password)
    )

    db.add(create_user_request)
    db.commit()


@router.post("/token", response_model=Token)
async def login_for_access_token(
        form_data: form_dependency,
        db: db_dependency):
    user = authenticate_user(form_data.username, form_data.password, db)

    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Incorrect username or password")
    token = create_access_token(user.username, user.id, timedelta(minutes=20))

    return {
        "access_token": token,
        "token_type": "bearer",
    }

    