from decimal import Decimal
from enum import Enum
from typing import Optional

from pydantic import BaseModel, Field
from pydantic.alias_generators import to_camel


class CustomBase(BaseModel):
    class Config:
        alias_generator = to_camel
        populate_by_name = True
        json_encoders = {
            Decimal: str
        }


class ProjectStatus(str, Enum):
    active = "active"
    inactive = "inactive"

class ProjectDomain(CustomBase):
    id: str
    name: str
    description: str
    status: ProjectStatus

class ProjectCreate(CustomBase):
    name: str = Field(min_length=1,max_length=100)
    description: str = Field(min_length=1, max_length=255)
    status: ProjectStatus = ProjectStatus.inactive

class ProjectUpdate(CustomBase):
    name: Optional[str] = Field(min_length=1,max_length=100)
    description: Optional[str] = Field(min_length=1, max_length=255)
    status: Optional[ProjectStatus] = ProjectStatus.active

class ProjectOut(CustomBase):
    name: str
    description: str
    status : ProjectStatus