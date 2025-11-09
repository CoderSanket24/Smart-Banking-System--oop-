package gui;

import services.AuthenticationService;
import services.BankingService;
import dao.UserDAO;
import models.*;
import exceptions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ManagerMainFrame extends JFrame {
    private AuthenticationService authService;
    private BankingService bankingService;
    private UserDAO userDAO;
    private User currentUser;
    
    private JTabbedPane tabbedPane;
    private JTable accountTable;
    private JTable transactionTable;
    private JTable customerTable;
    private DefaultTableModel accountTableModel;
    private DefaultTableModel transactionTableModel;
    private DefaultTableModel customerTableModel;
    
    public ManagerMainFrame(AuthenticationService authService) {
        this.authService = authService;
        this.bankingService = new BankingService();
        this.userDAO = new UserDAO();
        this.currentUser = authService.getCurrentUser();
        
        initializeGUI();
        loadManagerData();
    }
    
    private void initializeGUI() {
        setTitle("Smart Banking System - Manager Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Make window fullscreen/maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Set minimum size
        setMinimumSize(new Dimension(1200, 700));
        
        // Set modern background color
        getContentPane().setBackground(new Color(240, 242, 245));
        
        // Create menu bar
        createMenuBar();
        
        // Create modern tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);
        
        // Add tabs with icons
        tabbedPane.addTab("💳 All Accounts", createAccountsPanel());
        tabbedPane.addTab("📊 All Transactions", createTransactionsPanel());
        tabbedPane.addTab("👥 Customer Management", createCustomersPanel());
        tabbedPane.addTab("📈 Reports", createReportsPanel());
        tabbedPane.addTab("⚙️ System Admin", createAdminPanel());
        
        add(tabbedPane);
        
        // Welcome message
        showWelcomeMessage();
        
        // Set up auto-refresh timer with visual feedback
        setupAutoRefresh();
        
        // Add interactive features
        setupInteractiveFeatures();
    }
    
    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(220, 50));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem refreshItem = new JMenuItem("Refresh All Data");
        JMenuItem exportItem = new JMenuItem("Export Reports");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        refreshItem.addActionListener(e -> refreshData());
        exportItem.addActionListener(e -> exportReports());
        exitItem.addActionListener(e -> logout());
        
        fileMenu.add(refreshItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Account menu
        JMenu accountMenu = new JMenu("Accounts");
        JMenuItem createAccountItem = new JMenuItem("Create Account");
        JMenuItem closeAccountItem = new JMenuItem("Close Account");
        JMenuItem freezeAccountItem = new JMenuItem("Freeze Account");
        
        createAccountItem.addActionListener(e -> createAccountForCustomer());
        closeAccountItem.addActionListener(e -> closeAccount());
        freezeAccountItem.addActionListener(e -> freezeAccount());
        
        accountMenu.add(createAccountItem);
        accountMenu.add(closeAccountItem);
        accountMenu.add(freezeAccountItem);
        
        // System menu
        JMenu systemMenu = new JMenu("System");
        JMenuItem creditInterestItem = new JMenuItem("Credit Interest to All Accounts");
        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        JMenuItem logoutItem = new JMenuItem("Logout");
        
        creditInterestItem.addActionListener(e -> creditInterestToAll());
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        logoutItem.addActionListener(e -> logout());
        
        systemMenu.add(creditInterestItem);
        systemMenu.addSeparator();
        systemMenu.add(changePasswordItem);
        systemMenu.add(logoutItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(accountMenu);
        menuBar.add(systemMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Account table
        String[] columnNames = {"Account Number", "Account Holder", "Type", "Balance", "Customer ID", "Status", "Created Date"};
        accountTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountTable = new JTable(accountTableModel);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountTable.setRowHeight(35);
        accountTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        accountTable.getTableHeader().setBackground(new Color(41, 128, 185));
        accountTable.getTableHeader().setForeground(Color.WHITE);
        accountTable.setSelectionBackground(new Color(52, 152, 219));
        accountTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton createAccountButton = createModernButton("➕ Create Account", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton viewTransactionsButton = createModernButton("📋 View Transactions", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton depositButton = createModernButton("💰 Manual Deposit", new Color(26, 188, 156), new Color(22, 160, 133));
        JButton withdrawButton = createModernButton("💵 Manual Withdrawal", new Color(231, 76, 60), new Color(192, 57, 43));
        JButton closeAccountButton = createModernButton("🔒 Close Account", new Color(149, 165, 166), new Color(127, 140, 141));
        JButton refreshButton = createModernButton("🔄 Refresh", new Color(155, 89, 182), new Color(142, 68, 173));
        
        createAccountButton.addActionListener(e -> createAccountForCustomer());
        viewTransactionsButton.addActionListener(e -> viewAccountTransactions());
        depositButton.addActionListener(e -> performManualDeposit());
        withdrawButton.addActionListener(e -> performManualWithdrawal());
        closeAccountButton.addActionListener(e -> closeAccount());
        refreshButton.addActionListener(e -> refreshAccountData());
        
        buttonPanel.add(createAccountButton);
        buttonPanel.add(viewTransactionsButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(closeAccountButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Transaction table
        String[] columnNames = {"Transaction ID", "Account", "Type", "Amount", "Balance After", "Date", "Description"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transactionTable.setRowHeight(35);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        transactionTable.getTableHeader().setBackground(new Color(41, 128, 185));
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.setSelectionBackground(new Color(52, 152, 219));
        transactionTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        // Filter panel with modern styling
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        filterPanel.setBackground(new Color(240, 242, 245));
        
        JTextField thresholdField = new JTextField("10000", 10);
        thresholdField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        thresholdField.setPreferredSize(new Dimension(120, 40));
        
        JButton filterHighValueButton = createModernButton("💎 Filter High Value", new Color(230, 126, 34), new Color(211, 84, 0));
        JButton filterByTypeButton = createModernButton("🔍 Filter by Type", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton showAllButton = createModernButton("📋 Show All", new Color(155, 89, 182), new Color(142, 68, 173));
        
        filterHighValueButton.addActionListener(e -> filterHighValueTransactions(thresholdField));
        filterByTypeButton.addActionListener(e -> filterTransactionsByType());
        showAllButton.addActionListener(e -> refreshTransactionData());
        
        JLabel thresholdLabel = new JLabel("💰 Amount Threshold:");
        thresholdLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        filterPanel.add(thresholdLabel);
        filterPanel.add(thresholdField);
        filterPanel.add(filterHighValueButton);
        filterPanel.add(filterByTypeButton);
        filterPanel.add(showAllButton);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Customer table
        String[] columnNames = {"User ID", "Username", "Full Name", "Email", "Status", "Created Date", "Last Login"};
        customerTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(customerTableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerTable.setRowHeight(35);
        customerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        customerTable.getTableHeader().setBackground(new Color(41, 128, 185));
        customerTable.getTableHeader().setForeground(Color.WHITE);
        customerTable.setSelectionBackground(new Color(52, 152, 219));
        customerTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton viewAccountsButton = createModernButton("👁️ View Customer Accounts", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton createAccountButton = createModernButton("➕ Create Account", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton activateButton = createModernButton("✅ Activate Customer", new Color(26, 188, 156), new Color(22, 160, 133));
        JButton deactivateButton = createModernButton("❌ Deactivate Customer", new Color(231, 76, 60), new Color(192, 57, 43));
        JButton refreshButton = createModernButton("🔄 Refresh", new Color(155, 89, 182), new Color(142, 68, 173));
        
        viewAccountsButton.addActionListener(e -> viewCustomerAccounts());
        createAccountButton.addActionListener(e -> createAccountForSelectedCustomer());
        activateButton.addActionListener(e -> toggleCustomerStatus(true));
        deactivateButton.addActionListener(e -> toggleCustomerStatus(false));
        refreshButton.addActionListener(e -> refreshCustomerData());
        
        buttonPanel.add(viewAccountsButton);
        buttonPanel.add(createAccountButton);
        buttonPanel.add(activateButton);
        buttonPanel.add(deactivateButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextArea reportArea = new JTextArea(25, 60);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        reportArea.setBackground(new Color(236, 240, 241));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton summaryButton = createModernButton("📊 Bank Summary", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton highValueButton = createModernButton("💎 High Value Transactions", new Color(230, 126, 34), new Color(211, 84, 0));
        JButton customerReportButton = createModernButton("👥 Customer Report", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton accountReportButton = createModernButton("💳 Account Report", new Color(26, 188, 156), new Color(22, 160, 133));
        JButton transactionReportButton = createModernButton("📈 Transaction Report", new Color(155, 89, 182), new Color(142, 68, 173));
        JButton clearButton = createModernButton("🗑️ Clear Report", new Color(149, 165, 166), new Color(127, 140, 141));
        
        summaryButton.setPreferredSize(new Dimension(250, 60));
        highValueButton.setPreferredSize(new Dimension(250, 60));
        customerReportButton.setPreferredSize(new Dimension(250, 60));
        accountReportButton.setPreferredSize(new Dimension(250, 60));
        transactionReportButton.setPreferredSize(new Dimension(250, 60));
        clearButton.setPreferredSize(new Dimension(250, 60));
        
        summaryButton.addActionListener(e -> generateBankSummary(reportArea));
        highValueButton.addActionListener(e -> generateHighValueReport(reportArea));
        customerReportButton.addActionListener(e -> generateCustomerReport(reportArea));
        accountReportButton.addActionListener(e -> generateAccountReport(reportArea));
        transactionReportButton.addActionListener(e -> generateTransactionReport(reportArea));
        clearButton.addActionListener(e -> reportArea.setText(""));
        
        buttonPanel.add(summaryButton);
        buttonPanel.add(highValueButton);
        buttonPanel.add(customerReportButton);
        buttonPanel.add(accountReportButton);
        buttonPanel.add(transactionReportButton);
        buttonPanel.add(clearButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        JButton creditInterestButton = createModernButton("💰 Credit Interest to All Accounts", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton backupDataButton = createModernButton("💾 Backup Database", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton systemStatsButton = createModernButton("📊 System Statistics", new Color(230, 126, 34), new Color(211, 84, 0));
        JButton auditLogButton = createModernButton("📋 View Audit Log", new Color(155, 89, 182), new Color(142, 68, 173));
        
        creditInterestButton.setPreferredSize(new Dimension(350, 65));
        backupDataButton.setPreferredSize(new Dimension(350, 65));
        systemStatsButton.setPreferredSize(new Dimension(350, 65));
        auditLogButton.setPreferredSize(new Dimension(350, 65));
        
        creditInterestButton.addActionListener(e -> creditInterestToAll());
        backupDataButton.addActionListener(e -> backupDatabase());
        systemStatsButton.addActionListener(e -> showSystemStatistics());
        auditLogButton.addActionListener(e -> showAuditLog());
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(creditInterestButton, gbc);
        
        gbc.gridy = 1;
        panel.add(backupDataButton, gbc);
        
        gbc.gridy = 2;
        panel.add(systemStatsButton, gbc);
        
        gbc.gridy = 3;
        panel.add(auditLogButton, gbc);
        
        return panel;
    }    
    
private void loadManagerData() {
        refreshAccountData();
        refreshTransactionData();
        refreshCustomerData();
    }
    
    private void refreshData() {
        refreshAccountData();
        refreshTransactionData();
        refreshCustomerData();
    }
    
    private void refreshAccountData() {
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            
            accountTableModel.setRowCount(0);
            for (Account account : accounts) {
                accountTableModel.addRow(new Object[]{
                    account.getAccountNumber(),
                    account.getAccountHolderName(),
                    account.getAccountType(),
                    String.format("₹%.2f", account.getBalance()),
                    account.getCustomerId(),
                    account.getStatus(),
                    account.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                });
            }
        } catch (SQLException e) {
            showError("Error loading account data: " + e.getMessage());
        }
    }
    
    private void refreshTransactionData() {
        try {
            List<Transaction> transactions = bankingService.getAllTransactions();
            
            transactionTableModel.setRowCount(0);
            for (Transaction transaction : transactions) {
                transactionTableModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    transaction.getAccountNumber(),
                    transaction.getType(),
                    String.format("₹%.2f", transaction.getAmount()),
                    String.format("₹%.2f", transaction.getBalanceAfter()),
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    transaction.getDescription()
                });
            }
        } catch (SQLException e) {
            showError("Error loading transaction data: " + e.getMessage());
        }
    }
    
    private void refreshCustomerData() {
        try {
            List<User> customers = userDAO.getAllCustomers();
            
            customerTableModel.setRowCount(0);
            for (User customer : customers) {
                customerTableModel.addRow(new Object[]{
                    customer.getUserId(),
                    customer.getUsername(),
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.isActive() ? "Active" : "Inactive",
                    customer.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    "N/A" // Last login - would need to be tracked separately
                });
            }
        } catch (SQLException e) {
            showError("Error loading customer data: " + e.getMessage());
        }
    }
    
    private void createAccountForCustomer() {
        // Get customer ID
        String customerIdStr = JOptionPane.showInputDialog(this, "Enter Customer ID:");
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            return;
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr.trim());
            
            // Verify customer exists
            Optional<User> customerOpt = userDAO.getUserById(customerId);
            if (!customerOpt.isPresent()) {
                showError("Customer not found with ID: " + customerId);
                return;
            }
            
            User customer = customerOpt.get();
            
            // Get account details
            String[] accountTypes = {"SAVINGS", "CURRENT"};
            String selectedType = (String) JOptionPane.showInputDialog(
                this, "Select account type for " + customer.getFullName() + ":",
                "Account Type", JOptionPane.QUESTION_MESSAGE, null, accountTypes, accountTypes[0]);
            
            if (selectedType != null) {
                String initialBalanceStr = JOptionPane.showInputDialog(this, "Enter initial balance:");
                
                if (initialBalanceStr != null && !initialBalanceStr.trim().isEmpty()) {
                    double initialBalance = Double.parseDouble(initialBalanceStr.trim());
                    
                    String accountNumber = bankingService.createAccount(
                        customer.getFullName(), selectedType, initialBalance, customerId);
                    
                    showSuccess("Account created successfully!\nAccount Number: " + accountNumber +
                              "\nCustomer: " + customer.getFullName());
                    refreshAccountData();
                }
            }
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers");
        } catch (SQLException e) {
            showError("Account creation failed: " + e.getMessage());
        }
    }
    
    private void createAccountForSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a customer");
            return;
        }
        
        int customerId = (Integer) customerTableModel.getValueAt(selectedRow, 0);
        String customerName = (String) customerTableModel.getValueAt(selectedRow, 2);
        
        // Get account details
        String[] accountTypes = {"SAVINGS", "CURRENT"};
        String selectedType = (String) JOptionPane.showInputDialog(
            this, "Select account type for " + customerName + ":",
            "Account Type", JOptionPane.QUESTION_MESSAGE, null, accountTypes, accountTypes[0]);
        
        if (selectedType != null) {
            String initialBalanceStr = JOptionPane.showInputDialog(this, "Enter initial balance:");
            
            if (initialBalanceStr != null && !initialBalanceStr.trim().isEmpty()) {
                try {
                    double initialBalance = Double.parseDouble(initialBalanceStr.trim());
                    
                    String accountNumber = bankingService.createAccount(
                        customerName, selectedType, initialBalance, customerId);
                    
                    showSuccess("Account created successfully!\nAccount Number: " + accountNumber +
                              "\nCustomer: " + customerName);
                    refreshAccountData();
                    
                } catch (NumberFormatException e) {
                    showError("Please enter a valid amount");
                } catch (SQLException e) {
                    showError("Account creation failed: " + e.getMessage());
                }
            }
        }
    }
    
    private void performManualDeposit() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an account");
            return;
        }
        
        String accountNumber = (String) accountTableModel.getValueAt(selectedRow, 0);
        String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr.trim());
                String description = JOptionPane.showInputDialog(this, "Enter description:", "Manager deposit");
                
                bankingService.deposit(accountNumber, amount, description);
                showSuccess("Deposit successful!");
                refreshAccountData();
                refreshTransactionData();
                
            } catch (NumberFormatException e) {
                showError("Please enter a valid amount");
            } catch (AccountNotFoundException | InsufficientFundsException | SQLException e) {
                showError("Deposit failed: " + e.getMessage());
            }
        }
    }
    
    private void performManualWithdrawal() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an account");
            return;
        }
        
        String accountNumber = (String) accountTableModel.getValueAt(selectedRow, 0);
        String amountStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr.trim());
                String description = JOptionPane.showInputDialog(this, "Enter description:", "Manager withdrawal");
                
                bankingService.withdraw(accountNumber, amount, description);
                showSuccess("Withdrawal successful!");
                refreshAccountData();
                refreshTransactionData();
                
            } catch (NumberFormatException e) {
                showError("Please enter a valid amount");
            } catch (AccountNotFoundException | InsufficientFundsException | SQLException e) {
                showError("Withdrawal failed: " + e.getMessage());
            }
        }
    }
    
    private void viewAccountTransactions() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an account");
            return;
        }
        
        String accountNumber = (String) accountTableModel.getValueAt(selectedRow, 0);
        
        try {
            List<Transaction> transactions = bankingService.getTransactionHistory(accountNumber);
            
            // Switch to transactions tab and filter by account
            tabbedPane.setSelectedIndex(1);
            
            transactionTableModel.setRowCount(0);
            for (Transaction transaction : transactions) {
                transactionTableModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    transaction.getAccountNumber(),
                    transaction.getType(),
                    String.format("₹%.2f", transaction.getAmount()),
                    String.format("₹%.2f", transaction.getBalanceAfter()),
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    transaction.getDescription()
                });
            }
            
        } catch (SQLException e) {
            showError("Error loading transactions: " + e.getMessage());
        }
    }
    
    private void viewCustomerAccounts() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a customer");
            return;
        }
        
        int customerId = (Integer) customerTableModel.getValueAt(selectedRow, 0);
        String customerName = (String) customerTableModel.getValueAt(selectedRow, 2);
        
        try {
            List<Account> accounts = bankingService.getAccountsByCustomer(customerId);
            
            // Switch to accounts tab and filter by customer
            tabbedPane.setSelectedIndex(0);
            
            accountTableModel.setRowCount(0);
            for (Account account : accounts) {
                accountTableModel.addRow(new Object[]{
                    account.getAccountNumber(),
                    account.getAccountHolderName(),
                    account.getAccountType(),
                    String.format("₹%.2f", account.getBalance()),
                    account.getCustomerId(),
                    account.getStatus(),
                    account.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                });
            }
            
            showInfo("Showing accounts for customer: " + customerName);
            
        } catch (SQLException e) {
            showError("Error loading customer accounts: " + e.getMessage());
        }
    }
    
    private void closeAccount() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an account");
            return;
        }
        
        String accountNumber = (String) accountTableModel.getValueAt(selectedRow, 0);
        String accountHolder = (String) accountTableModel.getValueAt(selectedRow, 1);
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to close account " + accountNumber + " for " + accountHolder + "?",
            "Close Account", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean success = bankingService.closeAccount(accountNumber);
                if (success) {
                    showSuccess("Account closed successfully");
                    refreshAccountData();
                } else {
                    showError("Failed to close account");
                }
            } catch (SQLException | AccountNotFoundException e) {
                showError("Error closing account: " + e.getMessage());
            }
        }
    }
    
    private void freezeAccount() {
        showInfo("Account freeze functionality will be implemented in the next version.");
    }
    
    private void toggleCustomerStatus(boolean activate) {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a customer");
            return;
        }
        
        int customerId = (Integer) customerTableModel.getValueAt(selectedRow, 0);
        String customerName = (String) customerTableModel.getValueAt(selectedRow, 2);
        
        try {
            Optional<User> userOpt = userDAO.getUserById(customerId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setActive(activate);
                
                boolean success = userDAO.updateUser(user);
                if (success) {
                    showSuccess("Customer " + (activate ? "activated" : "deactivated") + " successfully");
                    refreshCustomerData();
                } else {
                    showError("Failed to update customer status");
                }
            }
        } catch (SQLException e) {
            showError("Error updating customer status: " + e.getMessage());
        }
    }
    
    private void filterHighValueTransactions(JTextField thresholdField) {
        try {
            double threshold = Double.parseDouble(thresholdField.getText().trim());
            List<Transaction> highValueTransactions = bankingService.getHighValueTransactions(threshold);
            
            transactionTableModel.setRowCount(0);
            for (Transaction transaction : highValueTransactions) {
                transactionTableModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    transaction.getAccountNumber(),
                    transaction.getType(),
                    String.format("₹%.2f", transaction.getAmount()),
                    String.format("₹%.2f", transaction.getBalanceAfter()),
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    transaction.getDescription()
                });
            }
            
            showInfo("Showing " + highValueTransactions.size() + " transactions above ₹" + threshold);
            
        } catch (NumberFormatException e) {
            showError("Please enter a valid threshold amount");
        } catch (SQLException e) {
            showError("Error filtering transactions: " + e.getMessage());
        }
    }
    
    private void filterTransactionsByType() {
        String[] types = {"DEPOSIT", "WITHDRAWAL", "TRANSFER_IN", "TRANSFER_OUT", "INTEREST_CREDIT"};
        String selectedType = (String) JOptionPane.showInputDialog(
            this, "Select transaction type:", "Filter by Type",
            JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        
        if (selectedType != null) {
            try {
                List<Transaction> allTransactions = bankingService.getAllTransactions();
                List<Transaction> filteredTransactions = bankingService.filterTransactionsByType(allTransactions, selectedType);
                
                transactionTableModel.setRowCount(0);
                for (Transaction transaction : filteredTransactions) {
                    transactionTableModel.addRow(new Object[]{
                        transaction.getTransactionId(),
                        transaction.getAccountNumber(),
                        transaction.getType(),
                        String.format("₹%.2f", transaction.getAmount()),
                        String.format("₹%.2f", transaction.getBalanceAfter()),
                        transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                        transaction.getDescription()
                    });
                }
                
                showInfo("Showing " + filteredTransactions.size() + " " + selectedType + " transactions");
                
            } catch (SQLException e) {
                showError("Error filtering transactions: " + e.getMessage());
            }
        }
    }
    
    private void creditInterestToAll() {
        int choice = JOptionPane.showConfirmDialog(this,
            "This will credit interest to all active accounts. Continue?",
            "Credit Interest", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            try {
                bankingService.creditInterest();
                showSuccess("Interest credited to all eligible accounts");
                refreshAccountData();
                refreshTransactionData();
            } catch (SQLException e) {
                showError("Error crediting interest: " + e.getMessage());
            }
        }
    }    

    private void generateBankSummary(JTextArea reportArea) {
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            List<Transaction> transactions = bankingService.getAllTransactions();
            List<User> customers = userDAO.getAllCustomers();
            
            double totalBalance = bankingService.calculateTotalBalance(accounts);
            List<Account> activeAccounts = bankingService.getActiveAccounts(accounts);
            
            long savingsCount = accounts.stream().filter(acc -> acc.getAccountType().equals("SAVINGS")).count();
            long currentCount = accounts.stream().filter(acc -> acc.getAccountType().equals("CURRENT")).count();
            
            StringBuilder report = new StringBuilder();
            report.append("=== BANK SUMMARY REPORT ===\n");
            report.append("Generated on: ").append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n\n");
            
            report.append("CUSTOMER STATISTICS:\n");
            report.append("Total Customers: ").append(customers.size()).append("\n");
            report.append("Active Customers: ").append(customers.stream().filter(User::isActive).count()).append("\n\n");
            
            report.append("ACCOUNT STATISTICS:\n");
            report.append("Total Accounts: ").append(accounts.size()).append("\n");
            report.append("Active Accounts: ").append(activeAccounts.size()).append("\n");
            report.append("Savings Accounts: ").append(savingsCount).append("\n");
            report.append("Current Accounts: ").append(currentCount).append("\n");
            report.append("Total Balance: ₹").append(String.format("%.2f", totalBalance)).append("\n\n");
            
            report.append("TRANSACTION STATISTICS:\n");
            report.append("Total Transactions: ").append(transactions.size()).append("\n");
            
            double totalDeposits = transactions.stream()
                .filter(t -> t.getType().equals("DEPOSIT") || t.getType().equals("TRANSFER_IN"))
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            double totalWithdrawals = transactions.stream()
                .filter(t -> t.getType().equals("WITHDRAWAL") || t.getType().equals("TRANSFER_OUT"))
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            report.append("Total Deposits: ₹").append(String.format("%.2f", totalDeposits)).append("\n");
            report.append("Total Withdrawals: ₹").append(String.format("%.2f", totalWithdrawals)).append("\n");
            
            reportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateHighValueReport(JTextArea reportArea) {
        try {
            List<Transaction> highValueTransactions = bankingService.getHighValueTransactions(10000);
            
            StringBuilder report = new StringBuilder();
            report.append("=== HIGH VALUE TRANSACTIONS REPORT (>₹10,000) ===\n");
            report.append("Generated on: ").append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n\n");
            
            if (highValueTransactions.isEmpty()) {
                report.append("No high value transactions found.\n");
            } else {
                report.append("Total High Value Transactions: ").append(highValueTransactions.size()).append("\n\n");
                
                for (Transaction transaction : highValueTransactions) {
                    report.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
                    report.append("Account: ").append(transaction.getAccountNumber()).append("\n");
                    report.append("Type: ").append(transaction.getType()).append("\n");
                    report.append("Amount: ₹").append(String.format("%.2f", transaction.getAmount())).append("\n");
                    report.append("Date: ").append(transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n");
                    report.append("Description: ").append(transaction.getDescription()).append("\n");
                    report.append("---\n");
                }
            }
            
            reportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateCustomerReport(JTextArea reportArea) {
        try {
            List<User> customers = userDAO.getAllCustomers();
            
            StringBuilder report = new StringBuilder();
            report.append("=== CUSTOMER REPORT ===\n");
            report.append("Generated on: ").append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n\n");
            
            for (User customer : customers) {
                List<Account> customerAccounts = bankingService.getAccountsByCustomer(customer.getUserId());
                double totalBalance = bankingService.calculateTotalBalance(customerAccounts);
                
                report.append("Customer ID: ").append(customer.getUserId()).append("\n");
                report.append("Name: ").append(customer.getFullName()).append("\n");
                report.append("Username: ").append(customer.getUsername()).append("\n");
                report.append("Email: ").append(customer.getEmail()).append("\n");
                report.append("Status: ").append(customer.isActive() ? "Active" : "Inactive").append("\n");
                report.append("Total Accounts: ").append(customerAccounts.size()).append("\n");
                report.append("Total Balance: ₹").append(String.format("%.2f", totalBalance)).append("\n");
                report.append("---\n");
            }
            
            reportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateAccountReport(JTextArea reportArea) {
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            
            StringBuilder report = new StringBuilder();
            report.append("=== DETAILED ACCOUNT REPORT ===\n");
            report.append("Generated on: ").append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n\n");
            
            for (Account account : accounts) {
                List<Transaction> accountTransactions = bankingService.getTransactionHistory(account.getAccountNumber());
                
                report.append("Account Number: ").append(account.getAccountNumber()).append("\n");
                report.append("Account Holder: ").append(account.getAccountHolderName()).append("\n");
                report.append("Account Type: ").append(account.getAccountType()).append("\n");
                report.append("Current Balance: ₹").append(String.format("%.2f", account.getBalance())).append("\n");
                report.append("Status: ").append(account.getStatus()).append("\n");
                report.append("Customer ID: ").append(account.getCustomerId()).append("\n");
                report.append("Created Date: ").append(account.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n");
                report.append("Total Transactions: ").append(accountTransactions.size()).append("\n");
                
                double monthlyInterest = account.calculateInterest();
                report.append("Monthly Interest: ₹").append(String.format("%.2f", monthlyInterest)).append("\n");
                
                report.append("---\n");
            }
            
            reportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateTransactionReport(JTextArea reportArea) {
        try {
            List<Transaction> transactions = bankingService.getAllTransactions();
            
            StringBuilder report = new StringBuilder();
            report.append("=== TRANSACTION SUMMARY REPORT ===\n");
            report.append("Generated on: ").append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n\n");
            
            // Group transactions by type using Java 8 Streams
            java.util.Map<String, java.util.List<Transaction>> transactionsByType = transactions.stream()
                .collect(java.util.stream.Collectors.groupingBy(Transaction::getType));
            
            for (java.util.Map.Entry<String, java.util.List<Transaction>> entry : transactionsByType.entrySet()) {
                String type = entry.getKey();
                java.util.List<Transaction> typeTransactions = entry.getValue();
                
                double totalAmount = typeTransactions.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
                
                report.append("Transaction Type: ").append(type).append("\n");
                report.append("Count: ").append(typeTransactions.size()).append("\n");
                report.append("Total Amount: ₹").append(String.format("%.2f", totalAmount)).append("\n");
                report.append("Average Amount: ₹").append(String.format("%.2f", totalAmount / typeTransactions.size())).append("\n");
                report.append("---\n");
            }
            
            reportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Error generating report: " + e.getMessage());
        }
    }
    
    private void exportReports() {
        showInfo("Report export functionality will be implemented in the next version.");
    }
    
    private void backupDatabase() {
        showInfo("Database backup functionality will be implemented in the next version.");
    }
    
    private void showSystemStatistics() {
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            List<Transaction> transactions = bankingService.getAllTransactions();
            List<User> customers = userDAO.getAllCustomers();
            
            String stats = "System Statistics:\n\n" +
                          "Total Customers: " + customers.size() + "\n" +
                          "Total Accounts: " + accounts.size() + "\n" +
                          "Total Transactions: " + transactions.size() + "\n" +
                          "Total Bank Balance: ₹" + String.format("%.2f", bankingService.calculateTotalBalance(accounts)) + "\n" +
                          "Active Accounts: " + bankingService.getActiveAccounts(accounts).size() + "\n" +
                          "System Uptime: " + getSystemUptime();
            
            JOptionPane.showMessageDialog(this, stats, "System Statistics", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            showError("Error loading system statistics: " + e.getMessage());
        }
    }
    
    private String getSystemUptime() {
        long uptime = java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime();
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        return String.format("%d hours, %d minutes, %d seconds", 
                           hours, minutes % 60, seconds % 60);
    }
    
    private void showAuditLog() {
        showInfo("Audit log functionality will be implemented in the next version.");
    }
    
    private void showChangePasswordDialog() {
        // Similar to customer change password dialog
        JDialog dialog = new JDialog(this, "Change Password", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPasswordField oldPasswordField = new JPasswordField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        panel.add(oldPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        panel.add(newPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        
        JButton changeButton = new JButton("Change");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        
        changeButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (!newPassword.equals(confirmPassword)) {
                showError("New passwords do not match");
                return;
            }
            
            if (newPassword.length() < 6) {
                showError("Password must be at least 6 characters");
                return;
            }
            
            boolean success = authService.changePassword(oldPassword, newPassword);
            if (success) {
                showSuccess("Password changed successfully");
                dialog.dispose();
            } else {
                showError("Failed to change password. Check your current password.");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void showWelcomeMessage() {
        SwingUtilities.invokeLater(() -> {
            String message = "Welcome, " + currentUser.getFullName() + "!\n" +
                           "You are logged in as a Bank Manager.\n" +
                           "You have full access to all banking operations and reports.";
            
            JOptionPane.showMessageDialog(this, message, "Welcome", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void showAboutDialog() {
        String message = "Smart Banking System v1.0 - Manager Portal\n\n" +
                        "Features:\n" +
                        "• Complete Account Management\n" +
                        "• Customer Administration\n" +
                        "• Transaction Monitoring\n" +
                        "• Comprehensive Reports\n" +
                        "• System Administration\n" +
                        "• Real-time Data Updates\n\n" +
                        "Built with Java Swing, MySQL, and multithreading";
        
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            authService.logout();
            dispose();
            new ModernLoginFrame().setVisible(true);
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setupAutoRefresh() {
        // Create refresh indicator with animation
        JLabel refreshIndicator = new JLabel("●");
        refreshIndicator.setForeground(Color.GREEN);
        refreshIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        refreshIndicator.setToolTipText("Auto-refresh every 15 seconds");
        
        // Add to menu bar
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            menuBar.add(Box.createHorizontalGlue());
            menuBar.add(new JLabel("System Status: "));
            menuBar.add(refreshIndicator);
        }
        
        // Auto-refresh with visual feedback
        Timer refreshTimer = new Timer(15000, e -> {
            // Animate refresh indicator
            refreshIndicator.setForeground(Color.ORANGE);
            refreshData();
            
            // Reset indicator color
            Timer resetTimer = new Timer(1000, evt -> refreshIndicator.setForeground(Color.GREEN));
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
        refreshTimer.start();
    }
    
    private void setupInteractiveFeatures() {
        // Add keyboard shortcuts
        setupKeyboardShortcuts();
        
        // Add table interactions
        setupTableInteractions();
        
        // Add status bar
        setupStatusBar();
        
        // Add dashboard widgets
        setupDashboard();
    }
    
    private void setupKeyboardShortcuts() {
        // F5 for refresh
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("F5"), "refresh");
        getRootPane().getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
                showStatusMessage("All data refreshed", Color.BLUE);
            }
        });
        
        // Ctrl+N for new account
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ctrl N"), "newAccount");
        getRootPane().getActionMap().put("newAccount", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccountForCustomer();
            }
        });
        
        // Ctrl+R for reports
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ctrl R"), "reports");
        getRootPane().getActionMap().put("reports", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(3); // Switch to reports tab
            }
        });
        
        // Ctrl+I for interest credit
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ctrl I"), "interest");
        getRootPane().getActionMap().put("interest", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creditInterestToAll();
            }
        });
    }
    
    private void setupTableInteractions() {
        // Double-click interactions
        accountTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewAccountTransactions();
                }
            }
        });
        
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewCustomerAccounts();
                }
            }
        });
        
        // Context menus
        setupContextMenus();
    }
    
    private void setupContextMenus() {
        // Account context menu
        JPopupMenu accountContextMenu = new JPopupMenu();
        JMenuItem viewTransactionsItem = new JMenuItem("View Transactions");
        JMenuItem depositItem = new JMenuItem("Manual Deposit");
        JMenuItem withdrawItem = new JMenuItem("Manual Withdrawal");
        JMenuItem closeAccountItem = new JMenuItem("Close Account");
        
        viewTransactionsItem.addActionListener(e -> viewAccountTransactions());
        depositItem.addActionListener(e -> performManualDeposit());
        withdrawItem.addActionListener(e -> performManualWithdrawal());
        closeAccountItem.addActionListener(e -> closeAccount());
        
        accountContextMenu.add(viewTransactionsItem);
        accountContextMenu.addSeparator();
        accountContextMenu.add(depositItem);
        accountContextMenu.add(withdrawItem);
        accountContextMenu.addSeparator();
        accountContextMenu.add(closeAccountItem);
        
        accountTable.setComponentPopupMenu(accountContextMenu);
        
        // Customer context menu
        JPopupMenu customerContextMenu = new JPopupMenu();
        JMenuItem viewAccountsItem = new JMenuItem("View Customer Accounts");
        JMenuItem createAccountItem = new JMenuItem("Create Account");
        JMenuItem activateItem = new JMenuItem("Activate Customer");
        JMenuItem deactivateItem = new JMenuItem("Deactivate Customer");
        
        viewAccountsItem.addActionListener(e -> viewCustomerAccounts());
        createAccountItem.addActionListener(e -> createAccountForSelectedCustomer());
        activateItem.addActionListener(e -> toggleCustomerStatus(true));
        deactivateItem.addActionListener(e -> toggleCustomerStatus(false));
        
        customerContextMenu.add(viewAccountsItem);
        customerContextMenu.add(createAccountItem);
        customerContextMenu.addSeparator();
        customerContextMenu.add(activateItem);
        customerContextMenu.add(deactivateItem);
        
        customerTable.setComponentPopupMenu(customerContextMenu);
    }
    
    private void setupStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        
        JLabel statusLabel = new JLabel("Manager Portal Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        JLabel statsLabel = new JLabel();
        statsLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        // Update stats and time
        Timer updateTimer = new Timer(5000, e -> {
            try {
                List<Account> accounts = bankingService.getAllAccounts();
                List<User> customers = userDAO.getAllCustomers();
                double totalBalance = bankingService.calculateTotalBalance(accounts);
                
                statsLabel.setText(String.format("Accounts: %d | Customers: %d | Total: ₹%.0f", 
                    accounts.size(), customers.size(), totalBalance));
                
                timeLabel.setText(java.time.LocalTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            } catch (Exception ex) {
                statsLabel.setText("Error loading stats");
            }
        });
        updateTimer.start();
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(statsLabel, BorderLayout.CENTER);
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        add(statusBar, BorderLayout.SOUTH);
        
        // Store reference for status updates
        this.statusLabel = statusLabel;
    }
    
    private void setupDashboard() {
        // Add a mini dashboard to the first tab
        SwingUtilities.invokeLater(() -> {
            try {
                JPanel dashboardPanel = new JPanel(new GridLayout(1, 4, 10, 10));
                dashboardPanel.setBorder(BorderFactory.createTitledBorder("Quick Stats"));
                dashboardPanel.setPreferredSize(new Dimension(getWidth(), 80));
                
                // Create stat cards
                JPanel totalAccountsCard = createStatCard("Total Accounts", "0", Color.BLUE);
                JPanel totalCustomersCard = createStatCard("Total Customers", "0", Color.GREEN);
                JPanel totalBalanceCard = createStatCard("Total Balance", "₹0", Color.ORANGE);
                JPanel activeAccountsCard = createStatCard("Active Accounts", "0", new Color(128, 0, 128));
                
                dashboardPanel.add(totalAccountsCard);
                dashboardPanel.add(totalCustomersCard);
                dashboardPanel.add(totalBalanceCard);
                dashboardPanel.add(activeAccountsCard);
                
                // Add to the first tab
                JPanel accountsPanel = (JPanel) tabbedPane.getComponentAt(0);
                accountsPanel.add(dashboardPanel, BorderLayout.NORTH);
                
                // Update stats periodically
                Timer statsTimer = new Timer(10000, e -> updateDashboardStats(
                    totalAccountsCard, totalCustomersCard, totalBalanceCard, activeAccountsCard));
                statsTimer.start();
                
                // Initial update
                updateDashboardStats(totalAccountsCard, totalCustomersCard, totalBalanceCard, activeAccountsCard);
                
            } catch (Exception e) {
                System.err.println("Error setting up dashboard: " + e.getMessage());
            }
        });
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void updateDashboardStats(JPanel totalAccountsCard, JPanel totalCustomersCard, 
                                    JPanel totalBalanceCard, JPanel activeAccountsCard) {
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            List<User> customers = userDAO.getAllCustomers();
            double totalBalance = bankingService.calculateTotalBalance(accounts);
            List<Account> activeAccounts = bankingService.getActiveAccounts(accounts);
            
            ((JLabel) totalAccountsCard.getComponent(1)).setText(String.valueOf(accounts.size()));
            ((JLabel) totalCustomersCard.getComponent(1)).setText(String.valueOf(customers.size()));
            ((JLabel) totalBalanceCard.getComponent(1)).setText("₹" + String.format("%.0f", totalBalance));
            ((JLabel) activeAccountsCard.getComponent(1)).setText(String.valueOf(activeAccounts.size()));
            
        } catch (Exception e) {
            System.err.println("Error updating dashboard stats: " + e.getMessage());
        }
    }
    
    private JLabel statusLabel;
    
    private void showStatusMessage(String message, Color color) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setForeground(color);
            
            // Clear after 5 seconds
            Timer clearTimer = new Timer(5000, e -> {
                statusLabel.setText("Manager Portal Ready");
                statusLabel.setForeground(Color.BLACK);
            });
            clearTimer.setRepeats(false);
            clearTimer.start();
        }
    }
}