import os

from service.LocalFileService import LocalFileService


def main():
    image_root_path = os.getenv("IMAGE_ROOT_DIR")
    localFileService = LocalFileService(image_root_path)
    result = localFileService.process_files()

if __name__ == "__main__":
    main()