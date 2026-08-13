import os

from GPSPhoto import gpsphoto

from config.Database import db_dependency
from model.Photo import Photo


class LocalFileService:
    def __init__(self, root_dir: str):
        self.__root_dir = root_dir

    def process_files(self, collection: db_dependency):
        file_name_list = self.__get_files()
        photo_list: list[Photo] = list()

        for file_name in file_name_list:
            photo_list.append(self.__construct_photo_from_file(file_name))

        collection.insert_many(photo_list)

        return photo_list

    @staticmethod
    def __construct_photo_from_file(file):
        coordinates = gpsphoto.getGPSData(file)
        return Photo(file, coordinates.get("Latitude"), coordinates.get("Longitude"))



    def __get_files(self):
        norm_path = os.path.normpath(self.__root_dir)
        file_names = os.listdir(norm_path)
        return [
            os.path.join(self.__root_dir, f)
            for f in file_names
        ]


