# Courier Tracking API

A Spring Boot REST API for tracking courier locations and calculating distances.

## Prerequisites
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)
- (Optional) [Java 17+](https://adoptopenjdk.net/) and [Maven](https://maven.apache.org/) if running locally without Docker

## Quick Start (Recommended)

### 1. Clone the repository

```bash
git clone https://github.com/DenizOzal/courier-tracking-service.git
cd courier-tracking-service
```

### 2. Build & Run with Docker Compose

```bash
./mvn clean package -DskipTests
# or: mvnw clean package -DskipTests

docker-compose up --build
```

- The API will be available at: http://localhost:8080

### 3. Run Locally (Without Docker)

```bash
./mvnw spring-boot:run
# or: mvn spring-boot:run
```

## API Endpoints
- **POST** `/api/courier/location` – submit single courier location
- **POST** `/api/courier/location/batch` – submit list of courier locations
- **GET** `/api/courier/distance/{courierId}` – get total distance for courier

## Running Tests

```bash
./mvnw test
# or: mvn test
```

## Design Patterns Used
- **Strategy Pattern** for distance calculation (Haversine or Euclidean)
- **Singleton Pattern** via Spring's `@Service` for `CourierService`

## Troubleshooting
- If you encounter port conflicts, ensure port 8080 is free or update `docker-compose.yml` and `application.yaml`.
- For database issues, check Docker logs for the database container.
- If `./mvnw` is not executable, run `chmod +x mvnw`.

## Contact
For questions or issues, please open an issue in this repository.
