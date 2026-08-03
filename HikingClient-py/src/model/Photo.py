import uuid
from dataclasses import field, dataclass


@dataclass
class Photo:
    path: str
    lan: str
    lon: str
    id: str = field(default=str(uuid.uuid4()))