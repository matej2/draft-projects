from typing import Annotated

import uvicorn
from fastapi import FastAPI, Depends, Request
from starlette import status
from starlette.templating import Jinja2Templates

from src.config.DirectoryConfig import DirectoryConfig

app = FastAPI(title="GPX Client")
#app.include_router(auth_router)

image_env_dependency = Annotated[Jinja2Templates, Depends(DirectoryConfig.get_jinja_template)]

@app.get("/", status_code=status.HTTP_200_OK)
async def user(templates: image_env_dependency, request: Request):
    return templates.TemplateResponse(
        request=request,
        name="index.html",
    )

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000)

