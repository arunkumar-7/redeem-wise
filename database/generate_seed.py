import pandas as pd
import re
import os

excel_path = r"C:\Users\ARUN\Downloads\RedeemWise_Credit_Card_Rewards_Model.xlsx"

os.makedirs("database/seed", exist_ok=True)

# ==================== PARSE CARDS ====================
df_cards = pd.read_excel(excel_path, sheet_name='Cards', header=2)
df_cards = df_cards.dropna(subset=['Card ID']).reset_index(drop=True)

# Build card ID mapping: Excel Card ID -> Long database ID (1-49)
card_id_map = {}
for idx, row in df_cards.iterrows():
    card_id_map[row['Card ID']] = idx + 1

# Network inference
def infer_network(card_name, bank_name):
    """Infer card network from card name or bank name."""
    if 'Amex' in bank_name or 'American Express' in bank_name:
        return 'AMEX'
    if 'Diners' in card_name:
        return 'MASTERCARD'
    return 'VISA'

# RewardType mapping
reward_type_map = {
    'Air Miles': 'AIR_MILES',
    'Cashback': 'CASHBACK',
    'Fuel Points': 'REWARD_POINTS',
    'Reward Points': 'REWARD_POINTS',
}

def escape_sql(s):
    """Escape single quotes for SQL."""
    if s is None:
        return 'NULL'
    return str(s).replace("'", "\\'")

# Generate cards.sql
cards_sql_lines = []
cards_sql_lines.append("-- ============================================================")
cards_sql_lines.append("-- RedeemWise Card Service - Seed Data")
cards_sql_lines.append("-- Source: RedeemWise_Credit_Card_Rewards_Model.xlsx > Cards sheet")
cards_sql_lines.append("-- Total Cards: 49")
cards_sql_lines.append("-- Generated: 2026-08-23")
cards_sql_lines.append("-- ============================================================")
cards_sql_lines.append("")
cards_sql_lines.append("USE redeemwise_card;")
cards_sql_lines.append("")
cards_sql_lines.append("SET FOREIGN_KEY_CHECKS = 0;")
cards_sql_lines.append("TRUNCATE TABLE cards;")
cards_sql_lines.append("SET FOREIGN_KEY_CHECKS = 1;")
cards_sql_lines.append("")
cards_sql_lines.append("-- ============================================================")
cards_sql_lines.append("-- Card ID Mapping Reference (Excel Card ID -> DB Long ID)")
cards_sql_lines.append("-- ============================================================")
cards_sql_lines.append("--")
for excel_id, db_id in card_id_map.items():
    cards_sql_lines.append(f"-- {excel_id} -> {db_id}")
cards_sql_lines.append("--")
cards_sql_lines.append("")
cards_sql_lines.append("INSERT INTO cards (id, card_name, bank_name, network, reward_type, annual_fee, joining_fee, active, created_at, updated_at)")
cards_sql_lines.append("VALUES")

values = []
for idx, row in df_cards.iterrows():
    db_id = idx + 1
    card_name = escape_sql(row['Card Name'])
    bank_name = escape_sql(row['Bank Name'])
    network = infer_network(row['Card Name'], row['Bank Name'])
    reward_type = reward_type_map.get(row['Reward Type'], 'REWARD_POINTS')
    annual_fee = row['Annual Fee']
    joining_fee = row['Joining Fee']
    active = 'TRUE'
    ts = 'NOW()'

    line = f"  ({db_id}, '{card_name}', '{bank_name}', '{network}', '{reward_type}', {annual_fee}, {joining_fee}, {active}, {ts}, {ts})"
    values.append(line)

cards_sql_lines.append(",\n".join(values) + ";")
cards_sql_lines.append("")
cards_sql_lines.append("-- ============================================================")
cards_sql_lines.append("-- NOTE: Network values are inferred (not in Excel source data).")
cards_sql_lines.append("-- Amex cards -> AMEX, Diners cards -> MASTERCARD, All others -> VISA")
cards_sql_lines.append("-- Review and update if actual network data becomes available.")
cards_sql_lines.append("-- ============================================================")

cards_sql = "\n".join(cards_sql_lines)

with open("database/seed/cards.sql", "w", encoding="utf-8") as f:
    f.write(cards_sql)

print(f"Generated cards.sql with {len(df_cards)} cards")

# ==================== PARSE REDEMPTION OPTIONS ====================
df_ro = pd.read_excel(excel_path, sheet_name='RedemptionOptions', header=2)
df_ro = df_ro.dropna(subset=[df_ro.columns[0]])
df_ro = df_ro[~df_ro.iloc[:, 0].astype(str).str.contains('Option ID|RedeemWise', case=False, na=False)]
df_ro = df_ro.reset_index(drop=True)

