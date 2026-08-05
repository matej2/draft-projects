def handle_requests(event, context):
    print(event)

    return {
        "statusCode": 200, "body": "Hello world"
    }