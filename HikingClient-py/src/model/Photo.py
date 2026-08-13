import uuid
from dataclasses import field, dataclass

from model.Base import Base


@dataclass
class Photo(Base):
    path: str
    lan: str
    lon: str
    id: str = field(default=str(uuid.uuid4()))