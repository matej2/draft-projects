from fastapi import APIRouter, UploadFile, File
from starlette import status

from model.Image import Image
from router.common_dependencies import db_dependency

image_router = APIRouter(
    prefix="/img",
    tags=["image"]
)

@image_router.post("/upload", status_code=status.HTTP_201_CREATED)
async def process_gpx(
        db: db_dependency,
        file: UploadFile = File(...)):
    contents = await file.read()
    image = Image(
        content=contents
    )
    db.add(image)
    db.commit()