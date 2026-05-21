import uuid
from contextlib import asynccontextmanager
from typing import List, Annotated

from fastapi import APIRouter, Depends
from starlette import status
from starlette.exceptions import HTTPException

import models
from models import ProjectStatus
from storage import Storage

router = APIRouter(prefix="/api/v1/project")

@router.get("/", response_model=List[models.ProjectOut])
async def get_projects(storage: Annotated[Storage, Depends(Storage)]):
    """Retrieve all projects"""
    issues = storage.load_data()
    return issues

@router.get("/{id}", response_model=models.ProjectOut)
def get_project(project_id: str, storage: Annotated[Storage, Depends(Storage)]):
    """Retrieve a project by its id"""
    issues  = storage.load_data()
    for issue in issues:
        if issue["id"] == project_id:
            return issue
    raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Project not found")

@router.post("/", response_model=models.ProjectOut, status_code=status.HTTP_201_CREATED)
async def create_project(project: models.ProjectCreate, storage: Annotated[Storage, Depends(Storage)]):
    """Create a new project"""
    projects = storage.load_data()
    new_project = {
        "id": str(uuid.uuid4()),
        "name": project.name,
        "description": project.description,
        "status": ProjectStatus.inactive
    }
    # new_project = models.ProjectDomain(
    #     id=str(uuid.uuid4()),
    #     name=project.name,
    #     description=project.description,
    #     status=project.status,
    # )
    projects.append(new_project)
    storage.save_data(projects)
    return new_project

@asynccontextmanager
async def lifespan(app):
    # Load the ML model
    yield
    # Clean up the ML models and release the resources
    print("end")