# YotiSolution
The application is build using Spring Boot. The main class in Application.java. On successfull process of the input the data wil be stored in Postgres database. I have used Postgres 9.4 version database.
The table creation and sequence creation code is present in schema.sql and it is located in resource folder.
There is no need to run this script in the database as Spring boot will run this script when starting the application.

The application.properties file has details for the
__database server name (localhost)__,
__username__ and
__password__. 
__These needs to be changed match the running environment.__
The server port is set to 8085. This can also be changed to any desired port.
Once the database is set up and the details are correctly updated in the application.properties file the application can be run by maven command from the project folder having the pom.xml
> *mvn clean install
> *mvn spring-boot:run

Once the application has started you can go to the following link
[http://localhost:8085/swagger-ui.html]()
(please update the port number to match the port in server.port in application.properties

The swagger ui will have the details to post a json data as input and see the response. The Swagger UI has been updated to display the documentation for the API
