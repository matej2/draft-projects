import pymongo
from dotenv import load_dotenv
from stravalib.protocol import AccessInfo


class CacheService:
    def __init__(self, mongo_uri: str):
        load_dotenv()

        self.client = pymongo.MongoClient(mongo_uri)
        db = self.client.test_database
        self.access_info = db.posts

    def save_access_info(self, access_info: AccessInfo):
        self.access_info.insert_one(dict(access_info))
        pass
    def load_access_info(self):
        document = self.access_info.find_one()
        if document is not None:
            return AccessInfo(**self.access_info.find_one())
        return None
