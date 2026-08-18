# TESTING.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial testing document |

---

## 1. Purpose

This document defines the complete testing strategy, test cases, and quality assurance processes for the RedeemWise system. It serves as the primary reference for ensuring system reliability, functionality, and performance.

---

## 2. Testing Strategy Overview

### 2.1 Testing Pyramid

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│                         ╱╲                                      │
│                        ╱  ╲                                     │
│                       ╱ E2E╲        End-to-End Tests (10%)      │
│                      ╱──────╲                                   │
│                     ╱        ╲                                   │
│                    ╱Integration╲    Integration Tests (20%)      │
│                   ╱──────────────╲                               │
│                  ╱                ╲                               │
│                 ╱    Unit Tests    ╲   Unit Tests (70%)          │
│                ╱────────────────────╲                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 Testing Types

| Type | Coverage Target | Tool | Frequency |
|------|-----------------|------|-----------|
| **Unit Tests** | 80%+ | JUnit 5, Mockito | Every commit |
| **Integration Tests** | 70%+ | Spring Boot Test | Every PR |
| **API Tests** | 100% endpoints | Postman, RestAssured | Every PR |
| **E2E Tests** | Critical flows | Cypress, Selenium | Daily |
| **Performance Tests** | Key scenarios | JMeter, Gatling | Weekly |
| **Security Tests** | OWASP Top 10 | OWASP ZAP | Bi-weekly |

---

## 3. Unit Testing Standards

### 3.1 Java Unit Tests

#### 3.1.1 Test Class Structure

```java
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should create user successfully")
    void createUser_WithValidRequest_ReturnsCreatedUser() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password123");
        request.setName("Test User");

        User savedUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .name("Test User")
            .role(Role.CUSTOMER)
            .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception for duplicate email")
    void createUser_WithDuplicateEmail_ThrowsException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("existing@example.com");
        request.setPassword("Password123");
        request.setName("Test User");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(request))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("email");
    }
}
```

#### 3.1.2 Test Naming Conventions

| Pattern | Example |
|---------|---------|
| `method_condition_expectedResult` | `createUser_withValidRequest_returnsUser` |
| `method_condition_throwsException` | `getUserById_withInvalidId_throwsNotFoundException` |

### 3.2 TypeScript Unit Tests

#### 3.2.1 Component Tests

```typescript
// src/components/cards/__tests__/CardList.test.tsx

import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CardList } from '../CardList';
import { cardService } from '../../../services/cardService';

jest.mock('../../../services/cardService');

describe('CardList', () => {
  const mockCards = [
    {
      id: '1',
      bankName: 'HDFC Bank',
      cardType: 'PLATINUM',
      cardNumber: '****-****-****-3456',
      rewardProgram: 'HDFC Rewards',
    },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders loading state initially', () => {
    render(<CardList userId="1" />);
    expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
  });

  it('renders cards after successful fetch', async () => {
    (cardService.getCardsByUserId as jest.Mock).mockResolvedValue(mockCards);

    render(<CardList userId="1" />);

    await waitFor(() => {
      expect(screen.getByText('HDFC Bank')).toBeInTheDocument();
    });
  });

  it('renders error state on fetch failure', async () => {
    (cardService.getCardsByUserId as jest.Mock).mockRejectedValue(
      new Error('API Error')
    );

    render(<CardList userId="1" />);

    await waitFor(() => {
      expect(screen.getByText(/failed to fetch/i)).toBeInTheDocument();
    });
  });

  it('calls onCardSelect when card is clicked', async () => {
    const onCardSelect = jest.fn();
    (cardService.getCardsByUserId as jest.Mock).mockResolvedValue(mockCards);

    render(<CardList userId="1" onCardSelect={onCardSelect} />);

    await waitFor(() => {
      userEvent.click(screen.getByText('HDFC Bank'));
    });

    expect(onCardSelect).toHaveBeenCalledWith(mockCards[0]);
  });
});
```

#### 3.2.2 Service Tests

```typescript
// src/services/__tests__/cardService.test.ts

import { cardService } from '../cardService';
import axios from 'axios';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('CardService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getCards', () => {
    it('fetches cards successfully', async () => {
      const mockResponse = {
        data: {
          data: {
            content: [{ id: '1', bankName: 'HDFC' }],
          },
        },
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await cardService.getCards();

      expect(result).toEqual([{ id: '1', bankName: 'HDFC' }]);
      expect(mockedAxios.get).toHaveBeenCalledWith('', { params: { page: 0, size: 10 } });
    });

    it('throws error on failure', async () => {
      mockedAxios.get.mockRejectedValue(new Error('Network Error'));

      await expect(cardService.getCards()).rejects.toThrow('Network Error');
    });
  });

  describe('createCard', () => {
    it('creates card successfully', async () => {
      const cardData = { bankName: 'HDFC', cardType: 'PLATINUM' };
      const mockResponse = {
        data: { data: { id: '1', ...cardData } },
      };
      mockedAxios.post.mockResolvedValue(mockResponse);

      const result = await cardService.createCard(cardData);

      expect(result).toEqual({ id: '1', ...cardData });
    });
  });
});
```

