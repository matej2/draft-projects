import base64
import json
import os

from confluent_kafka import Producer

from domain.dto.Request import ImageUploadRequest
from util.LoggingUtil import LoggingUtil

# Get Kafka broker address from environment variable or use default
# When running in Docker container, use 'broker:9092'
# When running on host machine, use 'localhost:9092'
kafka_broker_url = os.getenv("KAFKA_BROKER_URL", "localhost")
kafka_broker_port = os.getenv("KAFKA_BROKER_PORT", "9092")
kafka_broker = f"{kafka_broker_url}:{kafka_broker_port}"

producer_config = {
    "bootstrap.servers": kafka_broker,
    "socket.timeout.ms": 10000,  # 10 second timeout
}

logger = LoggingUtil.get_logger(__name__)

producer = Producer(producer_config)

def delivery_report(err, msg):
    if err:
        logger.error(f"Message delivery failed: {err}")
    else:
        logger.info("Delivery OK")
        logger.debug(f"Partition {msg.partition()} on topic {msg.topic()}, offset {msg.offset()}")

def produce_message(image: ImageUploadRequest):
    try:
        data = image.model_dump()

        if 'content' in data and isinstance(data['content'], bytes):
            data['content'] = base64.b64encode(data['content']).decode('utf-8')

        value = json.dumps(data).encode("utf-8")

        producer.produce(
            topic="images",
            value=value,
            callback=delivery_report)
        producer.flush()
        logger.info("Message produced successfully!")
    except Exception as e:
        logger.error(f"Error producing message: {e}")


if __name__ == "__main__":
    produce_message()
