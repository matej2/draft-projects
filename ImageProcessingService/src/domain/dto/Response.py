from dataclasses import dataclass


@dataclass
class CurrentUserResponse:
    username: str
    id: str

@dataclass
class UserResponse:
    username: str


@dataclass
class TokenResponse:
    user: UserResponse
    access_token: str
    token_type: str