---

## 4. Integration Testing

### 4.1 API Integration Tests

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CardControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("redeemwise_test")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    @DisplayName("Should create card successfully")
    void createCard_WithValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("Password123");
        userRequest.setName("Test User");

        // Create user first
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated());

        // Login to get token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123");

        String token = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        // Create card
        CreateCardRequest cardRequest = new CreateCardRequest();
        cardRequest.setBankName("HDFC Bank");
        cardRequest.setCardType("PLATINUM");
        cardRequest.setCardNumber("1234567890123456");
        cardRequest.setRewardProgram("HDFC Rewards");
        cardRequest.setExpiryDate(LocalDate.of(2027, 12, 31));

        // Act & Assert
        mockMvc.perform(post("/api/cards")
            .header("Authorization", "Bearer " + extractToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cardRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.bankName").value("HDFC Bank"))
            .andExpect(jsonPath("$.data.cardType").value("PLATINUM"));
    }

    @Test
    @DisplayName("Should return 401 for unauthorized request")
    void createCard_WithoutToken_ReturnsUnauthorized() throws Exception {
        CreateCardRequest cardRequest = new CreateCardRequest();
        cardRequest.setBankName("HDFC Bank");

        mockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cardRequest)))
            .andExpect(status().isUnauthorized());
    }
}
```

### 4.2 Database Integration Tests

```java
@SpringBootTest
@Testcontainers
@Transactional
class UserRepositoryIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("redeemwise_test")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and retrieve user")
    void saveAndRetrieveUser_Success() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .password("encodedPassword")
            .name("Test User")
            .role(Role.CUSTOMER)
            .build();

        // Act
        User savedUser = userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_ExistingEmail_ReturnsUser() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .password("encodedPassword")
            .name("Test User")
            .role(Role.CUSTOMER)
            .build();
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }
}
```

---

## 5. API Testing

### 5.1 Postman Collection Structure

```
RedeemWise API v1/
├── Authentication/
│   ├── Register User
│   ├── Login User
│   └── Get Current User
├── Cards/
│   ├── Create Card
│   ├── Get All Cards
│   ├── Get Card by ID
│   ├── Update Card
│   └── Delete Card
├── Rewards/
│   ├── Add Reward Points
│   ├── Get All Rewards
│   ├── Update Reward Points
│   ├── Delete Reward Points
│   └── Get Reward Summary
├── Redemption Options/
│   ├── Get All Options
│   └── Get Option by ID
├── Recommendations/
│   ├── Get Recommendations
│   └── Calculate Value Per Point
└── Dashboard/
    └── Get Dashboard Data
```

### 5.2 API Test Cases

| Test ID | Endpoint | Method | Description | Expected Status |
|---------|----------|--------|-------------|-----------------|
| API-001 | /api/auth/register | POST | Register new user | 201 |
| API-002 | /api/auth/register | POST | Register with duplicate email | 400 |
| API-003 | /api/auth/login | POST | Login with valid credentials | 200 |
| API-004 | /api/auth/login | POST | Login with invalid credentials | 401 |
| API-005 | /api/cards | POST | Create card with valid data | 201 |
| API-006 | /api/cards | POST | Create card without auth | 401 |
| API-007 | /api/cards | GET | Get all cards | 200 |
| API-008 | /api/cards/{id} | GET | Get card by valid ID | 200 |
| API-009 | /api/cards/{id} | GET | Get card by invalid ID | 404 |
| API-010 | /api/cards/{id} | PUT | Update card | 200 |
| API-011 | /api/cards/{id} | DELETE | Delete card | 204 |
| API-012 | /api/rewards | POST | Add reward points | 201 |
| API-013 | /api/rewards | GET | Get all rewards | 200 |
| API-014 | /api/rewards/{id} | PUT | Update reward points | 200 |
| API-015 | /api/rewards/{id} | DELETE | Delete reward points | 204 |
| API-016 | /api/rewards/summary | GET | Get reward summary | 200 |
| API-017 | /api/rewards/options | GET | Get redemption options | 200 |
| API-018 | /api/recommendations | GET | Get recommendations | 200 |
| API-019 | /api/recommendations/value-per-point | GET | Calculate VPP | 200 |
| API-020 | /api/dashboard | GET | Get dashboard data | 200 |

---

## 6. End-to-End Testing

### 6.1 E2E Test Scenarios

| Scenario | Steps | Expected Result |
|----------|-------|-----------------|
| User Registration | 1. Navigate to register<br>2. Fill form<br>3. Submit | Account created, redirect to login |
| User Login | 1. Navigate to login<br>2. Enter credentials<br>3. Submit | JWT token received, redirect to dashboard |
| Add Credit Card | 1. Login<br>2. Navigate to cards<br>3. Click add<br>4. Fill form<br>5. Submit | Card added, appears in list |
| Add Reward Points | 1. Login<br>2. Navigate to rewards<br>3. Click add<br>4. Fill form<br>5. Submit | Points added, appears in list |
| View Recommendations | 1. Login<br>2. Add cards and rewards<br>3. Navigate to recommendations | Recommendations displayed with VPP |
| Dashboard Overview | 1. Login<br>2. View dashboard | Stats displayed correctly |

### 6.2 Cypress Test Example

```typescript
// cypress/e2e/userRegistration.cy.ts

