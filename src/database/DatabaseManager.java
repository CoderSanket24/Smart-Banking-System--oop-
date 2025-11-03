package database;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    
    static {
        try {
            // Load MySQL JDBC driver
            Class.forName(DatabaseConfig.DB_DRIVER);
            logger.info("MySQL JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to load MySQL JDBC driver", e);
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.DB_USERNAME, 
                DatabaseConfig.DB_PASSWORD
            );
            conn.setAutoCommit(false); // Enable transaction management
            return conn;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to establish database connection", e);
            throw e;
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
            insertDefaultData(conn);
            conn.commit();
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    private static void createTables(Connection conn) throws SQLException {
        // Create Users table
        String createUsersTable = """
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
            )
        """;
        
        // Create Accounts table
        String createAccountsTable = """
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
            )
        """;
        
        // Create Transactions table
        String createTransactionsTable = """
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
            )
        """;
        
        // Create Audit Log table
        String createAuditLogTable = """
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
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
            stmt.execute(createAuditLogTable);
            logger.info("Database tables created successfully");
        }
    }
    
    private static void insertDefaultData(Connection conn) throws SQLException {
        // Insert default bank manager
        String insertManager = """
            INSERT IGNORE INTO users (username, password, full_name, email, role) 
            VALUES ('manager', 'manager123', 'Bank Manager', 'manager@bank.com', 'BANK_MANAGER')
        """;
        
        // Insert sample customer
        String insertCustomer = """
            INSERT IGNORE INTO users (username, password, full_name, email, role) 
            VALUES ('customer', 'customer123', 'John Doe', 'john.doe@email.com', 'CUSTOMER')
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertManager);
            stmt.execute(insertCustomer);
            logger.info("Default users inserted successfully");
        }
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Failed to close database connection", e);
            }
        }
    }
    
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Failed to rollback transaction", e);
            }
        }
    }
}