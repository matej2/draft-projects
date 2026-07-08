from pathlib import Path

from sqlalchemy import create_engine

def get_absolute_project_path():
    return Path(__file__).parent.parent.parent.resolve()


SQLALCHEMY_DATABASE_URI = f"sqlite:///{get_absolute_project_path()}/db.sqlite3"

ENGINE = create_engine(SQLALCHEMY_DATABASE_URI, connect_args={"check_same_thread": False})


