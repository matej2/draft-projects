
# Sample app

This is a sample Python app that we use to deploy to AWS. It returns visitor count and a user id.

We do deployment on Lambda service. Database is set up in DynamoDB. 

# CD

Besides that, there is also a Github CD configuration to deploy code on every push to master. 

CD pipeline uses secrets that are read from env variables. The following values are required in "Secrets and variables" section in your GitHub environment:

- BETA_AWS_ACCESS_KEY_ID
- BETA_AWS_SECRET_ACCESS_KEY

These access credentials are set for non-root user in IAM that is configured in a way so that it can only deploy app to Lambda. Another separate user is configured to only access DynamoDB.

## Deployment

Deployment can be done using either:

- stack deployment script found in `aws_python/aws_python_stack.py`. This script gets invoked when running `cdk deploy`
- running command `sam deploy`

After running deploy, we can see our deployed stack in CloudFormation (service to manage resources). We can also go directly to Lambda service and see if instance was created.

## Permissions

For permission management we use connectors. For each source service we can define destination and type of connection (write, read). If we want more granular and controlled permision management we can use policy (inline, managed)

## Network

Virtual private cloud is used to configure what is private and what is internet facing. We can configure private an public subnet. Services inside private subnets can still reach outside internet (example for updates) if you set NAT. 

Use security groups to more granually define which services can talk to each other.



## Commands

For each region you need to run:

    cdk bootstrap

Next you deploy your stack using the command:

    cdk deploy

This will output changes to IAM. Confirm them. After the deploy is done you can see your stack in "CloudFormation" section in 


## Useful commands

 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

