import uvicorn
from fastapi import FastAPI
from starlette import status

app = FastAPI(title="GPX Client")
#app.include_router(auth_router)

@app.get("/", status_code=status.HTTP_200_OK)
async def user():
    return "Ok"

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

