# CODING_STANDARDS.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial coding standards document |

---

## 1. Purpose

This document defines the coding standards, conventions, and best practices for the RedeemWise project. It serves as the primary reference for all developers to ensure consistent, maintainable, and high-quality code.

---

## 2. General Principles

### 2.1 Core Values

| Principle | Description |
|-----------|-------------|
| **Clean Code** | Write code that is easy to read and understand |
| **SOLID Principles** | Follow Single Responsibility, Open/Closed, etc. |
| **DRY** | Don't Repeat Yourself - avoid code duplication |
| **KISS** | Keep It Simple, Stupid - prefer simple solutions |
| **YAGNI** | You Aren't Gonna Need It - don't over-engineer |
| **Fail Fast** | Validate inputs and fail early with clear errors |

---

## 3. Java Coding Standards

### 3.1 Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| **Packages** | lowercase, dot-separated | `com.redeemwise.auth.service` |
| **Classes** | PascalCase, singular noun | `UserService`, `CardController` |
| **Interfaces** | PascalCase, no prefix | `UserService`, `CardRepository` |
| **Methods** | camelCase, verb-first | `getUserById()`, `createCard()` |
| **Variables** | camelCase, meaningful | `userId`, `rewardPoints` |
| **Constants** | UPPER_SNAKE_CASE | `MAX_CARD_LIMIT`, `JWT_SECRET` |
| **Enums** | PascalCase, singular | `CardType`, `RedemptionCategory` |
| **Parameters** | camelCase, descriptive | `userId`, `cardNumber` |

### 3.2 Class Structure

```java
package com.redeemwise.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing user authentication operations.
 * 
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // Constants
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    // Dependencies (injected via constructor)
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Public methods
    public User createUser(CreateUserRequest request) {
        // Implementation
    }

    // Private methods
    private void validateEmail(String email) {
        // Implementation
    }
}
```

### 3.3 Method Guidelines

| Rule | Description |
|------|-------------|
| **Method Length** | Maximum 30 lines (excluding blank lines and comments) |
| **Parameters** | Maximum 4 parameters |
| **Nesting Depth** | Maximum 3 levels |
| **Return Type** | Prefer returning objects over modifying parameters |

### 3.4 Comments and Documentation

```java
/**
 * Calculates the value per point for a given redemption option.
 * 
 * This method implements the formula:
 * Value Per Point = Cash Value / Points Required
 * 
 * @param cashValue the cash value of the redemption option in INR
 * @param pointsRequired the number of points required for redemption
 * @return the value per point as a BigDecimal
 * @throws IllegalArgumentException if pointsRequired is zero or negative
 * 
 * @example
 * calculateValuePerPoint(500, 5000) returns 0.10
 */
public BigDecimal calculateValuePerPoint(BigDecimal cashValue, int pointsRequired) {
    if (pointsRequired <= 0) {
        throw new IllegalArgumentException("Points required must be positive");
    }
    return cashValue.divide(BigDecimal.valueOf(pointsRequired), 4, RoundingMode.HALF_UP);
}
```

### 3.5 Exception Handling

```java
// Custom exceptions
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}

// Exception handling in service
public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
}

// Global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

## 4. Spring Boot Standards

### 4.1 Package Structure

```
src/main/java/com/redeemwise/{service}/
├── controller/
│   ├── AuthController.java
│   └── CardController.java
├── service/
│   ├── UserService.java
│   └── CardService.java
├── repository/
│   ├── UserRepository.java
│   └── CardRepository.java
├── entity/
│   ├── User.java
│   └── Card.java
├── dto/
│   ├── request/
│   │   ├── CreateUserRequest.java
│   │   └── UpdateCardRequest.java
│   └── response/
│       ├── UserResponse.java
│       └── CardResponse.java
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── mapper/
│   ├── UserMapper.java
│   └── CardMapper.java
└── util/
    └── ValidationUtil.java
```

### 4.2 Entity Guidelines

```java
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

