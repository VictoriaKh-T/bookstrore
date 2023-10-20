# Java Bookstore Project

This project is an example of a Java application that implements a book catalog and includes 
a testing module to verify its functionality. 
It utilizes Docker for containerization and Swagger for API documentation.

## Project Description

In this project, the following functionality is implemented:

- Adding new books.
- Searching for books by title, author and categories.
- Removing books from the catalog.
- Displaying a list of all books in the catalog.
- Authentication.
- Choose book to shopping cart. 
- Create order.

## Project Requirements

To run this project and execute the tests, you will need:

- Java 17;
- JUnit 5 ;
- Spring Boot;
- Maven;
- Docker;
- Swagger;
- Lombok;
- MySQL;
- JWT(JSON Web Tokens);
- Spring Security;
- Liquibase;
- Jackson Datatype JSR-310;
- Postman;

<table>
  <tr>
    <td style="font-size: 16px;"><strong>Project Structure</strong></td>
  </tr>
  <tr>
    <td>
      <table style="font-size: 12px;">
        <tr>
          <td><strong>src/main/resources</strong></td>
            <td>Application resources,including configuration files and Liquibase scripts.</td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/config</strong></td>
          <td>Application configuration. These files define various settings and properties for the application.</td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/controller</strong></td>
          <td>Controllers handle incoming HTTP requests, perform actions, and return responses.</td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/mapper</strong></td>
           <td>The "mapper" directory contains mapper interfaces and classes. </td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/model</strong></td>
          <td>This directory contains all the models used by the project. </td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/repository</strong></td>
          <td>Repository classes are responsible for interacting with the database and querying the data storage.</td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/service</strong></td>
          <td>The service directory contains the service classes which encapsulate the application's business logic.</td>
        </tr>
        <tr>
          <td><strong>mate/academy/bookstore/security</strong></td>
          <td>This directory is dedicated to security-related components, including JWT TOKEN.</td>
        </tr>
        <tr>
          <td><strong>src/test</strong></td>
          <td>Tests for the project are here.</td>
        </tr>
      </table>
    </td>
  </tr>
</table>

## Running the Project

1. Clone the repository to your computer.

2. Open the project in your favorite Integrated Development Environment (IDE).

3. Use the build system (Maven or Gradle) to build the project.

4. Run the application.

## Additional Information

### Swagger Documentation

This project uses Swagger for API documentation. You can access the API documentation by navigating 
to http://localhost:8081/bookstore/swagger-ui/index.html after running the application.


### Docker Containerization

This project is Dockerized for easy deployment. To build a Docker container, use the following commands:

``` 
docker build -t bookstore-app .
docker run -p 8080:8080 bookstore-app
``` 

## Running Tests

1. Ensure that the project is built.

2. Use the build system to run the tests.

Example of running tests using Maven:

```shell
mvn test

