package dao;

import database.DatabaseManager;
import models.*;
import exceptions.AccountNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AccountDAO {
    private static final Logger logger = Logger.getLogger(AccountDAO.class.getName());
    
    public boolean createAccount(Account account) throws SQLException {
        String sql = """
            INSERT INTO accounts (account_number, account_holder_name, account_type, 
            balance, customer_id, status) VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getAccountHolderName());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setInt(5, account.getCustomerId());
            pstmt.setString(6, account.getStatus().name());
            
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            
            if (affectedRows > 0) {
                logger.info("Account created successfully: " + account.getAccountNumber());
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating account: " + account.getAccountNumber(), e);
            throw e;
        }
    }
    
    public Optional<Account> getAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching account: " + accountNumber, e);
            throw e;
        }
        return Optional.empty();
    }
    
    public List<Account> getAccountsByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY created_date DESC";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching accounts for customer: " + customerId, e);
            throw e;
        }
        return accounts;
    }
    
    public List<Account> getAllAccounts() throws SQLException {
        String sql = "SELECT * FROM accounts ORDER BY created_date DESC";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all accounts", e);
            throw e;
        }
        return accounts;
    }
    
    public boolean updateAccount(Account account) throws SQLException {
        String sql = """
            UPDATE accounts SET account_holder_name = ?, balance = ?, 
            status = ?, last_transaction_date = CURRENT_TIMESTAMP 
            WHERE account_number = ?
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getAccountHolderName());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setString(3, account.getStatus().name());
            pstmt.setString(4, account.getAccountNumber());
            
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            
            if (affectedRows > 0) {
                logger.info("Account updated successfully: " + account.getAccountNumber());
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating account: " + account.getAccountNumber(), e);
            throw e;
        }
    }
    
    public boolean updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = """
            UPDATE accounts SET balance = ?, last_transaction_date = CURRENT_TIMESTAMP 
            WHERE account_number = ?
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating balance for account: " + accountNumber, e);
            throw e;
        }
    }
    
    public boolean deleteAccount(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            
            if (affectedRows > 0) {
                logger.info("Account deleted successfully: " + accountNumber);
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting account: " + accountNumber, e);
            throw e;
        }
    }
    
    public String generateAccountNumber() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM accounts";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt("count");
                return String.format("ACC%010d", count + 1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error generating account number", e);
            throw e;
        }
        return "ACC0000000001";
    }
    
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        String accountType = rs.getString("account_type");
        Account account;
        
        if ("SAVINGS".equals(accountType)) {
            account = new SavingsAccount();
        } else {
            account = new CurrentAccount();
        }
        
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountHolderName(rs.getString("account_holder_name"));
        account.setBalance(rs.getDouble("balance"));
        account.setCustomerId(rs.getInt("customer_id"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        
        Timestamp createdTimestamp = rs.getTimestamp("created_date");
        if (createdTimestamp != null) {
            account.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        return account;
    }
}