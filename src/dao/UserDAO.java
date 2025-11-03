package dao;

import database.DatabaseManager;
import models.User;
import models.UserRole;
import exceptions.AuthenticationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());
    
    public Optional<User> authenticate(String username, String password) throws AuthenticationException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    updateLastLogin(user.getUserId());
                    conn.commit();
                    logger.info("User authenticated successfully: " + username);
                    return Optional.of(user);
                } else {
                    logger.warning("Authentication failed for username: " + username);
                    throw new AuthenticationException("Invalid username or password");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during authentication", e);
            throw new AuthenticationException("Authentication failed due to system error", e);
        }
    }
    
    public boolean createUser(User user) throws SQLException {
        String sql = """
            INSERT INTO users (username, password, full_name, email, role, is_active) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole().name());
            pstmt.setBoolean(6, user.isActive());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    }
                }
                conn.commit();
                logger.info("User created successfully: " + user.getUsername());
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating user: " + user.getUsername(), e);
            throw e;
        }
    }
    
    public Optional<User> getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching user by ID: " + userId, e);
            throw e;
        }
        return Optional.empty();
    }
    
    public Optional<User> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching user by username: " + username, e);
            throw e;
        }
        return Optional.empty();
    }
    
    public List<User> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM users WHERE role = 'CUSTOMER' ORDER BY full_name";
        List<User> customers = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all customers", e);
            throw e;
        }
        return customers;
    }
    
    public boolean updateUser(User user) throws SQLException {
        String sql = """
            UPDATE users SET username = ?, password = ?, full_name = ?, 
            email = ?, role = ?, is_active = ? WHERE user_id = ?
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole().name());
            pstmt.setBoolean(6, user.isActive());
            pstmt.setInt(7, user.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            
            if (affectedRows > 0) {
                logger.info("User updated successfully: " + user.getUsername());
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user: " + user.getUsername(), e);
            throw e;
        }
    }
    
    private void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdTimestamp = rs.getTimestamp("created_date");
        if (createdTimestamp != null) {
            user.setCreatedDate(createdTimestamp.toLocalDateTime());
        }
        
        return user;
    }
}