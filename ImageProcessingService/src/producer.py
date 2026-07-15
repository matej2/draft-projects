import json
import uuid
import os

from confluent_kafka import Producer

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
        print(f"Delivery OK: {msg.value().decode('utf-8')}", flush=True)

def produce_message():
    try:
        order = {
            "order_id": str(uuid.uuid4()),
            "user": "John"
        }

        # Encode to byte format
        value = json.dumps(order).encode("utf-8")

        producer.produce(
            topic="orders",
            value=value,
            callback=delivery_report)
        producer.flush()
        print("Message produced successfully!", flush=True)
    except Exception as e:
        print(f"Error producing message: {e}", flush=True)


if __name__ == "__main__":
    produce_message()
