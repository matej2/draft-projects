import uvicorn
from fastapi import FastAPI

import models
from auth.auth_router import router
from database import engine

app = FastAPI(title="GPX Client")
app.include_router(router)

models.Base.metadata.create_all(bind=engine)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

