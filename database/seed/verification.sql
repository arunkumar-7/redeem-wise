-- ============================================================
-- RedeemWise - Data Verification Queries
-- Run after importing seed data
-- ============================================================

-- ============================================================
-- SECTION 1: Record Counts
-- ============================================================

SELECT '--- Card Service (redeemwise_card) ---' AS section;
SELECT COUNT(*) AS total_cards FROM cards;
SELECT COUNT(*) AS active_cards FROM cards WHERE active = TRUE;
SELECT COUNT(*) AS inactive_cards FROM cards WHERE active = FALSE;

SELECT '--- Reward Service (redeemwise_reward) ---' AS section;
SELECT COUNT(*) AS total_reward_options FROM reward_options;
SELECT COUNT(*) AS active_reward_options FROM reward_options WHERE active = TRUE;
SELECT COUNT(*) AS recommended_options FROM reward_options WHERE recommended_flag = TRUE;

-- ============================================================
-- SECTION 2: Distribution by Bank
-- ============================================================

SELECT bank_name, COUNT(*) AS card_count
FROM cards
GROUP BY bank_name
ORDER BY card_count DESC;

-- ============================================================
-- SECTION 3: Distribution by Network
-- ============================================================

SELECT network, COUNT(*) AS card_count
FROM cards
GROUP BY network
ORDER BY card_count DESC;

-- ============================================================
-- SECTION 4: Distribution by Reward Type
-- ============================================================

SELECT reward_type, COUNT(*) AS card_count
FROM cards
GROUP BY reward_type
ORDER BY card_count DESC;

-- ============================================================
-- SECTION 5: Redemption Options by Category
-- ============================================================

SELECT redemption_category, COUNT(*) AS option_count
FROM reward_options
GROUP BY redemption_category
ORDER BY option_count DESC;

-- ============================================================
-- SECTION 6: Options per Card (Top 10)
-- ============================================================

SELECT c.card_name, c.bank_name, COUNT(r.id) AS reward_options_count
FROM cards c
LEFT JOIN reward_options r ON c.id = r.card_id
GROUP BY c.id, c.card_name, c.bank_name
ORDER BY reward_options_count DESC
LIMIT 10;

-- ============================================================
-- SECTION 7: Referential Integrity Check
-- ============================================================

-- Reward options referencing non-existent cards (should return 0)
SELECT r.id, r.card_id
FROM reward_options r
LEFT JOIN cards c ON r.card_id = c.id
WHERE c.id IS NULL;

-- Cards with no reward options (may be valid for cashback cards)
SELECT c.id, c.card_name, c.bank_name
FROM cards c
LEFT JOIN reward_options r ON c.id = r.card_id
WHERE r.id IS NULL;

-- ============================================================
-- SECTION 8: Data Quality Checks
-- ============================================================

-- Cards with annual_fee = 0
SELECT card_name, bank_name, annual_fee FROM cards WHERE annual_fee = 0;

-- Reward options with value_per_point > 1 (flag for review)
SELECT id, card_id, redemption_category, value_per_point
FROM reward_options
WHERE value_per_point > 1;

-- All recommended options should have priority <= 4
SELECT id, card_id, redemption_category, priority_rank, recommended_flag
FROM reward_options
WHERE recommended_flag = TRUE AND priority_rank > 4;