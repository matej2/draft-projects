from sqlalchemy import Column, LargeBinary, Integer

from domain.model.Base import Base


class Image(Base):
    __tablename__ = "images"
    id = Column(Integer, primary_key=True)
    content = Column(LargeBinary)