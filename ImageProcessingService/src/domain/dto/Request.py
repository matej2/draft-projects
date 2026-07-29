from dataclasses import dataclass
from typing import Annotated

from fastapi import Depends
from pydantic import BaseModel

class ImageSize(BaseModel):
    width: int
    height: int

class ImageCrop(ImageSize):
    x: int
    y: int

class ImageTransformRequest(BaseModel):
    resize: ImageSize

class ImageUploadRequest(BaseModel):
    type: str
    content: bytes
    name: str


def get_image_transform(width: int, height: int) -> ImageTransformRequest:
    size = ImageSize(width=width, height=height)
    return ImageTransformRequest(resize=size)

image_transform_dependency = Annotated[ImageTransformRequest, Depends(get_image_transform)]