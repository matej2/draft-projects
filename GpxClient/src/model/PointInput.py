from pydantic import BaseModel


class PhotoInput(BaseModel):
    data: bytes


