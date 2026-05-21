import uuid
from unittest import TestCase

from starlette.testclient import TestClient
from app.main import app
from app.models import ProjectStatus, ProjectCreate
from app.storage import Storage

client = TestClient(app)

class TestProject(TestCase):
    def setUp(self) -> None:
        self.storage = Storage()
        projects = self.storage.load_data()
        self.project_id = str(uuid.uuid4())
        new_project = {
            "id": self.project_id,
            "name": "Test",
            "description": "Description",
            "status": ProjectStatus.inactive
        }
        # new_project = models.ProjectDomain(
        #     id=str(uuid.uuid4()),
        #     name=project.name,
        #     description=project.description,
        #     status=project.status,
        # )
        projects.append(new_project)
        self.storage.save_data(projects)

    def test_get_projects(self):

        response = client.get("/api/v1/project/")
        assert response.status_code == 200

        project_list = response.json()
        assert len(project_list) == 1
        project = project_list[0]
        assert project["name"] == "Test"
        assert project["description"] == "Description"
        assert project["status"] == ProjectStatus.inactive


    def test_get_projects_with_existing_project_id(self):

        response = client.get(f"/api/v1/project/{self.project_id}")
        assert response.status_code == 200

        project_list = response.json()
        assert len(project_list) == 1
        project = project_list[0]
        assert project["name"] == "Test"
        assert project["description"] == "Description"
        assert project["status"] == ProjectStatus.inactive

    def test_get_projects_with_existing_project_id(self):

        response = client.get(f"/api/v1/project/65")
        assert response.status_code == 404

        response = response.json()
        assert response["detail"] == "Project not found"

    def test_create_project(self):
        project_create = ProjectCreate(
            name="Test",
            description="Description",
            status=ProjectStatus.inactive)

        response = client.post(f"/api/v1/project/", json=dict(project_create))

        assert response.status_code == 201

        json_response = response.json()

        assert json_response["name"] == "Test"
        assert json_response["description"] == "Description"
        assert json_response["status"] == ProjectStatus.inactive



    def tearDown(self):
        with open(self.storage.DATA_FILE, "w") as f:
            f.write("")