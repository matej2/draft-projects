from dataclasses import dataclass


@dataclass
class CurrentUserResponse:
    username: str
    id: str