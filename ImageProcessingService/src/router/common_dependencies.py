from typing import Annotated

from fastapi import Depends
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session

from config.auth import oauth2_bearer
from service.DatabaseService import get_db

db_dependency = Annotated[Session, Depends(get_db)]
form_dependency = Annotated[OAuth2PasswordRequestForm, Depends()]
oauth2bearer_dependency = Annotated[str, Depends(oauth2_bearer)]
