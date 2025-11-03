# Smart Banking System with MySQL and Role-Based GUI

A comprehensive desktop banking application built with Java, featuring MySQL database integration, role-based authentication, and separate GUI interfaces for customers and bank managers.

## Features

### Core Banking Operations
- **Role-Based Authentication**: Separate login for customers and bank managers
- **Account Management**: Create, view, and manage savings and current accounts
- **Transaction Processing**: Deposits, withdrawals, and fund transfers
- **Real-time Updates**: Live balance and transaction updates
- **Transaction History**: Complete audit trail with filtering capabilities

### Advanced Java Features
- **Java 8 Streams**: Advanced filtering and data processing
- **Optional Class**: Null-safe operations throughout the application
- **Multithreading**: Background services for interest calculation
- **Exception Handling**: Custom exceptions with proper error handling
- **MySQL Integration**: Full JDBC implementation with connection pooling
- **File Handling**: Transaction logging and data persistence

### GUI Features
- **Role-Based Interfaces**: Different GUI for customers vs managers
- **Large, User-Friendly Interface**: Bigger input fields, buttons, and fonts
- **Interactive Elements**: Hover effects, animations, and visual feedback
- **Quick Actions**: One-click login buttons and keyboard shortcuts
- **Professional Design**: Modern styling with colors and borders
- **Tabbed Navigation**: Organized sections with context menus
- **Real-time Data**: Auto-refresh with visual indicators
- **Interactive Reports**: Dynamic report generation with live stats
- **Responsive Design**: Resizable windows with status bars
- **Dashboard Widgets**: Live statistics and system monitoring

### Security Features
- **User Authentication**: Secure login system
- **Role-Based Permissions**: Different access levels
- **Password Management**: Change password functionality
- **Session Management**: Proper login/logout handling

## Project Structure

```
src/
├── BankingApplication.java          # Main application entry point
├── models/                          # Data models
│   ├── User.java                   # User entity with roles
│   ├── Account.java                # Abstract account class
│   ├── SavingsAccount.java         # Savings account implementation
│   ├── CurrentAccount.java         # Current account implementation
│   └── Transaction.java            # Transaction entity
├── exceptions/                      # Custom exceptions
│   ├── InsufficientFundsException.java
│   ├── AccountNotFoundException.java
│   └── AuthenticationException.java
├── database/                        # Database layer
│   ├── DatabaseConfig.java         # Database configuration
│   └── DatabaseManager.java        # Connection management
├── dao/                            # Data Access Objects
│   ├── UserDAO.java                # User database operations
│   ├── AccountDAO.java             # Account database operations
│   └── TransactionDAO.java         # Transaction database operations
├── services/                       # Business logic layer
│   ├── AuthenticationService.java  # Login/logout handling
│   └── BankingService.java         # Core banking operations
└── gui/                           # User interface
    ├── LoginFrame.java             # Login window
    ├── CustomerMainFrame.java      # Customer interface
    └── ManagerMainFrame.java       # Manager interface

build/                              # Compiled classes
lib/                               # External libraries
setup_database.sql                 # Database setup script
compile.sh                         # Compilation script
run.sh                            # Execution script
```

## Prerequisites

- **Java 8 or higher**
- **MySQL Server 5.7 or higher**
- **Internet connection** (for downloading MySQL JDBC driver)

## Setup Instructions

### 1. Database Setup

1. **Install MySQL Server** if not already installed
2. **Start MySQL service**
3. **Create database and user**:
   ```sql
   mysql -u root -p < setup_database.sql
   ```
   
   Or manually:
   ```sql
   CREATE DATABASE banking_system;
   USE banking_system;
   -- Tables will be created automatically by the application
   ```

4. **Update database configuration** in `src/database/DatabaseConfig.java` if needed:
   ```java
   public static final String DB_URL = "jdbc:mysql://localhost:3306/banking_system";
   public static final String DB_USERNAME = "root";
   public static final String DB_PASSWORD = "your_password";
   ```

### 2. Application Setup

1. **Make scripts executable**:
   ```bash
   chmod +x compile.sh run.sh
   ```

2. **Compile the application**:
   ```bash
   ./compile.sh
   ```

3. **Run the application**:
   ```bash
   ./run.sh
   ```

### Manual Compilation (if scripts don't work)

```bash
# Create directories
mkdir -p build/classes lib

# Download MySQL JDBC driver
curl -L -o lib/mysql-connector-java-8.0.33.jar https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar

# Compile
find src -name "*.java" -print0 | xargs -0 javac -cp "lib/mysql-connector-java-8.0.33.jar" -d build/classes

# Run
java -cp "build/classes:lib/mysql-connector-java-8.0.33.jar" BankingApplication
```

## Default Login Credentials

### Bank Manager
- **Username**: `manager`
- **Password**: `manager123`
- **Permissions**: Full access to all features

### Customer
- **Username**: `customer`
- **Password**: `customer123`
- **Permissions**: Limited to own accounts and transactions

