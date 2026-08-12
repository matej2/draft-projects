import base64

from domain.model.Image import Image
from domain.dto.Request import ImageUploadRequest


def create_image_upload_request(content_type: str, content: bytes, name: str):
    return ImageUploadRequest(
        type=content_type,
        content=content,
        name=name
    )

def create_image_from_json_dict(image: dict):
    content_bytes = base64.b64decode(image.get("content"))

    return Image(
        type=image.get("type"),
        name=image.get("name"),
        content=content_bytes
    )