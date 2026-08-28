from contextlib import asynccontextmanager

import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

from app.middleware.loggingmiddleware import LoggingMiddleware
from app.middleware.timer import timing_middleware
from app.project import router as project_router
from prometheus_fastapi_instrumentator import Instrumentator


@asynccontextmanager
async def lifespan(app):
    # Load the ML model
    yield
    # Clean up the ML models and release the resources
    print("end")

app = FastAPI(lifespan=lifespan)

app.add_middleware(LoggingMiddleware)
app.middleware("http")(timing_middleware)
app.add_middleware(CORSMiddleware,allow_origins=["*"])

app.include_router(project_router)

Instrumentator().instrument(app).expose(app, endpoint="/metrics")

class Item(BaseModel):
    text: str = None
    is_done: bool = False

items = list()

@app.get("/")
def get_items(limit: int = 10):
    return items[:limit]

@app.post("/items")
def create_item(item: Item):
    items.append(item)
    return items

@app.post("/{item_id}", response_model=Item)
def get_item(item_id: int) -> Item:
    if 0 <= item_id < len(items):
        return items[item_id]
    else:
        raise HTTPException(status_code=404, detail=f"Item {item_id} not found")


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)