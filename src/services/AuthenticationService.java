package services;

import dao.UserDAO;
import models.User;
import models.UserRole;
import exceptions.AuthenticationException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AuthenticationService {
    private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());
    private final UserDAO userDAO;
    private User currentUser;
    
    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }
    
    public User login(String username, String password) throws AuthenticationException {
        try {
            Optional<User> userOpt = userDAO.authenticate(username, password);
            if (userOpt.isPresent()) {
                currentUser = userOpt.get();
                logger.info("User logged in successfully: " + username + " (" + currentUser.getRole() + ")");
                return currentUser;
            } else {
                throw new AuthenticationException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            logger.warning("Login failed for username: " + username);
            throw e;
        }
    }
    
    public void logout() {
        if (currentUser != null) {
            logger.info("User logged out: " + currentUser.getUsername());
            currentUser = null;
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isManager() {
        return currentUser != null && currentUser.getRole() == UserRole.BANK_MANAGER;
    }
    
    public boolean isCustomer() {
        return currentUser != null && currentUser.getRole() == UserRole.CUSTOMER;
    }
    
    public boolean registerCustomer(String username, String password, String fullName, String email) {
        try {
            // Check if username already exists
            Optional<User> existingUser = userDAO.getUserByUsername(username);
            if (existingUser.isPresent()) {
                logger.warning("Registration failed - username already exists: " + username);
                return false;
            }
            
            User newUser = new User(username, password, fullName, email, UserRole.CUSTOMER);
            boolean created = userDAO.createUser(newUser);
            
            if (created) {
                logger.info("New customer registered successfully: " + username);
            }
            return created;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error during customer registration", e);
            return false;
        }
    }
    
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        
        if (!currentUser.getPassword().equals(oldPassword)) {
            logger.warning("Password change failed - incorrect old password for user: " + currentUser.getUsername());
            return false;
        }
        
        try {
            currentUser.setPassword(newPassword);
            boolean updated = userDAO.updateUser(currentUser);
            
            if (updated) {
                logger.info("Password changed successfully for user: " + currentUser.getUsername());
            }
            return updated;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error changing password for user: " + currentUser.getUsername(), e);
            return false;
        }
    }
    
    public boolean hasPermission(String action) {
        if (currentUser == null) {
            return false;
        }
        
        // Define permissions based on roles
        switch (currentUser.getRole()) {
            case BANK_MANAGER:
                return true; // Managers have all permissions
                
            case CUSTOMER:
                // Customers can only perform certain actions
                return action.equals("VIEW_OWN_ACCOUNTS") ||
                       action.equals("VIEW_OWN_TRANSACTIONS") ||
                       action.equals("DEPOSIT") ||
                       action.equals("WITHDRAW") ||
                       action.equals("TRANSFER") ||
                       action.equals("CHANGE_PASSWORD");
                       
            default:
                return false;
        }
    }
}