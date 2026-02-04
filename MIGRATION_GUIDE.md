# Migration Guide: Legacy to Modern Spring Boot

This guide helps you understand the migration from the old Spring 4 + Hibernate + JSP application to the modern Spring Boot 3 + REST API version.

## Architecture Changes

### Old Architecture
```
JSP Views → Spring MVC Controllers → Service Layer → DAO Layer → Hibernate → Oracle DB
```

### New Architecture
```
REST API Clients → Spring Boot Controllers → Service Layer → Spring Data JPA → PostgreSQL
```

## Key Changes

### 1. Spring Framework

**Old (Spring 4.0):**
- XML configuration (`spring-servlet.xml`, `web.xml`)
- Manual bean configuration
- Servlet 2.5
- Manual dependency management

**New (Spring Boot 3.2):**
- Java-based configuration with annotations
- Auto-configuration
- Embedded Tomcat
- Starter dependencies

### 2. Data Access Layer

**Old:**
```java
// Manual DAO implementation
public class CandidateDAOImpl implements CandidateDAO {
    @Autowired
    SessionFactory sessionFactory;
    
    public String createCandidate(CandidateBean cb) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(cb);
        transaction.commit();
        return "added";
    }
}
```

**New:**
```java
// Spring Data JPA - zero implementation code!
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    List<Candidate> findByPrimarySkillsAndQualification(String skills, String qual);
}
```

### 3. Controllers

**Old (JSP-based):**
```java
@RequestMapping("add")
public String add() {
    return "add";  // Returns JSP view name
}

@RequestMapping("addCandidate")
public String addCandidate(HttpServletRequest request, Map<String, Object> map) {
    // Manual parameter extraction
    String firstName = request.getParameter("firstname");
    // ...
    map.put("msg", "Success");
    return "add";
}
```

**New (REST API):**
```java
@RestController
@RequestMapping("/api/admin/candidates")
public class CandidateController {
    
    @PostMapping
    public ResponseEntity<ApiResponse<CandidateResponse>> create(
            @Valid @RequestBody CandidateRequest request) {
        CandidateResponse response = candidateService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
```

### 4. Authentication

**Old (Session-based):**
```java
@RequestMapping("login")
public String login(HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.setAttribute("cb", credentialsBean);
    return "admin";  // Redirect to JSP
}
```

**New (JWT-based):**
```java
@PostMapping("/api/auth/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(LoginResponse.builder()
        .accessToken(token)
        .userType(user.getUserType())
        .build());
}
```

### 5. Entity Classes

**Old:**
```java
@Entity
@Table(name = "ITS_Candidate")
public class CandidateBean {
    @Id
    private String candidateID;
    @Column(name = "primarySkills")
    private String primarySkills;
    // Manual getters/setters
}
```

**New:**
```java
@Entity
@Table(name = "candidates")
@Data  // Lombok generates getters/setters
@Builder
public class Candidate {
    @Id
    @Column(name = "candidate_id")
    private String candidateId;
    
    @Column(name = "primary_skills")
    private String primarySkills;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
```

### 6. Configuration

**Old (XML):**
```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver" />
    <property name="url" value="jdbc:oracle:thin:@localhost:1521/xe" />
</bean>
```

