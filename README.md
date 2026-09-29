# Bioshield

Bioshield is a full-stack laboratory equipment management system. It provides an intuitive, responsive dashboard for lab administrators and personnel to track equipment, operational statuses, and calibration schedules, ensuring compliance and efficiency.

## Architecture

This project is built using a monolithic backend architecture with a separated frontend client:

* **Backend:** Java 17 with Spring Boot. Uses a standard layered architecture consisting of Models (Entities), Repositories (Data Access), Services (Business Logic), and Controllers (REST APIs).
* **Frontend:** Angular 17+ (located in the `bioshield-frontend/` directory).
* **Database:** PostgreSQL for production data, and an H2 in-memory database for unit testing.
* **Deployment:** The backend is containerized using Docker (optimized for Google Cloud deployment), and the frontend is optimized for Firebase Hosting.

## Prerequisites

Before running the application locally, ensure you have the following installed:
* Java JDK 17
* Node.js (v18+)
* Angular CLI (`npm install -g @angular/cli`)
* Maven (optional, as the project includes the Maven Wrapper `mvnw`)
* PostgreSQL database

## Getting Started

### 1. Backend Setup (Spring Boot)

1. Open a terminal at the root directory of the project.
2. Ensure your PostgreSQL server is running. You may need to configure your database credentials (URL, username, password) inside `src/main/resources/application.properties` or `application.yml` if not using environment variables.
3. Build the backend using the included Maven wrapper:
   ```bash
   ./mvnw clean install
   ```
4. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The backend API will start and listen on `http://localhost:8080`.

### 2. Frontend Setup (Angular)

1. Open a new terminal and navigate to the frontend directory:
   ```bash
   cd bioshield-frontend
   ```
2. Install the necessary Node.js dependencies:
   ```bash
   npm install
   ```
3. Start the Angular development server:
   ```bash
   ng serve
   ```
4. Open your browser and navigate to `http://localhost:4200` to view the application.

## Testing

The backend includes a comprehensive suite of unit tests verifying business logic and REST endpoints. These tests utilize JUnit 5, Mockito, and a runtime H2 in-memory database.

To execute the backend test suite, run:
```bash
./mvnw test
```

## Docker Containerization

The backend application can be containerized using the provided `Dockerfile`. It utilizes a multi-stage build process to optimize image size and security by separating the build environment from the runtime environment.

1. Build the Docker image from the project root:
   ```bash
   docker build -t bioshield-backend .
   ```
2. Run the Docker container (mapping port 8080):
   ```bash
   docker run -p 8080:8080 bioshield-backend
   ```
