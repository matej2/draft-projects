from dataclasses import dataclass
from typing import Any


@dataclass
class DocumentSaveRequest:
    data: list[Any]
    