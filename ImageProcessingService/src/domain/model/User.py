from sqlalchemy import Column, String, Integer

from model.Base import DbBase


class User(DbBase):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    username = Column(String)
    hashed_password = Column(String)
