# Courier Tracking API

### Setup
- Requires Docker & Docker Compose

### Build & Run
```bash
./mvnw clean package -DskipTests
docker-compose up --build
```

### Endpoints
- POST `/api/courier/location` – submit single courier location
- POST `/api/courier/location/batch` – submit list of courier locations
- GET `/api/courier/distance/{courierId}` – get total distance for courier

### Design Patterns Used
- **Strategy Pattern** for distance calculation (Haversine or Euclidean)
- **Singleton Pattern** via Spring's `@Service` for `CourierService`