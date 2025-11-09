package interfaces;

import exceptions.InsufficientFundsException;
import models.Transaction;
import java.util.List;

/**
 * Interface for entities that can perform transactions
 * Demonstrates Interface OOP concept
 */
public interface Transactional {
    /**
     * Deposit money into the account
     * @param amount Amount to deposit
     * @throws InsufficientFundsException if amount is invalid
     */
    void deposit(double amount) throws InsufficientFundsException;
    
    /**
     * Withdraw money from the account
     * @param amount Amount to withdraw
     * @throws InsufficientFundsException if insufficient balance
     */
    void withdraw(double amount) throws InsufficientFundsException;
    
    /**
     * Get transaction history
     * @return List of transactions
     */
    List<Transaction> getTransactions();
    
    /**
     * Add a transaction to history
     * @param transaction Transaction to add
     */
    void addTransaction(Transaction transaction);
}
