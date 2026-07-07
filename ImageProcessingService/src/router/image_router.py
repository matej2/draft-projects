import base64
import io
from pathlib import Path

from fastapi import APIRouter, UploadFile, File, HTTPException
from fastapi.openapi.models import Response
from sqlalchemy import update
from starlette import status
from PIL import Image as PILImage
from domain.dto.Request import image_transform_dependency
from domain.dto.Response import ImageResponse
from domain.model.Image import Image
from router.common_dependencies import db_dependency
from router.pagination import pagination_dependency, select_and_paginate_query, \
    get_order_for_pagination

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
    file_path = Path(str(file.filename))
    image = Image(
        extension=file_path.suffix.lower(),
        content=contents,
        name=file_path.stem
    )
    db.add(image)
    db.commit()
    return ImageResponse(id=image.id, content="", extension=None, name=None)

@image_router.get("/{image_id}",
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
        content=image_result.content,
        extension=image_result.extension
    )
    return image_response



@image_router.get("/",
                   status_code=status.HTTP_200_OK)
async def list_images(
        db: db_dependency,
        pagination: pagination_dependency
) -> list[ImageResponse]:
    order = get_order_for_pagination(pagination)
    query = select_and_paginate_query(Image, pagination).order_by(order(Image.id))

    result_list  = [
        ImageResponse(
            id=row[0].id,
            content=(base64.b64encode(row[0].content).decode('ascii') if row[0].content is not None else None),
            extension=row[0].extension,
            name=row[0].name
        )
        for row in db.execute(query)
    ]

    return result_list

@image_router.post("/{image_id}/transform",
                   status_code=status.HTTP_200_OK)
async def transform_image(
        image_id: int,
        db: db_dependency,
        transform: image_transform_dependency):
    image_result = db.query(Image).filter(Image.id == image_id).first()
    if not image_result:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)


    loaded_image = PILImage.open(io.BytesIO(image_result.content))
    loaded_image.resize((transform.resize.width, transform.resize.height))
    img_bytes = loaded_image.tobytes()

    stmt = (
        update(Image)
        .where(Image.id == image_result.id)
        .values(content=img_bytes)
    )
    updated_images = db.execute(stmt)
    return image_result.id
