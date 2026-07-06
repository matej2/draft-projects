import io
from PIL import Image as ImagePIL
from PIL.ImageFile import ImageFile

from dto.Request import ImageTransformRequest
from model.Image import Image


def get_image_from_bytes(image: Image):
    image_stream = io.BytesIO(image.content)
    return ImagePIL.open(image_stream)


def resize(image: Image, transform: ImageTransformRequest) -> ImageFile:
    image = get_image_from_bytes(image)

    resized_image = image.resize((
        transform.resize.width,
        transform.resize.height
    ))
    return resized_image