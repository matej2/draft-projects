import os
from pathlib import Path

from fastapi import APIRouter, UploadFile, File, HTTPException, Response
from sqlalchemy import update
from starlette import status

from broker.producer import produce_message
from domain.dto.Request import image_transform_dependency
from domain.dto.Response import ImageResponse
from domain.model.Image import Image
from factory.ImageFactory import create_image_upload_request
from router.common_dependencies import db_dependency, oauth2bearer_dependency
from router.pagination import pagination_dependency, select_and_paginate_query, \
    get_order_for_pagination
from service.ImageProcessorService import image_processor_dependency

image_router = APIRouter(
    prefix="/images",
    tags=["images"]
)

valid_content_types = str(os.getenv("IMAGE_VALID_CONTENT_TYPES"))

@image_router.post("/",
                   status_code=status.HTTP_201_CREATED,
                   response_model=ImageResponse)
async def upload_image(
        oauth2_bearer: oauth2bearer_dependency,
        file: UploadFile = File(...)):
    if file.content_type not in valid_content_types:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Invalid file type")

    contents = await file.read()
    file_path = str(Path(str(file.filename)).stem)
    content_type = str(file.content_type)
    image = create_image_upload_request(
        content_type,
        contents,
        file_path
    )
    produce_message(image)
    return ImageResponse(type=content_type, name=file_path, content=contents)

@image_router.get("/{image_id}",
                   status_code=status.HTTP_200_OK,
                   response_class = Response)
async def get_image(
        image_id: int,
        oauth2_bearer: oauth2bearer_dependency,
        db: db_dependency):
    image_result = db.query(Image).filter(Image.id == image_id).first()
    if not image_result:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)


    return Response(content=image_result.content, media_type=str(image_result.type))



@image_router.get("/",
                   status_code=status.HTTP_200_OK)
async def list_images(
        db: db_dependency,
        oauth2_bearer: oauth2bearer_dependency,
        pagination: pagination_dependency
) -> list[ImageResponse]:
    order = get_order_for_pagination(pagination)
    query = select_and_paginate_query(Image, pagination).order_by(order(Image.id))

    result_list  = [
        ImageResponse(
            id=row[0].id,
            type=row[0].type,
            name=row[0].name,
            content=row[0].content
        )
        for row in db.execute(query)
    ]

    return result_list

@image_router.post("/{image_id}/transform",
                   status_code=status.HTTP_200_OK)
async def transform_image(
        image_id: int,
        db: db_dependency,
        oauth2_bearer: oauth2bearer_dependency,
        transform: image_transform_dependency,
        image_processor: image_processor_dependency):
    image_result = db.query(Image).filter(Image.id == image_id).first()
    if not image_result:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)

    compressed_bytes = image_processor_dependency.resize_image(image_result.content)

    stmt = (
        update(Image)
        .where(Image.id == image_result.id)
        .values(content=compressed_bytes)
    )
    updated_images = db.execute(stmt)
    db.commit()
    return updated_images