### 4.3 Repository Guidelines

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAllActiveUsers();

    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :id")
    int softDeleteUser(@Param("id") Long id);
}
```

### 4.4 Service Guidelines

```java
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());
        
        // Validate
        validateEmail(request.getEmail());
        
        // Map DTO to entity
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        
        // Save
        User savedUser = userRepository.save(user);
        
        log.info("User created successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("User", "email", email);
        }
    }
}
```

### 4.5 Controller Guidelines

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        log.info("Received request to create user: {}", request.getEmail());
        
        User user = userService.createUser(request);
        UserResponse response = UserMapper.toResponse(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("User created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponse response = UserMapper.toResponse(user);
        
        return ResponseEntity.ok(ApiResponse.success("User retrieved", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<User> users = userService.getAllUsers(PageRequest.of(page, size));
        Page<UserResponse> responses = users.map(UserMapper::toResponse);
        
        return ResponseEntity.ok(ApiResponse.success("Users retrieved", responses));
    }
}
```

---

## 5. TypeScript/React Standards

### 5.1 Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| **Files** | PascalCase for components | `UserCard.tsx` |
| **Files** | camelCase for utilities | `formatDate.ts` |
| **Components** | PascalCase | `UserCard`, `LoginForm` |
| **Functions** | camelCase | `getUserData`, `formatCurrency` |
| **Variables** | camelCase | `userName`, `isLoading` |
| **Constants** | UPPER_SNAKE_CASE | `API_BASE_URL`, `MAX_RETRY_COUNT` |
| **Interfaces** | PascalCase, no prefix | `User`, `CardData` |
| **Types** | PascalCase | `UserType`, `CardProps` |
| **Hooks** | camelCase, use prefix | `useAuth`, `useApi` |

### 5.2 Component Structure

```typescript
// src/components/cards/CardList.tsx

import React, { useState, useEffect } from 'react';
import { Card } from '../../types';
import { cardService } from '../../services/cardService';
import { Loading } from '../common/Loading';
import { Error } from '../common/Error';

interface CardListProps {
  userId: string;
  onCardSelect?: (card: Card) => void;
}

export const CardList: React.FC<CardListProps> = ({ userId, onCardSelect }) => {
  const [cards, setCards] = useState<Card[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchCards();
  }, [userId]);

  const fetchCards = async () => {
    try {
      setLoading(true);
      const data = await cardService.getCardsByUserId(userId);
      setCards(data);
    } catch (err) {
      setError('Failed to fetch cards');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loading />;
  if (error) return <Error message={error} />;

  return (
    <div className="card-list">
      {cards.map((card) => (
        <div
          key={card.id}
          className="card-item"
          onClick={() => onCardSelect?.(card)}
        >
          <h3>{card.bankName}</h3>
          <p>{card.cardType}</p>
          <p>{card.cardNumber}</p>
        </div>
      ))}
    </div>
  );
};
```

### 5.3 Hook Structure

```typescript
// src/hooks/useAuth.ts

import { useState, useCallback, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { authService } from '../services/authService';

export const useAuth = () => {
  const context = useContext(AuthContext);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async (email: string, password: string) => {
    try {
      setLoading(true);
      setError(null);
      const response = await authService.login(email, password);
      context?.setUser(response.user);
      context?.setToken(response.token);
      return response;
    } catch (err) {
      setError('Login failed');
      throw err;
    } finally {
      setLoading(false);
    }
  }, [context]);

  const logout = useCallback(() => {
    context?.setUser(null);
    context?.setToken(null);
    authService.logout();
  }, [context]);

  return {
    login,
    logout,
    loading,
    error,
    user: context?.user,
    isAuthenticated: !!context?.token,
  };
};
```

### 5.4 Service Structure

