from fastapi import APIRouter, UploadFile, File, HTTPException
from starlette import status

from dto.Response import ImageResponse
from model.Image import Image
from router.common_dependencies import db_dependency

image_router = APIRouter(
    prefix="/images",
    tags=["image"]
)

@image_router.post("/",
                   status_code=status.HTTP_201_CREATED,
                   response_model=ImageResponse)
async def process_gpx(
        db: db_dependency,
        file: UploadFile = File(...)):
    contents = await file.read()
    image = Image(
        content=contents
    )
    db.add(image)
    db.commit()
    return ImageResponse(id=image.id, content=b"")

@image_router.post("/{image_id}",
                   status_code=status.HTTP_200_OK,
                   response_model = ImageResponse)
async def process_gpx(
        image_id: int,
        db: db_dependency):
    image_result = db.query(Image).filter(Image.id == image_id).first()
    if not image_result:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
    image_response = ImageResponse(
        id=image_result.id,
        content=image_result.content
    )
    return image_response