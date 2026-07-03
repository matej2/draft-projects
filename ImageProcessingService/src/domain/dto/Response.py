from dataclasses import dataclass


@dataclass
class CurrentUserResponse:
    username: str
    id: str

@dataclass
class TokenResponse:
    access_token: str
    token_type: str