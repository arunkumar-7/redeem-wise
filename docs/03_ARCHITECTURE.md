# ARCHITECTURE.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial architecture document |

---

## 1. Purpose

This document defines the system architecture for the RedeemWise platform, including the architectural style, service decomposition, communication patterns, technology choices, and design decisions. It serves as the primary technical reference for the development team.

---

## 2. Architectural Style

### 2.1 Microservices Architecture

RedeemWise adopts a **Microservices Architecture** pattern, where the system is decomposed into independently deployable services, each responsible for a specific business capability.

**Why Microservices?**

| Benefit | Description |
|---------|-------------|
| **Independent Deployment** | Each service can be deployed independently |
| **Technology Flexibility | Services can use different technologies if needed |
| **Scalability** | Individual services can scale based on demand |
| **Fault Isolation** | Failure in one service doesn't cascade to others |
| **Team Autonomy** | Different teams can work on different services |

---

## 3. High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT LAYER                                   │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                     React Frontend Application                      │   │
│  │   (TypeScript + Tailwind CSS + Axios)                               │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              GATEWAY LAYER                                  │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                     Spring Cloud Gateway                            │   │
│  │   (Route Management, Load Balancing, Security)                      │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           SERVICE LAYER                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  Auth        │  │  Card        │  │  Reward      │  │  Recommend   │   │
│  │  Service     │  │  Service     │  │  Service     │  │  Service     │   │
│  │  (8081)      │  │  (8082)      │  │  (8083)      │  │  (8084)      │   │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          DISCOVERY LAYER                                    │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                     Eureka Discovery Server                         │   │
│  │                     (Port: 8761)                                     │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           DATA LAYER                                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  Auth DB     │  │  Card DB     │  │  Reward DB   │  │  Recommend   │   │
│  │  (MySQL)     │  │  (MySQL)     │  │  (MySQL)     │  │  DB (MySQL)  │   │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Service Decomposition

### 4.1 Infrastructure Services

#### 4.1.1 Eureka Discovery Service

| Property | Value |
|----------|-------|
| **Port** | 8761 |
| **Responsibility** | Service registration and discovery |
| **Technology** | Spring Cloud Netflix Eureka |
| **Dependencies** | None |

**Responsibilities:**
- Register all microservices
- Maintain service registry
- Provide service discovery to other services
- Health monitoring of registered services

#### 4.1.2 API Gateway

| Property | Value |
|----------|-------|
| **Port** | 8080 |
| **Responsibility** | Request routing, security, load balancing |
| **Technology** | Spring Cloud Gateway |
| **Dependencies** | Eureka Server |

**Responsibilities:**
- Route requests to appropriate microservices
- Apply JWT authentication/authorization
- Load balancing across service instances
- Request/response logging
- Rate limiting
- CORS configuration

### 4.2 Business Services

#### 4.2.1 Auth Service

| Property | Value |
|----------|-------|
| **Port** | 8081 |
| **Responsibility** | User authentication and authorization |
| **Technology** | Spring Boot + Spring Security + JWT |
| **Database** | redeemwise_auth |

**Responsibilities:**
- User registration
- User login
- JWT token generation and validation
- Password hashing (BCrypt)
- Session management

#### 4.2.2 Card Service

| Property | Value |
|----------|-------|
| **Port** | 8082 |
| **Responsibility** | Credit card management |
| **Technology** | Spring Boot + Spring Data JPA |
| **Database** | redeemwise_card |

**Responsibilities:**
- CRUD operations for credit cards
- Card validation
- Card-search and filtering
- User-card association

#### 4.2.3 Reward Service

| Property | Value |
|----------|-------|
| **Port** | 8083 |
| **Responsibility** | Reward point management |
| **Technology** | Spring Boot + Spring Data JPA |
| **Database** | redeemwise_reward |

**Responsibilities:**
- Manage reward point balances
- Store redemption catalog
- Track point transactions
- Expiry management

#### 4.2.4 Recommendation Service

| Property | Value |
|----------|-------|
| **Port** | 8084 |
| **Responsibility** | Redemption optimization |
| **Technology** | Spring Boot + Spring Data JPA |
| **Database** | redeemwise_recommendation |

