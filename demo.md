# Smart Banking System - Demo Guide

## Quick Demo Instructions

### Prerequisites
1. **MySQL Server** must be running on localhost:3306
2. **Database Setup**: Run the setup script or let the application create tables automatically
3. **Compiled Application**: Run `./compile.sh` first

### Demo Scenario

#### 1. Start the Application
```bash
./run.sh
```

#### 2. Login as Bank Manager
- **Username**: `manager`
- **Password**: `manager123`

**Manager Features to Demo:**
- View all accounts in the system
- Create new accounts for customers
- View all transactions across the bank
- Generate comprehensive reports
- Perform manual deposits/withdrawals
- Credit interest to all accounts
- Manage customer accounts

#### 3. Login as Customer
- **Username**: `customer`
- **Password**: `customer123`

**Customer Features to Demo:**
- View personal accounts only
- Perform deposits and withdrawals
- Transfer funds between accounts
- View transaction history
- Filter high-value transactions (>₹10,000)
- Request new accounts
- Change password

### Step-by-Step Demo

#### Manager Workflow:
1. **Login** as manager
2. **Create Account**: Go to "All Accounts" → "Create Account"
   - Enter Customer ID: 2 (for the default customer)
   - Select Account Type: SAVINGS
   - Initial Balance: ₹5000
3. **View Reports**: Go to "Reports" → "Bank Summary"
4. **Credit Interest**: Go to "System Admin" → "Credit Interest to All Accounts"
5. **Monitor Transactions**: Go to "All Transactions" → Filter by high value

#### Customer Workflow:
1. **Login** as customer
2. **View Accounts**: Check "My Accounts" tab
3. **Deposit Money**: Select account → "Deposit" → Enter ₹15000
4. **Transfer Funds**: Go to "Transfer Funds" tab
   - From: Your account
   - To: Another account number
   - Amount: ₹12000
5. **Filter Transactions**: Go to "Transactions" → "Filter High Value"

### Key Features Demonstrated

#### Java 8 Streams Usage:
- **High-value transaction filtering**: Automatically filters transactions >₹10,000
- **Account statistics**: Uses streams for calculating totals and averages
- **Transaction grouping**: Groups transactions by type in reports

#### Optional Class Usage:
- **Safe account retrieval**: All database operations use Optional
- **Null-safe operations**: No null pointer exceptions

#### Multithreading:
- **Background services**: Interest calculation runs automatically
- **Asynchronous operations**: Database operations don't block UI

#### Exception Handling:
- **Custom exceptions**: Try insufficient funds, invalid accounts
- **Graceful error handling**: User-friendly error messages

#### Role-Based Security:
- **Different interfaces**: Manager vs Customer views
- **Permission-based access**: Customers can't see other accounts
- **Secure authentication**: Password-based login

### Database Integration:
- **MySQL connectivity**: Full JDBC implementation
- **Transaction management**: Proper commit/rollback
- **Data persistence**: All data stored in MySQL database

### Sample Data for Testing:

#### Create Test Accounts (as Manager):
1. **Savings Account**: Customer ID 2, Balance ₹10000
2. **Current Account**: Customer ID 2, Balance ₹25000

#### Perform Test Transactions (as Customer):
1. **Deposit**: ₹15000 (creates high-value transaction)
2. **Withdrawal**: ₹5000
3. **Transfer**: ₹12000 between accounts

#### Generate Reports (as Manager):
1. **Bank Summary**: Shows total accounts, customers, balances
2. **High Value Transactions**: Shows transactions >₹10000
3. **Customer Report**: Shows all customers and their account details

### Error Scenarios to Test:

1. **Insufficient Funds**: Try to withdraw more than balance
2. **Invalid Account**: Try to transfer to non-existent account
3. **Wrong Credentials**: Try logging in with wrong password
4. **Database Connection**: Stop MySQL to see connection error handling

### Performance Features:

- **Real-time Updates**: GUI refreshes automatically every 10-15 seconds
- **Efficient Queries**: Indexed database searches
- **Memory Management**: Proper resource cleanup
- **Background Processing**: Non-blocking operations

This demo showcases a complete enterprise-level banking application with proper architecture, security, and user experience.