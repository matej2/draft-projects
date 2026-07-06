from typing import Annotated

from fastapi import Depends, HTTPException
from jose import jwt, JWTError
from starlette import status

from config.auth import SECRET_KEY, ALGORITHM, oauth2_bearer, SessionLocal
from domain.dto.Response import CurrentUserResponse


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


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
        return CurrentUserResponse(
            username,
            user_id
        )
    except JWTError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Could not validate jwt",
        )

