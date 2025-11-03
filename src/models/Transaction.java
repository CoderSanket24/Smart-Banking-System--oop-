package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String description;
    private String referenceNumber;
    
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this();
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = type + " of ₹" + String.format("%.2f", amount);
    }
    
    public Transaction(String accountNumber, String type, double amount, double balanceAfter, String description) {
        this(accountNumber, type, amount, balanceAfter);
        this.description = description;
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    
    @Override
    public String toString() {
        return String.format("%s | %s | ₹%.2f | Balance: ₹%.2f | %s | %s", 
            transactionId, type, amount, balanceAfter, 
            timestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
            description != null ? description : "");
    }
}