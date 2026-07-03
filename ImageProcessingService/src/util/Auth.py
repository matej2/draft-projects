from datetime import datetime, UTC, timedelta

from jose import jwt

from config.auth import bcrypt_context, SECRET_KEY, ALGORITHM
from domain.model.User import User


def authenticate_user(username: str, password: str, db):
    user = db.query(User).filter(User.username == username).first()
    if not user:
        return False
    if not bcrypt_context.verify(password, user.hashed_password):
        return False
    return user

def create_access_token(username: str, user_id: str, expires_after: timedelta):
    encode = {"sub": username, "id": user_id}
    expires_after = datetime.now(UTC) + expires_after
    encode.update({"exp": str(int(expires_after.timestamp()))})
    return jwt.encode(encode, SECRET_KEY, algorithm=ALGORITHM)