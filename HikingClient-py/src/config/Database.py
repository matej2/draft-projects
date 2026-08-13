from typing import Annotated, Any

import pymongo
from fastapi import Depends


def get_db():
    client = pymongo.MongoClient("localhost", 27017)
    database = client["test"]
    return database["new"]

db_dependency = Annotated[Any, Depends(get_db)]
