package interfaces;

/**
 * Interface for accounts that earn interest
 * Demonstrates Interface OOP concept
 */
public interface InterestBearing {
    /**
     * Calculate interest for the account
     * @return Interest amount
     */
    double calculateInterest();
    
    /**
     * Get the interest rate
     * @return Interest rate as decimal (e.g., 0.04 for 4%)
     */
    double getInterestRate();
    
    /**
     * Credit interest to the account
     */
    void creditInterest();
}
