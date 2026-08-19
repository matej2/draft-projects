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

Data seeding will use a separate database account.

Data seeing is done using docker entrypoint shell scripts. 

Scripts are designed that they

## TODO: Authentication

Generate a keypair file: openssl genrs -out keypair.pen 2048

Use keypair to generate public key: openssl rsa -in keypair.pen -pubout -out public.pem

Use keypair to generate privatekey: openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

After this you can remove keypair file.

## TODO: Profile setup, deploy configuration, docker