## Image processing service

This is an example microservice API that demonstrates: 
- programming principles
- containerization config
- use of message brokers (producing and subscribing to messages)
- design and arhitecture
- transferring and handling of raw byte data

# Development process

Main app can be run directly using module src/main.py. However, the app needs Kafka dependency. These additional services can be run using docker images, located in root directory. When running these images, you need to also specify ports:

    docker build -t kafka -f ./Dockerfile-kafka . && docker run -p 9092:9092 -p 9093:9093  -itd kafka  