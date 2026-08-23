# RedeemWise Database Seed Import Guide

## Overview

This guide explains how to import the seed data for RedeemWise's card-service and reward-service databases from the Excel workbook into MySQL.

**Source:** `RedeemWise_Credit_Card_Rewards_Model.xlsx`

| Metric | Count |
|--------|-------|
| Total Cards | 49 |
| Total Reward Options | 182 |
| Importable Rows | 231 (49 cards + 182 reward options) |
| Rows Requiring Manual Review | 0 (all imported with notes) |

---

## Data Mapping

### Cards Sheet → `redeemwise_card.cards`

| Excel Column | DB Column | Type | Notes |
|---|---|---|---|
| Card ID | (mapped to Long) | `BIGINT` | Excel `CRD_HDFC_001` → DB `1` |
| Card Name | `card_name` | `VARCHAR(150)` | Direct mapping |
| Bank Name | `bank_name` | `VARCHAR(150)` | Direct mapping |
| (not in Excel) | `network` | `ENUM` | **Inferred** — see below |
| Reward Type | `reward_type` | `ENUM` | Mapped — see below |
| Annual Fee | `annual_fee` | `DECIMAL(10,2)` | Direct mapping |
| Joining Fee | `joining_fee` | `DECIMAL(10,2)` | Direct mapping |
| Active Status | `active` | `BOOLEAN` | All cards are `TRUE` |
| (auto) | `created_at` | `DATETIME` | Set to `NOW()` |
| (auto) | `updated_at` | `DATETIME` | Set to `NOW()` |

**Columns ignored:** Bank Category, Card Category, Reward Program Name, Reward Currency, Point Expiry Rule, Notes

### RedemptionOptions Sheet → `redeemwise_reward.reward_options`

| Excel Column | DB Column | Type | Notes |
|---|---|---|---|
| Option ID | (not stored) | — | Excel business ID, not in entity |
| Card ID | `card_id` | `BIGINT` | **Mapped** to DB Long ID |
| Redemption Channel / Partner | `conversion_formula` | `VARCHAR(500)` | Free-text description |
| Standardized Redemption Type | `redemption_category` | `ENUM` | Mapped — see below |
| Effective Value Per Point (INR) | `value_per_point` | `DECIMAL(10,4)` | Direct mapping |
| Minimum Points Threshold | `minimum_redemption` | `DECIMAL(10,2)` | Direct mapping |
| Priority Rank | `priority_rank` | `INT` | Direct mapping |
| Is Recommended | `recommended_flag` | `BOOLEAN` | Direct mapping |
| Notes/Example | (parsed) | — | Extracted transfer partners |
| (auto) | `transfer_partner` | `VARCHAR(150)` | Extracted from description |
| (auto) | `transfer_ratio` | `VARCHAR(20)` | Extracted from description |
| (auto) | `active` | `BOOLEAN` | Set to `TRUE` |
| (auto) | `created_at` | `DATETIME` | Set to `NOW()` |
| (auto) | `updated_at` | `DATETIME` | Set to `NOW()` |

---

## Enum Mapping

### Network (Card Service) — INFERRED

The Excel does not contain network data. The following inference rules were applied:

| Condition | Network Value | Cards Affected |
|---|---|---|
| Bank is "American Express" | `AMEX` | 5 cards |
| Card name contains "Diners" | `MASTERCARD` | 1 card (HDFC Diners Club Black) |
| All other banks | `VISA` | 43 cards |

> **Action Required:** Update network values if actual network data becomes available.

### RewardType (Card Service)

| Excel Value | DB Enum Value |
|---|---|
| `Air Miles` | `AIR_MILES` |
| `Cashback` | `CASHBACK` |
| `Fuel Points` | `REWARD_POINTS` |
| `Reward Points` | `REWARD_POINTS` |

> **Note:** "Fuel Points" has no dedicated enum value and is mapped to `REWARD_POINTS`.

### RedemptionCategory (Reward Service)

| Excel Value | DB Enum Value | Count |
|---|---|---|
| `FLIGHT` | `FLIGHT` | Direct |
| `HOTEL` | `HOTEL` | Direct |
| `STATEMENT_CREDIT` | `STATEMENT_CREDIT` | Direct |
| `VOUCHER` | `VOUCHER` | Direct |
| `AIR_MILES_TRANSFER` | `AIR_MILES_TRANSFER` | Direct |
| `HOTEL_POINTS_TRANSFER` | `HOTEL_POINTS_TRANSFER` | Direct |
| `FUEL` | `FUEL` | Direct |
| `CASHBACK` | `STATEMENT_CREDIT` | **Mapped** |

