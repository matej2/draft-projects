from fastapi import APIRouter, UploadFile, File, HTTPException
from sqlalchemy import select, asc, desc
from starlette import status

from dto.Response import ImageResponse
from model.Image import Image
from router.common_dependencies import db_dependency
from router.pagination import pagination_dependency, get_offset_for_page, SortEnum, select_and_paginate_query, \
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
    image = Image(
        content=contents
    )
    db.add(image)
    db.commit()
    return ImageResponse(id=image.id, content=b"")

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
        content=image_result.content
    )
    return image_response



@image_router.get("/",
                   status_code=status.HTTP_200_OK)
async def process_gpx(
        db: db_dependency,
        pagination: pagination_dependency
):
    order = get_order_for_pagination(pagination)
    query = select_and_paginate_query(Image, pagination).order_by(order(Image.id))

    result_list  = [
        ImageResponse(id=row[0].id, content=row[0].content)
        for row in db.execute(query)
    ]

    return result_list