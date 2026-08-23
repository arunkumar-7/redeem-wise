-- ============================================================
-- RedeemWise Card Service - Seed Data
-- Source: RedeemWise_Credit_Card_Rewards_Model.xlsx > Cards sheet
-- Total Cards: 49
-- Generated: 2026-08-23
-- ============================================================

USE redeemwise_card;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE cards;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- Card ID Mapping Reference (Excel Card ID -> DB Long ID)
-- ============================================================
--
-- CRD_HDFC_001 -> 1
-- CRD_HDFC_002 -> 2
-- CRD_HDFC_003 -> 3
-- CRD_HDFC_004 -> 4
-- CRD_HDFC_005 -> 5
-- CRD_HDFC_006 -> 6
-- CRD_SBI_001 -> 7
-- CRD_SBI_002 -> 8
-- CRD_SBI_003 -> 9
-- CRD_SBI_004 -> 10
-- CRD_SBI_005 -> 11
-- CRD_SBI_006 -> 12
-- CRD_SBI_007 -> 13
-- CRD_AXIS_001 -> 14
-- CRD_AXIS_002 -> 15
-- CRD_AXIS_003 -> 16
-- CRD_AXIS_004 -> 17
-- CRD_AXIS_005 -> 18
-- CRD_AXIS_006 -> 19
-- CRD_AXIS_007 -> 20
-- CRD_ICICI_001 -> 21
-- CRD_ICICI_002 -> 22
-- CRD_ICICI_003 -> 23
-- CRD_ICICI_004 -> 24
-- CRD_ICICI_005 -> 25
-- CRD_AMEX_001 -> 26
-- CRD_AMEX_002 -> 27
-- CRD_AMEX_003 -> 28
-- CRD_AMEX_004 -> 29
-- CRD_AMEX_005 -> 30
-- CRD_HSBC_001 -> 31
-- CRD_HSBC_002 -> 32
-- CRD_HSBC_003 -> 33
-- CRD_INDUS_001 -> 34
-- CRD_INDUS_002 -> 35
-- CRD_INDUS_003 -> 36
-- CRD_INDUS_004 -> 37
-- CRD_AU_001 -> 38
-- CRD_AU_002 -> 39
-- CRD_AU_003 -> 40
-- CRD_AU_004 -> 41
-- CRD_SCB_001 -> 42
-- CRD_SCB_002 -> 43
-- CRD_SCB_003 -> 44
-- CRD_SCB_004 -> 45
-- CRD_YES_001 -> 46
-- CRD_YES_002 -> 47
-- CRD_YES_003 -> 48
-- CRD_YES_004 -> 49
--

