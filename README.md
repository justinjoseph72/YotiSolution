# YotiSolution
The application is build using Spring Boot. The main class in Application.java. On successfull process of the input the data wil be stored in Postgres database. I have used Postgres 9.4 version database.

To run the application following needs to be done:
Set up a Postgres database. The port should be set to default value 5432. If using docker please make sure the port is updated to use 5432.
You can use any database within the postgres. I have used justindb. I have also tested it with default database postgres.
Make sure you are able to connect to the database and it has 'public' schema. The new table will be created in 'public' schema. Please create a schema named 'public' if not present.
The application.properties file needs to be updated for the following 
database-server : change it to the database server name. If running on local machine then keep it as localhost.
database-name : Name of the database in postgres. I have used justindb
user-name: change it to your database username.
password: change it to your database password.
the application will run on port 8085. If you wish to change it please update server.port property.
Please make sure java 8 is running on your machine.
Please make sure you have maven 3  installed on your machine.

After setting up the Postgres database and application.properties file the application is ready to be launched.

To run the application:
Open the terminal and move to the project root folder having the pom.xml.
Run the following maven commands
mvn clean install
mvn spring-boot:run
Once the spring boot application is up, please go to http://localhost:8085/swagger-ui.html

I have developed the code using InteliJ Idea IDE and if you wish to use it you can import the code directly to it from git and can run it from the IDE as it has maven support. Before doing it please make sure the Postgres database is setup and the application.properties are updated.

The resource folder in the project had a database script schema.sql. This has the code to create a sequence and a table in the postgres database. The application is set to run this script during startup so there is no need to run this script in the database manually. But if you wish to first run this and not having Spring do it all the time, you can update the application.properties file property spring.datasource.initialize to false.
After this the script will have to manually run first in the postgres database to create the tables and sequence and then the application should be started.

The test folder has Junit HooverServiceTest.java.
I have included few test cases to check validations and processing of the input data.

Assumptions made while developing the application
The input data should have roomSize,coords,patches and instructions. If any of the data is missing it will not process the input and a validation message will be shown. The messages are sent in the response headers.
The roomSize,coords and patches should not have negative values.
All the patches should be inside the room.
The coord should be inside the room.
The instruction string should be made up of combination of NSEW. No other directions are allowed.
Each instruction will move the hoover to one block in the given direction. Eg If the hoover is in postion [1,2], then N will move the hoover one block in north direction and new position will be [1,3].



