## Database migrations and seeding

 Project uses init shell scripts for migration. These are copied into Postgres container and executed when container runs.

Shell script is used to create tables, insert data and create separate user. Database access permissions are designed with principle of least privilege in mind. We can further verify that permissions were properly created by executing `\ddp` command inside psql interface.

App is configured with separation of concerns in mind. A separate user is granted only the necessary permissions that are required for main application to run. This user is then referenced in application when initializing database connection. 

# Docker deployment

Dockerfile defines multi stage build pharse. In first stage it installs maven and downloads dependencies. Second stage builds the application. It copies source and runs build command. The third stage runs the application. It copies jar files from previous stage and runs it.


Docker compose file uses this image. Its configuration adds environment variables for datasource (url, username and password), specifies ddl auto (validate) and ads dependency on database config container.

`.env` file provides values for credentials when running locally - these values can be set elsewhere when running in CD jobs. Docker compose will use these files to initialize database, add users and connect to database.


