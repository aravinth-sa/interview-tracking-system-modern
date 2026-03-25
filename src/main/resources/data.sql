-- =============================================================
-- Dev seed data for H2 (runs automatically on every startup)
-- Flyway is disabled in dev — this file takes its place.
--
-- Hibernate create-drop creates the tables first,
-- then Spring Boot runs this file to insert the seed rows.
--
-- Passwords:
--   ADMIN001 → admin123
--   TECH001  → tech123
--   HR001    → hr123
-- =============================================================

MERGE INTO users (user_id, password, user_type, login_status, enabled, created_at)
KEY (user_id)
VALUES
    ('ADMIN001',
     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
     'ADMIN', FALSE, TRUE, NOW()),

    ('TECH001',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'TECH_PANEL', FALSE, TRUE, NOW()),

    ('TECH002',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'TECH_PANEL', FALSE, TRUE, NOW()),

    ('HR001',
     '$2a$10$Nhu6j1u8KoKMkd4J5UFv9OJhAMqNqRJiS7Y1T5JJf/7VYc9F9LiJy',
     'HR_PANEL', FALSE, TRUE, NOW()),

    ('HR002',
     '$2a$10$Nhu6j1u8KoKMkd4J5UFv9OJhAMqNqRJiS7Y1T5JJf/7VYc9F9LiJy',
     'HR_PANEL', FALSE, TRUE, NOW());
