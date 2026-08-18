# API_CONTRACT.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial API contract document |

---

## 1. Purpose

This document defines the complete REST API contract for the RedeemWise system. It serves as the primary reference for frontend and backend development teams to ensure consistent API implementation and consumption.

---

## 2. API Overview

### 2.1 Base URLs

| Environment | Base URL |
|-------------|----------|
| Development | http://localhost:8080 |
| Staging | https://api-staging.redeemwise.com |
| Production | https://api.redeemwise.com |

### 2.2 API Versioning

| Aspect | Convention |
|--------|------------|
| **Versioning Strategy** | URL path versioning |
| **Current Version** | v1 |
| **Version Header** | Not required (URL-based) |

### 2.3 Authentication

| Aspect | Details |
|--------|---------|
| **Type** | Bearer Token (JWT) |
| **Header** | `Authorization: Bearer {token}` |
| **Token Expiry** | 24 hours |
| **Protected Routes** | All routes except /api/auth/register and /api/auth/login |

### 2.4 Common Headers

| Header | Value | Required |
|--------|-------|----------|
| Content-Type | application/json | Yes |
| Accept | application/json | Yes |
| Authorization | Bearer {token} | Yes (except auth routes) |

---

## 3. Error Response Format

### 3.1 Standard Error Response

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

### 3.2 HTTP Status Codes

| Code | Usage | Description |
|------|-------|-------------|
| 200 | Success | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Successful deletion |
| 400 | Bad Request | Validation error |
| 401 | Unauthorized | Invalid or missing token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 500 | Server Error | Internal server error |

---

## 4. Authentication API

### 4.1 Register User

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123",
  "name": "John Doe"
}
```

**Request Validation:**

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| email | String | Yes | Valid email format, unique |
| password | String | Yes | Min 8 chars, 1 upper, 1 lower, 1 number |
| name | String | Yes | 2-100 characters |

**Success Response (201):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 201,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "CUSTOMER"
  }
}
```

**Error Response (400):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid input data",
  "path": "/api/auth/register",
  "details": [
    {
      "field": "email",
      "message": "Email already exists"
    }
  ]
}
```

### 4.2 Login User

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

**Request Validation:**

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| email | String | Yes | Valid email format |
| password | String | Yes | Non-empty |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "email": "user@example.com",
      "name": "John Doe",
      "role": "CUSTOMER"
    }
  }
}
```

**Error Response (401):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 401,
  "error": "Authentication Failed",
  "message": "Invalid email or password",
  "path": "/api/auth/login"
}
```

---

## 5. Card Management API

### 5.1 Add New Card

**Endpoint:** `POST /api/cards`

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "bankName": "HDFC Bank",
  "cardType": "PLATINUM",
  "cardNumber": "1234567890123456",
  "rewardProgram": "HDFC Rewards",
  "expiryDate": "2027-12-31"
}
```

**Request Validation:**

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| bankName | String | Yes | 1-100 characters |
| cardType | Enum | Yes | PLATINUM, GOLD, SILVER, OTHER |
| cardNumber | String | Yes | 16 digits |
| rewardProgram | String | Yes | 1-150 characters |
| expiryDate | Date | Yes | Future date |

**Success Response (201):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 201,
  "message": "Card added successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "bankName": "HDFC Bank",
    "cardType": "PLATINUM",
    "cardNumber": "****-****-****-3456",
    "rewardProgram": "HDFC Rewards",
    "expiryDate": "2027-12-31",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

**Error Response (400):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid card details",
  "path": "/api/cards",
  "details": [
    {
      "field": "cardNumber",
      "message": "Card number must be 16 digits"
    }
  ]
}
```

### 5.2 Get All Cards

**Endpoint:** `GET /api/cards`

**Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| bankName | String | No | Filter by bank name |
| cardType | String | No | Filter by card type |
| page | Integer | No | Page number (default: 0) |
| size | Integer | No | Page size (default: 10) |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Cards retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "userId": 1,
        "bankName": "HDFC Bank",
        "cardType": "PLATINUM",
        "cardNumber": "****-****-****-3456",
        "rewardProgram": "HDFC Rewards",
        "expiryDate": "2027-12-31",
        "createdAt": "2024-01-15T10:30:00Z"
      },
      {
        "id": 2,
        "userId": 1,
        "bankName": "ICICI Bank",
        "cardType": "GOLD",
        "cardNumber": "****-****-****-5678",
        "rewardProgram": "ICICI Reward Points",
        "expiryDate": "2026-06-30",
        "createdAt": "2024-01-15T10:35:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

### 5.3 Get Card by ID

**Endpoint:** `GET /api/cards/{id}`

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Card ID |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Card retrieved successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "bankName": "HDFC Bank",
    "cardType": "PLATINUM",
    "cardNumber": "****-****-****-3456",
    "rewardProgram": "HDFC Rewards",
    "expiryDate": "2027-12-31",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

**Error Response (404):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Card not found with id: 1",
  "path": "/api/cards/1"
}
```

### 5.4 Update Card

**Endpoint:** `PUT /api/cards/{id}`

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Card ID |

