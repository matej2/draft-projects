from pathlib import Path
from typing import Annotated

import jinja2
from fastapi import Depends
from fastapi.security import OAuth2PasswordRequestForm
from jinja2 import FileSystemLoader, Environment
from sqlalchemy.orm import Session

from config.auth import oauth2_bearer
from service.DatabaseService import get_db

db_dependency = Annotated[Session, Depends(get_db)]
form_dependency = Annotated[OAuth2PasswordRequestForm, Depends()]
oauth2bearer_dependency = Annotated[str, Depends(oauth2_bearer)]

def get_absolute_project_path():
    return Path(__file__).parent.parent.parent.resolve()

project_path_dependency = Annotated[Path, Depends(get_absolute_project_path)]

def get_images_env(project_path: project_path_dependency):
    templates_dir = project_path / "src" / "template"
    return jinja2.Environment(loader=FileSystemLoader(str(templates_dir)))

image_env_dependency = Annotated[Environment, Depends(get_images_env)]