```typescript
// src/services/cardService.ts

import axios from 'axios';
import { Card, CardFormData } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/api/cards`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth interceptor
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const cardService = {
  getCards: async (page = 0, size = 10): Promise<Card[]> => {
    const response = await apiClient.get('', { params: { page, size } });
    return response.data.data.content;
  },

  getCardById: async (id: string): Promise<Card> => {
    const response = await apiClient.get(`/${id}`);
    return response.data.data;
  },

  createCard: async (cardData: CardFormData): Promise<Card> => {
    const response = await apiClient.post('', cardData);
    return response.data.data;
  },

  updateCard: async (id: string, cardData: CardFormData): Promise<Card> => {
    const response = await apiClient.put(`/${id}`, cardData);
    return response.data.data;
  },

  deleteCard: async (id: string): Promise<void> => {
    await apiClient.delete(`/${id}`);
  },
};
```

---

## 6. Formatting Standards

### 6.1 Indentation

| Language | Spaces | Tabs |
|----------|--------|------|
| Java | 4 spaces | No |
| TypeScript | 2 spaces | No |
| HTML/CSS | 2 spaces | No |
| JSON | 2 spaces | No |
| YAML | 2 spaces | No |

### 6.2 Line Length

| Language | Maximum Length |
|----------|----------------|
| Java | 120 characters |
| TypeScript | 100 characters |
| HTML | 100 characters |
| CSS | 80 characters |

### 6.3 Braces

| Language | Style |
|----------|-------|
| Java | K&R style (opening brace on same line) |
| TypeScript | K&R style |
| HTML | New line for nested elements |

```java
// Java - K&R Style
public class UserService {
    public void getUser() {
        if (condition) {
            // do something
        } else {
            // do something else
        }
    }
}
```

```typescript
// TypeScript - K&R Style
export const UserService = () => {
  if (condition) {
    // do something
  } else {
    // do something else
  }
};
```

### 6.4 Blank Lines

| Context | Rules |
|---------|-------|
| Between methods | 1 blank line |
| Between classes | 2 blank lines |
| After package statement | 1 blank line |
| After imports | 1 blank line |

---

## 7. Git Standards

### 7.1 Branch Naming

| Branch Type | Convention | Example |
|-------------|------------|---------|
| Feature | `feature/{description}` | `feature/user-registration` |
| Bug Fix | `bugfix/{description}` | `bugfix/login-validation` |
| Hotfix | `hotfix/{description}` | `hotfix/security-patch` |
| Release | `release/{version}` | `release/1.0.0` |
| Develop | `develop` | `develop` |
| Main | `main` | `main` |

### 7.2 Commit Messages

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
| Type | Description |
|------|-------------|
| feat | New feature |
| fix | Bug fix |
| docs | Documentation changes |
| style | Code style changes (formatting, etc.) |
| refactor | Code refactoring |
| test | Adding or updating tests |
| chore | Maintenance tasks |

**Examples:**
```
feat(auth): implement JWT token generation

- Add JWT token creation logic
- Implement token validation
- Add token expiry handling

Closes #123
```

```
fix(cards): resolve card deletion issue

- Fix cascade delete for reward points
- Add proper error handling
- Update unit tests

Fixes #456
```

### 7.3 Pull Request Guidelines

| Element | Requirement |
|---------|-------------|
| **Title** | Clear, concise description |
| **Description** | What, why, how |
| **Tests** | Include test cases |
| **Screenshots** | For UI changes |
| **Reviewers** | At least 1 reviewer |
| **CI/CD** | All checks must pass |

---

## 8. Testing Standards

### 8.1 Unit Test Naming

```java
// Method being tested
public User createUser(CreateUserRequest request) { }

// Test class
public class UserServiceTest { }

// Test methods
@Test
void createUser_WithValidRequest_ReturnsCreatedUser() { }

@Test
void createUser_WithDuplicateEmail_ThrowsDuplicateResourceException() { }

