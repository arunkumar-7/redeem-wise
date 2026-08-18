# DATABASE.md

## RedeemWise – Reward Points Redemption Optimizer

### Document Version
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2024-01-15 | RedeemWise Team | Initial database document |

---

## 1. Purpose

This document defines the complete database architecture for the RedeemWise system, including entity relationships, schema definitions, indexing strategies, and data management policies. It serves as the primary reference for database design and implementation.

---

## 2. Database Strategy

### 2.1 Database per Service Pattern

RedeemWise follows the **Database per Service** pattern, where each microservice owns its own database to ensure loose coupling and independent scaling.

| Service | Database Name | Database Type | Purpose |
|---------|---------------|---------------|---------|
| Auth Service | redeemwise_auth | MySQL 8.0 | User authentication data |
| Card Service | redeemwise_card | MySQL 8.0 | Credit card information |
| Reward Service | redeemwise_reward | MySQL 8.0 | Reward points and redemption catalog |
| Recommendation Service | redeemwise_recommendation | MySQL 8.0 | Recommendation history |

---

## 3. Entity Relationship Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        ENTITY RELATIONSHIP DIAGRAM                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                              ┌──────────────┐                               │
│                              │     USER     │                               │
│                              │──────────────│                               │
│                              │ id (PK)      │                               │
│                              │ email        │                               │
│                              │ password     │                               │
│                              │ name         │                               │
│                              │ role         │                               │
│                              │ created_at   │                               │
│                              │ updated_at   │                               │
│                              └──────┬───────┘                               │
│                                     │                                       │
│                                     │ 1:N                                   │
│                                     ▼                                       │
│                              ┌──────────────┐                               │
│                              │     CARD     │                               │
│                              │──────────────│                               │
│                              │ id (PK)      │                               │
│                              │ user_id (FK) │                               │
│                              │ bank_name    │                               │
│                              │ card_type    │                               │
│                              │ card_number  │                               │
│                              │ reward_prog  │                               │
│                              │ expiry_date  │                               │
│                              │ created_at   │                               │
│                              │ updated_at   │                               │
│                              └──────┬───────┘                               │
│                                     │                                       │
│                                     │ 1:N                                   │
│                                     ▼                                       │
│                              ┌──────────────┐                               │
│                              │ REWARD_POINT │                               │
│                              │──────────────│                               │
│                              │ id (PK)      │                               │
│                              │ card_id (FK) │                               │
│                              │ points       │                               │
│                              │ expiry_date  │                               │
│                              │ earning_date │                               │
│                              │ created_at   │                               │
│                              │ updated_at   │                               │
│                              └──────────────┘                               │
│                                                                             │
│                              ┌──────────────┐                               │
│                              │  REDEMPTION  │                               │
│                              │    OPTION    │                               │
│                              │──────────────│                               │
│                              │ id (PK)      │                               │
│                              │ name         │                               │
│                              │ category     │                               │
│                              │ points_req   │                               │
│                              │ cash_value   │                               │
│                              │ description  │                               │
│                              │ image_url    │                               │
│                              │ is_active    │                               │
│                              │ created_at   │                               │
│                              │ updated_at   │                               │
│                              └──────┬───────┘                               │
│                                     │                                       │
│                                     │ 1:N                                   │
│                                     ▼                                       │
│                              ┌──────────────┐                               │
│                              │RECOMMENDATION│                               │
│                              │──────────────│                               │
│                              │ id (PK)      │                               │
│                              │ user_id (FK) │                               │
│                              │ option_id(FK)│                               │
│                              │ points_used  │                               │
│                              │ value_per_pt │                               │
│                              │ rank         │                               │
│                              │ created_at   │                               │
│                              └──────────────┘                               │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Database Schema Definitions

### 4.1 Auth Service Database (redeemwise_auth)

#### 4.1.1 users Table

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER', 'ADMIN') DEFAULT 'CUSTOMER',
    is_active BOOLEAN DEFAULT TRUE,
    failed_login_attempts INT DEFAULT 0,
    account_locked_until TIMESTAMP NULL,
    last_login_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_users_email (email),
    INDEX idx_users_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Field Descriptions:**

| Field | Type | Nullable | Description |
|-------|------|----------|-------------|
| id | BIGINT | No | Primary key, auto-increment |
| email | VARCHAR(255) | No | Unique user email |
| password | VARCHAR(255) | No | BCrypt hashed password |
| name | VARCHAR(100) | No | User's full name |
| role | ENUM | No | User role (CUSTOMER/ADMIN) |
| is_active | BOOLEAN | No | Account active status |
| failed_login_attempts | INT | No | Counter for failed logins |
| account_locked_until | TIMESTAMP | Yes | Lock expiry time |
| last_login_at | TIMESTAMP | Yes | Last successful login |
| created_at | TIMESTAMP | No | Record creation time |
| updated_at | TIMESTAMP | No | Last update time |

