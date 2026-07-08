from pathlib import Path
from starlette.templating import Jinja2Templates


class DirectoryConfig:
    @staticmethod
    def get_absolute_project_path():
        return Path(__file__).parent.parent.resolve()

    @staticmethod
    def get_jinja_template():
        abs_path = DirectoryConfig.get_absolute_project_path()
        templates = Jinja2Templates(directory=f"{abs_path}/templates")
        return templates