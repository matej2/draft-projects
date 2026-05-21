import json
import logging
from pathlib import Path

from pydantic.v1 import BaseSettings

from models import ProjectCreate, ProjectOut


class Storage:
    def __init__(self):
        self.DATA_DIR = Path("data")
        self.DATA_FILE = self.DATA_DIR / "projects.json"
        self.logger = logging.getLogger(__name__)

    def load_data(self) -> list[ProjectOut]:
        if self.DATA_FILE.is_file() and self.DATA_FILE.exists():
            with open(self.DATA_FILE, "r") as f:
                content = f.read()
                return json.loads(content)
        else:
            return list()

    def save_data(self, data: list[ProjectCreate]):
        self.DATA_DIR.mkdir(parents=True, exist_ok=True)
        with open(self.DATA_FILE, "w") as f:
            json_text = json.dumps(data, indent=2)
            #self.logger.info(f"Saving data to {self.DATA_FILE}: {json_text}")
            f.write(json_text)

class Settings(BaseSettings):
    test: str
    name: str