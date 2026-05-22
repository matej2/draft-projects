import os
from unittest import TestCase

from app.storage import Storage


class TestStorage(TestCase):

    @classmethod
    def setUpClass(cls):
        cls.storage = Storage()

    def test_load_data_with_empty_file(self):
        project_list = self.storage.load_data()
        assert project_list == []

    def test_load_data_with_nonexistent_file(self):
        os.remove(self.storage.DATA_FILE)
        project_list = self.storage.load_data()
        assert project_list == []


