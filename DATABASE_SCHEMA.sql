-- =====================================================
-- Smart Banking System - Complete Database Schema
-- =====================================================
-- This file contains the complete database schema with
-- detailed comments, constraints, and relationships
-- =====================================================

-- Create database
CREATE DATABASE IF NOT EXISTS banking_system 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE banking_system;

-- =====================================================
-- 1. USERS TABLE
-- =====================================================
-- Purpose: Store all system users (customers and managers)
-- Business Rules:
--   - Username and email must be unique
--   - Role determines system permissions
--   - Only active users can perform operations
-- =====================================================

CREATE TABLE users (
    -- Primary identifier
    user_id INT AUTO_INCREMENT PRIMARY KEY 
        COMMENT 'Unique identifier for each user',
    
    -- Authentication fields
    username VARCHAR(50) UNIQUE NOT NULL 
        COMMENT 'Login username - must be unique',
    password VARCHAR(255) NOT NULL 
        COMMENT 'Encrypted password for authentication',
    
    -- Personal information
    full_name VARCHAR(100) NOT NULL 
        COMMENT 'User full name for display',
    email VARCHAR(100) UNIQUE NOT NULL 
        COMMENT 'Email address - must be unique',
    
    -- System fields
    role ENUM('CUSTOMER', 'BANK_MANAGER') NOT NULL 
        COMMENT 'User role determining system permissions',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        COMMENT 'Account creation timestamp',
    is_active BOOLEAN DEFAULT TRUE 
        COMMENT 'Account status - only active users can login',
    last_login TIMESTAMP NULL 
        COMMENT 'Last successful login timestamp',
    
    -- Indexes for performance
    INDEX idx_username (username) COMMENT 'Fast username lookup for login',
    INDEX idx_email (email) COMMENT 'Fast email lookup for registration',
    INDEX idx_role (role) COMMENT 'Fast role-based queries',
    INDEX idx_active (is_active) COMMENT 'Fast active user queries'
) 
ENGINE=InnoDB 
COMMENT='System users including customers and bank managers';

-- =====================================================
-- 2. ACCOUNTS TABLE  
-- =====================================================
-- Purpose: Store bank account information
-- Business Rules:
--   - Each account belongs to one customer
--   - Account numbers are unique system-wide
--   - Only active accounts can perform transactions
--   - Different account types have different rules
-- =====================================================

