package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import exceptions.InsufficientFundsException;
import interfaces.Transactional;
import interfaces.InterestBearing;
import interfaces.Reportable;

public abstract class Account implements Transactional, InterestBearing, Reportable {
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
    public abstract String getAccountType();
    public abstract double getMinimumBalance();
    
    // From InterestBearing interface
    @Override
    public abstract double calculateInterest();
    
    @Override
    public abstract double getInterestRate();
    
    @Override
    public void creditInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            balance += interest;
            addTransaction(new Transaction(accountNumber, "INTEREST_CREDIT", interest, balance, "Monthly interest credit"));
        }
    }
    
    // From Transactional interface
    @Override
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
    
    @Override
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
    
    @Override
    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    // From Reportable interface
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(getReportTitle()).append("\n");
        report.append("=".repeat(50)).append("\n\n");
        report.append(getFormattedData());
        return report.toString();
    }
    
    @Override
    public String getReportTitle() {
        return "Account Report - " + accountNumber;
    }
    
    @Override
    public String getFormattedData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder data = new StringBuilder();
        data.append("Account Number: ").append(accountNumber).append("\n");
        data.append("Account Holder: ").append(accountHolderName).append("\n");
        data.append("Account Type: ").append(getAccountType()).append("\n");
        data.append("Current Balance: ₹").append(String.format("%.2f", balance)).append("\n");
        data.append("Status: ").append(status).append("\n");
        data.append("Created Date: ").append(createdDate.format(formatter)).append("\n");
        data.append("Total Transactions: ").append(transactions.size()).append("\n");
        data.append("Minimum Balance: ₹").append(String.format("%.2f", getMinimumBalance())).append("\n");
        data.append("Interest Rate: ").append(String.format("%.2f%%", getInterestRate() * 100)).append("\n");
        return data.toString();
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
    
    @Override
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

