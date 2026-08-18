# REQUIREMENTS.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial requirements document |

---

## 1. Purpose

This document defines the complete set of functional and non-functional requirements for the RedeemWise system. It serves as the primary reference for development, testing, and acceptance criteria.

---

## 2. Functional Requirements

### 2.1 User Management Module

#### 2.1.1 User Registration

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-UM-001 | New user registration | High | System shall allow new users to register with email and password |
| FR-UM-002 | Email validation | High | System shall validate email format and uniqueness |
| FR-UM-003 | Password strength | High | System shall enforce minimum 8 characters, 1 uppercase, 1 lowercase, 1 number |
| FR-UM-004 | Registration confirmation | Medium | System shall send confirmation message upon successful registration |
| FR-UM-005 | Duplicate prevention | High | System shall prevent duplicate email registrations |

**User Registration Flow:**
```
User submits registration form
    ↓
Validate email format
    ↓
Check email uniqueness
    ↓
Hash password
    ↓
Create user record
    ↓
Return success response
```

#### 2.1.2 User Login

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-UM-010 | User login | High | System shall authenticate users with email and password |
| FR-UM-011 | JWT token generation | High | System shall generate JWT token upon successful login |
| FR-UM-012 | Token expiry | High | JWT token shall expire after 24 hours |
| FR-UM-013 | Invalid credentials | High | System shall return appropriate error for invalid credentials |
| FR-UM-014 | Account lockout | Medium | System shall lock account after 5 failed login attempts |

#### 2.1.3 User Profile Management

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-UM-020 | View profile | Medium | Users shall be able to view their profile information |
| FR-UM-021 | Update profile | Medium | Users shall be able to update name and contact details |
| FR-UM-022 | Change password | High | Users shall be able to change their password |
| FR-UM-023 | Delete account | Low | Users shall be able to delete their account |

---

### 2.2 Card Management Module

#### 2.2.1 Add Credit Card

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-CM-001 | Add new card | High | System shall allow users to add credit cards |
| FR-CM-002 | Card details | High | System shall capture: Bank Name, Card Type, Card Number (masked), Reward Program |
| FR-CM-003 | Card validation | High | System shall validate card number format |
| FR-CM-004 | Duplicate check | Medium | System shall prevent adding duplicate cards |
| FR-CM-005 | Maximum cards | Medium | System shall limit to 10 cards per user |

**Card Details Schema:**
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| bankName | String | Yes | Max 100 chars |
| cardType | Enum | Yes | PLATINUM, GOLD, SILVER, OTHER |
| cardNumber | String | Yes | 16 digits, masked storage |
| rewardProgram | String | Yes | Max 150 chars |
| expiryDate | Date | Yes | Future date |

#### 2.2.2 View Credit Cards

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-CM-010 | View all cards | High | System shall display all user's credit cards |
| FR-CM-011 | View card details | High | System shall show complete card details |
| FR-CM-012 | Search cards | Medium | System shall allow searching by bank name |
| FR-CM-013 | Filter by type | Medium | System shall allow filtering by card type |

#### 2.2.3 Update Credit Card

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-CM-020 | Update card | High | System shall allow updating card details |
| FR-CM-021 | Partial update | Medium | System shall support partial field updates |
| FR-CM-022 | Validation on update | High | System shall validate updated fields |

#### 2.2.4 Delete Credit Card

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-CM-030 | Delete card | High | System shall allow deleting credit cards |
| FR-CM-031 | Confirmation | High | System shall require confirmation before deletion |
| FR-CM-032 | Cascade delete | Medium | System shall delete associated reward points |

---

### 2.3 Reward Management Module

#### 2.3.1 Add Reward Points

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RM-001 | Add points | High | System shall allow adding reward points for a card |
| FR-RM-002 | Points validation | High | System shall validate points as positive integer |
| FR-RM-003 | Card association | High | Points must be associated with existing card |
| FR-RM-004 | Initial balance | Medium | System shall set initial balance on first entry |

**Reward Points Schema:**
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| cardId | Long | Yes | Must exist |
| points | Integer | Yes | > 0 |
| expiryDate | Date | Yes | Future date |
| earningDate | Date | Yes | Not future |

#### 2.3.2 Update Reward Points

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RM-010 | Update points | High | System shall allow updating point balances |
| FR-RM-011 | Balance calculation | High | System shall calculate running balance |
| FR-RM-012 | Transaction history | Medium | System shall maintain point transaction history |

