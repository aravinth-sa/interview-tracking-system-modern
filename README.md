# Interview Tracking System - Modernized

A modernized version of the Interview Tracking System built with Spring Boot 3, REST APIs, and containerization support.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Data JPA**
- **Spring Security with JWT**
- **PostgreSQL** (Production)
- **H2 Database** (Development)
- **Lombok**
- **MapStruct**
- **Docker & Kubernetes**

## Project Structure

```
src/main/java/com/wipro/its/
├── entity/              # JPA Entities
├── repository/          # Spring Data JPA Repositories
├── service/             # Business Logic Layer
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
├── security/            # Security Configuration & JWT
├── exception/           # Exception Handling
├── config/              # Application Configuration
└── enums/               # Enumerations
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 15+ (for production)
- Docker (optional)

### Running Locally

1. **Clone the repository**
```bash
git clone <repository-url>
cd its-spring-boot-modern
```

2. **Run with H2 (Development)**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. **Run with PostgreSQL (Production)**
```bash
# Update application.yml with your database credentials
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

4. **Access the application**
- API Base URL: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (dev profile only)

### Building

```bash
# Build JAR
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run JAR
java -jar target/interview-tracking-system-2.0.0.jar
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Admin APIs
- `POST /api/admin/candidates` - Create candidate
- `GET /api/admin/candidates` - List/search candidates
- `GET /api/admin/candidates/{id}` - Get candidate details
- `PUT /api/admin/candidates/{id}` - Update candidate
- `DELETE /api/admin/candidates/{id}` - Delete candidate
- `POST /api/admin/interviews` - Schedule interview
- `PUT /api/admin/interviews/{id}/finalize` - Finalize result
- `PUT /api/admin/interviews/{id}/share` - Share result

### Tech Panel APIs
- `GET /api/tech/interviews` - View assigned interviews
- `POST /api/tech/interviews/{id}/rating` - Submit tech rating
- `GET /api/tech/interviews/{id}` - View interview details

### HR Panel APIs
- `GET /api/hr/interviews` - View assigned interviews
- `POST /api/hr/interviews/{id}/rating` - Submit HR rating
- `GET /api/hr/interviews/{id}` - View interview details

## Docker Support

### Build Docker Image
```bash
docker build -t its-app:latest .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

## Kubernetes Deployment

### Deploy to Kubernetes
```bash
# Create namespace
kubectl create namespace its

# Apply configurations
kubectl apply -f k8s/
```

### Access Services
```bash
# Admin Service
curl http://admin.its.local/api/admin/candidates

# HR Service
curl http://hr.its.local/api/hr/interviews

# Tech Service
curl http://tech.its.local/api/tech/interviews
```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | dev |
| `DB_URL` | Database URL | jdbc:postgresql://localhost:5432/its_db |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `JWT_SECRET` | JWT secret key | (must be set) |
| `SERVER_PORT` | Application port | 8080 |

## Development

### Code Style
- Follow Java naming conventions
- Use Lombok to reduce boilerplate
- Write meaningful commit messages
- Add unit tests for new features

### Testing
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=CandidateServiceTest
```

## Migration from Legacy Version

Key changes from the old Spring 4 + JSP version:

1. **Spring Boot 3** - Modern Spring framework with auto-configuration
2. **REST APIs** - Replaced JSP views with RESTful endpoints
3. **JWT Authentication** - Stateless authentication instead of sessions
4. **Spring Data JPA** - Eliminated manual DAO implementations
5. **DTOs** - Proper request/response objects
6. **Docker/K8s** - Containerization and orchestration support
7. **PostgreSQL** - Modern database instead of Oracle
8. **Java 17** - Latest LTS Java version

## Contributing

1. Create a feature branch
2. Make your changes
3. Write/update tests
4. Submit a pull request

## License

This project is for educational purposes.

## Contact

For questions or issues, please create an issue in the repository.
