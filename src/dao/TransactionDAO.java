package dao;

import database.DatabaseManager;
import models.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TransactionDAO {
    private static final Logger logger = Logger.getLogger(TransactionDAO.class.getName());
    
    public boolean saveTransaction(Transaction transaction) throws SQLException {
        String sql = """
            INSERT INTO transactions (transaction_id, account_number, transaction_type, 
            amount, balance_after, description, reference_number, created_by) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transaction.getTransactionId());
            pstmt.setString(2, transaction.getAccountNumber());
            pstmt.setString(3, transaction.getType());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setDouble(5, transaction.getBalanceAfter());
            pstmt.setString(6, transaction.getDescription());
            pstmt.setString(7, transaction.getReferenceNumber());
            pstmt.setObject(8, null); // created_by - can be set later if needed
            
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            
            if (affectedRows > 0) {
                logger.info("Transaction saved successfully: " + transaction.getTransactionId());
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving transaction: " + transaction.getTransactionId(), e);
            throw e;
        }
    }
    
    public List<Transaction> getTransactionsByAccount(String accountNumber) throws SQLException {
        String sql = """
            SELECT * FROM transactions WHERE account_number = ? 
            ORDER BY timestamp DESC
        """;
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching transactions for account: " + accountNumber, e);
            throw e;
        }
        return transactions;
    }
    
    public List<Transaction> getTransactionsByCustomer(int customerId) throws SQLException {
        String sql = """
            SELECT t.* FROM transactions t 
            JOIN accounts a ON t.account_number = a.account_number 
            WHERE a.customer_id = ? 
            ORDER BY t.timestamp DESC
        """;
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching transactions for customer: " + customerId, e);
            throw e;
        }
        return transactions;
    }
    
    public List<Transaction> getAllTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all transactions", e);
            throw e;
        }
        return transactions;
    }
    
    public List<Transaction> getHighValueTransactions(double threshold) throws SQLException {
        String sql = """
            SELECT * FROM transactions WHERE amount > ? 
            ORDER BY amount DESC, timestamp DESC
        """;
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, threshold);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching high value transactions", e);
            throw e;
        }
        return transactions;
    }
    
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = """
            SELECT * FROM transactions 
            WHERE timestamp BETWEEN ? AND ? 
            ORDER BY timestamp DESC
        """;
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching transactions by date range", e);
            throw e;
        }
        return transactions;
    }
    
    public double getTotalTransactionAmount(String accountNumber, String transactionType) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(amount), 0) as total 
            FROM transactions 
            WHERE account_number = ? AND transaction_type = ?
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, transactionType);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error calculating total transaction amount", e);
            throw e;
        }
        return 0.0;
    }
    
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getString("transaction_id"));
        transaction.setAccountNumber(rs.getString("account_number"));
        transaction.setType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setBalanceAfter(rs.getDouble("balance_after"));
        transaction.setDescription(rs.getString("description"));
        transaction.setReferenceNumber(rs.getString("reference_number"));
        
        Timestamp timestamp = rs.getTimestamp("timestamp");
        if (timestamp != null) {
            transaction.setTimestamp(timestamp.toLocalDateTime());
        }
        
        return transaction;
    }
}