> **Note:** `CASHBACK` is not in the `RedemptionCategory` enum and is mapped to `STATEMENT_CREDIT`.

---

## Card ID Mapping

The Excel uses string business IDs (e.g., `CRD_HDFC_001`), but the database uses auto-generated `BIGINT` IDs. The seed scripts use explicit IDs (1–49) with this mapping:

| Excel ID | DB ID | Excel ID | DB ID | Excel ID | DB ID |
|---|---|---|---|---|---|
| CRD_HDFC_001 | 1 | CRD_AXIS_004 | 17 | CRD_INDUS_003 | 36 |
| CRD_HDFC_002 | 2 | CRD_AXIS_005 | 18 | CRD_INDUS_004 | 37 |
| CRD_HDFC_003 | 3 | CRD_AXIS_006 | 19 | CRD_AU_001 | 38 |
| CRD_HDFC_004 | 4 | CRD_AXIS_007 | 20 | CRD_AU_002 | 39 |
| CRD_HDFC_005 | 5 | CRD_ICICI_001 | 21 | CRD_AU_003 | 40 |
| CRD_HDFC_006 | 6 | CRD_ICICI_002 | 22 | CRD_AU_004 | 41 |
| CRD_SBI_001 | 7 | CRD_ICICI_003 | 23 | CRD_SCB_001 | 42 |
| CRD_SBI_002 | 8 | CRD_ICICI_004 | 24 | CRD_SCB_002 | 43 |
| CRD_SBI_003 | 9 | CRD_ICICI_005 | 25 | CRD_SCB_003 | 44 |
| CRD_SBI_004 | 10 | CRD_AMEX_001 | 26 | CRD_SCB_004 | 45 |
| CRD_SBI_005 | 11 | CRD_AMEX_002 | 27 | CRD_YES_001 | 46 |
| CRD_SBI_006 | 12 | CRD_AMEX_003 | 28 | CRD_YES_002 | 47 |
| CRD_SBI_007 | 13 | CRD_AMEX_004 | 29 | CRD_YES_003 | 48 |
| CRD_AXIS_001 | 14 | CRD_AMEX_005 | 30 | CRD_YES_004 | 49 |
| CRD_AXIS_002 | 15 | CRD_HSBC_001 | 31 | | |
| CRD_AXIS_003 | 16 | CRD_HSBC_002 | 32 | | |
| | | CRD_HSBC_003 | 33 | | |
| | | CRD_INDUS_001 | 34 | | |
| | | CRD_INDUS_002 | 35 | | |

---

## Data Quality Report

### Issues Found & Resolved

| Issue | Resolution |
|---|---|
| `CASHBACK` not in `RedemptionCategory` enum | Mapped to `STATEMENT_CREDIT` |
| `Fuel Points` not in `RewardType` enum | Mapped to `REWARD_POINTS` |
| Network data missing from Excel | Inferred from bank/card name |
| Excel Card IDs are strings, DB uses Long | Deterministic 1–49 mapping |
| `transfer_partner` not in Excel | Extracted from redemption description text |
| `transfer_ratio` not in Excel | Parsed from description (e.g., "5:4 ratio") |
| `conversion_formula` not in Excel | Used "Redemption Channel / Partner" column |

### Data Quality Metrics

- **Duplicate Card IDs:** 0
- **Duplicate Option IDs:** 0
- **Cards without redemption options:** 0 (all 49 cards have ≥1 option)
- **Orphaned reward options:** 0 (all reference valid card IDs)
- **Null required fields:** 0
- **Invalid enum values:** 0 (all mapped)

### Cards Flagged for Review

| Card | Annual Fee | Notes |
|---|---|---|
| HSBC Premier Credit Card | 0 | Lifetime free (joining fee also 0) |
| IndusInd Bank Pinnacle | 0 | Lifetime free after high joining fee |
| IndusInd Bank Legend | 0 | Lifetime free |
| IndusInd Bank Tiger | 0 | Lifetime free |
| IndusInd Platinum Aura Edge | 0 | Lifetime free |

