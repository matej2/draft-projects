import base64

from pydantic import BaseModel, field_serializer
from typing import Optional


class CurrentUserResponse(BaseModel):
    username: str
    id: int

class UserResponse(BaseModel):
    username: str


class TokenResponse(BaseModel):
    user: UserResponse
    access_token: str
    token_type: str

class ImageResponse(BaseModel):
    id: Optional[str] = None
    type: Optional[str] = None
    name: Optional[str] = None
    content: Optional[bytes] = None

    @field_serializer('content')
    def serialize_bytes(self, content: bytes, _info):
        return base64.b64encode(content).decode('utf-8')