## User Roles and Features

### Customer Features
- **Account Overview**: View all personal accounts
- **Transaction History**: View personal transaction history
- **Deposit/Withdraw**: Perform cash transactions
- **Fund Transfer**: Transfer money between accounts
- **Account Services**: Request new accounts, change password
- **High-Value Filtering**: Filter transactions above ₹10,000

### Manager Features
- **All Customer Accounts**: View and manage all bank accounts
- **Customer Management**: Activate/deactivate customers
- **Transaction Monitoring**: View all bank transactions
- **Account Operations**: Create accounts, manual deposits/withdrawals
- **Comprehensive Reports**: Generate various banking reports
- **System Administration**: Credit interest, system statistics
- **Advanced Filtering**: Filter by amount, type, date ranges

## Key Java Concepts Demonstrated

### 1. Object-Oriented Programming
- **Inheritance**: Account hierarchy (SavingsAccount, CurrentAccount)
- **Polymorphism**: Different account behaviors
- **Encapsulation**: Private fields with public methods
- **Abstraction**: Abstract Account class and interfaces

### 2. Advanced Java Features
- **Java 8 Streams**: Transaction filtering and data processing
- **Optional Class**: Null-safe database operations
- **Lambda Expressions**: Event handling and stream operations
- **Method References**: Cleaner code with method references

### 3. Database Integration
- **JDBC**: MySQL connectivity with prepared statements
- **Connection Pooling**: Efficient database connections
- **Transaction Management**: Proper commit/rollback handling
- **SQL Injection Prevention**: Parameterized queries

### 4. Exception Handling
- **Custom Exceptions**: Domain-specific error handling
- **Try-with-resources**: Automatic resource management
- **Proper Error Propagation**: Meaningful error messages

### 5. Multithreading
- **Background Services**: Interest calculation threads
- **Synchronized Methods**: Thread-safe account operations
- **Timer Tasks**: Periodic background operations

### 6. GUI Programming
- **Swing Components**: Professional desktop interface
- **Event-Driven Programming**: User interaction handling
- **Layout Managers**: Responsive UI design
- **Role-Based UI**: Different interfaces for different users

## Sample Operations

### Customer Workflow
1. **Login** as customer
2. **View accounts** and current balances
3. **Perform deposit** of ₹15,000 (creates high-value transaction)
4. **Transfer funds** to another account
5. **Filter transactions** to view high-value transactions
6. **Change password** for security

### Manager Workflow
1. **Login** as manager
2. **View all accounts** in the system
3. **Create new account** for a customer
4. **Monitor transactions** across all accounts
5. **Generate reports** for bank analysis
6. **Credit interest** to all accounts
7. **Manage customers** (activate/deactivate)

## Database Schema

### Users Table
- `user_id` (Primary Key)
- `username` (Unique)
- `password`
- `full_name`
- `email`
- `role` (CUSTOMER/BANK_MANAGER)
- `created_date`
- `is_active`

### Accounts Table
- `account_number` (Primary Key)
- `account_holder_name`
- `account_type` (SAVINGS/CURRENT)
- `balance`
- `customer_id` (Foreign Key)
- `status` (ACTIVE/INACTIVE/CLOSED/FROZEN)
- `created_date`

### Transactions Table
- `transaction_id` (Primary Key)
- `account_number` (Foreign Key)
- `transaction_type`
- `amount`
- `balance_after`
- `description`
- `timestamp`

## Error Handling

The application handles various error scenarios:
- **Database connection issues**
- **Invalid login credentials**
- **Insufficient funds for transactions**
- **Account not found errors**
- **Network connectivity problems**
- **Invalid input validation**

## Performance Features

- **Connection pooling** for database efficiency
- **Lazy loading** of transaction data
- **Indexed database queries** for fast searches
- **Optimized GUI updates** with background threads
- **Memory-efficient data structures**

## Security Considerations

- **Password-based authentication**
- **Role-based access control**
- **SQL injection prevention**
- **Session management**
- **Input validation and sanitization**

## Future Enhancements

- **Encrypted password storage**
- **Audit logging system**
- **Report export functionality**
- **Email notifications**
- **Mobile app integration**
- **Advanced analytics dashboard**

## Troubleshooting

### Common Issues

1. **MySQL Connection Failed**
   - Ensure MySQL server is running
   - Check database credentials in DatabaseConfig.java
   - Verify database exists

2. **JDBC Driver Not Found**
   - Run `./compile.sh` to download driver automatically
   - Or manually download MySQL Connector/J

3. **Compilation Errors**
   - Ensure Java 8+ is installed
   - Check JAVA_HOME environment variable
   - Verify all source files are present

4. **GUI Not Displaying**
   - Check if DISPLAY variable is set (Linux)
   - Ensure X11 forwarding is enabled (SSH)
   - Try different look and feel settings

This banking system demonstrates enterprise-level Java development with proper architecture, security, and user experience considerations.