#### 2.3.3 View Reward Points

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RM-020 | View all points | High | System shall display all reward points |
| FR-RM-021 | Points by card | High | System shall allow filtering by card |
| FR-RM-022 | Points summary | Medium | System shall show total points across all cards |
| FR-RM-023 | Expiry alerts | Medium | System shall highlight points expiring soon |

---

### 2.4 Redemption Catalog Module

#### 2.4.1 Store Redemption Options

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RC-001 | Add redemption option | High | System shall store redemption options |
| FR-RC-002 | Option details | High | System shall capture: Name, Category, Points Required, Cash Value |
| FR-RC-003 | Category management | Medium | System shall support multiple categories |
| FR-RC-004 | Admin management | Medium | Redemption options managed by system admin |

**Redemption Option Schema:**
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| name | String | Yes | Max 200 chars |
| category | Enum | Yes | GIFT_CARD, CASHBACK, MERCHANDISE, TRAVEL, DINING |
| pointsRequired | Integer | Yes | > 0 |
| cashValue | Decimal | Yes | > 0 |
| description | String | No | Max 1000 chars |
| imageUrl | String | No | Valid URL |

#### 2.4.2 View Redemption Options

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RC-010 | View all options | High | System shall display all redemption options |
| FR-RC-011 | Filter by category | High | System shall allow filtering by category |
| FR-RC-012 | Search options | Medium | System shall allow searching by name |
| FR-RC-013 | Sort by value | High | System shall allow sorting by value per point |

---

### 2.5 Recommendation Engine Module

#### 2.5.1 Calculate Value Per Point

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RE-001 | Calculate VPP | High | System shall calculate Value Per Point for each option |
| FR-RE-002 | Formula | High | VPP = Cash Value / Points Required |
| FR-RE-003 | Display VPP | High | System shall display VPP prominently |
| FR-RE-004 | Currency format | Medium | System shall display values in INR format |

**Value Per Point Calculation Example:**
```
Redemption Option: Amazon Gift Card
Cash Value: ₹500
Points Required: 5000
Value Per Point = 500 / 5000 = ₹0.10 per point
```

#### 2.5.2 Rank Redemption Options

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RE-010 | Rank options | High | System shall rank options by VPP (descending) |
| FR-RE-011 | Show ranking | High | System shall display rank number for each option |
| FR-RE-012 | Highlight top 3 | Medium | System shall visually highlight top 3 options |

#### 2.5.3 Generate Recommendations

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-RE-020 | Generate recommendations | High | System shall generate best redemption recommendations |
| FR-RE-021 | Personalized | High | Recommendations based on user's point balance |
| FR-RE-022 | Multiple recommendations | Medium | System shall suggest top 3 recommendations |
| FR-RE-023 | Recommendation reason | Medium | System shall explain why option is recommended |

---

### 2.6 Dashboard Module

| Req ID | Requirement | Priority | Description |
|--------|-------------|----------|-------------|
| FR-DB-001 | Total cards | High | Dashboard shall display total number of cards |
| FR-DB-002 | Total points | High | Dashboard shall display total reward points |
| FR-DB-003 | Estimated value | High | Dashboard shall display estimated redemption value |
| FR-DB-004 | Best recommendation | High | Dashboard shall show top recommendation |
| FR-DB-005 | Points by card | Medium | Dashboard shall show points breakdown by card |
| FR-DB-006 | Expiry alerts | Medium | Dashboard shall show points expiring soon |

**Dashboard Layout:**
```
┌─────────────────────────────────────────────────┐
│  Total Cards: 5  │  Total Points: 75,000       │
├─────────────────────────────────────────────────┤
│  Estimated Value: ₹7,500  │  Best Option: ...  │
├─────────────────────────────────────────────────┤
│  Points by Card Table                          │
├─────────────────────────────────────────────────┤
│  Expiring Soon Alerts                          │
└─────────────────────────────────────────────────┘
```

---

## 3. Non-Functional Requirements

### 3.1 Performance Requirements

| Req ID | Requirement | Target |
|--------|-------------|--------|
| NFR-PF-001 | API response time | < 500ms (95th percentile) |
| NFR-PF-002 | Page load time | < 3 seconds |
| NFR-PF-003 | Database query time | < 100ms |
| NFR-PF-004 | Concurrent users | 1000+ simultaneous |
| NFR-PF-005 | Throughput | 100 requests/second |

