import json
import os

from confluent_kafka import Consumer
from opensearchpy import OpenSearch

from factory.ImageFactory import create_image_from_json_dict
from service.DatabaseService import save_using_session

kafka_broker = os.getenv("KAFKA_BROKER", "localhost:9092")

consumer_config = {
    "bootstrap.servers": kafka_broker,
    "socket.timeout.ms": 10000,  # 10 second timeout
    "group.id": "order-tracker",
    "auto.offset.reset": "earliest",
}

host = 'localhost'
port = 9200
auth = ('admin', 'admin') # For testing only. Don't store credentials in code.

# Create the client with SSL/TLS enabled, but hostname verification disabled.
client = OpenSearch(
    hosts = [{'host': host, 'port': port}],
    http_compress = True, # enables gzip compression for request bodies
    http_auth = auth,
)

def subscribe():
    consumer = Consumer(consumer_config)
    consumer.subscribe(["images"])

    print("Consumer is running and subscribed to Kafka topic orders")

    try:
        while True:
            msg = consumer.poll(1.0)
            if msg is None:
                continue
            if msg.error():
                print("Consumer error: {}".format(msg.error()))

            value = msg.value().decode("utf-8")
            json_image = json.loads(value)

            image = create_image_from_json_dict(json_image)
            save_using_session(image)

    except KeyboardInterrupt:
        print("\nClosing Kafka Consumer")
    finally:
        consumer.close()

if __name__ == "__main__":
    subscribe()
