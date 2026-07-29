import uuid

from sqlalchemy import Column, LargeBinary, String

from domain.model.Base import Base


class Image(Base):
    __tablename__ = "images"
    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    name = Column(String)
    type = Column(String)
    content = Column(LargeBinary)