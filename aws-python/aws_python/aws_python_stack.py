from aws_cdk import (
    # Duration,
    Stack,
    # aws_sqs as sqs,
    aws_lambda as _lambda,
aws_apigateway as apigw
)
from constructs import Construct

class AwsPythonStack(Stack):

    def __init__(self, scope: Construct, construct_id: str, **kwargs) -> None:
        super().__init__(scope, construct_id, **kwargs)

        # The code that defines your stack goes here

        # example resource
        # queue = sqs.Queue(
        #     self, "AwsPythonQueue",
        #     visibility_timeout=Duration.seconds(300),
        # )

        fn = _lambda.Function(
            self,
            "MyFunction",
            runtime=_lambda.Runtime.PYTHON_3_14,
            handler="main.handle_requests",
            code=_lambda.Code.from_asset("app")
        )

        fn_url =  fn.add_function_url(
            auth_type=_lambda.FunctionUrlAuthType.NONE,
            cors=_lambda.FunctionUrlCorsOptions(
                allowed_methods=[_lambda.HttpMethod.GET],
                allowed_origins=["*"],
                allowed_headers=["*"]
            )
        )

        endpoint = apigw.LambdaRestApi(
            self,
            "ApiGwEndpoint",
            handler=fn,
            rest_api_name="HelloApi",
        )