**Responsibilities:**
- Calculate Value Per Point
- Rank redemption options
- Generate personalized recommendations
- Compare redemption values

---

## 5. Service Communication

### 5.1 Communication Patterns

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        COMMUNICATION PATTERNS                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  SYNCHRONOUS (REST)                                                         │
│  ┌─────────────┐     HTTP/JSON      ┌─────────────┐                        │
│  │   Client    │ ─────────────────► │   Gateway   │                        │
│  └─────────────┘                     └─────────────┘                        │
│                                          │                                  │
│                                          ▼                                  │
│                                   ┌─────────────┐                          │
│                                   │   Service   │                          │
│                                   └─────────────┘                          │
│                                                                             │
│  INTER-SERVICE (REST Template / Feign)                                      │
│  ┌─────────────┐     HTTP/JSON      ┌─────────────┐                        │
│  │    Card     │ ─────────────────► │   Reward    │                        │
│  │   Service   │                    │   Service   │                        │
│  └─────────────┘                    └─────────────┘                        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.2 Service Interaction Matrix

| From \ To | Auth | Card | Reward | Recommendation |
|-----------|------|------|--------|----------------|
| **Auth** | - | - | - | - |
| **Card** | ✓ (validate token) | - | ✓ (manage points) | - |
| **Reward** | ✓ (validate token) | ✓ (get card info) | - | ✓ (provide data) |
| **Recommendation** | ✓ (validate token) | ✓ (get card info) | ✓ (get points & options) | - |

### 5.3 Communication Protocol

| Aspect | Choice | Rationale |
|--------|--------|-----------|
| **Protocol** | HTTP/REST | Simple, widely supported |
| **Data Format** | JSON | Lightweight, human-readable |
| **Service Discovery** | Eureka | Dynamic service location |
| **Load Balancing** | Spring Cloud LoadBalancer | Client-side load balancing |
| **Circuit Breaker** | Resilience4j | Fault tolerance |

---

## 6. Security Architecture

### 6.1 Authentication Flow

```
┌──────────┐                    ┌──────────┐                    ┌──────────┐
│  Client  │                    │  Gateway │                    │  Auth    │
└──────────┘                    └──────────┘                    └──────────┘
     │                               │                               │
     │  1. POST /auth/login          │                               │
     │  {email, password}            │                               │
     │ ─────────────────────────────►│                               │
     │                               │  2. Forward to Auth Service   │
     │                               │ ─────────────────────────────►│
     │                               │                               │
     │                               │  3. Validate credentials      │
     │                               │     Generate JWT token        │
     │                               │                               │
     │                               │  4. Return JWT token          │
     │                               │ ◄─────────────────────────────│
     │                               │                               │
     │  5. Return JWT token          │                               │
     │ ◄─────────────────────────────│                               │
     │                               │                               │
     │  6. Subsequent requests       │                               │
     │  Include Authorization header │                               │
     │  Bearer {token}               │                               │
     │ ─────────────────────────────►│                               │
     │                               │                               │
     │                               │  7. Validate JWT token        │
     │                               │     Extract user details      │
     │                               │                               │
     │                               │  8. Forward to target service │
     │                               │ ─────────────────────────────►│
```

### 6.2 JWT Token Structure

```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "user@example.com",
    "userId": 12345,
    "role": "CUSTOMER",
    "iat": 1705312200,
    "exp": 1705398600
  }
}
```

### 6.3 Security Configuration

| Aspect | Implementation |
|--------|----------------|
| **Password Hashing** | BCrypt (strength: 12) |
| **Token Expiry** | 24 hours |
| **Token Algorithm** | HS256 |
| **CORS** | Configured per environment |
| **Rate Limiting** | 100 requests/minute per user |

---

## 7. Database Architecture

### 7.1 Database per Service Pattern

Each microservice has its own dedicated database to ensure loose coupling:

| Service | Database | Tables |
|---------|----------|--------|
| Auth Service | redeemwise_auth | users |
| Card Service | redeemwise_card | cards |
| Reward Service | redeemwise_reward | reward_points, redemption_options |
| Recommendation Service | redeemwise_recommendation | recommendations |

### 7.2 Data Isolation

