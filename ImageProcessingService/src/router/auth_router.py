from datetime import timedelta

from fastapi import APIRouter, HTTPException
from starlette import status

from config.auth import bcrypt_context
from domain.dto.Auth import CreateUserRequest
from domain.model.User import User
from domain.dto.Response import TokenResponse, UserResponse
from router.common_dependencies import db_dependency, form_dependency
from util.Auth import authenticate_user, create_access_token

auth_router = APIRouter(
    tags=["auth"]
)

@auth_router.post("/", status_code=status.HTTP_201_CREATED)
async def create_user(
        db: db_dependency,
        create_user_request: CreateUserRequest):
    create_user_request = User(
        username = create_user_request.username,
        hashed_password=bcrypt_context.hash(create_user_request.password)
    )

    db.add(create_user_request)
    db.commit()


@auth_router.post("/login", response_model=TokenResponse)
async def login_for_access_token(
        form_data: form_dependency,
        db: db_dependency):
    user = authenticate_user(form_data.username, form_data.password, db)

    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Incorrect username or password")
    token = create_access_token(user.username, user.id, timedelta(minutes=20))

    user_response = UserResponse(user.username)

    return TokenResponse(
        user_response,
        token,
        "bearer"
    )
    