INSERT INTO cards (id, card_name, bank_name, network, reward_type, annual_fee, joining_fee, active, created_at, updated_at)
VALUES
  (1, 'HDFC Infinia Metal Edition', 'HDFC Bank', 'VISA', 'REWARD_POINTS', 12500, 12500, TRUE, NOW(), NOW()),
  (2, 'HDFC Diners Club Black Metal', 'HDFC Bank', 'MASTERCARD', 'REWARD_POINTS', 10000, 10000, TRUE, NOW(), NOW()),
  (3, 'HDFC Regalia Gold', 'HDFC Bank', 'VISA', 'REWARD_POINTS', 2500, 2500, TRUE, NOW(), NOW()),
  (4, 'HDFC Millennia', 'HDFC Bank', 'VISA', 'REWARD_POINTS', 1000, 1000, TRUE, NOW(), NOW()),
  (5, 'HDFC MoneyBack+', 'HDFC Bank', 'VISA', 'REWARD_POINTS', 500, 500, TRUE, NOW(), NOW()),
  (6, 'Tata Neu Infinity HDFC', 'HDFC Bank', 'VISA', 'REWARD_POINTS', 1499, 1499, TRUE, NOW(), NOW()),
  (7, 'SBI Card AURUM', 'SBI Card', 'VISA', 'REWARD_POINTS', 9999, 9999, TRUE, NOW(), NOW()),
  (8, 'SBI Card ELITE', 'SBI Card', 'VISA', 'REWARD_POINTS', 4999, 4999, TRUE, NOW(), NOW()),
  (9, 'SBI Card PRIME', 'SBI Card', 'VISA', 'REWARD_POINTS', 2999, 2999, TRUE, NOW(), NOW()),
  (10, 'SBI SimplyCLICK', 'SBI Card', 'VISA', 'REWARD_POINTS', 499, 499, TRUE, NOW(), NOW()),
  (11, 'SBI BPCL Octane', 'SBI Card', 'VISA', 'REWARD_POINTS', 1499, 1499, TRUE, NOW(), NOW()),
  (12, 'SBI BPCL', 'SBI Card', 'VISA', 'REWARD_POINTS', 499, 499, TRUE, NOW(), NOW()),
  (13, 'SBI Cashback Card', 'SBI Card', 'VISA', 'CASHBACK', 999, 999, TRUE, NOW(), NOW()),
  (14, 'Axis Reserve', 'Axis Bank', 'VISA', 'REWARD_POINTS', 50000, 50000, TRUE, NOW(), NOW()),
  (15, 'Axis Magnus', 'Axis Bank', 'VISA', 'REWARD_POINTS', 12500, 12500, TRUE, NOW(), NOW()),
  (16, 'Axis Atlas', 'Axis Bank', 'VISA', 'AIR_MILES', 5000, 5000, TRUE, NOW(), NOW()),
  (17, 'Axis Select', 'Axis Bank', 'VISA', 'REWARD_POINTS', 3000, 3000, TRUE, NOW(), NOW()),
  (18, 'Axis Horizon', 'Axis Bank', 'VISA', 'AIR_MILES', 3000, 3000, TRUE, NOW(), NOW()),
  (19, 'Axis Neo', 'Axis Bank', 'VISA', 'REWARD_POINTS', 250, 250, TRUE, NOW(), NOW()),
  (20, 'Axis Ace', 'Axis Bank', 'VISA', 'CASHBACK', 499, 499, TRUE, NOW(), NOW()),
  (21, 'ICICI Emeralde Private Metal', 'ICICI Bank', 'VISA', 'REWARD_POINTS', 12500, 12500, TRUE, NOW(), NOW()),
  (22, 'ICICI Emeralde', 'ICICI Bank', 'VISA', 'REWARD_POINTS', 12000, 12000, TRUE, NOW(), NOW()),
  (23, 'ICICI Sapphiro', 'ICICI Bank', 'VISA', 'REWARD_POINTS', 3500, 6500, TRUE, NOW(), NOW()),
  (24, 'ICICI Rubyx', 'ICICI Bank', 'VISA', 'REWARD_POINTS', 2000, 3000, TRUE, NOW(), NOW()),
  (25, 'ICICI Coral', 'ICICI Bank', 'VISA', 'REWARD_POINTS', 500, 500, TRUE, NOW(), NOW()),
  (26, 'American Express Platinum Card', 'American Express', 'AMEX', 'REWARD_POINTS', 66000, 66000, TRUE, NOW(), NOW()),
  (27, 'American Express Platinum Travel', 'American Express', 'AMEX', 'REWARD_POINTS', 5000, 3500, TRUE, NOW(), NOW()),
  (28, 'American Express Gold Card', 'American Express', 'AMEX', 'REWARD_POINTS', 4500, 1000, TRUE, NOW(), NOW()),
  (29, 'American Express Membership Rewards Credit Card (MRCC)', 'American Express', 'AMEX', 'REWARD_POINTS', 4500, 1000, TRUE, NOW(), NOW()),
  (30, 'American Express SmartEarn', 'American Express', 'AMEX', 'REWARD_POINTS', 495, 495, TRUE, NOW(), NOW()),
  (31, 'HSBC Premier Credit Card', 'HSBC India', 'VISA', 'AIR_MILES', 0, 0, TRUE, NOW(), NOW()),
  (32, 'HSBC Live+ Credit Card', 'HSBC India', 'VISA', 'CASHBACK', 999, 999, TRUE, NOW(), NOW()),
  (33, 'HSBC Cashback Credit Card', 'HSBC India', 'VISA', 'CASHBACK', 999, 999, TRUE, NOW(), NOW()),
  (34, 'IndusInd Bank Pinnacle', 'IndusInd Bank', 'VISA', 'REWARD_POINTS', 0, 50000, TRUE, NOW(), NOW()),
  (35, 'IndusInd Bank Legend', 'IndusInd Bank', 'VISA', 'REWARD_POINTS', 0, 10000, TRUE, NOW(), NOW()),
  (36, 'IndusInd Bank Tiger Credit Card', 'IndusInd Bank', 'VISA', 'REWARD_POINTS', 0, 0, TRUE, NOW(), NOW()),
  (37, 'IndusInd Platinum Aura Edge', 'IndusInd Bank', 'VISA', 'REWARD_POINTS', 0, 500, TRUE, NOW(), NOW()),
  (38, 'AU Zenith+ Credit Card', 'AU Small Finance Bank', 'VISA', 'REWARD_POINTS', 4999, 4999, TRUE, NOW(), NOW()),
  (39, 'AU Zenith Credit Card', 'AU Small Finance Bank', 'VISA', 'REWARD_POINTS', 7999, 7999, TRUE, NOW(), NOW()),
  (40, 'AU Vetta Credit Card', 'AU Small Finance Bank', 'VISA', 'REWARD_POINTS', 2999, 2999, TRUE, NOW(), NOW()),
  (41, 'AU Altura Plus', 'AU Small Finance Bank', 'VISA', 'REWARD_POINTS', 499, 499, TRUE, NOW(), NOW()),
  (42, 'Standard Chartered Ultimate', 'Standard Chartered', 'VISA', 'REWARD_POINTS', 5000, 5000, TRUE, NOW(), NOW()),
  (43, 'Standard Chartered EaseMyTrip', 'Standard Chartered', 'VISA', 'REWARD_POINTS', 350, 350, TRUE, NOW(), NOW()),
  (44, 'Standard Chartered Rewards', 'Standard Chartered', 'VISA', 'REWARD_POINTS', 1000, 1000, TRUE, NOW(), NOW()),
  (45, 'Standard Chartered Smart', 'Standard Chartered', 'VISA', 'CASHBACK', 499, 499, TRUE, NOW(), NOW()),
  (46, 'YES Marquee Credit Card', 'Yes Bank', 'VISA', 'REWARD_POINTS', 4999, 9999, TRUE, NOW(), NOW()),
  (47, 'YES Reserv Credit Card', 'Yes Bank', 'VISA', 'REWARD_POINTS', 1999, 1999, TRUE, NOW(), NOW()),
  (48, 'YES First Preferred', 'Yes Bank', 'VISA', 'REWARD_POINTS', 999, 999, TRUE, NOW(), NOW()),
  (49, 'YES POP-Club Credit Card', 'Yes Bank', 'VISA', 'REWARD_POINTS', 399, 399, TRUE, NOW(), NOW());

-- ============================================================
-- NOTE: Network values are inferred (not in Excel source data).
-- Amex cards -> AMEX, Diners cards -> MASTERCARD, All others -> VISA
-- Review and update if actual network data becomes available.
-- ============================================================