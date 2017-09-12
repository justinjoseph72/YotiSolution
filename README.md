# YotiSolution
The application is build using Spring Boot. The main class in Application.java. On successfull process of the input the data wil be stored in Postgres database. I have used Postgres 9.4 version database.
The table creation and sequence creation code is present in schema.sql and it is located in resource folder.
There is no need to run this script in the database as Spring boot will run this script when starting the application.

The application.properties file has details for the
> * __database server name (localhost)__
> * __username__ 
> * __password__

__The above properties needs to be changed match the running environment.__
The database-name proptery __should not be changed__ from 'postgres'.

The server port is set to 8085. This can also be changed to any desired port.
Once the database is set up and the details are correctly updated in the application.properties file the application can be run by maven command from the project folder having the pom.xml
> * mvn clean install
> * mvn spring-boot:run

Once the application has started you can go to the following link
[http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)

__please update the port number to match the port in server.port in application.properties__

The endpoint /cleanRoom is a PoST request which takes a json input and produces a json output.
The Swagger UI has been updated to display the documentation for the API operations and the response codes definition. In case of error the error message is passed in the response headers.


