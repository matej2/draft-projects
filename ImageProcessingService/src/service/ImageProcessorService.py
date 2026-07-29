import io
from collections.abc import Buffer
from typing import Annotated

from PIL import Image as PILImage
from fastapi import Depends
from pydantic.experimental.pipeline import transform


class ImageProcessorService:

    @staticmethod
    def resize_image(image_bytes: Buffer):
        loaded_image = PILImage.open(io.BytesIO(image_bytes))
        img_format = loaded_image.format

        resized_image = loaded_image.resize((transform.resize.width, transform.resize.height))

        buffer = io.BytesIO()
        resized_image.save(buffer, format=img_format)
        return buffer.getvalue()


def get_image_processor_service():
    return ImageProcessorService()

image_processor_dependency = Annotated[ImageProcessorService, Depends(get_image_processor_service)]