### 3.2 Security Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-SC-001 | Authentication | JWT-based stateless authentication |
| NFR-SC-002 | Authorization | Role-based access control (RBAC) |
| NFR-SC-003 | Data encryption | Passwords hashed using BCrypt |
| NFR-SC-004 | API security | All endpoints (except login/register) require valid JWT |
| NFR-SC-005 | Input validation | Server-side validation for all inputs |
| NFR-SC-006 | SQL injection prevention | Use parameterized queries |
| NFR-SC-007 | CORS policy | Configure appropriate CORS headers |

### 3.3 Scalability Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-SC-001 | Horizontal scaling | Microservices can scale independently |
| NFR-SC-002 | Service discovery | Eureka for dynamic service registration |
| NFR-SC-003 | Load balancing | Gateway-based load balancing |
| NFR-SC-004 | Database scaling | Read replicas for query performance |

### 3.4 Availability Requirements

| Req ID | Requirement | Target |
|--------|-------------|--------|
| NFR-AV-001 | System uptime | 99.5% |
| NFR-AV-002 | Recovery time | < 5 minutes |
| NFR-AV-003 | Data backup | Daily automated backups |
| NFR-AV-004 | Fault tolerance | Graceful degradation on service failure |

### 3.5 Maintainability Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-MT-001 | Code documentation | All public APIs documented |
| NFR-MT-002 | Logging | Structured logging with levels |
| NFR-MT-003 | Monitoring | Health check endpoints |
| NFR-MT-004 | Configuration | Externalized configuration |

### 3.6 Usability Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-US-001 | Responsive design | Mobile and desktop compatible |
| NFR-US-002 | Accessibility | WCAG 2.1 Level AA compliance |
| NFR-US-003 | Intuitive navigation | Maximum 3 clicks to any feature |
| NFR-US-004 | Error messages | Clear, actionable error messages |

### 3.7 Compatibility Requirements

| Req ID | Requirement | Description |
|--------|-------------|-------------|
| NFR-CP-001 | Browser support | Chrome, Firefox, Safari, Edge (latest 2 versions) |
| NFR-CP-002 | Java version | Java 21 LTS |
| NFR-CP-003 | Node.js version | Node.js 18+ for frontend |
| NFR-CP-004 | MySQL version | MySQL 8.0+ |

---

## 4. User Stories

### 4.1 Authentication Stories

| Story ID | As a... | I want to... | So that... |
|----------|---------|--------------|------------|
| US-001 | New user | Register with email and password | I can create an account |
| US-002 | Registered user | Login with my credentials | I can access my dashboard |
| US-003 | Logged-in user | Logout securely | My session is terminated |
| US-004 | User | Change my password | I can maintain account security |

### 4.2 Card Management Stories

| Story ID | As a... | I want to... | So that... |
|----------|---------|--------------|------------|
| US-010 | User | Add my credit card details | I can track its reward points |
| US-011 | User | View all my credit cards | I can see my card portfolio |
| US-012 | User | Update card information | I can keep details current |
| US-013 | User | Delete a card | I can remove unwanted cards |

### 4.3 Reward Management Stories

| Story ID | As a... | I want to... | So that... |
|----------|---------|--------------|------------|
| US-020 | User | Add reward points for a card | I can track my accumulation |
| US-021 | User | Update my point balance | I can keep balance accurate |
| US-022 | User | View points by card | I can see per-card breakdown |
| US-023 | User | See total points | I know my overall balance |

### 4.4 Recommendation Stories

| Story ID | As a... | I want to... | So that... |
|----------|---------|--------------|------------|
| US-030 | User | See value per redemption option | I can compare options |
| US-031 | User | Get best redemption recommendation | I can maximize my value |
| US-032 | User | Sort options by value | I can find best deals quickly |
| US-033 | User | See top 3 recommendations | I have clear choices |

### 4.5 Dashboard Stories

| Story ID | As a... | I want to... | So that... |
|----------|---------|--------------|------------|
| US-040 | User | See summary on dashboard | I have quick overview |
| US-041 | User | View estimated total value | I know potential worth |
| US-042 | User | See best recommendation | I can act quickly |

