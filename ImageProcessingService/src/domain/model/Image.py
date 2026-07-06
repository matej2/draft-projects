from sqlalchemy import Column, LargeBinary, Integer, String

from domain.model.Base import Base


class Image(Base):
    __tablename__ = "images"
    id = Column(Integer, primary_key=True)
    extension = Column(String)
    content = Column(LargeBinary)