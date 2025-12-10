-- Database: campusmatrix
CREATE DATABASE IF NOT EXISTS campusmatrix;
USE campusmatrix;

-- Users Table (for registration/login)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    year_of_study VARCHAR(20),
    student_id VARCHAR(50) UNIQUE,
    phone_number VARCHAR(20),
    profile_picture_url VARCHAR(500),
    role ENUM('STUDENT', 'FACULTY', 'ADMIN') DEFAULT 'STUDENT',
    is_active BOOLEAN DEFAULT TRUE,
    is_email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_student_id (student_id)
);

-- User Sessions (for JWT tokens if you want to store them)
CREATE TABLE user_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token(100)),
    INDEX idx_user_id (user_id)
);

-- Lost Items Table (from your frontend)
CREATE TABLE lost_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    date_lost DATE NOT NULL,
    time_lost TIME,
    contact_email VARCHAR(100),
    contact_phone VARCHAR(20),
    reward_amount DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('ACTIVE', 'RESOLVED', 'ARCHIVED') DEFAULT 'ACTIVE',
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_location (location)
);

-- Found Items Table (from your frontend)
CREATE TABLE found_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    date_found DATE NOT NULL,
    time_found TIME,
    storage_location VARCHAR(50),
    storage_details TEXT,
    contact_email VARCHAR(100),
    contact_phone VARCHAR(20),
    status ENUM('UNCLAIMED', 'CLAIMED', 'RETURNED', 'ARCHIVED') DEFAULT 'UNCLAIMED',
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_category (category)
);

-- Password Reset Tokens
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(100) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_user_id (user_id)
);