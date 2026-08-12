import json
import os

from confluent_kafka import Consumer

from domain.factory.ImageFactory import create_image_from_json_dict
from service.DatabaseService import save_using_session
from util.LoggingUtil import LoggingUtil

kafka_broker_url = os.getenv("KAFKA_BROKER_URL", "localhost")
kafka_broker_port = os.getenv("KAFKA_BROKER_PORT", "9092")

kafka_broker = f"{kafka_broker_url}:{kafka_broker_port}"

consumer_config = {
    "bootstrap.servers": kafka_broker,
    "socket.timeout.ms": 10000,  # 10 second timeout
    "group.id": "order-tracker",
    "auto.offset.reset": "earliest",
}

logger = LoggingUtil.get_logger(__name__)

def subscribe():
    consumer = Consumer(consumer_config)
    consumer.subscribe(["images"])

    logger.info("Consumer is running and subscribed to Kafka topic orders")

    try:
        while True:
            msg = consumer.poll(1.0)
            if msg is None:
                continue
            if msg.error():
                logger.error("Consumer error: {}".format(msg.error()))

            value = msg.value().decode("utf-8")
            json_image = json.loads(value)

            image = create_image_from_json_dict(json_image)
            save_using_session(image)

            logger.info(f"Created image: {image.name}")

    except KeyboardInterrupt:
        logger.info("\nClosing Kafka Consumer")
    finally:
        consumer.close()

if __name__ == "__main__":
    subscribe()
