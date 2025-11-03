-- Smart Banking System Database Setup
-- Run this script in MySQL to create the database and user

-- Create database
CREATE DATABASE IF NOT EXISTS banking_system;
USE banking_system;

-- Create user (optional - you can use root)
-- CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'banking_password';
-- GRANT ALL PRIVILEGES ON banking_system.* TO 'banking_user'@'localhost';
-- FLUSH PRIVILEGES;

-- The application will create tables automatically
-- But you can run this to create them manually if needed:

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('CUSTOMER', 'BANK_MANAGER') NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    account_holder_name VARCHAR(100) NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT') NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    customer_id INT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'CLOSED', 'FROZEN') DEFAULT 'ACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_transaction_date TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_customer_id (customer_id),
    INDEX idx_account_type (account_type),
    INDEX idx_status (status)
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    description TEXT,
    reference_number VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_account_number (account_number),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_timestamp (timestamp),
    INDEX idx_amount (amount)
);

-- Audit log table
CREATE TABLE IF NOT EXISTS audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id VARCHAR(50),
    old_values JSON,
    new_values JSON,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_timestamp (timestamp)
);

-- Insert default users
INSERT IGNORE INTO users (username, password, full_name, email, role) VALUES 
('manager', 'manager123', 'Bank Manager', 'manager@bank.com', 'BANK_MANAGER'),
('customer', 'customer123', 'John Doe', 'john.doe@email.com', 'CUSTOMER');

-- Show created tables
SHOW TABLES;

-- Show default users
SELECT user_id, username, full_name, role FROM users;