---

## Import Strategy

### Approach: SQL Seed Scripts

The safest production approach uses direct SQL `INSERT` statements:

1. **Idempotent:** Scripts use `TRUNCATE TABLE` before inserting (safe for re-runs)
2. **Explicit IDs:** Cards use deterministic Long IDs (1–49) for referential integrity
3. **Foreign Key Safe:** `SET FOREIGN_KEY_CHECKS = 0` before truncate, re-enabled after
4. **No Excel at Runtime:** Pure SQL scripts, no Java/Python code execution needed

### Execution Order

```
1. cards.sql          (redeemwise_card database)
2. reward_options.sql (redeemwise_reward database)
3. verification.sql   (both databases — run manually to validate)
```

---

## Deployment Guide

### Local Development Setup

```bash
# 1. Ensure MySQL is running
mysql -u root -p

# 2. Create databases (if not auto-created by JPA)
CREATE DATABASE IF NOT EXISTS redeemwise_card;
CREATE DATABASE IF NOT EXISTS redeemwise_reward;

# 3. Set environment variables
export DB_USERNAME=root
export DB_PASSWORD=yourpassword

# 4. Import card data
mysql -u root -p redeemwise_card < database/seed/cards.sql

# 5. Import reward data
mysql -u root -p redeemwise_reward < database/seed/reward_options.sql

# 6. Verify
mysql -u root -p redeemwise_card < database/seed/verification.sql
```

### Fresh Database Setup

```bash
# Drop and recreate databases
mysql -u root -p -e "
  DROP DATABASE IF EXISTS redeemwise_card;
  DROP DATABASE IF EXISTS redeemwise_reward;
  CREATE DATABASE redeemwise_card;
  CREATE DATABASE redeemwise_reward;
"

# Import seed data
mysql -u root -p redeemwise_card < database/seed/cards.sql
mysql -u root -p redeemwise_reward < database/seed/reward_options.sql

# Verify
mysql -u root -p redeemwise_card < database/seed/verification.sql
```

### Docker Deployment

```yaml
# docker-compose.yml addition
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - ./database/seed:/docker-entrypoint-initdb.d
    environment:
      MYSQL_DATABASE: redeemwise_card
```

> **Note:** Docker `initdb.d` runs scripts alphabetically. Use a numbered prefix:
> `01-create-databases.sql`, `02-cards.sql`, `03-reward_options.sql`

### Production Deployment

```bash
# 1. Backup existing data
mysqldump -u root -p redeemwise_card > backup_card_$(date +%Y%m%d).sql
mysqldump -u root -p redeemwise_reward > backup_reward_$(date +%Y%m%d).sql

# 2. Import with explicit credentials
mysql -h prod-host -u app_user -p redeemwise_card < database/seed/cards.sql
mysql -h prod-host -u app_user -p redeemwise_reward < database/seed/reward_options.sql

# 3. Verify
mysql -h prod-host -u app_user -p redeemwise_card -e "SELECT COUNT(*) FROM cards;"
mysql -h prod-host -u app_user -p redeemwise_reward -e "SELECT COUNT(*) FROM reward_options;"
```

---

## Verification Queries

After import, run `verification.sql` or manually check:

```sql
-- Card counts
SELECT COUNT(*) AS total_cards FROM cards;               -- Expected: 49
SELECT COUNT(*) AS active_cards FROM cards WHERE active = TRUE;  -- Expected: 49

-- Reward option counts
SELECT COUNT(*) AS total_options FROM reward_options;     -- Expected: 182
SELECT COUNT(*) AS recommended FROM reward_options WHERE recommended_flag = TRUE;

-- Referential integrity
SELECT COUNT(*) FROM reward_options r
LEFT JOIN cards c ON r.card_id = c.id
WHERE c.id IS NULL;                                      -- Expected: 0

-- Category distribution
SELECT redemption_category, COUNT(*) FROM reward_options
GROUP BY redemption_category ORDER BY COUNT(*) DESC;
```

---

## File Structure

```
database/
├── generate_seed.py          # Python script to regenerate SQL from Excel
├── seed/
│   ├── cards.sql             # 49 card INSERT statements
│   ├── reward_options.sql    # 182 reward option INSERT statements
│   └── verification.sql      # Post-import validation queries
└── docs/
    └── import-guide.md       # This document
```
