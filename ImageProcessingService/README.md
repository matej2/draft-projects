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

# AWS deployment

## Requirements

After my initial investigation I concluded that for AWS deployment I need the following services:

- Router 53: DNS settings and routing
- API gateways: To route API calls to lambda instances
- Lambda: Serverless computing with Amazon linux base image for running the API server
- Aurora: Serverless distributed sql database for storing information about images
- S3: Storage for distributing images
- SNS: Email notificatons for frequence app calls
- Cloud trail: Notifications for frequenct login retries
- App config: Env variables for allowed file types, default pagination

Code will be deployed using GitHub Actions. It will also run tests before every deployment.