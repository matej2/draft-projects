from typing import Annotated

from fastapi import Path, Depends


def get_absolute_project_path():
    return Path(__file__).parent.parent.parent.resolve()

def get_connection_string():
    return "mongodb+srv://admin:changeme@localhost:27017/myFirstDatabase"

project_path_dependency = Annotated[Path, Depends(get_absolute_project_path)]
