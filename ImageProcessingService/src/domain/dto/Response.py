from pydantic import BaseModel
from typing import Optional


class CurrentUserResponse(BaseModel):
    username: str
    id: str

class UserResponse(BaseModel):
    username: str


class TokenResponse(BaseModel):
    user: UserResponse
    access_token: str
    token_type: str

class ImageResponse(BaseModel):
    id: int
    type: Optional[str] = None
    name: Optional[str] = None

