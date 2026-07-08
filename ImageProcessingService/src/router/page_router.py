from fastapi import APIRouter, Request
from starlette import status
from starlette.templating import Jinja2Templates

from domain.model.Image import Image
from dto.Response import ImageResponse
from router.common_dependencies import image_env_dependency, db_dependency, get_images_env, get_absolute_project_path
from router.pagination import select_and_paginate_query, Pagination, SortEnum, get_order_for_pagination

page_router = APIRouter(
    prefix="/web",
    tags=["web"]
)

templates = Jinja2Templates(directory=f"{get_absolute_project_path()}/src/template")
@page_router.get("/",
                   status_code=status.HTTP_201_CREATED)
async def index(env: image_env_dependency, db: db_dependency, request: Request):

    pagination = Pagination(page=1, per_page=10, order=SortEnum.ASC)
    order = get_order_for_pagination(pagination)

    query = select_and_paginate_query(Image, pagination).order_by(order(Image.id))

    result_list  = [
        ImageResponse(
            id=row[0].id,
            type=row[0].type,
            name=row[0].name
        )
        for row in db.execute(query)
    ]

    return templates.TemplateResponse(
        request=request,
        name="images/images.html",
        context={"images": result_list},
    )