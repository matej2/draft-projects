import msal
import pymongo
from bson import json_util
from dotenv import load_dotenv

class CacheService:
    def __init__(self, mongo_uri: str):
        load_dotenv()

        self.client = pymongo.MongoClient(mongo_uri)
        db = self.client.test_database
        self.access_info = db.posts

    def save_access_info(self, cache: msal.SerializableTokenCache):
        cache_dict: dict = json_util.loads(cache.serialize())
        cache_dict["_id"] = str(cache)
        self.access_info.insert_one(json_util.loads(cache.serialize()))


    def load_access_info(self):
        cache = msal.SerializableTokenCache()
        saved_cache = self.access_info.find_one()
        if saved_cache:
                cache.deserialize(json_util.dumps(saved_cache))
        return cache