**New (YAML):**
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/its_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
```

## Step-by-Step Migration

### Step 1: Update Dependencies

Replace old dependencies with Spring Boot starters:
- `spring-webmvc` → `spring-boot-starter-web`
- `hibernate-core` → `spring-boot-starter-data-jpa`
- Manual JDBC → `spring-boot-starter-jdbc`

### Step 2: Convert Entities

1. Rename `*Bean` classes to proper entity names
2. Add Lombok annotations (`@Data`, `@Builder`)
3. Add audit fields (`@CreatedDate`, `@LastModifiedDate`)
4. Use modern Java types (`LocalDate`, `LocalDateTime`)

### Step 3: Replace DAOs with Repositories

Delete all `*DAOImpl` classes and create Spring Data JPA repositories:

```java
// Delete: CandidateDAOImpl (100+ lines)
// Create: CandidateRepository (5 lines)
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    List<Candidate> findByPrimarySkillsAndQualification(String skills, String qual);
}
```

### Step 4: Create DTOs

Separate internal entities from API contracts:
- `CandidateRequest` - for creating candidates
- `CandidateResponse` - for returning candidate data
- Never expose entities directly in REST APIs

### Step 5: Convert Controllers to REST

1. Change `@Controller` to `@RestController`
2. Remove JSP view names, return DTOs
3. Use `@RequestBody` for input
4. Return `ResponseEntity<T>` for proper HTTP status codes

### Step 6: Implement JWT Authentication

Replace session-based auth with stateless JWT:
1. Create `JwtService` for token generation/validation
2. Implement `JwtAuthenticationFilter`
3. Configure Spring Security
4. Remove session management code

### Step 7: Update Configuration

1. Delete `web.xml` and `spring-servlet.xml`
2. Create `application.yml`
3. Use environment variables for sensitive data
4. Add Spring Boot main class

### Step 8: Database Migration

1. Export data from Oracle
2. Convert schema to PostgreSQL
3. Update sequences and data types
4. Import data to PostgreSQL

## API Mapping

### Old JSP Endpoints → New REST Endpoints

| Old Endpoint | New REST Endpoint | Method |
|--------------|-------------------|--------|
| `/add` | `/api/admin/candidates` | POST |
| `/addCandidate` | `/api/admin/candidates` | POST |
| `/viewinfo` | `/api/admin/candidates?skills=X&qual=Y` | GET |
| `/editCandidate/{id}` | `/api/admin/candidates/{id}` | GET/PUT |
| `/delCandidate/{id}` | `/api/admin/candidates/{id}` | DELETE |
| `/schedule` | `/api/admin/interviews` | POST |
| `/techrating` | `/api/tech/interviews/{id}/rating` | POST |
| `/hrrating` | `/api/hr/interviews/{id}/rating` | POST |
| `/login` | `/api/auth/login` | POST |
| `/logout` | `/api/auth/logout` | POST |

## Testing the Migration

### 1. Test with Postman/cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Create Candidate
curl -X POST http://localhost:8080/api/admin/candidates \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "emailId": "john@example.com",
    "primarySkills": "Java",
    "qualification": "BE",
    "experience": 0.5
  }'
```

### 2. Use Swagger UI

Access http://localhost:8080/swagger-ui.html for interactive API testing.

## Benefits of Migration

1. **Reduced Code**: 80% less boilerplate code
2. **Modern Stack**: Latest Java and Spring features
3. **Stateless**: JWT-based authentication for scalability
4. **Cloud-Ready**: Docker and Kubernetes support
5. **Better Testing**: Easier to write unit and integration tests
6. **API First**: Can support multiple frontends (web, mobile, etc.)
7. **Developer Experience**: Hot reload, better tooling
8. **Security**: Modern security practices built-in

## Next Steps

1. Build a modern frontend (React/Vue/Angular)
2. Add comprehensive tests
3. Set up CI/CD pipeline
4. Deploy to Kubernetes
5. Add monitoring and logging
6. Implement caching (Redis)
7. Add API rate limiting
8. Implement API versioning

## Troubleshooting

### Common Issues

**Issue**: Application won't start
- Check Java version (must be 17+)
- Verify database connection
- Check port 8080 is available

**Issue**: JWT authentication fails
- Ensure JWT_SECRET is set and long enough (256 bits)
- Check token expiration time
- Verify Authorization header format: `Bearer <token>`

**Issue**: Database connection error
- Verify PostgreSQL is running
- Check connection string in application.yml
- Ensure database exists

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [JWT.io](https://jwt.io/)
