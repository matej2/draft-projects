from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException

from app.db import create_db_and_tables
from app.schemas import PostCreate

@asynccontextmanager
async def lifespan(app: FastAPI):
    await create_db_and_tables()
    yield

app = FastAPI(lifespan=lifespan)

text_posts = {
    1: {
        "title": "Hello",
        "content": "Description"
    },
    2: {
        "title": "Getting Started",
        "content": "This is a beginner guide to our platform."
    },
    3: {
        "title": "FastAPI Tips",
        "content": "Learn how to build APIs quickly with FastAPI."
    },
    4: {
        "title": "Docker Basics",
        "content": "An introduction to containerizing your applications."
    },
    5: {
        "title": "Testing in Python",
        "content": "Best practices for writing unit tests using unittest and pytest."
    },
    6: {
        "title": "Async Programming",
        "content": "Understanding async and await in Python."
    },
    7: {
        "title": "Logging Guide",
        "content": "How to implement structured logging in your apps."
    },
    8: {
        "title": "Deployment",
        "content": "Steps to deploy your FastAPI app using Docker and cloud services."
    }
}

@app.get("/posts")
def get_all_posts(limit: int = 10) -> list[PostCreate]:
    if limit:
        return list(text_posts.values())[:limit]
    return text_posts

@app.get("/posts/{post_id}")
def get_post(post_id: int) -> PostCreate:
    if post_id not in text_posts:
        raise HTTPException(status_code=404, detail="Post not found")
    return text_posts.get(post_id)

@app.post("/post")
def create_post(post: PostCreate) -> PostCreate:
    new_post = {
        "title": post.title,
        "content": post.content
    }
    text_posts[max(text_posts.keys()) + 1] = new_post
    return new_post




if __name__ == '__main__':
    print('PyCharm')