**Request Body:**
```json
{
  "bankName": "HDFC Bank",
  "cardType": "PLATINUM",
  "cardNumber": "1234567890123456",
  "rewardProgram": "HDFC Regalia Rewards",
  "expiryDate": "2028-12-31"
}
```

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Card updated successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "bankName": "HDFC Bank",
    "cardType": "PLATINUM",
    "cardNumber": "****-****-****-3456",
    "rewardProgram": "HDFC Regalia Rewards",
    "expiryDate": "2028-12-31",
    "updatedAt": "2024-01-15T10:35:00Z"
  }
}
```

### 5.5 Delete Card

**Endpoint:** `DELETE /api/cards/{id}`

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Card ID |

**Success Response (204):**
```
No Content
```

**Error Response (404):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Card not found with id: 1",
  "path": "/api/cards/1"
}
```

---

## 6. Reward Management API

### 6.1 Add Reward Points

**Endpoint:** `POST /api/rewards`

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "cardId": 1,
  "points": 5000,
  "expiryDate": "2025-12-31",
  "earningDate": "2024-01-15",
  "description": "Shopping rewards"
}
```

**Request Validation:**

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| cardId | Long | Yes | Must exist |
| points | Integer | Yes | > 0 |
| expiryDate | Date | Yes | Future date |
| earningDate | Date | Yes | Not future |
| description | String | No | Max 255 characters |

**Success Response (201):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 201,
  "message": "Reward points added successfully",
  "data": {
    "id": 1,
    "cardId": 1,
    "points": 5000,
    "expiryDate": "2025-12-31",
    "earningDate": "2024-01-15",
    "description": "Shopping rewards",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

### 6.2 Get All Reward Points

**Endpoint:** `GET /api/rewards`

**Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| cardId | Long | No | Filter by card ID |
| page | Integer | No | Page number (default: 0) |
| size | Integer | No | Page size (default: 10) |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Reward points retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "cardId": 1,
        "points": 5000,
        "expiryDate": "2025-12-31",
        "earningDate": "2024-01-15",
        "description": "Shopping rewards",
        "createdAt": "2024-01-15T10:30:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### 6.3 Update Reward Points

**Endpoint:** `PUT /api/rewards/{id}`

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Reward point ID |

**Request Body:**
```json
{
  "cardId": 1,
  "points": 7500,
  "expiryDate": "2025-12-31",
  "earningDate": "2024-01-15",
  "description": "Updated shopping rewards"
}
```

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Reward points updated successfully",
  "data": {
    "id": 1,
    "cardId": 1,
    "points": 7500,
    "expiryDate": "2025-12-31",
    "earningDate": "2024-01-15",
    "description": "Updated shopping rewards",
    "updatedAt": "2024-01-15T10:35:00Z"
  }
}
```

### 6.4 Delete Reward Points

**Endpoint:** `DELETE /api/rewards/{id}`

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Reward point ID |

**Success Response (204):**
```
No Content
```

### 6.5 Get Reward Points Summary

**Endpoint:** `GET /api/rewards/summary`

**Headers:**
```
Authorization: Bearer {token}
```

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Reward summary retrieved successfully",
  "data": {
    "totalPoints": 15000,
    "totalCards": 2,
    "expiringPoints": 5000,
    "expiringDate": "2025-06-30",
    "pointsByCard": [
      {
        "cardId": 1,
        "bankName": "HDFC Bank",
        "cardType": "PLATINUM",
        "points": 10000
      },
      {
        "cardId": 2,
        "bankName": "ICICI Bank",
        "cardType": "GOLD",
        "points": 5000
      }
    ]
  }
}
```

---

## 7. Redemption Catalog API

### 7.1 Get All Redemption Options

**Endpoint:** `GET /api/rewards/options`

**Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| category | String | No | Filter by category |
| sortBy | String | No | Sort by: value, points (default: value) |
| page | Integer | No | Page number (default: 0) |
| size | Integer | No | Page size (default: 10) |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Redemption options retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Amazon Gift Card ₹500",
        "category": "GIFT_CARD",
        "pointsRequired": 5000,
        "cashValue": 500.00,
        "valuePerPoint": 0.10,
        "description": "Amazon.in gift card worth ₹500",
        "imageUrl": "https://example.com/amazon-gift-card.jpg",
        "isActive": true
      },
      {
        "id": 2,
        "name": "Flipkart Voucher ₹1000",
        "category": "GIFT_CARD",
        "pointsRequired": 8000,
        "cashValue": 1000.00,
        "valuePerPoint": 0.125,
        "description": "Flipkart shopping voucher",
        "imageUrl": "https://example.com/flipkart-voucher.jpg",
        "isActive": true
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 5,
    "totalPages": 1
  }
}
```

### 7.2 Get Redemption Option by ID

