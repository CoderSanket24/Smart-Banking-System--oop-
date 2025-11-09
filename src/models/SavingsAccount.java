package models;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.04; // 4% annual interest
    private static final double MINIMUM_BALANCE = 1000.0; // ₹1000 minimum balance
    
    public SavingsAccount() {
        super();
    }
    
    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance, int customerId) {
        super(accountNumber, accountHolderName, initialBalance, customerId);
    }
    
    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE / 12; // Monthly interest
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
    
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    public static double getStaticInterestRate() {
        return INTEREST_RATE;
    }
}