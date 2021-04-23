# GeneData Rest Service
22-Apr-2020, David Herzig

This software component is part of the FHNW Medical Informatics Software project.

## Description
This component access the gene information data in the data base and
provides an interface for 3rd party applications to access this data.

## Build process
Apache Maven is used for the build process. With the following
command the application could be created:

mvn clean package -DskipTests

The skipTest part is needed at the moment as the unit test topic
has not yet been covered.

## Run
### Command Line
After building the package, the following command could be used
to start the application:

    java -jar target/JAR_FILE.jar

Adapt the JAR_FILE with the real file name, depending on your project
settings.

### Development Environment
Right click on the RestServiceApplication class and start the program.

## Port Configuration
The default port for the server component is 8080. Sometimes, this port
is already in use by other software components. To adjust the port,
adapt the server.port property in the file:

    src/main/resources/application.properties

## Database Connection
All data is retrieved from a database. All the details about the database connection
(e.g. host, username, password) is located in

    src/main/resources/application.properties

Adapt all values for your environment.

## Retrieve Data
Sample call from the browser:

    http://localhost:8080/geneservice/byid?id=11494045
    http://localhost:8080/geneservice/bysymbol?symbol=NDAI0A08340

