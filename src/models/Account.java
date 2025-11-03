package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import exceptions.InsufficientFundsException;

public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected LocalDateTime createdDate;
    protected List<Transaction> transactions;
    protected int customerId;
    protected AccountStatus status;
    
    public Account() {
        this.transactions = new ArrayList<>();
        this.createdDate = LocalDateTime.now();
        this.status = AccountStatus.ACTIVE;
    }
    
    public Account(String accountNumber, String accountHolderName, double initialBalance, int customerId) {
        this();
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.customerId = customerId;
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract double calculateInterest();
    public abstract String getAccountType();
    public abstract double getMinimumBalance();
    
    // Common methods
    public synchronized void deposit(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InsufficientFundsException("Deposit amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new InsufficientFundsException("Account is not active");
        }
        balance += amount;
        addTransaction(new Transaction(accountNumber, "DEPOSIT", amount, balance));
    }
    
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InsufficientFundsException("Withdrawal amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new InsufficientFundsException("Account is not active");
        }
        if (balance - amount < getMinimumBalance()) {
            throw new InsufficientFundsException("Insufficient funds. Minimum balance required: ₹" + getMinimumBalance());
        }
        balance -= amount;
        addTransaction(new Transaction(accountNumber, "WITHDRAWAL", amount, balance));
    }
    
    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("%s Account: %s - %s (₹%.2f)", 
                           getAccountType(), accountNumber, accountHolderName, balance);
    }
}

