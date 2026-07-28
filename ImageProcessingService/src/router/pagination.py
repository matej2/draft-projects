import os
from enum import Enum
from typing import Annotated, TypeVar, Type

from fastapi import Query, Depends
from pydantic import BaseModel
from sqlalchemy import select, asc, desc, Select

default_page_size = float(str(os.getenv("PAGINATION_DEFAULT_PAGE_SIZE")))

class SortEnum(Enum):
    ASC = "asc"
    DESC = "desc"

class Pagination(BaseModel):
    page: int
    per_page: int
    order: SortEnum

def pagination_params(
        page: int = Query(ge=1, required=False, default=1),
        per_page: int = Query(ge=2, le=100, required=False, default=default_page_size),
        order: SortEnum = SortEnum.ASC
):
    return Pagination(page=page, per_page=per_page, order=order)

T = TypeVar("T")

def get_order_for_pagination(pagination: Pagination):
    return desc if pagination.order == SortEnum.DESC else asc

def select_and_paginate_query(clazz: Type[T], pagination: Pagination) -> Select:

    return (select(clazz)
            .limit(pagination.per_page)
            .offset(get_offset_for_page(pagination.page, pagination.per_page)))

def get_offset_for_page(page: int, per_page: int):
    if page == 1:
        return page - 1
    else:
        return (page - 1) * per_page

pagination_dependency = Annotated[Pagination, Depends(pagination_params)]