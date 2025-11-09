package gui;

import services.AuthenticationService;
import models.User;
import models.UserRole;
import exceptions.AuthenticationException;
import utils.UIConstants;
import utils.ModernUIComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern, Beautiful Login Interface
 * Features: Gradient background, smooth animations, modern design
 */
public class ModernLoginFrame extends JFrame {
    private AuthenticationService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    
    public ModernLoginFrame() {
        authService = new AuthenticationService();
        initializeModernGUI();
    }
    
    private void initializeModernGUI() {
        setTitle("Smart Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Make window fullscreen/maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Create main container with gradient background
        JPanel mainContainer = createGradientPanel();
        mainContainer.setLayout(new BorderLayout());
        
        // Create login card
        JPanel loginCard = createLoginCard();
        
        // Wrap in scroll pane for better accessibility
        JScrollPane scrollPane = new JScrollPane(loginCard);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Create wrapper panel to center the card with proper margins
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 30, 30, 30);  // Symmetric margins around scroll pane
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        wrapperPanel.add(scrollPane, gbc);
        mainContainer.add(wrapperPanel, BorderLayout.CENTER);
        
        setContentPane(mainContainer);
        
        // Setup event listeners
        setupEventListeners();
        
        // Set focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private JPanel createGradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create beautiful multi-color gradient background
                int w = getWidth();
                int h = getHeight();
                
                // Create radial gradient effect
                Color color1 = new Color(41, 128, 185);  // Bright Blue
                Color color2 = new Color(109, 213, 250); // Sky Blue
                Color color3 = new Color(142, 68, 173);  // Purple
                
                GradientPaint gradient1 = new GradientPaint(
                    0, 0, color2,
                    w, h, color1
                );
                g2d.setPaint(gradient1);
                g2d.fillRect(0, 0, w, h);
                
                // Add overlay gradient for depth
                GradientPaint gradient2 = new GradientPaint(
                    0, 0, new Color(142, 68, 173, 50),
                    w, h, new Color(41, 128, 185, 50)
                );
                g2d.setPaint(gradient2);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }
    
