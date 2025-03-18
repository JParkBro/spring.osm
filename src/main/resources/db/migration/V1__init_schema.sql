CREATE TABLE IF NOT EXISTS USERS (
     user_id VARCHAR(50) PRIMARY KEY,
     role VARCHAR(20) NOT NULL,
     password VARCHAR(100) NOT NULL,
     name VARCHAR(50) NOT NULL,
     email VARCHAR(100) NOT NULL,
     mobile_phone VARCHAR(20),
     postal_code VARCHAR(10),
     address VARCHAR(200),
     detail_address VARCHAR(200),
     terms_agreed BOOLEAN NOT NULL DEFAULT FALSE,
     privacy_agreed BOOLEAN NOT NULL DEFAULT FALSE,
     verification_key VARCHAR(100),
     created_user VARCHAR(50),
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_user VARCHAR(50),
     updated_at TIMESTAMP,
     use_yn CHAR(1) DEFAULT 'Y'
);