**Endpoint:** `GET /api/rewards/options/{id}`

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Redemption option ID |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Redemption option retrieved successfully",
  "data": {
    "id": 1,
    "name": "Amazon Gift Card ₹500",
    "category": "GIFT_CARD",
    "pointsRequired": 5000,
    "cashValue": 500.00,
    "valuePerPoint": 0.10,
    "description": "Amazon.in gift card worth ₹500",
    "imageUrl": "https://example.com/amazon-gift-card.jpg",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

---

## 8. Recommendation API

### 8.1 Get Recommendations

**Endpoint:** `GET /api/recommendations`

**Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| cardId | Long | No | Specific card ID |
| topN | Integer | No | Number of recommendations (default: 3) |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Recommendations generated successfully",
  "data": {
    "userId": 1,
    "totalPoints": 15000,
    "recommendations": [
      {
        "rank": 1,
        "redemptionOption": {
          "id": 3,
          "name": "Cashback to Account",
          "category": "CASHBACK",
          "pointsRequired": 4000,
          "cashValue": 400.00
        },
        "valuePerPoint": 0.10,
        "isRecommended": true,
        "reason": "Best value per point ratio"
      },
      {
        "rank": 2,
        "redemptionOption": {
          "id": 5,
          "name": "Flight Discount Voucher",
          "category": "TRAVEL",
          "pointsRequired": 10000,
          "cashValue": 1200.00
        },
        "valuePerPoint": 0.12,
        "isRecommended": false,
        "reason": "High value for travel enthusiasts"
      },
      {
        "rank": 3,
        "redemptionOption": {
          "id": 2,
          "name": "Flipkart Voucher ₹1000",
          "category": "GIFT_CARD",
          "pointsRequired": 8000,
          "cashValue": 1000.00
        },
        "valuePerPoint": 0.125,
        "isRecommended": false,
        "reason": "Good value for shopping"
      }
    ]
  }
}
```

### 8.2 Calculate Value Per Point

**Endpoint:** `GET /api/recommendations/value-per-point`

**Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| optionId | Long | Yes | Redemption option ID |

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Value per point calculated successfully",
  "data": {
    "optionId": 1,
    "optionName": "Amazon Gift Card ₹500",
    "pointsRequired": 5000,
    "cashValue": 500.00,
    "valuePerPoint": 0.10,
    "currency": "INR"
  }
}
```

---

## 9. Dashboard API

### 9.1 Get Dashboard Data

**Endpoint:** `GET /api/dashboard`

**Headers:**
```
Authorization: Bearer {token}
```

**Success Response (200):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 200,
  "message": "Dashboard data retrieved successfully",
  "data": {
    "totalCards": 3,
    "totalPoints": 25000,
    "estimatedValue": 2500.00,
    "bestRecommendation": {
      "rank": 1,
      "optionName": "Cashback to Account",
      "valuePerPoint": 0.10,
      "pointsRequired": 4000,
      "cashValue": 400.00
    },
    "pointsByCard": [
      {
        "cardId": 1,
        "bankName": "HDFC Bank",
        "cardType": "PLATINUM",
        "points": 12000,
        "estimatedValue": 1200.00
      },
      {
        "cardId": 2,
        "bankName": "ICICI Bank",
        "cardType": "GOLD",
        "points": 8000,
        "estimatedValue": 800.00
      },
      {
        "cardId": 3,
        "bankName": "SBI Cards",
        "cardType": "SILVER",
        "points": 5000,
        "estimatedValue": 500.00
      }
    ],
    "expiringPoints": [
      {
        "cardId": 2,
        "points": 3000,
        "expiryDate": "2025-03-31",
        "daysUntilExpiry": 75
      }
    ]
  }
}
```

---

## 10. API Rate Limiting

### 10.1 Rate Limit Configuration

| Endpoint Category | Limit | Window |
|-------------------|-------|--------|
| Authentication | 10 requests | 1 minute |
| Read Operations | 100 requests | 1 minute |
| Write Operations | 50 requests | 1 minute |
| Recommendation | 20 requests | 1 minute |

### 10.2 Rate Limit Response

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Please try again after 60 seconds.",
  "path": "/api/cards",
  "retryAfter": 60
}
```

---

## 11. API Documentation

### 11.1 Swagger/OpenAPI

| Aspect | Details |
|--------|---------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI Spec** | http://localhost:8080/v3/api-docs |
| **Version** | OpenAPI 3.0 |

### 11.2 Postman Collection

| Aspect | Details |
|--------|---------|
| **Collection Name** | RedeemWise API v1 |
| **Import URL** | Available in repository |
| **Environment** | Development, Staging, Production |

---

## 12. API Versioning Strategy

### 12.1 Versioning Rules

| Rule | Description |
|------|-------------|
| **Breaking Changes** | Require new version (v2) |
| **Non-Breaking Changes** | Add to current version |
| **Deprecation Notice** | 6 months before removal |
| **Version Header** | Not required (URL-based) |

### 12.2 Change Types

| Change Type | Version Impact | Example |
|-------------|----------------|---------|
| Adding new field | Non-breaking | Add "phone" to user |
| Removing field | Breaking | Remove "address" |
| Changing field type | Breaking | Change "age" to "dateOfBirth" |
| Adding new endpoint | Non-breaking | Add "GET /api/reports" |

---

*Document maintained by RedeemWise Development Team*