describe('User Registration', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should register a new user successfully', () => {
    // Arrange
    const user = {
      name: 'Test User',
      email: `test${Date.now()}@example.com`,
      password: 'Password123',
    };

    // Act
    cy.get('[data-testid="name-input"]').type(user.name);
    cy.get('[data-testid="email-input"]').type(user.email);
    cy.get('[data-testid="password-input"]').type(user.password);
    cy.get('[data-testid="confirm-password-input"]').type(user.password);
    cy.get('[data-testid="register-button"]').click();

    // Assert
    cy.url().should('include', '/login');
    cy.get('[data-testid="success-message"]').should('contain', 'Registration successful');
  });

  it('should show error for duplicate email', () => {
    // Arrange
    const existingUser = {
      name: 'Existing User',
      email: 'existing@example.com',
      password: 'Password123',
    };

    // Act
    cy.get('[data-testid="name-input"]').type(existingUser.name);
    cy.get('[data-testid="email-input"]').type(existingUser.email);
    cy.get('[data-testid="password-input"]').type(existingUser.password);
    cy.get('[data-testid="confirm-password-input"]').type(existingUser.password);
    cy.get('[data-testid="register-button"]').click();

    // Assert
    cy.get('[data-testid="error-message"]').should('contain', 'Email already exists');
  });
});
```

---

## 7. Performance Testing

### 7.1 Performance Test Scenarios

| Scenario | Concurrent Users | Duration | Expected Response Time |
|----------|------------------|----------|------------------------|
| Login | 100 | 5 minutes | < 500ms |
| Get Cards | 500 | 10 minutes | < 300ms |
| Get Rewards | 500 | 10 minutes | < 300ms |
| Get Recommendations | 200 | 5 minutes | < 1000ms |
| Dashboard Load | 300 | 10 minutes | < 500ms |

### 7.2 JMeter Test Plan

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan>
  <TestPlan>
    <elementProp name="HTTP Request" elementType="HTTPSampler">
      <stringProp name="HTTPSampler.domain">localhost</stringProp>
      <stringProp name="HTTPSampler.port">8080</stringProp>
      <stringProp name="HTTPSampler.path">/api/auth/login</stringProp>
      <stringProp name="HTTPSampler.method">POST</stringProp>
    </elementProp>
  </TestPlan>
</jmeterTestPlan>
```

### 7.3 Performance Metrics

| Metric | Target | Critical Threshold |
|--------|--------|-------------------|
| Response Time (Avg) | < 300ms | > 1000ms |
| Response Time (95th) | < 500ms | > 2000ms |
| Throughput | > 100 req/s | < 50 req/s |
| Error Rate | < 1% | > 5% |
| CPU Usage | < 70% | > 90% |
| Memory Usage | < 80% | > 95% |

---

## 8. Security Testing

### 8.1 OWASP Top 10 Test Cases

| Category | Test Case | Expected Result |
|----------|-----------|-----------------|
| **A01** | Injection | SQL injection attempts blocked |
| **A02** | Broken Authentication | Invalid tokens rejected |
| **A03** | Sensitive Data Exposure | Passwords hashed, HTTPS enforced |
| **A04** | XML External Entities | XXE attacks prevented |
| **A05** | Broken Access Control | Unauthorized access blocked |
| **A06** | Security Misconfiguration | Default credentials changed |
| **A07** | Cross-Site Scripting | XSS attacks prevented |
| **A08** | Insecure Deserialization | Malicious payloads rejected |
| **A09** | Using Components with Known Vulnerabilities | Dependencies updated |
| **A10** | Insufficient Logging | Security events logged |