df_ro.columns = ['Option_ID', 'Card_ID_Excel', 'Card_Name', 'Bank_Name', 'Redemption_Desc',
                  'Category', 'Value_Per_Point', 'Min_Points', 'Priority', 'Recommended', 'Notes']

# RedemptionCategory mapping
redemption_cat_map = {
    'CASHBACK': 'STATEMENT_CREDIT',
    'FLIGHT': 'FLIGHT',
    'HOTEL': 'HOTEL',
    'STATEMENT_CREDIT': 'STATEMENT_CREDIT',
    'VOUCHER': 'VOUCHER',
    'AIR_MILES_TRANSFER': 'AIR_MILES_TRANSFER',
    'HOTEL_POINTS_TRANSFER': 'HOTEL_POINTS_TRANSFER',
    'FUEL': 'FUEL',
}

# Generate reward_options.sql
ro_sql_lines = []
ro_sql_lines.append("-- ============================================================")
ro_sql_lines.append("-- RedeemWise Reward Service - Seed Data")
ro_sql_lines.append("-- Source: RedeemWise_Credit_Card_Rewards_Model.xlsx > RedemptionOptions sheet")
ro_sql_lines.append(f"-- Total Reward Options: {len(df_ro)}")
ro_sql_lines.append("-- Generated: 2026-08-23")
ro_sql_lines.append("-- ============================================================")
ro_sql_lines.append("")
ro_sql_lines.append("USE redeemwise_reward;")
ro_sql_lines.append("")
ro_sql_lines.append("SET FOREIGN_KEY_CHECKS = 0;")
ro_sql_lines.append("TRUNCATE TABLE reward_options;")
ro_sql_lines.append("SET FOREIGN_KEY_CHECKS = 1;")
ro_sql_lines.append("")
ro_sql_lines.append("INSERT INTO reward_options (id, card_id, redemption_category, conversion_formula, value_per_point, minimum_redemption, transfer_partner, transfer_ratio, priority_rank, recommended_flag, active, created_at, updated_at)")
ro_sql_lines.append("VALUES")

values = []
errors = []
for idx, row in df_ro.iterrows():
    db_id = idx + 1

    excel_card_id = row['Card_ID_Excel']
    if excel_card_id not in card_id_map:
        errors.append(f"Row {idx}: Card ID {excel_card_id} not found in cards table")
        continue
    db_card_id = card_id_map[excel_card_id]

    excel_category = row['Category']
    db_category = redemption_cat_map.get(excel_category, 'OTHER')

    conversion_formula = escape_sql(str(row['Redemption_Desc'])[:500] if pd.notna(row['Redemption_Desc']) else '')

    value_per_point = row['Value_Per_Point']
    min_redemption = row['Min_Points']
    priority_rank = int(row['Priority'])
    recommended = 'TRUE' if row['Recommended'] else 'FALSE'

    transfer_partner = ''
    transfer_ratio = 'NULL'

    desc = str(row['Redemption_Desc']) if pd.notna(row['Redemption_Desc']) else ''
    if 'transfer' in desc.lower():
        ratio_match = re.search(r'(\d+:\d+)', desc)
        if ratio_match:
            transfer_ratio = f"'{ratio_match.group(1)}'"
        if 'Marriott' in desc:
            transfer_partner = 'Marriott Bonvoy'
        elif 'Singapore' in desc or 'KrisFlyer' in desc:
            transfer_partner = 'Singapore Airlines KrisFlyer'
        elif 'British Airways' in desc:
            transfer_partner = 'British Airways'
        elif 'Air India' in desc:
            transfer_partner = 'Air India'
        elif 'Virgin' in desc:
            transfer_partner = 'Virgin Atlantic'
        elif 'InterMiles' in desc:
            transfer_partner = 'InterMiles'
        elif 'Vistara' in desc:
            transfer_partner = 'Vistara'
        elif 'Accor' in desc:
            transfer_partner = 'Accor'

    ts = 'NOW()'
    active = 'TRUE'

    line = (
        f"  ({db_id}, {db_card_id}, '{db_category}', '{conversion_formula}', "
        f"{value_per_point}, {min_redemption}, '{transfer_partner}', {transfer_ratio}, "
        f"{priority_rank}, {recommended}, {active}, {ts}, {ts})"
    )
    values.append(line)

ro_sql_lines.append(",\n".join(values) + ";")

if errors:
    ro_sql_lines.append("")
    ro_sql_lines.append("-- ============================================================")
    ro_sql_lines.append("-- ERRORS ENCOUNTERED:")
    ro_sql_lines.append("-- ============================================================")
    for e in errors:
        ro_sql_lines.append(f"-- {e}")

ro_sql_lines.append("")
ro_sql_lines.append("-- ============================================================")
ro_sql_lines.append("-- NOTE: 'CASHBACK' category in Excel mapped to 'STATEMENT_CREDIT'")
ro_sql_lines.append("-- (CASHBACK not in RedemptionCategory enum). Review if needed.")
ro_sql_lines.append("-- ============================================================")

