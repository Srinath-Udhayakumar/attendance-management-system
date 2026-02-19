# Quick Start Guide - Employee Attendance System

## 🚀 First Time Setup (5 minutes)

### 1. Prerequisites Check
```bash
# Check Java version
java -version        # Should be 17+

# Check PostgreSQL
psql --version       # Should be 12+
```

### 2. Create Database
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE attendance_db;

# Exit psql
\q
```

### 3. Configure Application
```bash
# Copy environment template
cp .env.example .env

# Edit .env with your database credentials (if needed)
# Default: postgres / 231429
```

### 4. Build & Run
```bash
# Navigate to project directory
cd attendance-system/backend/attendance-system

# Build the project
./mvnw clean package -DskipTests

# Run the application
java -jar target/attendance-management-system-0.0.1-SNAPSHOT.jar
```

✅ Application starts at `http://localhost:8081`

---

## 📋 Test the API

### 1. Register Employee
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@company.com",
    "password": "Employee@123",
    "departmentId": "550e8400-e29b-41d4-a716-446655440101"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

### 2. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.smith@company.com",
    "password": "Employee@123"
  }'
```

### 3. Check-In
```bash
curl -X POST http://localhost:8081/api/employee/check-in \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "date": "2026-02-19",
  "checkInTime": "2026-02-19T08:45:00",
  "checkOutTime": null,
  "status": "PRESENT",
  "totalHours": 0.0,
  "lateApproved": false
}
```

### 4. Check-Out
```bash
curl -X POST http://localhost:8081/api/employee/check-out \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### 5. View Today's Status
```bash
curl -X GET http://localhost:8081/api/employee/attendance/today \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. View Attendance History
```bash
curl -X GET "http://localhost:8081/api/employee/attendance/history?from=2026-01-01&to=2026-02-19&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 7. View Employee Dashboard
```bash
curl -X GET http://localhost:8081/api/employee/dashboard \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response:**
```json
{
  "todayStatus": "PRESENT",
  "presentCount": 15,
  "absentCount": 2,
  "lateCount": 1,
  "halfDayCount": 0,
  "totalHoursThisMonth": 160.5,
  "last7DaysAttendance": [...]
}
```

### 8. Manager - View All Attendances
```bash
# Login as manager first
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alex.manager@company.com",
    "password": "Employee@123"
  }'

# Then fetch attendances
curl -X GET "http://localhost:8081/api/manager/attendance?date=2026-02-19&page=0&size=20" \
  -H "Authorization: Bearer MANAGER_JWT_TOKEN"
```

### 9. Manager - View Specific Employee
```bash
curl -X GET "http://localhost:8081/api/manager/attendance/550e8400-e29b-41d4-a716-446655440201?from=2026-01-01&to=2026-02-19&page=0&size=10" \
  -H "Authorization: Bearer MANAGER_JWT_TOKEN"
```

### 10. Manager Dashboard
```bash
curl -X GET http://localhost:8081/api/manager/dashboard \
  -H "Authorization: Bearer MANAGER_JWT_TOKEN"
```

---

## 🔑 Test Credentials (Pre-seeded)

| Role | Email | Password |
|------|-------|----------|
| Employee | john.smith@company.com | Employee@123 |
| Employee | jane.doe@company.com | Employee@123 |
| Manager | alex.manager@company.com | Employee@123 |

---

## 📂 Project Structure Overview

```
src/main/
├── java/com/srinath/attendance/
│   ├── config/              # Spring configuration
│   ├── controller/          # REST endpoints
│   │   ├── AuthController      (register, login)
│   │   ├── EmployeeController  (check-in, history, dashboard)
│   │   └── ManagerController   (view all, approve, export)
│   ├── dto/                 # Request/Response objects
│   ├── entity/              # JPA entities
│   ├── exception/           # Custom exceptions
│   ├── repository/          # Data access
│   ├── security/            # JWT & authentication
│   └── service/             # Business logic
└── resources/
    ├── application.yaml     # Configuration
    └── db/migration/        # Flyway migrations
```

---

## 🔍 Key Configuration Files

### application.yaml
- Database connection settings
- JWT secret and expiration
- Logging levels
- Office hours configuration

### V1__initial_schema.sql
- Creates all tables with constraints
- Sets up indexes for performance

### V2__seed_master_data.sql
- Inserts 2 roles (EMPLOYEE, MANAGER)
- Inserts 4 departments (IT, HR, Finance, Operations)
- Inserts 5 test users

---

## 📊 Attendance Status Rules

| Check-in Time | Status |
|---------------|--------|
| Before 9:30 AM | PRESENT |
| 9:30 AM - 5:00 PM | LATE |
| After 5:00 PM | HALF_DAY |
| No check-in by 6 PM | ABSENT |

---

## 🛠️ Development Commands

### Compile Only
```bash
./mvnw compile
```

### Run Tests
```bash
./mvnw test
```

### Run Specific Test
```bash
./mvnw test -Dtest=AttendanceServiceTest
```

### Build JAR
```bash
./mvnw clean package -DskipTests
```

### Run with Spring Boot Plugin
```bash
./mvnw spring-boot:run
```

### View Dependency Tree
```bash
./mvnw dependency:tree
```

---

## 🐛 Troubleshooting

### Database Connection Failed
```bash
# Verify PostgreSQL is running
psql -U postgres -d attendance_db

# Check credentials in application.yaml
# Default: postgres / 231429
```

### Port 8081 Already in Use
```bash
# Change port in application.yaml
server:
  port: 8082
```

### JWT Token Invalid
- Ensure you're using the token from login/register response
- Check token format: `Authorization: Bearer {token}`
- Verify token hasn't expired (24 hours default)

### Migration Failed
```bash
# Verify database exists
psql -U postgres -d attendance_db

# Check migration files in src/main/resources/db/migration/
```

---

## 📈 Performance Tips

1. **Use Pagination** - Always add `?page=0&size=10` to list endpoints
2. **Filter by Date** - Use `from` and `to` parameters to limit results
3. **Lazy Loading** - Relationships are lazily loaded for better performance
4. **Indexes** - Database has indexes on commonly queried fields

---

## 🔐 Security Notes

- ✅ All passwords are BCrypt encrypted
- ✅ JWT tokens expire after 24 hours
- ✅ Tokens are validated on each request
- ✅ Role-based access control enforced
- ✅ CORS enabled for frontend development

---

## 📞 Support

For issues or questions, refer to:
1. README.md - Comprehensive documentation
2. IMPLEMENTATION_SUMMARY.md - Technical details
3. Application logs - Check console/file for errors

---

**Happy Coding! 🚀**