### 8.2 Security Test Cases

| Test ID | Description | Steps | Expected Result |
|---------|-------------|-------|-----------------|
| SEC-001 | SQL Injection | Enter `' OR '1'='1` in login | Request rejected |
| SEC-002 | XSS Attack | Enter `<script>alert('xss')</script>` | Script not executed |
| SEC-003 | JWT Tampering | Modify JWT token payload | Token rejected |
| SEC-004 | Brute Force | 5+ failed login attempts | Account locked |
| SEC-005 | CSRF Attack | Submit form without token | Request rejected |
| SEC-006 | Rate Limiting | 100+ requests in 1 minute | Rate limit enforced |
| SEC-007 | Password Strength | Enter weak password | Registration rejected |

---

## 9. Test Environment

### 9.1 Environment Configuration

| Environment | Purpose | Database | API URL |
|-------------|---------|----------|---------|
| **Local** | Development | Local MySQL | localhost:8080 |
| **Test** | Automated testing | Test DB (Docker) | test.redeemwise.com |
| **Staging** | Pre-production | Staging DB | staging.redeemwise.com |
| **Production** | Live system | Production DB | api.redeemwise.com |

### 9.2 Test Data Management

| Aspect | Strategy |
|--------|----------|
| **Test Data Creation** | Factory pattern, fixtures |
| **Test Data Isolation** | Separate database per test |
| **Test Data Cleanup** | @Transactional, @DirtiesContext |
| **Seed Data** | SQL scripts, Flyway migrations |

---

## 10. Test Reporting

### 10.1 Test Report Format

```xml
<testsuite name="UserServiceTest" tests="10" failures="0" errors="0" time="1.234">
  <testcase name="createUser_WithValidRequest_ReturnsUser" time="0.123"/>
  <testcase name="createUser_WithDuplicateEmail_ThrowsException" time="0.089"/>
  <testcase name="getUserById_WithValidId_ReturnsUser" time="0.067"/>
</testsuite>
```

### 10.2 Coverage Report

| Metric | Target | Tool |
|--------|--------|------|
| Line Coverage | > 80% | JaCoCo |
| Branch Coverage | > 70% | JaCoCo |
| Method Coverage | > 80% | JaCoCo |
| Class Coverage | > 90% | JaCoCo |

### 10.3 Test Dashboard Metrics

| Metric | Current | Target | Status |
|--------|---------|--------|--------|
| Total Tests | 0 | 500+ | Pending |
| Passing Tests | 0 | 100% | Pending |
| Code Coverage | 0% | 80%+ | Pending |
| API Test Coverage | 0% | 100% | Pending |
| E2E Coverage | 0% | Critical flows | Pending |

---

## 11. Test Automation

### 11.1 CI/CD Integration

```yaml
# .github/workflows/test.yml
name: Test Suite

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: redeemwise_test
        ports:
          - 3306:3306
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      
      - name: Run Unit Tests
        run: mvn test
      
      - name: Run Integration Tests
        run: mvn verify -P integration-test
      
      - name: Generate Coverage Report
        run: mvn jacoco:report
      
      - name: Upload Coverage to Codecov
        uses: codecov/codecov-action@v3
```

### 11.2 Test Execution Order

| Order | Test Type | Command |
|-------|-----------|---------|
| 1 | Unit Tests | `mvn test` |
| 2 | Integration Tests | `mvn verify -P integration-test` |
| 3 | API Tests | `mvn verify -P api-test` |
| 4 | E2E Tests | `npm run test:e2e` |
| 5 | Performance Tests | `jmeter -n -t test-plan.jmx` |

---

## 12. Bug Reporting

### 12.1 Bug Report Template

```markdown
## Bug Report

**Title:** [Brief description]

**Environment:**
- Browser: [Chrome/Firefox/Safari]
- OS: [Windows/macOS/Linux]
- Version: [Version number]

**Steps to Reproduce:**
1. [Step 1]
2. [Step 2]
3. [Step 3]

**Expected Result:**
[What should happen]

**Actual Result:**
[What actually happens]

**Screenshots:**
[If applicable]

**Additional Context:**
[Any other relevant information]
```

### 12.2 Bug Severity Levels

| Level | Description | Response Time |
|-------|-------------|---------------|
| **Critical** | System down, data loss | Immediate |
| **High** | Major feature broken | 24 hours |
| **Medium** | Feature impaired, workaround exists | 3 days |
| **Low** | Minor issue, cosmetic | 1 week |

---

*Document maintained by RedeemWise Development Team*