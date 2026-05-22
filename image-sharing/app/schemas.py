from pydantic import BaseModel

class CustomBaseModel(BaseModel):
    is_active: bool = True

class PostCreate(CustomBaseModel):
    title: str
    content: str