```
┌─────────────────────────────────────────────────────────────────┐
│                    DATABASE ISOLATION                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐      ┌─────────────┐      ┌─────────────┐     │
│  │   Auth      │      │   Card      │      │   Reward    │     │
│  │   Service   │      │   Service   │      │   Service   │     │
│  └─────────────┘      └─────────────┘      └─────────────┘     │
│        │                    │                    │              │
│        ▼                    ▼                    ▼              │
│  ┌─────────────┐      ┌─────────────┐      ┌─────────────┐     │
│  │  Auth DB    │      │  Card DB    │      │  Reward DB  │     │
│  │  (MySQL)    │      │  (MySQL)    │      │  (MySQL)    │     │
│  └─────────────┘      └─────────────┘      └─────────────┘     │
│                                                                 │
│  Each service owns its data. No direct DB access between        │
│  services. Communication via REST APIs only.                    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8. API Gateway Architecture

### 8.1 Routing Configuration

| Route | Target Service | Method |
|-------|----------------|--------|
| /api/auth/** | Auth Service (8081) | ALL |
| /api/cards/** | Card Service (8082) | ALL |
| /api/rewards/** | Reward Service (8083) | ALL |
| /api/recommendations/** | Recommendation Service (8084) | ALL |
| /api/dashboard/** | Recommendation Service (8084) | GET |

### 8.2 Gateway Filters

```
┌─────────────────────────────────────────────────────────────────┐
│                      GATEWAY PIPELINE                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Incoming Request                                               │
│       │                                                         │
│       ▼                                                         │
│  ┌─────────────┐                                               │
│  │ CORS Filter │  → Handle cross-origin requests                │
│  └─────────────┘                                               │
│       │                                                         │
│       ▼                                                         │
│  ┌─────────────┐                                               │
│  │ Rate Limit  │  → Prevent abuse                               │
│  │ Filter      │                                               │
│  └─────────────┘                                               │
│       │                                                         │
│       ▼                                                         │
│  ┌─────────────┐                                               │
│  │ Auth Filter │  → Validate JWT token                          │
│  │ (JWT)       │                                               │
│  └─────────────┘                                               │
│       │                                                         │
│       ▼                                                         │
│  ┌─────────────┐                                               │
│  │ Logging     │  → Request/response logging                    │
│  │ Filter      │                                               │
│  └─────────────┘                                               │
│       │                                                         │
│       ▼                                                         │
│  Route to Target Service                                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 9. Frontend Architecture

### 9.1 Component Structure

```
src/
├── components/
│   ├── common/
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   ├── Card.tsx
│   │   ├── Modal.tsx
│   │   └── Loading.tsx
│   ├── auth/
│   │   ├── LoginForm.tsx
│   │   └── RegisterForm.tsx
│   ├── cards/
│   │   ├── CardList.tsx
│   │   ├── CardForm.tsx
│   │   └── CardDetails.tsx
│   ├── rewards/
│   │   ├── RewardList.tsx
│   │   ├── RewardForm.tsx
│   │   └── RewardDetails.tsx
│   ├── recommendations/
│   │   ├── RecommendationList.tsx
│   │   └── RecommendationCard.tsx
│   └── dashboard/
│       ├── Dashboard.tsx
│       ├── StatsCard.tsx
│       └── PointsChart.tsx
├── services/
│   ├── api.ts
│   ├── authService.ts
│   ├── cardService.ts
│   ├── rewardService.ts
│   └── recommendationService.ts
├── hooks/
│   ├── useAuth.ts
│   └── useApi.ts
├── context/
│   └── AuthContext.tsx
├── types/
│   └── index.ts
└── utils/
    ├── formatters.ts
    └── validators.ts
```

### 9.2 State Management

| Aspect | Implementation |
|--------|----------------|
| **Global State** | React Context API |
| **Local State** | React useState/useReducer |
| **Server State** | Axios + Custom hooks |
| **Form State** | React Hook Form |

---

## 10. Deployment Architecture

### 10.1 Deployment Topology

