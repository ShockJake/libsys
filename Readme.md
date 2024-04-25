# Libsys

**Libsys** is a web application where people can share their thoughts about different books.

## Features

- Creation of blog posts.
- Authorization and authentication.
- Searching posts.
- Liking posts.
- Saving data on a database.
- Logging.
- Basic messaging system.

## Running

### Prerequisites

- Java 11
- Maven
- Git

### Steps

- Clone the repository:

    ```bash
  git clone https://github.com/ShockJake/libsys.git; cd libsys
    ``` 
- [Install](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/) Setup MySQL Database and fill the application properties with the database name and password

```mysql
CREATE DATABASE IF NOT EXISTS libsysDB;
CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'libsys_092314!';
GRANT ALL PRIVILEGES on *.* TO 'springuser'@'localhost' WITH GRANT OPTION;
```

- Compile with maven:

    ```bash
  mvn package spring-boot:repackage
    ```
- Define `STORAGE_FOLDER`
    - For Linux - `export STORAGE_FOLDER=path/to/folder/to/storage/files`
    - For PowerShell - `$Env:STORAGE_FOLDER = path\to\folder\to\storage\files`
    - For Windows CMD - `set STORAGE_FOLDER=path\to\folder\to\storage\files`

- Run the application

    ```bash
  java -jar target\libsys-0.0.1-SNAPSHOT.jar  
  ```

## Technologies

### Spring Framework

**Libsys** is build with [Spring Framework](https://spring.io/).
This framework was chosen because of:

1. **Modularity**: Spring follows a modular approach, allowing developers to use the components they need and ignore the
   ones they don't. This modularity promotes flexibility and makes the framework lightweight.
2. **Inversion of Control (IoC)**: Spring implements the principle of Inversion of Control, also known as dependency
   injection. This design pattern allows for loosely coupled components, making the application easier to maintain,
   test, and extend.
3. **Dependency Injection (DI)**: Spring's DI container manages the dependencies between objects, making it easier to
   develop and test applications. With DI, objects are given their dependencies instead of creating or looking for them.
4. **Transaction Management**: Spring provides a powerful abstraction for transaction management, supporting both
   programmatic and declarative transaction management. This simplifies the handling of database transactions in
   enterprise applications.
5. **Simplified Configuration**: Spring offers various configuration options, including XML configuration, Java-based
   configuration, and annotation-based configuration. These options allow developers to choose the most suitable
   approach based on their preferences and project requirements.
6. **Integration with Other Frameworks**: Spring integrates seamlessly with other frameworks and technologies, such as
   Hibernate, JPA, JDBC, and RESTful web services. This integration simplifies development tasks and enables developers
   to leverage existing technologies within their Spring-based applications.

Several helpful sub-libraries, that are provided by Spring Framework, are used:

- **Spring Data JPA** - is used to communicate with database via ORM.
- **Spring Validation** - is used to validate input from Client side.
- **Spring Web** - is used to build Web-side of application (Controllers and Routing).
- **Spring Thymeleaf** - is used to generate templated HTML pages.
- **Spring Security** - is used to secure Web components with authorization and authentication and password hashing.

### Logging

For logging the [LOG4J](https://logging.apache.org/log4j/2.x/) is used with the [SLF4J](https://www.slf4j.org/) on top.

### Testing

- **JUnit5** - is used to power-up the testing (Includes assertions and different testing tools)
- **Mockito** - is used to create Mocks of different components used in tests.
- **Hamcrest** - is used to create special matchers used in tests.

### Other

- **Lombok** - is used to create data classes.
- **mysql-connector** - is used for accessing MySql database.
- Apache **commons-io** and **commons-lang3** - Java utilities.

## Data Entities

Libsys data models and fields:

- User
  - User Id
  - Login
  - Password (encoded)
  - Name
  - User Role
  - Number of posts
- Post
  - Post id
  - Id of post writer
  - Post text
  - Header of post
  - Image file path (images are stored on hard drive)
  - Timestamp of creation
  - Is liked property
- Message
  - Message id
  - Message text
  - Receiver id
  - Sender id
  - Message status
- Libsys Request
  - Request id
  - Id of user that made a request
  - Request type
  - Request status
- Liked Post Entry
  - Entry id
  - Id of user that liked a post
  - Id of post
