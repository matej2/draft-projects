from sqlalchemy import create_engine

SQLALCHEMY_DATABASE_URI = "sqlite:///db.sqlite3"

ENGINE = create_engine(SQLALCHEMY_DATABASE_URI, connect_args={"check_same_thread": False})


