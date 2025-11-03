# Smart Banking System - Visual ER Diagram

## Simplified Visual Representation

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           SMART BANKING SYSTEM ER DIAGRAM                      │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────┐         ┌─────────────────────────┐
│         USERS           │         │        ACCOUNTS         │
├─────────────────────────┤         ├─────────────────────────┤
│ 🔑 user_id (PK)        │◄────────┤ 🔑 account_number (PK) │
│ 🔒 username (UK)       │    1:N  │ 🔗 customer_id (FK)    │
│ 🔒 password            │         │    account_holder_name  │
│    full_name           │         │    account_type         │
│ 🔒 email (UK)          │         │    balance              │
│    role                │         │    status               │
│    created_date        │         │    created_date         │
│    is_active           │         │    last_transaction_date│
│    last_login          │         └─────────────────────────┘
└─────────────────────────┘                      │
           │                                     │
           │ 1:N                                 │ 1:N
           │                                     │
           ▼                                     ▼
┌─────────────────────────┐         ┌─────────────────────────┐
│      AUDIT_LOG          │         │     TRANSACTIONS        │
├─────────────────────────┤         ├─────────────────────────┤
│ 🔑 log_id (PK)         │         │ 🔑 transaction_id (PK) │
│ 🔗 user_id (FK)        │         │ 🔗 account_number (FK) │
│    action              │         │ 🔗 created_by (FK)     │
│    table_name          │         │    transaction_type     │
│    record_id           │         │    amount               │
│    old_values          │         │    balance_after        │
│    new_values          │         │    description          │
│    timestamp           │         │    reference_number     │
│    ip_address          │         │    timestamp            │
└─────────────────────────┘         └─────────────────────────┘
```

## Entity Relationship Matrix

| Entity | Primary Key | Foreign Keys | Relationships |
|--------|-------------|--------------|---------------|
| **USERS** | user_id | None | → ACCOUNTS (1:N)<br>→ TRANSACTIONS (1:N)<br>→ AUDIT_LOG (1:N) |
| **ACCOUNTS** | account_number | customer_id → USERS | ← USERS (N:1)<br>→ TRANSACTIONS (1:N) |
| **TRANSACTIONS** | transaction_id | account_number → ACCOUNTS<br>created_by → USERS | ← ACCOUNTS (N:1)<br>← USERS (N:1) |
| **AUDIT_LOG** | log_id | user_id → USERS | ← USERS (N:1) |

## Cardinality Details

```
USERS (1) ──────── (N) ACCOUNTS
  │                     │
  │ One user can have   │ Each account belongs
  │ multiple accounts   │ to one customer
  │                     │
  
ACCOUNTS (1) ──────── (N) TRANSACTIONS  
  │                        │
  │ One account can have   │ Each transaction
  │ multiple transactions  │ belongs to one account
  │                        │

USERS (1) ──────── (N) TRANSACTIONS
  │                     │
  │ One user can create │ Each transaction
  │ multiple transactions│ created by one user
  │                     │ (nullable)

USERS (1) ──────── (N) AUDIT_LOG
  │                     │
  │ One user can have   │ Each log entry
  │ multiple log entries│ belongs to one user
  │                     │ (nullable)
```

## Key Constraints Summary

### 🔑 Primary Keys
- **USERS**: `user_id` (Auto-increment)
- **ACCOUNTS**: `account_number` (Generated string)
- **TRANSACTIONS**: `transaction_id` (Generated string)
- **AUDIT_LOG**: `log_id` (Auto-increment)

### 🔗 Foreign Keys
- **ACCOUNTS.customer_id** → **USERS.user_id** (CASCADE DELETE)
- **TRANSACTIONS.account_number** → **ACCOUNTS.account_number** (CASCADE DELETE)
- **TRANSACTIONS.created_by** → **USERS.user_id** (SET NULL)
- **AUDIT_LOG.user_id** → **USERS.user_id** (SET NULL)

### 🔒 Unique Constraints
- **USERS.username** (Unique login identifier)
- **USERS.email** (Unique email address)
- **ACCOUNTS.account_number** (Unique account identifier)

### 📊 Indexes
```sql
-- USERS table indexes
INDEX idx_username ON users(username)
INDEX idx_email ON users(email)

-- ACCOUNTS table indexes  
INDEX idx_customer_id ON accounts(customer_id)
INDEX idx_account_type ON accounts(account_type)
INDEX idx_status ON accounts(status)

-- TRANSACTIONS table indexes
INDEX idx_account_number ON transactions(account_number)
INDEX idx_transaction_type ON transactions(transaction_type)
INDEX idx_timestamp ON transactions(timestamp)
INDEX idx_amount ON transactions(amount)

-- AUDIT_LOG table indexes
INDEX idx_user_id ON audit_log(user_id)
INDEX idx_action ON audit_log(action)
INDEX idx_timestamp ON audit_log(timestamp)
```

## Data Flow Diagram

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   USER      │    │   ACCOUNT   │    │ TRANSACTION │
│   LOGIN     │───▶│   CREATION  │───▶│  PROCESSING │
└─────────────┘    └─────────────┘    └─────────────┘
       │                   │                   │
       ▼                   ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ AUDIT_LOG   │    │ AUDIT_LOG   │    │ AUDIT_LOG   │
│ (Login)     │    │ (Account)   │    │ (Transaction)│
└─────────────┘    └─────────────┘    └─────────────┘
```

## Business Logic Flow

### 1. User Registration & Login
```
User Registration → USERS table
                 → AUDIT_LOG (user creation)
                 
User Login → Validate against USERS
          → Update last_login
          → AUDIT_LOG (login activity)
```

### 2. Account Management
```
Account Creation → ACCOUNTS table
                → Link to USERS via customer_id
                → AUDIT_LOG (account creation)
                
Account Operations → Update ACCOUNTS
                  → AUDIT_LOG (account changes)
```

### 3. Transaction Processing
```
Transaction → Validate ACCOUNTS status
           → Update ACCOUNTS balance
           → Insert TRANSACTIONS record
           → AUDIT_LOG (transaction activity)
           
Transfer → Two TRANSACTIONS records
        → Update both ACCOUNTS
        → Link via reference_number
        → AUDIT_LOG (transfer activity)
```

### 4. Audit Trail
```
All Operations → AUDIT_LOG entry
              → Track user_id, action, timestamp
              → Store old/new values for changes
              → Record IP address for security
```

This ER diagram ensures:
- **Data Integrity**: Through proper constraints and relationships
- **Security**: Via audit logging and user management
- **Scalability**: With appropriate indexing strategy
- **Compliance**: Through comprehensive audit trails
- **Performance**: With optimized query patterns