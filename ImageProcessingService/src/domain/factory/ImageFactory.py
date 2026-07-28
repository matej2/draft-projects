import uuid

from dto.Request import ImageUploadRequest


def create_image_upload_request(content_type: str, content: bytes, name: str):
    return ImageUploadRequest(
        id=uuid.uuid4().int,
        type=content_type,
        content=content,
        name=name
    )