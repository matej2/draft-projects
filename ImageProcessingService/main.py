from typing import Annotated

import uvicorn
from fastapi import FastAPI, HTTPException, Depends
from starlette import status

import models
from auth.auth_router import router, db_dependency, get_current_user
from database import engine

app = FastAPI(title="GPX Client")
app.include_router(router)

models.Base.metadata.create_all(bind=engine)

user_dependency = Annotated[models.User, Depends(get_current_user)]

@app.get("/", status_code=status.HTTP_200_OK)
async def user(user: user_dependency, db: db_dependency):
    if user is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Authentication failed")
    return {"user": user}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