CREATE TABLE accounts (
    -- Primary identifier
    account_number VARCHAR(20) PRIMARY KEY 
        COMMENT 'Unique account identifier (e.g., ACC1234567890)',
    
    -- Account information
    account_holder_name VARCHAR(100) NOT NULL 
        COMMENT 'Name of the account holder',
    account_type ENUM('SAVINGS', 'CURRENT') NOT NULL 
        COMMENT 'Account type determining interest and rules',
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00 
        COMMENT 'Current account balance in currency',
    
    -- Relationships
    customer_id INT NOT NULL 
        COMMENT 'Reference to the customer who owns this account',
    
    -- Status and tracking
    status ENUM('ACTIVE', 'INACTIVE', 'CLOSED', 'FROZEN') DEFAULT 'ACTIVE' 
        COMMENT 'Account status - only ACTIVE accounts can transact',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        COMMENT 'Account creation timestamp',
    last_transaction_date TIMESTAMP NULL 
        COMMENT 'Timestamp of last transaction on this account',
    
    -- Foreign key constraints
    FOREIGN KEY (customer_id) REFERENCES users(user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
        COMMENT 'Account belongs to a customer',
    
    -- Indexes for performance
    INDEX idx_customer_id (customer_id) COMMENT 'Fast customer account lookup',
    INDEX idx_account_type (account_type) COMMENT 'Fast account type queries',
    INDEX idx_status (status) COMMENT 'Fast status-based queries',
    INDEX idx_balance (balance) COMMENT 'Fast balance range queries',
    INDEX idx_created_date (created_date) COMMENT 'Fast date range queries'
) 
ENGINE=InnoDB 
COMMENT='Bank accounts owned by customers';

-- =====================================================
-- 3. TRANSACTIONS TABLE
-- =====================================================
-- Purpose: Record all financial transactions
-- Business Rules:
--   - Every transaction must reference a valid account
--   - Transaction amounts must be positive
--   - Balance after transaction must be accurate
--   - Transfers use reference numbers for linking
-- =====================================================

CREATE TABLE transactions (
    -- Primary identifier
    transaction_id VARCHAR(50) PRIMARY KEY 
        COMMENT 'Unique transaction identifier (e.g., TXN1699123456789)',
    
    -- Transaction details
    account_number VARCHAR(20) NOT NULL 
        COMMENT 'Account involved in this transaction',
    transaction_type VARCHAR(20) NOT NULL 
        COMMENT 'Type: DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST_CREDIT',
    amount DECIMAL(15,2) NOT NULL 
        COMMENT 'Transaction amount (always positive)',
    balance_after DECIMAL(15,2) NOT NULL 
        COMMENT 'Account balance after this transaction',
    
    -- Optional fields
    description TEXT 
        COMMENT 'Human-readable transaction description',
    reference_number VARCHAR(50) 
        COMMENT 'Reference number linking related transactions (transfers)',
    
    -- Tracking fields
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        COMMENT 'When the transaction occurred',
    created_by INT 
        COMMENT 'User who initiated this transaction (nullable for system)',
    
    -- Foreign key constraints
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
        COMMENT 'Transaction belongs to an account',
    FOREIGN KEY (created_by) REFERENCES users(user_id) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE
        COMMENT 'Transaction created by a user (nullable)',
    
    -- Indexes for performance
    INDEX idx_account_number (account_number) COMMENT 'Fast account transaction lookup',
    INDEX idx_transaction_type (transaction_type) COMMENT 'Fast transaction type queries',
    INDEX idx_timestamp (timestamp) COMMENT 'Fast date range queries',
    INDEX idx_amount (amount) COMMENT 'Fast amount range queries',
    INDEX idx_reference_number (reference_number) COMMENT 'Fast transfer lookup',
    INDEX idx_created_by (created_by) COMMENT 'Fast user transaction lookup'
) 
ENGINE=InnoDB 
COMMENT='All financial transactions in the system';

-- =====================================================
-- 4. AUDIT_LOG TABLE
-- =====================================================
-- Purpose: Track all system activities for security and compliance
-- Business Rules:
--   - All user actions are logged
--   - Data changes are recorded with before/after values
--   - Audit logs are never deleted
--   - IP addresses are tracked for security
-- =====================================================

CREATE TABLE audit_log (
    -- Primary identifier
    log_id INT AUTO_INCREMENT PRIMARY KEY 
        COMMENT 'Unique identifier for each audit log entry',
    
    -- User and action information
    user_id INT 
        COMMENT 'User who performed the action (nullable for system actions)',
    action VARCHAR(100) NOT NULL 
        COMMENT 'Description of the action performed',
    
    -- Data change tracking
    table_name VARCHAR(50) 
        COMMENT 'Database table affected by the action',
    record_id VARCHAR(50) 
        COMMENT 'Primary key of the affected record',
    old_values JSON 
        COMMENT 'Previous values before the change (JSON format)',
    new_values JSON 
        COMMENT 'New values after the change (JSON format)',
    
    -- Tracking fields
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        COMMENT 'When the action occurred',
    ip_address VARCHAR(45) 
        COMMENT 'IP address of the user (supports IPv6)',
    
    -- Foreign key constraints
    FOREIGN KEY (user_id) REFERENCES users(user_id) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE
        COMMENT 'Audit entry belongs to a user (nullable)',
    
    -- Indexes for performance
    INDEX idx_user_id (user_id) COMMENT 'Fast user audit lookup',
    INDEX idx_action (action) COMMENT 'Fast action type queries',
    INDEX idx_timestamp (timestamp) COMMENT 'Fast date range queries',
    INDEX idx_table_name (table_name) COMMENT 'Fast table-specific queries'
) 
ENGINE=InnoDB 
COMMENT='Audit trail for all system activities';

-- =====================================================
-- 5. VIEWS FOR COMMON QUERIES
-- =====================================================

-- Customer account summary view
CREATE VIEW customer_account_summary AS
SELECT 
    u.user_id,
    u.username,
    u.full_name,
    COUNT(a.account_number) as total_accounts,
    SUM(CASE WHEN a.status = 'ACTIVE' THEN 1 ELSE 0 END) as active_accounts,
    COALESCE(SUM(CASE WHEN a.status = 'ACTIVE' THEN a.balance ELSE 0 END), 0) as total_balance,
    MAX(a.last_transaction_date) as last_activity
FROM users u
LEFT JOIN accounts a ON u.user_id = a.customer_id
WHERE u.role = 'CUSTOMER'
GROUP BY u.user_id, u.username, u.full_name;

-- Account transaction summary view
CREATE VIEW account_transaction_summary AS
SELECT 
    a.account_number,
    a.account_holder_name,
    a.account_type,
    a.balance,
    a.status,
    COUNT(t.transaction_id) as total_transactions,
    COALESCE(SUM(CASE WHEN t.transaction_type IN ('DEPOSIT', 'TRANSFER_IN', 'INTEREST_CREDIT') THEN t.amount ELSE 0 END), 0) as total_credits,
    COALESCE(SUM(CASE WHEN t.transaction_type IN ('WITHDRAWAL', 'TRANSFER_OUT') THEN t.amount ELSE 0 END), 0) as total_debits,
    MAX(t.timestamp) as last_transaction
FROM accounts a
LEFT JOIN transactions t ON a.account_number = t.account_number
GROUP BY a.account_number, a.account_holder_name, a.account_type, a.balance, a.status;

-- High value transactions view (>10000)
CREATE VIEW high_value_transactions AS
SELECT 
    t.*,
    a.account_holder_name,
    a.account_type,
    u.full_name as created_by_name
FROM transactions t
JOIN accounts a ON t.account_number = a.account_number
LEFT JOIN users u ON t.created_by = u.user_id
WHERE t.amount > 10000
ORDER BY t.timestamp DESC;

-- =====================================================
-- 6. STORED PROCEDURES FOR COMMON OPERATIONS
-- =====================================================

DELIMITER //

-- Procedure to create a new account
CREATE PROCEDURE CreateAccount(
    IN p_customer_id INT,
    IN p_account_holder_name VARCHAR(100),
    IN p_account_type ENUM('SAVINGS', 'CURRENT'),
    IN p_initial_balance DECIMAL(15,2),
    OUT p_account_number VARCHAR(20)
)
BEGIN
    DECLARE v_account_count INT;
    
    -- Generate account number
    SELECT COUNT(*) + 1 INTO v_account_count FROM accounts;
    SET p_account_number = CONCAT('ACC', LPAD(v_account_count, 10, '0'));
    
    -- Insert account
    INSERT INTO accounts (account_number, account_holder_name, account_type, balance, customer_id)
    VALUES (p_account_number, p_account_holder_name, p_account_type, p_initial_balance, p_customer_id);
    
    -- Log initial deposit if balance > 0
    IF p_initial_balance > 0 THEN
        INSERT INTO transactions (transaction_id, account_number, transaction_type, amount, balance_after, description)
        VALUES (CONCAT('TXN', UNIX_TIMESTAMP(), FLOOR(RAND() * 1000)), p_account_number, 'INITIAL_DEPOSIT', p_initial_balance, p_initial_balance, 'Account opening deposit');
    END IF;
END //

-- Procedure to process a transaction
CREATE PROCEDURE ProcessTransaction(
    IN p_account_number VARCHAR(20),
    IN p_transaction_type VARCHAR(20),
    IN p_amount DECIMAL(15,2),
    IN p_description TEXT,
    IN p_created_by INT,
    OUT p_transaction_id VARCHAR(50),
    OUT p_new_balance DECIMAL(15,2)
)
BEGIN
    DECLARE v_current_balance DECIMAL(15,2);
    DECLARE v_account_status ENUM('ACTIVE', 'INACTIVE', 'CLOSED', 'FROZEN');
    
    -- Get current account info
    SELECT balance, status INTO v_current_balance, v_account_status
    FROM accounts WHERE account_number = p_account_number;
    
    -- Check account status
    IF v_account_status != 'ACTIVE' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Account is not active';
    END IF;
    
    -- Calculate new balance
    IF p_transaction_type IN ('DEPOSIT', 'TRANSFER_IN', 'INTEREST_CREDIT') THEN
        SET p_new_balance = v_current_balance + p_amount;
    ELSE
        SET p_new_balance = v_current_balance - p_amount;
        -- Check sufficient funds
        IF p_new_balance < 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient funds';
        END IF;
    END IF;
    
    -- Generate transaction ID
    SET p_transaction_id = CONCAT('TXN', UNIX_TIMESTAMP(), FLOOR(RAND() * 1000));
    
    -- Insert transaction
    INSERT INTO transactions (transaction_id, account_number, transaction_type, amount, balance_after, description, created_by)
    VALUES (p_transaction_id, p_account_number, p_transaction_type, p_amount, p_new_balance, p_description, p_created_by);
    
    -- Update account balance
    UPDATE accounts 
    SET balance = p_new_balance, last_transaction_date = CURRENT_TIMESTAMP
    WHERE account_number = p_account_number;
END //

DELIMITER ;

-- =====================================================
-- 7. TRIGGERS FOR AUDIT LOGGING
-- =====================================================

DELIMITER //

-- Trigger for user changes
CREATE TRIGGER users_audit_trigger
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (user_id, action, table_name, record_id, old_values, new_values)
    VALUES (
        NEW.user_id,
        'UPDATE_USER',
        'users',
        NEW.user_id,
        JSON_OBJECT('username', OLD.username, 'full_name', OLD.full_name, 'email', OLD.email, 'is_active', OLD.is_active),
        JSON_OBJECT('username', NEW.username, 'full_name', NEW.full_name, 'email', NEW.email, 'is_active', NEW.is_active)
    );
END //

-- Trigger for account changes
CREATE TRIGGER accounts_audit_trigger
AFTER UPDATE ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (user_id, action, table_name, record_id, old_values, new_values)
    VALUES (
        NULL,
        'UPDATE_ACCOUNT',
        'accounts',
        NEW.account_number,
        JSON_OBJECT('balance', OLD.balance, 'status', OLD.status),
        JSON_OBJECT('balance', NEW.balance, 'status', NEW.status)
    );
END //

DELIMITER ;

-- =====================================================
-- 8. INSERT DEFAULT DATA
-- =====================================================

-- Insert default users
INSERT IGNORE INTO users (username, password, full_name, email, role) VALUES 
('manager', 'manager123', 'Bank Manager', 'manager@bank.com', 'BANK_MANAGER'),
('customer', 'customer123', 'John Doe', 'john.doe@email.com', 'CUSTOMER');

-- =====================================================
-- 9. PERFORMANCE OPTIMIZATION
-- =====================================================

-- Additional composite indexes for complex queries
CREATE INDEX idx_accounts_customer_status ON accounts(customer_id, status);
CREATE INDEX idx_transactions_account_type ON transactions(account_number, transaction_type);
CREATE INDEX idx_transactions_timestamp_amount ON transactions(timestamp, amount);
CREATE INDEX idx_audit_user_timestamp ON audit_log(user_id, timestamp);

-- =====================================================
-- 10. SECURITY SETTINGS
-- =====================================================

-- Create application user with limited privileges
-- CREATE USER 'banking_app'@'localhost' IDENTIFIED BY 'secure_password_here';
-- GRANT SELECT, INSERT, UPDATE ON banking_system.* TO 'banking_app'@'localhost';
-- GRANT DELETE ON banking_system.audit_log TO 'banking_app'@'localhost'; -- Only for cleanup procedures
-- FLUSH PRIVILEGES;

-- =====================================================
-- END OF SCHEMA
-- =====================================================