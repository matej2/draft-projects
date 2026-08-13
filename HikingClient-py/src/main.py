import os

from config.Database import get_db
from service.LocalFileService import LocalFileService


def main():
    image_root_path = os.getenv("IMAGE_ROOT_DIR")
    localFileService = LocalFileService(image_root_path)
    result = localFileService.process_files(get_db())

if __name__ == "__main__":
    main()