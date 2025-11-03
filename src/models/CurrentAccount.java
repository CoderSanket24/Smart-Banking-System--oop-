package models;

import exceptions.InsufficientFundsException;

public class CurrentAccount extends Account {
    private static final double INTEREST_RATE = 0.02; // 2% annual interest
    private static final double MINIMUM_BALANCE = -10000.0; // ₹10,000 overdraft limit
    private static final double OVERDRAFT_LIMIT = 10000.0;
    
    public CurrentAccount() {
        super();
    }
    
    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance, int customerId) {
        super(accountNumber, accountHolderName, initialBalance, customerId);
    }
    
    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InsufficientFundsException("Withdrawal amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new InsufficientFundsException("Account is not active");
        }
        if (balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException("Overdraft limit of ₹" + OVERDRAFT_LIMIT + " exceeded");
        }
        balance -= amount;
        addTransaction(new Transaction(accountNumber, "WITHDRAWAL", amount, balance));
    }
    
    @Override
    public double calculateInterest() {
        return balance > 0 ? balance * INTEREST_RATE / 12 : 0; // No interest on negative balance
    }
    
    @Override
    public String getAccountType() {
        return "CURRENT";
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    public static double getInterestRate() {
        return INTEREST_RATE;
    }
    
    public static double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }
}