import os

import boto3

def handle_requests(event, context):
    path = event["path"]
    if path != "/":
        return {
            "statusCode": 404,
            "body": "Not found"
        }

    personId = event["queryStringParameters"]["personId"]
    dynamodb = boto3.resource("dynamodb")
    table = dynamodb.Table(os.environ.get("TABLE_NAME"))
    response = table.get_item(key={"key": "visit_count"})

    if "Item" in response:
        visit_count = response["Item"]["visit_count"]
    else:
        visit_count = 0

    new_visit_count= visit_count + 1
    table.put_item(Item={"visit_count": new_visit_count})

    return {
        "statusCode": 200,
        "body": json.dumps({
            "personId": personId + " from Lambda",
            "message": "Hello world"
        }),
        "visit_count": new_visit_count,
    }