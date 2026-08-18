# RedeemWise

## Reward Points Redemption Optimizer

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB.svg)](https://reactjs.org/)

---

## Project Overview

RedeemWise is a comprehensive Reward Points Optimization Platform that helps credit card users maximize the value of their accumulated reward points across multiple banks and credit cards.

### Vision Statement

> "To empower credit card users with intelligent tools that optimize reward point redemption, ensuring maximum value extraction from accumulated rewards across multiple financial institutions."

---

## Problem Statement

Credit card users accumulate reward points from multiple banks and credit cards but often fail to redeem them effectively. The current landscape presents several critical issues:

| Challenge | Description | Impact |
|-----------|-------------|--------|
| **Poor Redemption Value** | Most redemption portals offer suboptimal value per point | Users lose 30-50% of potential value |
| **Limited Product Inventory** | Redemption catalogs have restricted product options | Users cannot find desirable items |
| **No Cross-Card Comparison** | Users cannot compare redemption options across different cards | Suboptimal redemption decisions |
| **Poor Cash Conversion Rates** | Cashback conversions often have unfavorable rates | Reduced financial benefit |
| **No Optimization Guidance** | Users lack tools to identify best redemption strategies | Points expire unused |
| **Reward Point Expiry** | Points expire before users can redeem them | Direct financial loss |

---

## Business Objective

Build a Reward Points Optimization Platform that:

- **Tracks** reward points across multiple credit cards
- **Stores** redemption options from various providers
- **Compares** redemption values across different options
- **Calculates** value per point for informed decision-making
- **Recommends** the best redemption option
- **Helps** users maximize reward value

---

## Architecture Overview

RedeemWise adopts a **Microservices Architecture** pattern with the following key characteristics:

- **Independent Deployment**: Each service can be deployed independently
- **Technology Flexibility**: Services can use different technologies if needed
- **Scalability**: Individual services can scale based on demand
- **Fault Isolation**: Failure in one service doesn't cascade to others

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                                  │
│              React Frontend Application                              │
│         (TypeScript + Tailwind CSS + Axios)                         │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        GATEWAY LAYER                                 │
│              Spring Cloud Gateway                                    │
│     (Route Management, Load Balancing, Security)                    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       SERVICE LAYER                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │   Auth   │  │   Card   │  │  Reward  │  │ Recommend│           │
│  │ Service  │  │ Service  │  │ Service  │  │  Service │           │
│  │  :8081   │  │  :8082   │  │  :8083   │  │  :8084   │           │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘           │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       DATA LAYER                                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │ Auth DB  │  │ Card DB  │  │ Reward DB│  │ Recommend│           │
│  │ (MySQL)  │  │ (MySQL)  │  │ (MySQL)  │  │ DB (MySQL│           │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘           │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Technology Stack

### Backend

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Primary programming language |
| Spring Boot | 3.x | Application framework |
| Spring Security | 6.x | Authentication and authorization |
| Spring Data JPA | 3.x | Database access layer |
| MySQL | 8.x | Primary database |
| JWT | - | Stateless authentication |
| Spring Cloud Gateway | 4.x | API Gateway |
| Eureka Server | 2.x | Service discovery |

### Frontend

| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18.x | UI library |
| TypeScript | 5.x | Type-safe JavaScript |
| Tailwind CSS | 3.x | Utility-first CSS framework |
| Axios | 1.x | HTTP client |

### Development Tools

| Tool | Purpose |
|------|---------|
| Maven | Build automation |
| Git | Version control |
| GitHub | Code repository |
| Postman | API testing |
| Docker | Containerization |

---

## Microservices List

### Infrastructure Services

| Service | Port | Responsibility |
|---------|------|----------------|
| **Eureka Discovery Service** | 8761 | Service registration and discovery |
| **API Gateway** | 8080 | Request routing, security, load balancing |

### Business Services

| Service | Port | Responsibility |
|---------|------|----------------|
| **Auth Service** | 8081 | User authentication, JWT token management |
| **Card Service** | 8082 | Credit card CRUD operations |
| **Reward Service** | 8083 | Reward points management, redemption catalog |
| **Recommendation Service** | 8084 | Value calculation, recommendations |

---

## Project Structure

```
redeem-wise/
│
├── docs/
│   ├── 01_PROJECT_CONTEXT.md
│   ├── 02_REQUIREMENTS.md
│   ├── 03_ARCHITECTURE.md
│   ├── 04_DATABASE.md
│   ├── 05_API_CONTRACT.md
│   ├── 06_UI_FLOW.md
│   ├── 07_TASKS.md
│   ├── 08_CODING_STANDARDS.md
│   ├── 09_TESTING.md
│   └── 10_DEPLOYMENT.md
│
├── auth-service/
├── card-service/
├── reward-service/
├── recommendation-service/
├── api-gateway/
├── eureka-server/
├── frontend/
│
├── README.md
├── .gitignore
└── LICENSE
```

---

## Development Roadmap

### Phase 1: Foundation (Week 1-2)
- Architecture design
- Database schema
- API contracts
- Project setup

### Phase 2: Core Services (Week 3-6)
- Auth Service implementation
- Card Service implementation
- Reward Service implementation
- Gateway configuration

### Phase 3: Business Logic (Week 7-9)
- Recommendation engine
- Value per point calculation
- Dashboard aggregation

### Phase 4: Frontend (Week 8-11)
- React UI development
- Dashboard implementation
- Service integration

### Phase 5: Integration (Week 12-14)
- End-to-end testing
- Performance optimization
- Security testing

### Phase 6: Documentation (Week 15-16)
- Final documentation
- Presentation preparation

---

## Key Features

### Authentication & Security
- JWT-based authentication
- BCrypt password hashing
- Role-based access control
- Secure API endpoints

### Card Management
- Add, update, delete credit cards
- Card search and filtering
- Multiple card support

### Reward Management
- Track reward points across cards
- Expiry date monitoring
- Points summary

### Recommendation Engine
- Calculate Value Per Point (VPP)
- Rank redemption options
- Personalized recommendations

### Dashboard
- Total cards overview
- Total reward points
- Estimated redemption value
- Best recommendation display

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | User login |
| POST | `/api/cards` | Add credit card |
| GET | `/api/cards` | Get all cards |
| PUT | `/api/cards/{id}` | Update card |
| DELETE | `/api/cards/{id}` | Delete card |
| POST | `/api/rewards` | Add reward points |
| GET | `/api/rewards` | Get all rewards |
| GET | `/api/rewards/summary` | Get reward summary |
| GET | `/api/recommendations` | Get recommendations |
| GET | `/api/dashboard` | Get dashboard data |

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Node.js 18+
- MySQL 8.0
- Docker (optional)

### Local Development

```bash
# Clone the repository
git clone https://github.com/arunkumar-7/redeem-wise.git
cd redeem-wise

# Start infrastructure services
docker-compose up -d eureka-server api-gateway

# Start business services (in separate terminals)
cd auth-service && mvn spring-boot:run
cd card-service && mvn spring-boot:run
cd reward-service && mvn spring-boot:run
cd recommendation-service && mvn spring-boot:run

# Start frontend
cd frontend && npm install && npm start
```

### Docker Deployment

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

---

## Testing

### Run Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify -P integration-test

# Frontend tests
cd frontend && npm test

# E2E tests
cd frontend && npm run test:e2e
```

### Test Coverage

- **Unit Tests**: 80%+ coverage target
- **Integration Tests**: 70%+ coverage target
- **API Tests**: 100% endpoint coverage
- **E2E Tests**: Critical user flows

---

## Future Enhancements

### Short Term (v1.1)
- Real-time bank API integration
- Push notifications for point expiry
- Advanced analytics dashboard

### Medium Term (v2.0)
- Mobile applications (iOS/Android)
- Multi-language support
- OAuth2 integration

### Long Term (v3.0)
- AI-powered recommendations
- Predictive analytics
- International expansion
- Partner integrations

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## Documentation

Comprehensive documentation is available in the `docs/` directory:

| Document | Description |
|----------|-------------|
| [Project Context](docs/01_PROJECT_CONTEXT.md) | Project overview and context |
| [Requirements](docs/02_REQUIREMENTS.md) | Functional and non-functional requirements |
| [Architecture](docs/03_ARCHITECTURE.md) | System architecture and design |
| [Database](docs/04_DATABASE.md) | Database schema and design |
| [API Contract](docs/05_API_CONTRACT.md) | REST API specifications |
| [UI Flow](docs/06_UI_FLOW.md) | User interface flows |
| [Tasks](docs/07_TASKS.md) | Implementation task breakdown |
| [Coding Standards](docs/08_CODING_STANDARDS.md) | Development guidelines |
| [Testing](docs/09_TESTING.md) | Testing strategy and procedures |
| [Deployment](docs/10_DEPLOYMENT.md) | Deployment architecture |

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- Spring Boot Documentation
- React Documentation
- MySQL Documentation
- Docker Documentation

---

**RedeemWise Team** | MCA Final Year Project