import database.DatabaseManager;

import javax.swing.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Smart Banking System - Main Application Class
 * 
 * Features:
 * - Role-based authentication (Customer/Manager)
 * - MySQL database integration with JDBC
 * - Comprehensive GUI with Swing
 * - Real-time transaction processing
 * - Java 8 Streams for data filtering
 * - Optional class for null safety
 * - Multithreading for background services
 * - Exception handling with custom exceptions
 * - File handling for logging
 * 
 * @author Banking System Team
 * @version 1.0
 */
public class BankingApplication {
    private static final Logger logger = Logger.getLogger(BankingApplication.class.getName());
    
    public static void main(String[] args) {
        // Set system properties for better GUI appearance
        System.setProperty("java.awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Configure logging
        configureLogging();
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                setLookAndFeel();
                
                // Initialize database
                logger.info("Initializing database...");
                DatabaseManager.initializeDatabase();
                logger.info("Database initialized successfully");
                
                // Launch modern login window
                logger.info("Starting Banking Application GUI...");
                new gui.ModernLoginFrame().setVisible(true);
                logger.info("Banking Application started successfully");
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to start Banking Application", e);
                showErrorDialog("Failed to start application: " + e.getMessage());
                System.exit(1);
            }
        });
    }
    
    private static void configureLogging() {
        // Configure root logger
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        
        // Add console handler if needed
        java.util.logging.ConsoleHandler consoleHandler = new java.util.logging.ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new java.util.logging.SimpleFormatter());
        
        // Check if console handler already exists
        boolean hasConsoleHandler = false;
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            if (handler instanceof java.util.logging.ConsoleHandler) {
                hasConsoleHandler = true;
                break;
            }
        }
        
        if (!hasConsoleHandler) {
            rootLogger.addHandler(consoleHandler);
        }
    }
    
    private static void setLookAndFeel() {
        try {
            // Use default look and feel - more compatible
            logger.info("Using default Look and Feel");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Look and feel setup failed", e);
        }
    }
    
    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Banking System Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shutdown hook to clean up resources
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Banking Application shutting down...");
            // Add any cleanup code here
            logger.info("Banking Application shutdown complete");
        }));
    }
}