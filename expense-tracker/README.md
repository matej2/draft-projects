# About

This project is inspired by: https://roadmap.sh/projects/expense-tracker-api

## Domain model

Each expense should include expense cost, frequency, note and category. Expense cost should include tax and shipment cost and be normalized to be in single currency. Note should include a text that identifies that event - item name(s), location or event note. 

Frequency includes a name and number of appearances in year. Although most business cases are focused on monthly frequency, it is important to notice that some edge cases include yearly frequency. Predefined categories are as follows:  

- Yearly: 1
- Monthly: 12
- Daily: 365
- Weekly (average): 52

Expense can fall into one or more categories. Category includes name and descriptions. According to business use case it would be beneficial that app should provide a way to optimize costs. Therefore, category should also include data flag if this cost is leisure / one time cost. Predefined categories are as follows:

- Groceries
- Leisure
- Electronics
- Utilities
- Clothing
- Health
- Others

Result of this application would be to provide an insight in recurring and non-recurring costs and provide future cost predictions with a certain percentage probability. Besides that it should also provide a way to optimize costs by restructuring them or removing unnecessary purchases.

## Database user permissions

Database access permissions are designed with principle of least privilege in mind. I decided to use FlyWay. It requires to disable Hibernate, so I defined all DDL statements in a single file, along with permission definition. This file is then copied in database container.

We can further verify that permissions were properly created by executing `\ddp` command inside psql interface.

## Data seeding

After considering business case, I decided to with CommandLineRunner. It defines a seeding method that check if each frequency is present and if its not, adds it.   
This method is async and it runs in a separate transaction.

I later decided to implement a separation of concerns by completely separating this process. 

I created a shell script that would create tables and insert data and create user. The user that is created is granted only the necessary permissions that are required for main application.

This user is then referenced in application when we initialize database connection. Specifically, this is done by specifying a reference to environment variable that defines password for that user.


## Authentication

Generate a keypair file: openssl genrs -out keypair.pen 2048

Use keypair to generate public key: `openssl rsa -in keypair.pen -pubout -out public.pem`

Use keypair to generate privatekey: `openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem`

After this you can remove keypair file.

## JWT token authentication

Previously generated keypair is used to sign and validate JWT. A custom user detail class is used to hold data about user.

# Docker deployment

Dockerfile defines multi stage build pharse. In first stage it installs maven and downloads dependencies. Second stage builds the application. It copies source and runs build command. The third stage runs the application. It copies jar files from previous image and runs it.


Docker compose file was modified to run this image. Configuration adds environment variables for datasource (url, username and password), specifies ddl auto (validate) and ads dependency on database config container.

Dot env file was added to provide values for passwords when running locally - these values can be set elsewhere when running in CD jobs. Docker compose will use these files to initialize database, add users and connect to database.



## TODO: Profile setup, deploy configuration, docker