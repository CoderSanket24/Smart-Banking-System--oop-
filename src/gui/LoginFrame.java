package gui;

import services.AuthenticationService;
import models.User;
import models.UserRole;
import exceptions.AuthenticationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginFrame extends JFrame {
    private AuthenticationService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    
    public LoginFrame() {
        authService = new AuthenticationService();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Smart Banking System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);  // Even bigger window
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Create main panel with proper layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Title section
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 248, 255));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Smart Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Secure • Modern • Reliable");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subtitleLabel);
        
        // Login form panel with simple layout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                "Login to Your Account", 
                0, 0, 
                new Font("Arial", Font.BOLD, 18)
            ),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(500, 300));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username section
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(new Color(50, 50, 50));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(400, 45));
        usernameField.setMaximumSize(new Dimension(400, 45));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernamePanel.add(usernameLabel);
        usernamePanel.add(Box.createVerticalStrut(8));
        usernamePanel.add(usernameField);
        
        // Password section
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(new Color(50, 50, 50));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(400, 45));
        passwordField.setMaximumSize(new Dimension(400, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createVerticalStrut(8));
        passwordPanel.add(passwordField);
        
        // Add form components
        formPanel.add(usernamePanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Main login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.setMaximumSize(new Dimension(200, 50));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Register button
        registerButton = new JButton("Register New Customer");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setPreferredSize(new Dimension(200, 40));
        registerButton.setMaximumSize(new Dimension(200, 40));
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(registerButton);
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Quick login section
        JPanel quickLoginPanel = createQuickLoginPanel();
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
                "Default Credentials", 
                0, 0, 
                new Font("Arial", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        infoPanel.setBackground(new Color(248, 248, 255));
        infoPanel.setMaximumSize(new Dimension(500, 120));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel managerInfo = new JLabel("Manager: username = manager, password = manager123");
        managerInfo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        managerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel customerInfo = new JLabel("Customer: username = customer, password = customer123");
        customerInfo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        customerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(managerInfo);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(customerInfo);
        
        // Add all components to main panel
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(quickLoginPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(infoPanel);
        
        // Add main panel to frame
        add(new JScrollPane(mainPanel));
        
        add(new JScrollPane(mainPanel));
        
        // Setup event listeners
        setupEventListeners();
        
        // Set focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private void setupEventListeners() {
        // Event listeners
        loginButton.addActionListener(new LoginActionListener());
        registerButton.addActionListener(new RegisterActionListener());
        
        // Enhanced keyboard interactions
        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Add hover effects for buttons
        addButtonHoverEffects(loginButton, new Color(0, 102, 204), new Color(0, 82, 164));
        addButtonHoverEffects(registerButton, new Color(34, 139, 34), new Color(24, 119, 24));
    }
    
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performLogin();
        }
    }
    
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showRegistrationDialog();
        }
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", Color.RED);
            return;
        }
        
        try {
            loginButton.setEnabled(false);
            
            // Add loading animation
            Timer loadingTimer = new Timer(150, null);
            final int[] dots = {0};
            
            ActionListener loadingAction = e -> {
                dots[0] = (dots[0] % 3) + 1;
                loginButton.setText("Logging in" + ".".repeat(dots[0]));
            };
            
            loadingTimer.addActionListener(loadingAction);
            loadingTimer.start();
            
            // Simulate some processing time for better UX
            Timer loginTimer = new Timer(800, e -> {
                loadingTimer.stop();
                
                try {
                    User user = authService.login(username, password);
                    
                    loginButton.setText("Success!");
                    loginButton.setBackground(new Color(34, 139, 34));
                    showStatus("Login successful! Welcome " + user.getFullName(), new Color(0, 128, 0));
                    
                    // Fade out effect before opening main window
                    Timer fadeTimer = new Timer(1000, evt -> {
                        SwingUtilities.invokeLater(() -> {
                            if (user.getRole() == UserRole.BANK_MANAGER) {
                                new ManagerMainFrame(authService).setVisible(true);
                            } else {
                                new CustomerMainFrame(authService).setVisible(true);
                            }
                            dispose();
                        });
                    });
                    fadeTimer.setRepeats(false);
                    fadeTimer.start();
                    
                } catch (AuthenticationException ex) {
                    loginButton.setText("Failed!");
                    loginButton.setBackground(new Color(220, 20, 60));
                    showStatus("Login failed: " + ex.getMessage(), Color.RED);
                    
                    // Reset button after 2 seconds
                    Timer resetTimer = new Timer(2000, evt -> {
                        loginButton.setEnabled(true);
                        loginButton.setText("Login");
                        loginButton.setBackground(new Color(0, 102, 204));
                        passwordField.setText("");
                    });
                    resetTimer.setRepeats(false);
                    resetTimer.start();
                }
            });
            loginTimer.setRepeats(false);
            loginTimer.start();
            
        } catch (Exception ex) {
            showStatus("System error: " + ex.getMessage(), Color.RED);
            loginButton.setEnabled(true);
            loginButton.setText("Login");
            passwordField.setText("");
        }
    }
    
    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register New Customer", true);
        dialog.setSize(550, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(248, 248, 255));
        
        // Title
        JLabel titleLabel = new JLabel("Create New Customer Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(450, 350));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create input fields with consistent styling
        JTextField regUsernameField = createStyledTextField("Username");
        JPasswordField regPasswordField = createStyledPasswordField("Password");
        JPasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        JTextField fullNameField = createStyledTextField("Full Name");
        JTextField emailField = createStyledTextField("Email Address");
        
        // Add form fields
        formPanel.add(createFieldPanel("Username", regUsernameField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldPanel("Password", regPasswordField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldPanel("Confirm Password", confirmPasswordField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldPanel("Full Name", fullNameField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createFieldPanel("Email Address", emailField));
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(248, 248, 255));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton registerBtn = new JButton("Create Account");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 16));
        registerBtn.setPreferredSize(new Dimension(150, 45));
        registerBtn.setMaximumSize(new Dimension(150, 45));
        registerBtn.setBackground(new Color(34, 139, 34));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
        cancelBtn.setPreferredSize(new Dimension(150, 45));
        cancelBtn.setMaximumSize(new Dimension(150, 45));
        cancelBtn.setBackground(new Color(220, 20, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(registerBtn);
        buttonPanel.add(Box.createHorizontalGlue());
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel);
        
        registerBtn.addActionListener(e -> {
            String regUsername = regUsernameField.getText().trim();
            String regPassword = new String(regPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            
            if (regUsername.isEmpty() || regPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!regPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (regPassword.length() < 6) {
                JOptionPane.showMessageDialog(dialog, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = authService.registerCustomer(regUsername, regPassword, fullName, email);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Registration failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Clear status after 5 seconds
        Timer timer = new Timer(5000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    private void addButtonHoverEffects(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    private JPanel createQuickLoginPanel() {
        JPanel quickLoginPanel = new JPanel();
        quickLoginPanel.setLayout(new BoxLayout(quickLoginPanel, BoxLayout.X_AXIS));
        quickLoginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
                "Quick Login", 
                0, 0, 
                new Font("Arial", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        quickLoginPanel.setBackground(new Color(248, 248, 255));
        quickLoginPanel.setMaximumSize(new Dimension(500, 80));
        quickLoginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton managerQuickBtn = new JButton("Login as Manager");
        managerQuickBtn.setFont(new Font("Arial", Font.BOLD, 14));
        managerQuickBtn.setPreferredSize(new Dimension(180, 40));
        managerQuickBtn.setMaximumSize(new Dimension(180, 40));
        managerQuickBtn.setBackground(new Color(70, 130, 180));
        managerQuickBtn.setForeground(Color.WHITE);
        managerQuickBtn.setFocusPainted(false);
        managerQuickBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JButton customerQuickBtn = new JButton("Login as Customer");
        customerQuickBtn.setFont(new Font("Arial", Font.BOLD, 14));
        customerQuickBtn.setPreferredSize(new Dimension(180, 40));
        customerQuickBtn.setMaximumSize(new Dimension(180, 40));
        customerQuickBtn.setBackground(new Color(60, 179, 113));
        customerQuickBtn.setForeground(Color.WHITE);
        customerQuickBtn.setFocusPainted(false);
        customerQuickBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Quick login actions
        managerQuickBtn.addActionListener(e -> {
            usernameField.setText("manager");
            passwordField.setText("manager123");
            performLogin();
        });
        
        customerQuickBtn.addActionListener(e -> {
            usernameField.setText("customer");
            passwordField.setText("customer123");
            performLogin();
        });
        
        addButtonHoverEffects(managerQuickBtn, new Color(70, 130, 180), new Color(50, 110, 160));
        addButtonHoverEffects(customerQuickBtn, new Color(60, 179, 113), new Color(40, 159, 93));
        
        quickLoginPanel.add(Box.createHorizontalGlue());
        quickLoginPanel.add(managerQuickBtn);
        quickLoginPanel.add(Box.createHorizontalStrut(20));
        quickLoginPanel.add(customerQuickBtn);
        quickLoginPanel.add(Box.createHorizontalGlue());
        
        return quickLoginPanel;
    }
    
    private void addLoadingAnimation() {
        // Simple loading animation
        Timer timer = new Timer(100, null);
        final int[] dots = {0};
        
        ActionListener loadingAction = e -> {
            dots[0] = (dots[0] + 1) % 4;
            String dotString = ".".repeat(dots[0]);
            loginButton.setText("Logging in" + dotString);
        };
        
        timer.addActionListener(loadingAction);
        timer.start();
        
        // Stop after 3 seconds max
        Timer stopTimer = new Timer(3000, e -> {
            timer.stop();
            loginButton.setText("Login");
        });
        stopTimer.setRepeats(false);
        stopTimer.start();
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 35));
        field.setMaximumSize(new Dimension(350, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 35));
        field.setMaximumSize(new Dimension(350, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Use default look and feel
            
            new LoginFrame().setVisible(true);
        });
    }
}