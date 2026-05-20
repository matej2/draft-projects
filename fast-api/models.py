from enum import Enum
from typing import Optional

from pydantic import BaseModel, Field


class ProjectStatus(str, Enum):
    active = "active"
    inactive = "inactive"

class ProjectDomain(BaseModel):
    id: str
    name: str
    description: str
    status: ProjectStatus

class ProjectCreate(BaseModel):
    name: str = Field(min_length=1,max_length=100)
    description: str = Field(min_length=1, max_length=255)
    status: ProjectStatus = ProjectStatus.inactive

class ProjectUpdate(BaseModel):
    name: Optional[str] = Field(min_length=1,max_length=100)
    description: Optional[str] = Field(min_length=1, max_length=255)
    status: Optional[ProjectStatus] = ProjectStatus.active

class ProjectOut(BaseModel):
    name: str
    description: str
    status : ProjectStatus