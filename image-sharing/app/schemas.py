import uuid

from fastapi_users import schemas
from pydantic import BaseModel

class CustomBaseModel(BaseModel):
    is_active: bool = True

class PostCreate(CustomBaseModel):
    title: str
    content: str

class PostResponse(BaseModel):
    title: str
    content: str

class UserRead(schemas.BaseUser[uuid.UUID]):
    pass

class UserCreate(schemas.BaseUserCreate):
    pass

class UserUpdate(schemas.BaseUserUpdate):
    pass