ro_sql = "\n".join(ro_sql_lines)

with open("database/seed/reward_options.sql", "w", encoding="utf-8") as f:
    f.write(ro_sql)

print(f"Generated reward_options.sql with {len(values)} reward options")
if errors:
    print(f"Errors: {errors}")

# ==================== GENERATE VERIFICATION ====================
verify_lines = []
verify_lines.append("-- ============================================================")
verify_lines.append("-- RedeemWise - Data Verification Queries")
verify_lines.append("-- Run after importing seed data")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 1: Record Counts")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("SELECT '--- Card Service (redeemwise_card) ---' AS section;")
verify_lines.append("SELECT COUNT(*) AS total_cards FROM cards;")
verify_lines.append("SELECT COUNT(*) AS active_cards FROM cards WHERE active = TRUE;")
verify_lines.append("SELECT COUNT(*) AS inactive_cards FROM cards WHERE active = FALSE;")
verify_lines.append("")
verify_lines.append("SELECT '--- Reward Service (redeemwise_reward) ---' AS section;")
verify_lines.append("SELECT COUNT(*) AS total_reward_options FROM reward_options;")
verify_lines.append("SELECT COUNT(*) AS active_reward_options FROM reward_options WHERE active = TRUE;")
verify_lines.append("SELECT COUNT(*) AS recommended_options FROM reward_options WHERE recommended_flag = TRUE;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 2: Distribution by Bank")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("SELECT bank_name, COUNT(*) AS card_count")
verify_lines.append("FROM cards")
verify_lines.append("GROUP BY bank_name")
verify_lines.append("ORDER BY card_count DESC;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 3: Distribution by Network")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("SELECT network, COUNT(*) AS card_count")
verify_lines.append("FROM cards")
verify_lines.append("GROUP BY network")
verify_lines.append("ORDER BY card_count DESC;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 4: Distribution by Reward Type")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("SELECT reward_type, COUNT(*) AS card_count")
verify_lines.append("FROM cards")
verify_lines.append("GROUP BY reward_type")
verify_lines.append("ORDER BY card_count DESC;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 5: Redemption Options by Category")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("SELECT redemption_category, COUNT(*) AS option_count")
verify_lines.append("FROM reward_options")
verify_lines.append("GROUP BY redemption_category")
verify_lines.append("ORDER BY option_count DESC;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 6: Options per Card (Top 10)")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("SELECT c.card_name, c.bank_name, COUNT(r.id) AS reward_options_count")
verify_lines.append("FROM cards c")
verify_lines.append("LEFT JOIN reward_options r ON c.id = r.card_id")
verify_lines.append("GROUP BY c.id, c.card_name, c.bank_name")
verify_lines.append("ORDER BY reward_options_count DESC")
verify_lines.append("LIMIT 10;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 7: Referential Integrity Check")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("-- Reward options referencing non-existent cards (should return 0)")
verify_lines.append("SELECT r.id, r.card_id")
verify_lines.append("FROM reward_options r")
verify_lines.append("LEFT JOIN cards c ON r.card_id = c.id")
verify_lines.append("WHERE c.id IS NULL;")
verify_lines.append("")
verify_lines.append("-- Cards with no reward options (may be valid for cashback cards)")
verify_lines.append("SELECT c.id, c.card_name, c.bank_name")
verify_lines.append("FROM cards c")
verify_lines.append("LEFT JOIN reward_options r ON c.id = r.card_id")
verify_lines.append("WHERE r.id IS NULL;")
verify_lines.append("")
verify_lines.append("-- ============================================================")
verify_lines.append("-- SECTION 8: Data Quality Checks")
verify_lines.append("-- ============================================================")
verify_lines.append("")
verify_lines.append("-- Cards with annual_fee = 0")
verify_lines.append("SELECT card_name, bank_name, annual_fee FROM cards WHERE annual_fee = 0;")
verify_lines.append("")
verify_lines.append("-- Reward options with value_per_point > 1 (flag for review)")
verify_lines.append("SELECT id, card_id, redemption_category, value_per_point")
verify_lines.append("FROM reward_options")
verify_lines.append("WHERE value_per_point > 1;")
verify_lines.append("")
verify_lines.append("-- All recommended options should have priority <= 4")
verify_lines.append("SELECT id, card_id, redemption_category, priority_rank, recommended_flag")
verify_lines.append("FROM reward_options")
verify_lines.append("WHERE recommended_flag = TRUE AND priority_rank > 4;")
verify_sql = "\n".join(verify_lines)

with open("database/seed/verification.sql", "w", encoding="utf-8") as f:
    f.write(verify_sql)

print(f"Generated verification.sql")
print("All SQL files generated successfully!")
