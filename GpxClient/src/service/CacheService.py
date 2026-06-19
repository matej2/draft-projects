import pymongo
from stravalib.protocol import AccessInfo


class CacheService:
    def __init__(self):
        self.client = pymongo.MongoClient("mongodb://0.0.0.0:27017/myDatabase")
        db = self.client.test_database
        self.access_info = db.posts

    def save_access_info(self, access_info: AccessInfo):
        self.access_info.insert_one(dict(access_info))
        pass
    def load_access_info(self):
        return AccessInfo(**self.access_info.find_one())