    private JPanel createLoginCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIConstants.CARD_BACKGROUND);
        
        // Add beautiful shadow and rounded border effect with symmetric padding
        int symmetricPadding = 35;  // Symmetric padding on all sides
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 3),
                BorderFactory.createLineBorder(UIConstants.CARD_BACKGROUND, 2)
            ),
            BorderFactory.createEmptyBorder(
                symmetricPadding,  // top
                symmetricPadding,  // left
                symmetricPadding,  // bottom
                symmetricPadding   // right
            )
        ));
        // Set fixed width but let height be determined by content
        card.setPreferredSize(new Dimension(500, 900));
        card.setMaximumSize(new Dimension(500, 2000));
        
        // Logo/Title Section
        card.add(createTitleSection());
        card.add(Box.createVerticalStrut(20));
        
        // Login Form
        card.add(createLoginForm());
        card.add(Box.createVerticalStrut(20));
        
        // Buttons
        card.add(createButtonSection());
        card.add(Box.createVerticalStrut(20));
        
        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(UIConstants.FONT_NORMAL_BOLD);
        statusLabel.setForeground(UIConstants.ERROR_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(20));
        
        // Quick Login Section
        card.add(createQuickLoginSection());
        card.add(Box.createVerticalStrut(20));
        
        // Info Section
        card.add(createInfoSection());
        card.add(Box.createVerticalStrut(10));  // Bottom padding
        
        return card;
    }
    
    private JPanel createTitleSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Bank Icon
        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 64));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Smart Banking");
        titleLabel.setFont(UIConstants.FONT_HUGE);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Secure • Modern • Reliable");
        subtitleLabel.setFont(UIConstants.FONT_SUBTITLE);
        subtitleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_TINY));
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    private JPanel createLoginForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set consistent width for all form elements
        Dimension fieldSize = new Dimension(550, 50);
        
        // Username Field
        JPanel usernamePanel = createFieldPanel(
            UIConstants.ICON_USER + " Username",
            usernameField = ModernUIComponents.createModernTextField("Enter your username")
        );
        usernameField.setPreferredSize(fieldSize);
        usernameField.setMaximumSize(fieldSize);
        usernameField.setMinimumSize(fieldSize);
        
        // Password Field
        JPanel passwordPanel = createFieldPanel(
            UIConstants.ICON_LOCK + " Password",
            passwordField = ModernUIComponents.createModernPasswordField()
        );
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        
        // Add with symmetric spacing
        panel.add(usernamePanel);
        panel.add(Box.createVerticalStrut(25));  // Consistent spacing
        panel.add(passwordPanel);
        
        return panel;
    }
    
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel label = ModernUIComponents.createFieldLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        panel.add(field);
        
        return panel;
    }
    
    private JPanel createButtonSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login Button with vibrant color
        loginButton = ModernUIComponents.createModernButton(
            "🔐 Login to Account",
            new Color(41, 128, 185),  // Vibrant blue
            new Color(52, 152, 219)   // Lighter blue on hover
        );
        loginButton.setPreferredSize(new Dimension(550, 60));
        loginButton.setMaximumSize(new Dimension(550, 60));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 20));
        
        // Register Button with vibrant green
        registerButton = ModernUIComponents.createModernButton(
            "✨ Create New Account",
            new Color(46, 204, 113),  // Vibrant green
            new Color(39, 174, 96)    // Darker green on hover
        );
        registerButton.setPreferredSize(new Dimension(550, 55));
        registerButton.setMaximumSize(new Dimension(550, 55));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_MEDIUM));
        panel.add(registerButton);
        
        return panel;
    }
    
    private JPanel createQuickLoginSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));  // Light gray
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(
                UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM
            )
        ));
        panel.setMaximumSize(new Dimension(550, 130));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel quickLabel = new JLabel("⚡ Quick Access");
        quickLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        quickLabel.setForeground(new Color(52, 73, 94));
        quickLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, UIConstants.SPACING_MEDIUM, 0));
        buttonsPanel.setBackground(new Color(236, 240, 241));
        
        JButton managerBtn = ModernUIComponents.createModernButton(
            "👨‍💼 Manager Login",
            new Color(155, 89, 182),   // Purple
            new Color(142, 68, 173)    // Darker purple
        );
        managerBtn.setPreferredSize(new Dimension(240, 50));
        managerBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 15));
        managerBtn.addActionListener(e -> quickLogin("manager", "manager123"));
        
        JButton customerBtn = ModernUIComponents.createModernButton(
            "👤 Customer Login",
            new Color(26, 188, 156),   // Turquoise
            new Color(22, 160, 133)    // Darker turquoise
        );
        customerBtn.setPreferredSize(new Dimension(240, 50));
        customerBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 15));
        customerBtn.addActionListener(e -> quickLogin("customer", "customer123"));
        
        buttonsPanel.add(managerBtn);
        buttonsPanel.add(customerBtn);
        
        panel.add(quickLabel);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        panel.add(buttonsPanel);
        
        return panel;
    }
    
    private JPanel createInfoSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(254, 242, 220));  // Light orange
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 3),  // Orange border
            BorderFactory.createEmptyBorder(
                UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM,
                UIConstants.PADDING_MEDIUM
            )
        ));
        panel.setMaximumSize(new Dimension(550, 110));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoTitle = new JLabel("💡 Demo Credentials");
        infoTitle.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        infoTitle.setForeground(new Color(211, 84, 0));  // Dark orange
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel managerInfo = new JLabel("Manager: manager / manager123");
        managerInfo.setFont(new Font(UIConstants.FONT_FAMILY_MONO, Font.BOLD, 14));
        managerInfo.setForeground(new Color(52, 73, 94));
        managerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel customerInfo = new JLabel("Customer: customer / customer123");
        customerInfo.setFont(new Font(UIConstants.FONT_FAMILY_MONO, Font.BOLD, 14));
        customerInfo.setForeground(new Color(52, 73, 94));
        customerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(infoTitle);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        panel.add(managerInfo);
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_TINY));
        panel.add(customerInfo);
        
        return panel;
    }
    
    private void setupEventListeners() {
        // Login button action
        loginButton.addActionListener(e -> performLogin());
        
        // Register button action
        registerButton.addActionListener(e -> showRegistrationDialog());
        
        // Enter key navigation
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
    }
    
    private void quickLogin(String username, String password) {
        usernameField.setText(username);
        passwordField.setText(password);
        performLogin();
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", UIConstants.ERROR_COLOR);
            return;
        }
        
        // Disable button and show loading
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        
        // Simulate async login with timer
        Timer loginTimer = new Timer(800, e -> {
            try {
                User user = authService.login(username, password);
                
                loginButton.setText(UIConstants.ICON_SUCCESS + " Success!");
                loginButton.setBackground(UIConstants.SUCCESS_COLOR);
                showStatus("Welcome, " + user.getFullName() + "!", UIConstants.SUCCESS_COLOR);
                
                // Open main window after delay
                Timer openTimer = new Timer(1000, evt -> {
                    SwingUtilities.invokeLater(() -> {
                        if (user.getRole() == UserRole.BANK_MANAGER) {
                            new ManagerMainFrame(authService).setVisible(true);
                        } else {
                            new CustomerMainFrame(authService).setVisible(true);
                        }
                        dispose();
                    });
                });
                openTimer.setRepeats(false);
                openTimer.start();
                
            } catch (AuthenticationException ex) {
                loginButton.setText(UIConstants.ICON_ERROR + " Login Failed");
                loginButton.setBackground(UIConstants.ERROR_COLOR);
                showStatus("Invalid username or password", UIConstants.ERROR_COLOR);
                
                // Reset button after delay
                Timer resetTimer = new Timer(2000, evt -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login to Account");
                    loginButton.setBackground(UIConstants.PRIMARY_COLOR);
                    passwordField.setText("");
                });
                resetTimer.setRepeats(false);
                resetTimer.start();
            }
        });
        loginTimer.setRepeats(false);
        loginTimer.start();
    }
    
    private void showRegistrationDialog() {
        // Create modern registration dialog
        JDialog dialog = new JDialog(this, "Create New Account", true);
        dialog.setSize(600, 800);
        dialog.setLocationRelativeTo(this);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(109, 213, 250),
                    0, getHeight(), new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Registration card with symmetric padding
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIConstants.CARD_BACKGROUND);
        
        int symmetricPadding = 30;  // Same padding on all sides
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 3),
                BorderFactory.createLineBorder(UIConstants.CARD_BACKGROUND, 2)
            ),
            BorderFactory.createEmptyBorder(
                symmetricPadding,  // top
                symmetricPadding,  // left
                symmetricPadding,  // bottom
                symmetricPadding   // right
            )
        ));
        card.setPreferredSize(new Dimension(500, 680));
        card.setMaximumSize(new Dimension(500, 2000));
        
        // Title section
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(UIConstants.FONT_XLARGE);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join Smart Banking Today");
        subtitleLabel.setFont(UIConstants.FONT_SUBTITLE);
        subtitleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(20));
        
        // Form fields with consistent sizing
        Dimension fieldSize = new Dimension(380, 45);
        int fieldSpacing = 15;  // Consistent spacing between fields
        
        JTextField usernameField = ModernUIComponents.createModernTextField("Choose a username");
        usernameField.setPreferredSize(fieldSize);
        usernameField.setMaximumSize(fieldSize);
        usernameField.setMinimumSize(fieldSize);
        
        JPasswordField passwordField = ModernUIComponents.createModernPasswordField();
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        
        JPasswordField confirmPasswordField = ModernUIComponents.createModernPasswordField();
        confirmPasswordField.setPreferredSize(fieldSize);
        confirmPasswordField.setMaximumSize(fieldSize);
        confirmPasswordField.setMinimumSize(fieldSize);
        
        JTextField fullNameField = ModernUIComponents.createModernTextField("Enter your full name");
        fullNameField.setPreferredSize(fieldSize);
        fullNameField.setMaximumSize(fieldSize);
        fullNameField.setMinimumSize(fieldSize);
        
        JTextField emailField = ModernUIComponents.createModernTextField("Enter your email");
        emailField.setPreferredSize(fieldSize);
        emailField.setMaximumSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        
        // Add fields with labels and symmetric spacing
        card.add(createFieldPanel("👤 Username", usernameField));
        card.add(Box.createVerticalStrut(fieldSpacing));
        card.add(createFieldPanel("🔒 Password", passwordField));
        card.add(Box.createVerticalStrut(fieldSpacing));
        card.add(createFieldPanel("🔒 Confirm Password", confirmPasswordField));
        card.add(Box.createVerticalStrut(fieldSpacing));
        card.add(createFieldPanel("👤 Full Name", fullNameField));
        card.add(Box.createVerticalStrut(fieldSpacing));
        card.add(createFieldPanel("📧 Email Address", emailField));
        card.add(Box.createVerticalStrut(20));
        
        // Buttons with symmetric layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(380, 60));
        
        Dimension buttonSize = new Dimension(180, 50);
        
        JButton cancelBtn = ModernUIComponents.createDangerButton("✗ Cancel");
        cancelBtn.setPreferredSize(buttonSize);
        cancelBtn.setMaximumSize(buttonSize);
        cancelBtn.setMinimumSize(buttonSize);
        cancelBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        
        JButton registerBtn = ModernUIComponents.createSuccessButton("✓ Create Account");
        registerBtn.setPreferredSize(buttonSize);
        registerBtn.setMaximumSize(buttonSize);
        registerBtn.setMinimumSize(buttonSize);
        registerBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));  // Symmetric gap
        buttonPanel.add(registerBtn);
        
        card.add(buttonPanel);
        
        // Event handlers
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                ModernUIComponents.showErrorMessage(dialog, "Please fill all fields");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                ModernUIComponents.showErrorMessage(dialog, "Passwords do not match");
                return;
            }
            
            if (password.length() < 6) {
                ModernUIComponents.showErrorMessage(dialog, "Password must be at least 6 characters");
                return;
            }
            
            boolean success = authService.registerCustomer(username, password, fullName, email);
            if (success) {
                ModernUIComponents.showSuccessMessage(dialog, "Account created successfully! You can now login.");
                dialog.dispose();
            } else {
                ModernUIComponents.showErrorMessage(dialog, "Registration failed. Username may already exist.");
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        // Wrap card in scroll pane
        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Create wrapper panel with proper margins
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));  // Top, Left, Bottom, Right margins
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(wrapperPanel, BorderLayout.CENTER);
        
        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Clear after 5 seconds
        Timer timer = new Timer(5000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new ModernLoginFrame().setVisible(true);
        });
    }
}