---

### 4.2 Card Service Database (redeemwise_card)

#### 4.2.1 cards Table

```sql
CREATE TABLE cards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    card_type ENUM('PLATINUM', 'GOLD', 'SILVER', 'OTHER') NOT NULL,
    card_number VARCHAR(255) NOT NULL,
    reward_program VARCHAR(150) NOT NULL,
    expiry_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_cards_user_id (user_id),
    INDEX idx_cards_bank_name (bank_name),
    INDEX idx_cards_active (is_active),
    CONSTRAINT fk_cards_user FOREIGN KEY (user_id) 
        REFERENCES auth_service.users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Field Descriptions:**

| Field | Type | Nullable | Description |
|-------|------|----------|-------------|
| id | BIGINT | No | Primary key, auto-increment |
| user_id | BIGINT | No | Foreign key to users table |
| bank_name | VARCHAR(100) | No | Name of the issuing bank |
| card_type | ENUM | No | Card tier (PLATINUM/GOLD/SILVER/OTHER) |
| card_number | VARCHAR(255) | No | Encrypted card number |
| reward_program | VARCHAR(150) | No | Name of reward program |
| expiry_date | DATE | No | Card expiry date |
| is_active | BOOLEAN | No | Soft delete flag |
| created_at | TIMESTAMP | No | Record creation time |
| updated_at | TIMESTAMP | No | Last update time |

---

### 4.3 Reward Service Database (redeemwise_reward)

#### 4.3.1 reward_points Table

```sql
CREATE TABLE reward_points (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    card_id BIGINT NOT NULL,
    points INT NOT NULL,
    expiry_date DATE NOT NULL,
    earning_date DATE NOT NULL,
    description VARCHAR(255) NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_reward_points_card_id (card_id),
    INDEX idx_reward_points_expiry (expiry_date),
    INDEX idx_reward_points_active (is_active),
    CONSTRAINT fk_reward_points_card FOREIGN KEY (card_id) 
        REFERENCES card_service.cards(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Field Descriptions:**

| Field | Type | Nullable | Description |
|-------|------|----------|-------------|
| id | BIGINT | No | Primary key, auto-increment |
| card_id | BIGINT | No | Foreign key to cards table |
| points | INT | No | Number of reward points |
| expiry_date | DATE | No | Points expiry date |
| earning_date | DATE | No | Date points were earned |
| description | VARCHAR(255) | Yes | Transaction description |
| is_active | BOOLEAN | No | Soft delete flag |
| created_at | TIMESTAMP | No | Record creation time |
| updated_at | TIMESTAMP | No | Last update time |

#### 4.3.2 redemption_options Table

```sql
CREATE TABLE redemption_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    category ENUM('GIFT_CARD', 'CASHBACK', 'MERCHANDISE', 'TRAVEL', 'DINING') NOT NULL,
    points_required INT NOT NULL,
    cash_value DECIMAL(10,2) NOT NULL,
    description TEXT NULL,
    image_url VARCHAR(500) NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_redemption_category (category),
    INDEX idx_redemption_active (is_active),
    INDEX idx_redemption_points (points_required)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Field Descriptions:**

| Field | Type | Nullable | Description |
|-------|------|----------|-------------|
| id | BIGINT | No | Primary key, auto-increment |
| name | VARCHAR(200) | No | Redemption option name |
| category | ENUM | No | Option category |
| points_required | INT | No | Points needed for redemption |
| cash_value | DECIMAL(10,2) | No | Cash equivalent value |
| description | TEXT | Yes | Detailed description |
| image_url | VARCHAR(500) | Yes | Product image URL |
| is_active | BOOLEAN | No | Active status |
| created_at | TIMESTAMP | No | Record creation time |
| updated_at | TIMESTAMP | No | Last update time |

---

### 4.4 Recommendation Service Database (redeemwise_recommendation)

#### 4.4.1 recommendations Table

```sql
CREATE TABLE recommendations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    redemption_option_id BIGINT NOT NULL,
    points_used INT NOT NULL,
    value_per_point DECIMAL(10,4) NOT NULL,
    rank_position INT NOT NULL,
    is_recommended BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_recommendations_user (user_id),
    INDEX idx_recommendations_option (redemption_option_id),
    INDEX idx_recommendations_rank (rank_position),
    CONSTRAINT fk_recommendations_option FOREIGN KEY (redemption_option_id) 
        REFERENCES reward_service.redemption_options(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Field Descriptions:**

| Field | Type | Nullable | Description |
|-------|------|----------|-------------|
| id | BIGINT | No | Primary key, auto-increment |
| user_id | BIGINT | No | Foreign key to users table |
| redemption_option_id | BIGINT | No | Foreign key to redemption_options |
| points_used | INT | No | Points used for this recommendation |
| value_per_point | DECIMAL(10,4) | No | Calculated value per point |
| rank_position | INT | No | Rank in recommendation list |
| is_recommended | BOOLEAN | No | Top recommendation flag |
| created_at | TIMESTAMP | No | Record creation time |

---

## 5. Indexing Strategy

### 5.1 Index Recommendations

| Table | Index Name | Columns | Purpose |
|-------|------------|---------|---------|
| users | idx_users_email | email | Login lookup |
| users | idx_users_active | is_active | Active user queries |
| cards | idx_cards_user_id | user_id | User's cards lookup |
| cards | idx_cards_bank_name | bank_name | Bank search |
| reward_points | idx_reward_points_card_id | card_id | Card's points lookup |
| reward_points | idx_reward_points_expiry | expiry_date | Expiry alerts |
| redemption_options | idx_redemption_category | category | Category filter |
| redemption_options | idx_redemption_points | points_required | Value sorting |
| recommendations | idx_recommendations_user | user_id | User recommendations |
| recommendations | idx_recommendations_rank | rank_position | Ranking queries |

### 5.2 Composite Indexes

```sql
-- Card search optimization
CREATE INDEX idx_cards_user_active ON cards(user_id, is_active);

-- Points lookup optimization
CREATE INDEX idx_points_card_active ON reward_points(card_id, is_active);

-- Expiry alert optimization
CREATE INDEX idx_points_expiry_active ON reward_points(expiry_date, is_active);
```

---

## 6. Data Types Reference

### 6.1 MySQL Data Types Used

| Data Type | Usage | Example |
|-----------|-------|---------|
| BIGINT | Primary keys, foreign keys | id, user_id, card_id |
| VARCHAR | Variable-length strings | email, bank_name |
| TEXT | Long text content | description |
| INT | Integer values | points, rank_position |
| DECIMAL | Precise decimal values | cash_value, value_per_point |
| DATE | Date values | expiry_date, earning_date |
| TIMESTAMP | Date-time values | created_at, updated_at |
| BOOLEAN | True/false flags | is_active, is_recommended |
| ENUM | Fixed value sets | card_type, category, role |

### 6.2 Enum Values

| Enum Type | Values |
|-----------|--------|
| Card Type | PLATINUM, GOLD, SILVER, OTHER |
| Category | GIFT_CARD, CASHBACK, MERCHANDISE, TRAVEL, DINING |
| Role | CUSTOMER, ADMIN |

---

## 7. Constraints

### 7.1 Primary Key Constraints

| Table | Column | Type |
|-------|--------|------|
| users | id | BIGINT AUTO_INCREMENT |
| cards | id | BIGINT AUTO_INCREMENT |
| reward_points | id | BIGINT AUTO_INCREMENT |
| redemption_options | id | BIGINT AUTO_INCREMENT |
| recommendations | id | BIGINT AUTO_INCREMENT |

### 7.2 Foreign Key Constraints

| Table | Column | References | On Delete |
|-------|--------|------------|-----------|
| cards | user_id | users.id | CASCADE |
| reward_points | card_id | cards.id | CASCADE |
| recommendations | redemption_option_id | redemption_options.id | CASCADE |

### 7.3 Unique Constraints

| Table | Column | Constraint |
|-------|--------|------------|
| users | email | UNIQUE |

### 7.4 Check Constraints

```sql
-- Ensure positive points
ALTER TABLE reward_points ADD CONSTRAINT chk_points_positive 
    CHECK (points > 0);

-- Ensure positive cash value
ALTER TABLE redemption_options ADD CONSTRAINT chk_cash_value_positive 
    CHECK (cash_value > 0);

-- Ensure positive points required
ALTER TABLE redemption_options ADD CONSTRAINT chk_points_required_positive 
    CHECK (points_required > 0);
```

---

## 8. Sample Data

### 8.1 Sample Users

```sql
INSERT INTO users (email, password, name, role) VALUES
('john.doe@email.com', '$2a$12$LJ3m4ys1G6XQ5Z5Y5Z5Y5e.K6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K', 'John Doe', 'CUSTOMER'),
('jane.smith@email.com', '$2a$12$LJ3m4ys1G6XQ5Z5Y5Z5Y5e.K6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K', 'Jane Smith', 'CUSTOMER'),
('admin@redeemwise.com', '$2a$12$LJ3m4ys1G6XQ5Z5Y5Z5Y5e.K6X7Y8Z9A0B1C2D3E4F5G6H7I8J9K', 'Admin User', 'ADMIN');
```

### 8.2 Sample Cards

```sql
INSERT INTO cards (user_id, bank_name, card_type, card_number, reward_program, expiry_date) VALUES
(1, 'HDFC Bank', 'PLATINUM', 'encrypted_1234567890123456', 'HDFC Rewards', '2027-12-31'),
(1, 'ICICI Bank', 'GOLD', 'encrypted_2345678901234567', 'ICICI Reward Points', '2026-06-30'),
(2, 'SBI Cards', 'SILVER', 'encrypted_3456789012345678', 'SBI Rewardz', '2025-09-30');
```

### 8.3 Sample Reward Points

```sql
INSERT INTO reward_points (card_id, points, expiry_date, earning_date, description) VALUES
(1, 15000, '2025-12-31', '2024-01-01', 'Welcome bonus'),
(1, 5000, '2025-06-30', '2024-01-15', 'Shopping rewards'),
(2, 8000, '2025-03-31', '2024-01-10', 'Fuel surcharge waiver');
```

### 8.4 Sample Redemption Options

```sql
INSERT INTO redemption_options (name, category, points_required, cash_value, description) VALUES
('Amazon Gift Card ₹500', 'GIFT_CARD', 5000, 500.00, 'Amazon.in gift card worth ₹500'),
('Flipkart Voucher ₹1000', 'GIFT_CARD', 8000, 1000.00, 'Flipkart shopping voucher'),
('Cashback to Account', 'CASHBACK', 4000, 400.00, 'Direct cash credit to bank account'),
('Movie Tickets (2)', 'DINING', 3000, 350.00, 'PVR movie tickets for 2 persons'),
('Flight Discount Voucher', 'TRAVEL', 10000, 1200.00, 'Domestic flight discount voucher');
```

---

## 9. Migration Strategy

### 9.1 Schema Migration Approach

| Aspect | Strategy |
|--------|----------|
| **Tool** | Flyway |
| **Versioning** | Sequential version numbers |
| **Naming** | V1__init.sql, V2__add_column.sql |
| **Rollback** | Manual rollback scripts |
| **Testing** | Migration tested in staging |

### 9.2 Migration File Structure

```
src/main/resources/db/migration/
├── V1__init_auth_schema.sql
├── V2__init_card_schema.sql
├── V3__init_reward_schema.sql
├── V4__init_recommendation_schema.sql
├── V5__add_user_lock_columns.sql
└── V6__add_composite_indexes.sql
```

---

## 10. Backup and Recovery

### 10.1 Backup Strategy

| Aspect | Strategy |
|--------|----------|
| **Full Backup** | Daily at 2:00 AM |
| **Incremental Backup** | Every 6 hours |
| **Retention** | 30 days |
| **Storage** | Cloud storage (S3-compatible) |
| **Encryption** | AES-256 encryption |

### 10.2 Recovery Procedures

| Scenario | Recovery Time | Procedure |
|----------|---------------|-----------|
| Data corruption | < 1 hour | Restore from latest backup |
| Accidental deletion | < 30 minutes | Point-in-time recovery |
| Database failure | < 5 minutes | Failover to replica |
| Complete loss | < 4 hours | Full restore from backup |

---

## 11. Performance Optimization

### 11.1 Connection Pool Configuration

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      max-lifetime: 1200000
      connection-timeout: 30000
```

### 11.2 Query Optimization Guidelines

| Guideline | Description |
|-----------|-------------|
| Use indexes | Always query indexed columns |
| Avoid SELECT * | Select only required columns |
| Use pagination | Limit result sets |
| Batch operations | Use batch inserts/updates |
| Cache frequently accessed data | Use Redis for caching |

---

## 12. Security Considerations

### 12.1 Data Protection

| Data Type | Protection Method |
|-----------|-------------------|
| Passwords | BCrypt hashing |
| Card numbers | AES encryption at rest |
| API keys | Environment variables |
| Connection strings | Encrypted configuration |

### 12.2 Access Control

| Aspect | Implementation |
|--------|----------------|
| Database users | Separate user per service |
| Permissions | Minimal required permissions |
| Network | Database not publicly accessible |
| Auditing | Enable general log for audit |

---

*Document maintained by RedeemWise Development Team*