@Test
void createUser_WithInvalidEmail_ThrowsValidationException() { }
```

### 8.2 Test Structure (AAA Pattern)

```java
@Test
void getUserById_WithValidId_ReturnsUser() {
    // Arrange
    Long userId = 1L;
    User expectedUser = createTestUser(userId);
    when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

    // Act
    User result = userService.getUserById(userId);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(userId);
    verify(userRepository).findById(userId);
}
```

### 8.3 React Test Structure

```typescript
describe('CardList', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders loading state initially', () => {
    render(<CardList userId="1" />);
    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });

  it('renders cards after successful fetch', async () => {
    const mockCards = [{ id: '1', bankName: 'HDFC' }];
    cardService.getCardsByUserId.mockResolvedValue(mockCards);

    render(<CardList userId="1" />);

    await waitFor(() => {
      expect(screen.getByText('HDFC')).toBeInTheDocument();
    });
  });

  it('renders error state on fetch failure', async () => {
    cardService.getCardsByUserId.mockRejectedValue(new Error('API Error'));

    render(<CardList userId="1" />);

    await waitFor(() => {
      expect(screen.getByText('Failed to fetch cards')).toBeInTheDocument();
    });
  });
});
```

---

## 9. Documentation Standards

### 9.1 JavaDoc Requirements

| Element | Required | Description |
|---------|----------|-------------|
| Class | Yes | Purpose, author, version |
| Public Method | Yes | Description, parameters, returns, throws |
| Private Method | Optional | Brief description |
| Constants | Yes | Purpose and valid values |

### 9.2 TypeScript Documentation

```typescript
/**
 * Formats a number as Indian Rupee currency.
 * 
 * @param amount - The amount to format
 * @param showSymbol - Whether to show ₹ symbol (default: true)
 * @returns Formatted currency string
 * 
 * @example
 * formatCurrency(1500) // "₹1,500"
 * formatCurrency(1500, false) // "1,500"
 */
export const formatCurrency = (amount: number, showSymbol = true): string => {
  const formatted = new Intl.NumberFormat('en-IN').format(amount);
  return showSymbol ? `₹${formatted}` : formatted;
};
```

### 9.3 API Documentation

```java
@Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User found",
        content = @Content(schema = @Schema(implementation = UserResponse.class))),
    @ApiResponse(responseCode = "404", description = "User not found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<UserResponse>> getUserById(
        @Parameter(description = "User ID") @PathVariable Long id) {
    // Implementation
}
```

---

## 10. Code Review Checklist

### 10.1 Java Code Review

- [ ] Follows naming conventions
- [ ] Has proper JavaDoc
- [ ] Handles exceptions properly
- [ ] Uses appropriate collections
- [ ] Follows SOLID principles
- [ ] Has proper logging
- [ ] No hardcoded values
- [ ] Tests cover all scenarios

### 10.2 TypeScript/React Code Review

- [ ] Follows naming conventions
- [ ] Has proper TypeScript types
- [ ] Components are properly memoized
- [ ] No direct DOM manipulation
- [ ] Proper error handling
- [ ] Loading states handled
- [ ] Accessibility considerations
- [ ] Tests cover all scenarios

### 10.3 General Code Review

- [ ] Code is readable and maintainable
- [ ] No code duplication
- [ ] Proper error messages
- [ ] Security considerations
- [ ] Performance implications
- [ ] Documentation is up to date

---

## 11. IDE Configuration

### 11.1 IntelliJ IDEA

```xml
<!-- .idea/codeStyles/Project.xml -->
<code_scheme name="RedeemWise" version="173">
  <option name="RIGHT_MARGIN" value="120" />
  <JavaCodeStyleSettings>
    <option name="CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999" />
    <option name="NAMES_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999" />
  </JavaCodeStyleSettings>
</code_scheme>
```

### 11.2 VS Code

```json
{
  "editor.tabSize": 2,
  "editor.insertSpaces": true,
  "editor.wordWrap": "on",
  "editor.rulers": [100],
  "editor.formatOnSave": true,
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": true
  },
  "typescript.preferences.importModuleSpecifier": "relative",
  "typescript.tsdk": "node_modules/typescript/lib"
}
```

---

*Document maintained by RedeemWise Development Team*