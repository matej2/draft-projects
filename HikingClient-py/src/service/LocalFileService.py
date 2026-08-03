import os

from GPSPhoto import gpsphoto

from model.Photo import Photo


class LocalFileService:
    def __init__(self, root_dir: str):
        self.__root_dir = root_dir

    def process_files(self):
        file_name_list = self.__get_files()
        photo_list: list[Photo] = list()

        for f in file_name_list:
            coordinates = gpsphoto.getGPSData(f)
            photo_list.append(Photo(f, coordinates.get("Latitude"), coordinates.get("Longitude")))

        return photo_list



    def __get_files(self):
        norm_path = os.path.normpath(self.__root_dir)
        file_names = os.listdir(norm_path)
        return [
            os.path.join(self.__root_dir, f)
            for f in file_names
        ]


