# Employee Attendance System - Backend

A production-grade Spring Boot application for managing employee attendance tracking with JWT-based authentication, role-based access control, and comprehensive reporting features.

## Tech Stack

- **Java 17+**
- **Spring Boot 4.0.2**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Database Migrations)
- **Maven**
- **Lombok**

## Features

### 👤 Employee Features
- ✅ User registration and JWT authentication
- ✅ Check-in/Check-out functionality with automatic status calculation
- ✅ Prevent duplicate check-ins per day
- ✅ Prevent check-out without check-in
- ✅ Attendance history with pagination and date range filtering
- ✅ Monthly attendance summary with stats (present, absent, late, half-day)
- ✅ Personal dashboard with today's status and 7-day history
- ✅ Total hours worked calculation with break deduction

### 👨‍💼 Manager Features
- ✅ View all employee attendances with date filtering
- ✅ Filter by employee, date range, and status
- ✅ View specific employee attendance history
- ✅ Team attendance summary
- ✅ Late approval workflow
- ✅ Manager dashboard with team metrics
- ✅ CSV export with streaming (production-optimized)

### 🔐 Security
- ✅ Stateless JWT authentication
- ✅ BCrypt password encryption
- ✅ Role-based access control (EMPLOYEE, MANAGER)
- ✅ Method-level security annotations
- ✅ CORS configuration for frontend integration
- ✅ Token expiry handling (24 hours default)

### 📊 Business Logic
- ✅ Automatic status determination (PRESENT, LATE, HALF_DAY, ABSENT)
- ✅ Late threshold configurable (default: 9:30 AM)
- ✅ Break deduction in hours calculation (default: 30 minutes)
- ✅ Scheduled job for auto-marking absent employees
- ✅ Composite unique constraint on (user_id, date)

## Project Structure

```
src/
├── main/
│   ├── java/com/srinath/attendance/
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST API endpoints
│   │   │   ├── AuthController
│   │   │   ├── EmployeeController
│   │   │   └── ManagerController
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/              # JPA entities
│   │   ├── exception/           # Custom exceptions
│   │   ├── repository/          # Data access layer
│   │   ├── security/            # JWT & Security
│   │   └── service/             # Business logic
│   └── resources/
│       ├── application.yaml     # Configuration
│       └── db/migration/        # Flyway migrations
└── test/
    └── java/                    # Unit & Integration tests
```

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.8+

## Setup Instructions

### 1. Database Setup

```bash
# Create PostgreSQL database
createdb attendance_db

# Set user password
psql -c "ALTER USER postgres WITH PASSWORD '231429';"
```

### 2. Environment Configuration

Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

Or configure `application.yaml` directly with your database details.

### 3. Build and Run

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Or run the JAR
java -jar target/attendance-management-system-0.0.1-SNAPSHOT.jar
```

The application will start at `http://localhost:8081`

## API Endpoints

### Authentication
```
POST   /api/auth/register          - Register new employee
POST   /api/auth/login             - Login and get JWT token
```

### Employee Endpoints
```
POST   /api/employee/check-in              - Check-in
POST   /api/employee/check-out             - Check-out
GET    /api/employee/attendance/today      - Today's attendance
GET    /api/employee/attendance/history    - Attendance history (paginated)
GET    /api/employee/attendance/monthly/{month}/{year}  - Monthly summary
GET    /api/employee/dashboard             - Employee dashboard
```

### Manager Endpoints
```
GET    /api/manager/attendance             - All attendances (with filters)
GET    /api/manager/attendance/{userId}    - Employee attendance history
POST   /api/manager/attendance/{id}/approve-late  - Approve late arrival
GET    /api/manager/dashboard              - Manager dashboard
GET    /api/manager/export/csv             - CSV export
```

## Example API Usage

### Register Employee
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john@company.com",
    "password": "Secure@123",
    "departmentId": "550e8400-e29b-41d4-a716-446655440101"
  }'
```

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@company.com",
    "password": "Secure@123"
  }'
```

### Check-in
```bash
curl -X POST http://localhost:8081/api/employee/check-in \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -H "Content-Type: application/json"
```

### Get Dashboard
```bash
curl -X GET http://localhost:8081/api/employee/dashboard \
  -H "Authorization: Bearer {JWT_TOKEN}"
```

## Configuration

Key configuration properties in `application.yaml`:

```yaml
server:
  port: 8081

jwt:
  secret: 12345678901234567890123456789012  # Change in production!
  expiration: 86400000  # 24 hours in milliseconds

office:
  start-time: "09:00"
  end-time: "17:00"
  late-threshold: "09:30"
  break-duration-minutes: 30
```

## Database Migrations

Migrations are automatically run on startup using Flyway:

- **V1__initial_schema.sql** - Creates all tables with constraints and indexes
- **V2__seed_master_data.sql** - Seeds roles, departments, and test users

Test Users:
- Employee: john.smith@company.com / Employee@123
- Manager: alex.manager@company.com / Employee@123

## Testing

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=ClassName
```

### Generate Code Coverage
```bash
./mvnw test jacoco:report
```

## Production Deployment

### Before Deployment

1. **Update JWT Secret**
   ```yaml
   jwt:
     secret: <generate-a-strong-secret-key>  # Use: openssl rand -base64 32
   ```

2. **Update Database Credentials**
   - Use environment variables or secrets management
   - Never commit passwords to repository

3. **Update CORS Origins**
   ```java
   configuration.setAllowedOrigins(Arrays.asList("https://yourfrontend.com"));
   ```

4. **Configure Logging**
   - Set `logging.level.root: INFO` in production
   - Monitor logs with ELK stack or similar

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Kubernetes Deployment

Create `deployment.yaml` with:
- Proper resource limits
- Health check probes
- Secret management for JWT key and DB credentials

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.yaml
server:
  port: 8082
```

### Database Connection Failed
```bash
# Verify PostgreSQL is running
psql -U postgres -d attendance_db

# Check connection string in application.yaml
spring.datasource.url: jdbc:postgresql://localhost:5432/attendance_db
```

### JWT Token Invalid
- Ensure JWT secret is consistent
- Check token expiration time
- Verify Authorization header format: `Bearer {token}`

## Performance Optimization

- ✅ Database indexes on frequently queried columns
- ✅ Pagination on all list endpoints
- ✅ Lazy loading for relationships
- ✅ Connection pooling (HikariCP)
- ✅ Batch processing for large operations
- ✅ CSV streaming to avoid memory issues

## Monitoring & Logging

The application includes comprehensive logging:

```properties
logging.level.com.srinath.attendance: DEBUG
logging.level.org.springframework.security: DEBUG
logging.level.org.hibernate.SQL: DEBUG
```

Monitor these metrics:
- JWT token validation failures
- Database query performance
- Attendance check-in/out operations
- Late approval workflows

## Contributing

1. Follow the existing code structure
2. Add unit tests for new features
3. Ensure all tests pass before committing
4. Update documentation

## License

This project is proprietary and confidential.

## Support

For issues or questions, contact the development team.

