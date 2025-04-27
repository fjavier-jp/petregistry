# Pet Registry Application

The **Pet Registry Application** is a Spring Boot-based project designed to manage pets and their owners. It provides RESTful APIs for creating, retrieving, updating, and deleting pets and their associated owners. The application also supports file uploads for pet pictures and includes validation, exception handling, and testing.

---

## Features

- **Pet Management**: Create, retrieve, and delete pets.
- **Owner Management**: Retrieve owner details based on pet information.
- **File Uploads**: Upload and store pet pictures.
- **Validation**: Input validation for pet and owner data.
- **Pagination and Filtering**: Query pets with filters and pagination.
- **Error Handling**: Global exception handling for resource and storage errors.
- **Testing**: Comprehensive unit and integration tests.

---

## Technologies Used

- **Spring Boot**: Backend framework.
- **Spring Data JPA**: Database interaction.
- **H2 Database**: In-memory database for development and testing.
- **Lombok**: Simplifies Java code with annotations.
- **Mockito**: Mocking framework for unit tests.
- **JUnit 5**: Testing framework.
- **DataFaker**: Generates fake data for testing.
- **Maven**: Build and dependency management.

---

## Project Structure

---

## Setup Instructions

### Prerequisites

- **Java 17 or higher**
- **Maven**

### Steps to Run

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd petregistry

2. Build the project:
    ```bash
    ./mvnw clean install

3. Run the application:
    ```bash
    ./mvnw spring-boot:run

4. Access the application:

    http://localhost:8080

Here is the entire README.md in proper Markdown format:

```markdown
# Pet Registry Application

The **Pet Registry Application** is a Spring Boot-based project designed to manage pets and their owners. It provides RESTful APIs for creating, retrieving, updating, and deleting pets and their associated owners. The application also supports file uploads for pet pictures and includes validation, exception handling, and testing.

---

## Features

- **Pet Management**: Create, retrieve, and delete pets.
- **Owner Management**: Retrieve owner details based on pet information.
- **File Uploads**: Upload and store pet pictures.
- **Validation**: Input validation for pet and owner data.
- **Pagination and Filtering**: Query pets with filters and pagination.
- **Error Handling**: Global exception handling for resource and storage errors.
- **Testing**: Comprehensive unit and integration tests.

---

## Technologies Used

- **Spring Boot**: Backend framework.
- **Spring Data JPA**: Database interaction.
- **H2 Database**: In-memory database for development and testing.
- **Lombok**: Simplifies Java code with annotations.
- **Mockito**: Mocking framework for unit tests.
- **JUnit 5**: Testing framework.
- **DataFaker**: Generates fake data for testing.
- **Maven**: Build and dependency management.

---

## Project Structure

```
src/
├── main/
│   ├── java/org/hopto/fjavierjp/petregistry/
│   │   ├── api/               # REST controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── exception/         # Custom exceptions and handlers
│   │   ├── factory/           # Factory classes
│   │   ├── mapper/            # Entity-to-DTO mappers
│   │   ├── model/             # JPA entities
│   │   ├── repository/        # Spring Data JPA repositories
│   │   ├── request/           # Query parameter handling
│   │   ├── service/           # Business logic services
│   │   └── util/              # Utility classes
│   └── resources/
│       ├── application.properties  # Main configuration
│       ├── import.sql              # Initial database data
│       └── static/uploads/images/  # Uploaded pet images
└── test/
    ├── java/org/hopto/fjavierjp/petregistry/
    │   ├── integration/       # Integration tests
    │   ├── unit/              # Unit tests
    └── resources/
        └── application-test.properties  # Test configuration
```

---

## Setup Instructions

### Prerequisites

- **Java 17 or higher**
- **Maven**

### Steps to Run

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd petregistry
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Access the application:
   - API Base URL: `http://localhost:8080`

---

## API Endpoints

### Pet Endpoints

- **GET /pets**: Retrieve all pets with optional filters and pagination.
- **GET /pets/{id}**: Retrieve a specific pet by ID.
- **POST /pets**: Create a new pet (multipart form data).
- **DELETE /pets/{id}**: Delete a pet by ID.

### Owner Endpoints

- **GET /owners/pets/{petId}**: Retrieve the owner of a specific pet.

---

## Configuration

### Application Properties

The application uses the following configuration properties:

```properties
# Database
spring.datasource.url=jdbc:h2:mem:petregistry
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop

# File Storage
storage.location=uploads/
storage.images.location=uploads/images/

# Multipart File Uploads
spring.servlet.multipart.max-file-size=128KB
spring.servlet.multipart.max-request-size=128KB
```

### Test Configuration

The test environment uses an in-memory H2 database:

```properties
spring.datasource.url=jdbc:h2:mem:petregistrytest
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## Testing

### Running Tests

To run all tests, use:
```bash
./mvnw test
```

### Test Coverage

- **Unit Tests**: Located under `src/test/java/org/hopto/fjavierjp/petregistry/unit/`.
- **Integration Tests**: Located under `src/test/java/org/hopto/fjavierjp/petregistry/integration/`.

---

## Example Data

The application initializes the database with sample data from `import.sql`:

- **Owners**: Predefined owners with names, addresses, and phone numbers.
- **Pets**: Predefined pets with names, species, and associated owners.

---

## Contributors

- **Author**: Francisco Javier Jiménez Paredes
- **Contact**: `https://github.com/fjavier-jp`

---