```
┌─────────────────────────────────────────────────────────────────┐
│                    DEPLOYMENT TOPOLOGY                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    LOAD BALANCER                         │   │
│  │                  (Nginx / Apache)                        │   │
│  └─────────────────────────────────────────────────────────┘   │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    APPLICATION SERVER                    │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐              │   │
│  │  │ Gateway  │  │  Eureka  │  │  Frontend│              │   │
│  │  │  :8080   │  │  :8761   │  │  :3000   │              │   │
│  │  └──────────┘  └──────────┘  └──────────┘              │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │   │
│  │  │  Auth    │  │  Card    │  │  Reward  │  │  Rec   │ │   │
│  │  │  :8081   │  │  :8082   │  │  :8083   │  │  :8084 │ │   │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │   │
│  └─────────────────────────────────────────────────────────┘   │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    DATABASE SERVER                       │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │   │
│  │  │  Auth DB │  │  Card DB │  │ Reward DB│  │  Rec   │ │   │
│  │  │  :3306   │  │  :3306   │  │  :3306   │  │  :3306 │ │   │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 10.2 Environment Configuration

| Environment | Purpose | URL |
|-------------|---------|-----|
| Development | Local development | localhost |
| Staging | Testing and QA | staging.redeemwise.com |
| Production | Live system | redeemwise.com |

---

## 11. Error Handling Architecture

### 11.1 Error Propagation

```
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│  Client  │     │  Gateway │     │ Service  │     │ Database │
└──────────┘     └──────────┘     └──────────┘     └──────────┘
     │                │                │                │
     │   Request      │                │                │
     │ ──────────────►│                │                │
     │                │   Request      │                │
     │                │ ──────────────►│                │
     │                │                │   Query        │
     │                │                │ ──────────────►│
     │                │                │                │
     │                │                │   Exception    │
     │                │                │ ◄──────────────│
     │                │                │                │
     │                │   Exception    │                │
     │                │ ◄──────────────│                │
     │                │                │                │
     │                │   Wrapped      │                │
     │                │   Response     │                │
     │                │   (500)        │                │
     │   Error        │                │                │
     │ ◄──────────────│                │                │
```

### 11.2 Global Exception Handler

| Exception Type | HTTP Status | Handling |
|----------------|-------------|----------|
| ValidationException | 400 | Return field errors |
| UnauthorizedException | 401 | Return auth error |
| ForbiddenException | 403 | Return permission error |
| ResourceNotFoundException | 404 | Return not found |
| DuplicateResourceException | 409 | Return conflict |
| DatabaseException | 500 | Log and return generic error |

---

## 12. Logging Architecture

### 12.1 Logging Levels

| Level | Usage |
|-------|-------|
| ERROR | System errors, exceptions |
| WARN | Unexpected conditions |
| INFO | Business events, operations |
| DEBUG | Development debugging |

### 12.2 Log Format

```
[2024-01-15 10:30:45.123] [INFO] [auth-service] [main] - User registered: user@example.com
[2024-01-15 10:30:46.456] [DEBUG] [card-service] [http-nio-8082-exec-1] - Fetching cards for user: 12345
```

---

## 13. Design Decisions

### 13.1 Architecture Decision Records (ADRs)

| ADR | Decision | Rationale |
|-----|----------|-----------|
| ADR-001 | Microservices over Monolith | Scalability, team independence, fault isolation |
| ADR-002 | JWT over Session-based auth | Stateless, scalable, no server-side session storage |
| ADR-003 | Database per Service | Data isolation, independent scaling, loose coupling |
| ADR-004 | REST over gRPC | Simplicity, wider tooling support, easier debugging |
| ADR-005 | MySQL over MongoDB | Structured data, ACID compliance, team familiarity |
| ADR-006 | Spring Cloud Gateway | Tight integration with Spring ecosystem |

---

## 14. Quality Attributes

| Attribute | Strategy |
|-----------|----------|
| **Performance** | Connection pooling, caching, async processing |
| **Security** | JWT auth, input validation, BCrypt passwords |
| **Scalability** | Stateless services, horizontal scaling |
| **Availability** | Health checks, circuit breakers, retry logic |
| **Maintainability** | Clean architecture, SOLID principles |
| **Testability** | Unit tests, integration tests, test containers |

---

*Document maintained by RedeemWise Development Team*