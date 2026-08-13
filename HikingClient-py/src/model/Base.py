from dataclasses import asdict, dataclass


@dataclass
class Base:
    def to_mongo(self) -> dict:
        data = asdict(self)
        return data