---

## 5. Acceptance Criteria

### 5.1 Registration Acceptance Criteria

```gherkin
Feature: User Registration

  Scenario: Successful registration
    Given I am a new user
    When I submit valid registration details
    Then my account is created
    And I receive a success message

  Scenario: Duplicate email registration
    Given I am a new user
    When I try to register with an existing email
    Then I receive an error message
    And my account is not created

  Scenario: Weak password rejection
    Given I am a new user
    When I submit a password shorter than 8 characters
    Then I receive a password strength error
```

### 5.2 Card Management Acceptance Criteria

```gherkin
Feature: Card Management

  Scenario: Add new card
    Given I am logged in
    When I add a new credit card with valid details
    Then the card is added to my account
    And I see it in my card list

  Scenario: Delete card with rewards
    Given I have a card with reward points
    When I delete the card
    Then I am asked for confirmation
    And the card and its rewards are deleted
```

### 5.3 Recommendation Acceptance Criteria

```gherkin
Feature: Redemption Recommendation

  Scenario: Calculate value per point
    Given a redemption option costs 5000 points for ₹500
    When I view the recommendation
    Then I see value per point as ₹0.10

  Scenario: Best recommendation generation
    Given I have 10000 reward points
    When I request recommendations
    Then I see the top 3 options ranked by value per point
    And the best option is highlighted
```

---

## 6. Data Validation Rules

### 6.1 Field Validation Rules

| Entity | Field | Rule | Error Message |
|--------|-------|------|---------------|
| User | email | Valid email format | "Invalid email format" |
| User | password | Min 8 chars, 1 upper, 1 lower, 1 number | "Password does not meet requirements" |
| User | name | 2-100 characters | "Name must be 2-100 characters" |
| Card | bankName | 1-100 characters | "Bank name is required" |
| Card | cardNumber | 16 digits | "Invalid card number" |
| Card | cardType | Valid enum value | "Invalid card type" |
| RewardPoint | points | Positive integer | "Points must be positive" |
| RewardPoint | expiryDate | Future date | "Expiry date must be in future" |
| RedemptionOption | pointsRequired | Positive integer | "Points required must be positive" |
| RedemptionOption | cashValue | Positive decimal | "Cash value must be positive" |

---

## 7. Error Handling Requirements

### 7.1 Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input data",
  "path": "/api/cards",
  "details": [
    {
      "field": "cardNumber",
      "message": "Card number must be 16 digits"
    }
  ]
}
```

### 7.2 HTTP Status Codes

| Code | Usage |
|------|-------|
| 200 | Successful operation |
| 201 | Resource created |
| 204 | Successful deletion |
| 400 | Bad request / validation error |
| 401 | Unauthorized / invalid token |
| 403 | Forbidden / insufficient permissions |
| 404 | Resource not found |
| 409 | Conflict / duplicate resource |
| 500 | Internal server error |

---

## 8. Assumptions and Dependencies

### 8.1 Assumptions

| ID | Assumption |
|----|------------|
| A-01 | Users will manually enter reward point balances |
| A-02 | Redemption option data is maintained by system administrators |
| A-03 | Single currency (INR) for all value calculations |
| A-04 | Users have basic understanding of credit card rewards |

### 8.2 Dependencies

| ID | Dependency | Impact |
|----|------------|--------|
| D-01 | MySQL database availability | Core data storage |
| D-02 | Java 21 runtime | Backend execution |
| D-03 | Node.js runtime | Frontend build |
| D-04 | Network connectivity | Microservices communication |

---

## 9. Traceability Matrix

| Requirement | User Story | API Endpoint | Test Case |
|-------------|------------|--------------|-----------|
| FR-UM-001 | US-001 | POST /api/auth/register | TC-REG-001 |
| FR-UM-010 | US-002 | POST /api/auth/login | TC-LOG-001 |
| FR-CM-001 | US-010 | POST /api/cards | TC-CARD-001 |
| FR-CM-010 | US-011 | GET /api/cards | TC-CARD-002 |
| FR-RM-001 | US-020 | POST /api/rewards | TC-REW-001 |
| FR-RE-001 | US-030 | GET /api/recommendations | TC-REC-001 |
| FR-DB-001 | US-040 | GET /api/dashboard | TC-DASH-001 |

---

*Document maintained by RedeemWise Development Team*