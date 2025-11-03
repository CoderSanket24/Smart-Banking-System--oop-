package database;

public class DatabaseConfig {
    // MySQL Database Configuration
    public static final String DB_URL = "jdbc:mysql://localhost:3306/banking_system";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "Sanket@2809";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connection Pool Settings
    public static final int MAX_CONNECTIONS = 10;
    public static final int CONNECTION_TIMEOUT = 30000; // 30 seconds
    
    // Database Schema
    public static final String SCHEMA_NAME = "banking_system";
    
    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_AUDIT_LOG = "audit_log";
    
    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }
}