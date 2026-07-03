from sqlalchemy.orm import declarative_base

from src.database import engine

Base = declarative_base()

class DbBase(Base):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.__table__.create(bind=engine)