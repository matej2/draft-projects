from fastapi import APIRouter
from starlette import status
from starlette.templating import Jinja2Templates

from util.PageUtil import get_absolute_project_path

page_router = APIRouter(
    tags=["web"]
)

templates = Jinja2Templates(directory=f"{get_absolute_project_path()}/src/template")
@page_router.get("/",
                   status_code=status.HTTP_201_CREATED)
async def index(db: db_dependency):

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