
# Stack blueprint

This is a sample Python app that we use to deploy to AWS. It returns visitor count and a user id.

We do deployment on Lambda service. Database is set up in DynamoDB. Besides that, there is also a Github CD configuration to deploy code on every push to master. 

This config uses secrets that are read from env variables. The following values are required in "Secrets and variables" section:

- BETA_AWS_ACCESS_KEY_ID
- BETA_AWS_SECRET_ACCESS_KEY

There is a non-root user in IAM that is set up so that it can only deploy app to Lambda. Besides that another user is configured to access DynamoDB.

Deployment can be done using either:

- stack deployment script found in `aws_python/aws_python_stack.py`
- running command `SAM deploy`





## Commands

For each region you need to run:

    cdk bootstrap

Next you deploy your stack using the command:

    cdk deploy

This will output changes to IAM. Confirm them. After the deploy is done you can see your stack in "CloudFormation" section in 

# Welcome to your CDK Python project!

This is a blank project for CDK development with Python.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

This project is set up like a standard Python project.  The initialization
process also creates a virtualenv within this project, stored under the `.venv`
directory.  To create the virtualenv it assumes that there is a `python3`
(or `python` for Windows) executable in your path with access to the `venv`
package. If for any reason the automatic creation of the virtualenv fails,
you can create the virtualenv manually.

To manually create a virtualenv on MacOS and Linux:

```
$ python3 -m venv .venv
```

After the init process completes and the virtualenv is created, you can use the following
step to activate your virtualenv.

```
$ source .venv/bin/activate
```

If you are a Windows platform, you would activate the virtualenv like this:

```
% .venv\Scripts\activate.bat
```

Once the virtualenv is activated, you can install the required dependencies.

```
$ pip install -r requirements.txt
```

At this point you can now synthesize the CloudFormation template for this code.

```
$ cdk synth
```

To add additional dependencies, for example other CDK libraries, just add
them to your `requirements.txt` file and rerun the `python -m pip install -r requirements.txt`
command.

## Useful commands

 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

