-- =============================================================
-- V2: Seed initial users
--
-- Passwords are BCrypt hashed (cost 10):
--   ADMIN001  → admin123
--   TECH001   → tech123
--   TECH002   → tech123
--   HR001     → hr123
--   HR002     → hr123
--
-- To regenerate hashes: https://bcrypt-generator.com
-- or in Java: new BCryptPasswordEncoder().encode("yourpassword")
-- =============================================================

INSERT INTO users (user_id, password, user_type, login_status, enabled, created_at)
VALUES
    ('ADMIN001',
     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',  -- admin123
     'ADMIN',
     FALSE, TRUE, NOW()),

    ('TECH001',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',   -- tech123
     'TECH_PANEL',
     FALSE, TRUE, NOW()),

    ('TECH002',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',   -- tech123
     'TECH_PANEL',
     FALSE, TRUE, NOW()),

    ('HR001',
     '$2a$10$Nhu6j1u8KoKMkd4J5UFv9OJhAMqNqRJiS7Y1T5JJf/7VYc9F9LiJy',  -- hr123
     'HR_PANEL',
     FALSE, TRUE, NOW()),

    ('HR002',
     '$2a$10$Nhu6j1u8KoKMkd4J5UFv9OJhAMqNqRJiS7Y1T5JJf/7VYc9F9LiJy',  -- hr123
     'HR_PANEL',
     FALSE, TRUE, NOW());
