import base64
import json
import os

from confluent_kafka import Producer

from dto.Request import ImageUploadRequest

# Get Kafka broker address from environment variable or use default
# When running in Docker container, use 'broker:9092'
# When running on host machine, use 'localhost:9092'
kafka_broker = os.getenv("KAFKA_BROKER", "localhost:9092")

producer_config = {
    "bootstrap.servers": kafka_broker,
    "socket.timeout.ms": 10000,  # 10 second timeout
}

producer = Producer(producer_config)

def delivery_report(err, msg):
    if err:
        print(f"Message delivery failed: {err}", flush=True)
    else:
        print("Delivery OK", flush=True)
        print(f"Partition {msg.partition()} on topic {msg.topic()}, offset {msg.offset()}", flush=True)

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
        print("Message produced successfully!", flush=True)
    except Exception as e:
        print(f"Error producing message: {e}", flush=True)


if __name__ == "__main__":
    produce_message()
