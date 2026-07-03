from fastapi.security import OAuth2PasswordBearer
from passlib.context import CryptContext
from sqlalchemy.orm import sessionmaker

from config.database import ENGINE

SECRET_KEY = "905f80d9834jf3894fj98czc4f67no0pv9t7vn6s4vbnp56t"
ALGORITHM = "HS256"

bcrypt_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_bearer = OAuth2PasswordBearer(tokenUrl="auth/token")

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=ENGINE)
