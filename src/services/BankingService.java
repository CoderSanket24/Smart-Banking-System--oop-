package services;

import dao.AccountDAO;
import dao.TransactionDAO;
import models.*;
import exceptions.InsufficientFundsException;
import exceptions.AccountNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BankingService {
    private static final Logger logger = Logger.getLogger(BankingService.class.getName());
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    
    public BankingService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }
    
    public String createAccount(String accountHolderName, String accountType, double initialBalance, int customerId) 
            throws SQLException {
        try {
            String accountNumber = accountDAO.generateAccountNumber();
            Account account;
            
            if ("SAVINGS".equalsIgnoreCase(accountType)) {
                account = new SavingsAccount(accountNumber, accountHolderName, initialBalance, customerId);
            } else if ("CURRENT".equalsIgnoreCase(accountType)) {
                account = new CurrentAccount(accountNumber, accountHolderName, initialBalance, customerId);
            } else {
                throw new IllegalArgumentException("Invalid account type: " + accountType);
            }
            
            // Check minimum balance requirement
            if (initialBalance < account.getMinimumBalance()) {
                throw new InsufficientFundsException(
                    "Initial balance must be at least ₹" + account.getMinimumBalance());
            }
            
            boolean created = accountDAO.createAccount(account);
            if (created && initialBalance > 0) {
                // Create initial deposit transaction
                Transaction initialDeposit = new Transaction(
                    accountNumber, "INITIAL_DEPOSIT", initialBalance, initialBalance, 
                    "Account opening deposit");
                transactionDAO.saveTransaction(initialDeposit);
            }
            
            logger.info("Account created successfully: " + accountNumber);
            return accountNumber;
            
        } catch (SQLException | InsufficientFundsException e) {
            logger.log(Level.SEVERE, "Error creating account", e);
            throw new SQLException("Failed to create account: " + e.getMessage(), e);
        }
    }
    
    public void deposit(String accountNumber, double amount, String description) 
            throws AccountNotFoundException, InsufficientFundsException, SQLException {
        try {
            Optional<Account> accountOpt = accountDAO.getAccountByNumber(accountNumber);
            if (!accountOpt.isPresent()) {
                throw new AccountNotFoundException("Account not found: " + accountNumber);
            }
            
            Account account = accountOpt.get();
            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new InsufficientFundsException("Account is not active");
            }
            
            account.deposit(amount);
            
            // Update database
            accountDAO.updateBalance(accountNumber, account.getBalance());
            
            // Save transaction
            Transaction transaction = new Transaction(
                accountNumber, "DEPOSIT", amount, account.getBalance(), description);
            transactionDAO.saveTransaction(transaction);
            
            logger.info("Deposit successful: ₹" + amount + " to account " + accountNumber);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error processing deposit", e);
            throw e;
        }
    }
    
    public void withdraw(String accountNumber, double amount, String description) 
            throws AccountNotFoundException, InsufficientFundsException, SQLException {
        try {
            Optional<Account> accountOpt = accountDAO.getAccountByNumber(accountNumber);
            if (!accountOpt.isPresent()) {
                throw new AccountNotFoundException("Account not found: " + accountNumber);
            }
            
            Account account = accountOpt.get();
            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new InsufficientFundsException("Account is not active");
            }
            
            account.withdraw(amount);
            
            // Update database
            accountDAO.updateBalance(accountNumber, account.getBalance());
            
            // Save transaction
            Transaction transaction = new Transaction(
                accountNumber, "WITHDRAWAL", amount, account.getBalance(), description);
            transactionDAO.saveTransaction(transaction);
            
            logger.info("Withdrawal successful: ₹" + amount + " from account " + accountNumber);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error processing withdrawal", e);
            throw e;
        }
    }
    
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount, String description) 
            throws AccountNotFoundException, InsufficientFundsException, SQLException {
        try {
            Optional<Account> fromAccountOpt = accountDAO.getAccountByNumber(fromAccountNumber);
            Optional<Account> toAccountOpt = accountDAO.getAccountByNumber(toAccountNumber);
            
            if (!fromAccountOpt.isPresent()) {
                throw new AccountNotFoundException("Source account not found: " + fromAccountNumber);
            }
            if (!toAccountOpt.isPresent()) {
                throw new AccountNotFoundException("Destination account not found: " + toAccountNumber);
            }
            
            Account fromAccount = fromAccountOpt.get();
            Account toAccount = toAccountOpt.get();
            
            if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new InsufficientFundsException("Source account is not active");
            }
            if (toAccount.getStatus() != AccountStatus.ACTIVE) {
                throw new InsufficientFundsException("Destination account is not active");
            }
            
            // Perform transfer
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    fromAccount.withdraw(amount);
                    toAccount.deposit(amount);
                    
                    // Update balances in database
                    accountDAO.updateBalance(fromAccountNumber, fromAccount.getBalance());
                    accountDAO.updateBalance(toAccountNumber, toAccount.getBalance());
                    
                    // Save transactions
                    String transferRef = "TRF" + System.currentTimeMillis();
                    
                    Transaction debitTransaction = new Transaction(
                        fromAccountNumber, "TRANSFER_OUT", amount, fromAccount.getBalance(), 
                        "Transfer to " + toAccountNumber + " - " + description);
                    debitTransaction.setReferenceNumber(transferRef);
                    
                    Transaction creditTransaction = new Transaction(
                        toAccountNumber, "TRANSFER_IN", amount, toAccount.getBalance(), 
                        "Transfer from " + fromAccountNumber + " - " + description);
                    creditTransaction.setReferenceNumber(transferRef);
                    
                    transactionDAO.saveTransaction(debitTransaction);
                    transactionDAO.saveTransaction(creditTransaction);
                }
            }
            
            logger.info("Transfer successful: ₹" + amount + " from " + fromAccountNumber + " to " + toAccountNumber);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error processing transfer", e);
            throw e;
        }
    }
    
    public Optional<Account> getAccount(String accountNumber) throws SQLException {
        return accountDAO.getAccountByNumber(accountNumber);
    }
    
    public List<Account> getAccountsByCustomer(int customerId) throws SQLException {
        return accountDAO.getAccountsByCustomerId(customerId);
    }
    
    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) throws SQLException {
        return transactionDAO.getTransactionsByAccount(accountNumber);
    }
    
    public List<Transaction> getCustomerTransactions(int customerId) throws SQLException {
        return transactionDAO.getTransactionsByCustomer(customerId);
    }
    
    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }
    
    public List<Transaction> getHighValueTransactions(double threshold) throws SQLException {
        return transactionDAO.getHighValueTransactions(threshold);
    }
    
    // Java 8 Streams usage for filtering and processing
    public List<Transaction> filterTransactionsByType(List<Transaction> transactions, String type) {
        return transactions.stream()
                .filter(transaction -> transaction.getType().equals(type))
                .collect(Collectors.toList());
    }
    
    public double calculateTotalBalance(List<Account> accounts) {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
    
    public List<Account> getActiveAccounts(List<Account> accounts) {
        return accounts.stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .collect(Collectors.toList());
    }
    
    public void creditInterest() throws SQLException {
        List<Account> accounts = getAllAccounts();
        
        for (Account account : accounts) {
            if (account.getStatus() == AccountStatus.ACTIVE) {
                double interest = account.calculateInterest();
                if (interest > 0) {
                    try {
                        deposit(account.getAccountNumber(), interest, "Monthly interest credit");
                        logger.info("Interest credited: ₹" + interest + " to account " + account.getAccountNumber());
                    } catch (AccountNotFoundException | InsufficientFundsException e) {
                        logger.log(Level.WARNING, "Failed to credit interest to account: " + account.getAccountNumber(), e);
                    }
                }
            }
        }
    }
    
    public boolean closeAccount(String accountNumber) throws SQLException, AccountNotFoundException {
        Optional<Account> accountOpt = accountDAO.getAccountByNumber(accountNumber);
        if (!accountOpt.isPresent()) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        
        Account account = accountOpt.get();
        account.setStatus(AccountStatus.CLOSED);
        
        boolean updated = accountDAO.updateAccount(account);
        if (updated) {
            logger.info("Account closed successfully: " + accountNumber);
        }
        return updated;
    }
}