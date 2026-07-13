from typing import Annotated

import uvicorn
from fastapi import FastAPI, Depends
from starlette import status

from config.database import ENGINE
from domain.model.Base import Base
from domain.model.User import User
from router.auth_router import auth_router
from router.image_router import image_router
from router.page_router import page_router
from service.DatabaseService import get_current_user

Base.metadata.create_all(ENGINE)

app = FastAPI(title="GPX Client")
app.include_router(auth_router)
app.include_router(image_router)
app.include_router(page_router)

user_dependency = Annotated[User, Depends(get_current_user)]

@app.get("/", status_code=status.HTTP_200_OK)
async def user(current_user: user_dependency):
    return current_user

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

