import base64

from domain.model.Image import Image
from dto.Request import ImageUploadRequest


def create_image_upload_request(content_type: str, content: bytes, name: str):
    return ImageUploadRequest(
        type=content_type,
        content=content,
        name=name
    )

def create_image_from_json_dict(image: dict):
    raw_content = image.get("content")

    content_bytes = base64.b64decode(raw_content)

    return Image(
        type=image.get("type"),
        name=image.get("name"),
        content=base64.b64decode(content_bytes)
    )