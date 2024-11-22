# Stack Overflow Clone

This project is a clone of Stack Overflow capabilities through REST APIs. It serves as a playground for learning and mastering Spring Boot.

## Features

- User registration and authentication
- CRUD operations for questions and answers
- Voting system for questions and answers
- Tagging system for questions
- Search functionality

## Technologies Used

- Java
- Spring Boot
- Maven
- PostgreSQL
- Testcontainers
- JUnit

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker

### Installation

1. Clone the repository:
	```sh
	git clone https://github.com/francislainy/so-be.git
	cd so-be
	```

2. Build the project:
	```sh
	mvn clean install
	```

3. Run the application locally (having a PostgreSQL instance running on port 5432 and a database named "so"):
	```sh
	mvn spring-boot:run
	```

4. Run the application through TestContainers:
Make sure you have Docker installed and running on your machine, then run the TestSoBeApplication class inside the scr/test/ package through the below command:
	```sh
	mvn test -Dtest=TestSoBeApplication
	```

### Running Tests

To run the tests, use the following command:
```sh
mvn test
