package gui;

import services.AuthenticationService;
import services.BankingService;
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

public class CustomerMainFrame extends JFrame {
    private AuthenticationService authService;
    private BankingService bankingService;
    private User currentUser;
    
    private JTabbedPane tabbedPane;
    private JTable accountTable;
    private JTable transactionTable;
    private DefaultTableModel accountTableModel;
    private DefaultTableModel transactionTableModel;
    
    public CustomerMainFrame(AuthenticationService authService) {
        this.authService = authService;
        this.bankingService = new BankingService();
        this.currentUser = authService.getCurrentUser();
        
        initializeGUI();
        loadCustomerData();
    }
    
    private void initializeGUI() {
        setTitle("Smart Banking System - Customer Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Make window fullscreen/maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Set minimum size
        setMinimumSize(new Dimension(1000, 600));
        
        // Set modern background color
        getContentPane().setBackground(new Color(240, 242, 245));
        
        // Create menu bar
        createMenuBar();
        
        // Create modern tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);
        
        // Add tabs with icons
        tabbedPane.addTab("💳 My Accounts", createAccountsPanel());
        tabbedPane.addTab("📊 Transactions", createTransactionsPanel());
        tabbedPane.addTab("💸 Transfer Funds", createTransferPanel());
        tabbedPane.addTab("⚙️ Account Services", createServicesPanel());
        
        add(tabbedPane);
        
        // Welcome message
        showWelcomeMessage();
        
        // Set up auto-refresh timer with visual indicator
        setupAutoRefresh();
        
        // Add interactive features
        setupInteractiveFeatures();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Account menu
        JMenu accountMenu = new JMenu("Account");
        JMenuItem refreshItem = new JMenuItem("Refresh Data");
        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        JMenuItem logoutItem = new JMenuItem("Logout");
        
        refreshItem.addActionListener(e -> refreshData());
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        logoutItem.addActionListener(e -> logout());
        
        accountMenu.add(refreshItem);
        accountMenu.addSeparator();
        accountMenu.add(changePasswordItem);
        accountMenu.addSeparator();
        accountMenu.add(logoutItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(accountMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Account table
        String[] columnNames = {"Account Number", "Account Type", "Balance", "Status", "Created Date"};
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
        
        JButton depositButton = createModernButton("💰 Deposit", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton withdrawButton = createModernButton("💵 Withdraw", new Color(231, 76, 60), new Color(192, 57, 43));
        JButton viewTransactionsButton = createModernButton("📋 View Transactions", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton refreshButton = createModernButton("🔄 Refresh", new Color(155, 89, 182), new Color(142, 68, 173));
        
        depositButton.addActionListener(e -> performDeposit());
        withdrawButton.addActionListener(e -> performWithdrawal());
        viewTransactionsButton.addActionListener(e -> viewAccountTransactions());
        refreshButton.addActionListener(e -> refreshAccountData());
        
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(viewTransactionsButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
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
        
        JButton filterButton = createModernButton("💎 Filter High Value (>₹10,000)", new Color(230, 126, 34), new Color(211, 84, 0));
        JButton showAllButton = createModernButton("📋 Show All", new Color(52, 152, 219), new Color(41, 128, 185));
        
        filterButton.addActionListener(e -> filterHighValueTransactions());
        showAllButton.addActionListener(e -> refreshTransactionData());
        
        filterPanel.add(filterButton);
        filterPanel.add(showAllButton);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JComboBox<String> fromAccountCombo = new JComboBox<>();
        fromAccountCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fromAccountCombo.setPreferredSize(new Dimension(350, 45));
        
        JTextField toAccountField = new JTextField(20);
        toAccountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toAccountField.setPreferredSize(new Dimension(350, 45));
        
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setPreferredSize(new Dimension(350, 45));
        
        JTextField descriptionField = new JTextField(20);
        descriptionField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionField.setPreferredSize(new Dimension(350, 45));
        
        JButton transferButton = createModernButton("💸 Transfer Funds", new Color(46, 204, 113), new Color(39, 174, 96));
        transferButton.setPreferredSize(new Dimension(350, 55));
        
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        resultArea.setBackground(new Color(236, 240, 241));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Populate from account combo
        loadAccountsIntoCombo(fromAccountCombo);
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel fromLabel = new JLabel("💳 From Account:");
        fromLabel.setFont(labelFont);
        panel.add(fromLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(fromAccountCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JLabel toLabel = new JLabel("🎯 To Account Number:");
        toLabel.setFont(labelFont);
        panel.add(toLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(toAccountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        JLabel amountLabel = new JLabel("💰 Amount:");
        amountLabel.setFont(labelFont);
        panel.add(amountLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        JLabel descLabel = new JLabel("📝 Description:");
        descLabel.setFont(labelFont);
        panel.add(descLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(descriptionField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(transferButton, gbc);
        
        gbc.gridy = 5;
        panel.add(new JScrollPane(resultArea), gbc);
        
        transferButton.addActionListener(e -> performTransfer(
            fromAccountCombo, toAccountField, amountField, descriptionField, resultArea));
        
        return panel;
    }
    
    private JPanel createServicesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        JButton requestAccountButton = createModernButton("➕ Request New Account", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton downloadStatementButton = createModernButton("📄 Download Statement", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton changePasswordButton = createModernButton("🔐 Change Password", new Color(230, 126, 34), new Color(211, 84, 0));
        JButton contactSupportButton = createModernButton("📞 Contact Support", new Color(155, 89, 182), new Color(142, 68, 173));
        
        requestAccountButton.setPreferredSize(new Dimension(300, 60));
        downloadStatementButton.setPreferredSize(new Dimension(300, 60));
        changePasswordButton.setPreferredSize(new Dimension(300, 60));
        contactSupportButton.setPreferredSize(new Dimension(300, 60));
        
        requestAccountButton.addActionListener(e -> requestNewAccount());
        downloadStatementButton.addActionListener(e -> downloadStatement());
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());
        contactSupportButton.addActionListener(e -> showContactSupport());
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(requestAccountButton, gbc);
        
        gbc.gridy = 1;
        panel.add(downloadStatementButton, gbc);
        
        gbc.gridy = 2;
        panel.add(changePasswordButton, gbc);
        
        gbc.gridy = 3;
        panel.add(contactSupportButton, gbc);
        
        return panel;
    }
    
    private void loadCustomerData() {
        refreshAccountData();
        refreshTransactionData();
    }
    
    private void refreshData() {
        refreshAccountData();
        refreshTransactionData();
    }
    
    private void refreshAccountData() {
        try {
            List<Account> accounts = bankingService.getAccountsByCustomer(currentUser.getUserId());
            
            accountTableModel.setRowCount(0);
            for (Account account : accounts) {
                accountTableModel.addRow(new Object[]{
                    account.getAccountNumber(),
                    account.getAccountType(),
                    String.format("₹%.2f", account.getBalance()),
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
            List<Transaction> transactions = bankingService.getCustomerTransactions(currentUser.getUserId());
            
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
    
    private void loadAccountsIntoCombo(JComboBox<String> combo) {
        try {
            List<Account> accounts = bankingService.getAccountsByCustomer(currentUser.getUserId());
            combo.removeAllItems();
            for (Account account : accounts) {
                if (account.getStatus() == AccountStatus.ACTIVE) {
                    combo.addItem(account.getAccountNumber() + " (" + account.getAccountType() + ")");
                }
            }
        } catch (SQLException e) {
            showError("Error loading accounts: " + e.getMessage());
        }
    }    
   
 private void performDeposit() {
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
                if (amount <= 0) {
                    showError("Amount must be positive");
                    return;
                }
                
                bankingService.deposit(accountNumber, amount, "Cash deposit");
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
    
    private void performWithdrawal() {
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
                if (amount <= 0) {
                    showError("Amount must be positive");
                    return;
                }
                
                bankingService.withdraw(accountNumber, amount, "Cash withdrawal");
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
    
    private void performTransfer(JComboBox<String> fromAccountCombo, JTextField toAccountField, 
                               JTextField amountField, JTextField descriptionField, JTextArea resultArea) {
        try {
            String fromAccountStr = (String) fromAccountCombo.getSelectedItem();
            if (fromAccountStr == null) {
                resultArea.setText("Please select a source account");
                return;
            }
            
            String fromAccount = fromAccountStr.split(" ")[0]; // Extract account number
            String toAccount = toAccountField.getText().trim();
            String amountStr = amountField.getText().trim();
            String description = descriptionField.getText().trim();
            
            if (toAccount.isEmpty() || amountStr.isEmpty()) {
                resultArea.setText("Please fill all required fields");
                return;
            }
            
            if (fromAccount.equals(toAccount)) {
                resultArea.setText("Cannot transfer to the same account");
                return;
            }
            
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                resultArea.setText("Amount must be positive");
                return;
            }
            
            if (description.isEmpty()) {
                description = "Fund transfer";
            }
            
            bankingService.transfer(fromAccount, toAccount, amount, description);
            
            // Clear fields
            toAccountField.setText("");
            amountField.setText("");
            descriptionField.setText("");
            
            resultArea.setText("Transfer successful!\n" +
                "From: " + fromAccount + "\n" +
                "To: " + toAccount + "\n" +
                "Amount: ₹" + String.format("%.2f", amount) + "\n" +
                "Description: " + description);
            
            refreshAccountData();
            refreshTransactionData();
            
        } catch (NumberFormatException e) {
            resultArea.setText("Please enter a valid amount");
        } catch (AccountNotFoundException | InsufficientFundsException | SQLException e) {
            resultArea.setText("Transfer failed: " + e.getMessage());
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
    
    private void filterHighValueTransactions() {
        try {
            List<Transaction> transactions = bankingService.getCustomerTransactions(currentUser.getUserId());
            List<Transaction> highValueTransactions = bankingService.filterTransactionsByType(transactions, "");
            
            // Filter using Java 8 Streams
            transactionTableModel.setRowCount(0);
            transactions.stream()
                .filter(t -> t.getAmount() > 10000)
                .forEach(transaction -> {
                    transactionTableModel.addRow(new Object[]{
                        transaction.getTransactionId(),
                        transaction.getAccountNumber(),
                        transaction.getType(),
                        String.format("₹%.2f", transaction.getAmount()),
                        String.format("₹%.2f", transaction.getBalanceAfter()),
                        transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                        transaction.getDescription()
                    });
                });
                
        } catch (SQLException e) {
            showError("Error filtering transactions: " + e.getMessage());
        }
    }
    
    private void requestNewAccount() {
        String[] accountTypes = {"SAVINGS", "CURRENT"};
        String selectedType = (String) JOptionPane.showInputDialog(
            this, "Select account type:", "New Account Request",
            JOptionPane.QUESTION_MESSAGE, null, accountTypes, accountTypes[0]);
        
        if (selectedType != null) {
            String initialBalanceStr = JOptionPane.showInputDialog(this, "Enter initial balance:");
            
            if (initialBalanceStr != null && !initialBalanceStr.trim().isEmpty()) {
                try {
                    double initialBalance = Double.parseDouble(initialBalanceStr.trim());
                    
                    String accountNumber = bankingService.createAccount(
                        currentUser.getFullName(), selectedType, initialBalance, currentUser.getUserId());
                    
                    showSuccess("Account created successfully!\nAccount Number: " + accountNumber);
                    refreshAccountData();
                    
                } catch (NumberFormatException e) {
                    showError("Please enter a valid amount");
                } catch (SQLException e) {
                    showError("Account creation failed: " + e.getMessage());
                }
            }
        }
    }
    
    private void downloadStatement() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an account");
            return;
        }
        
        // This is a placeholder - in a real application, you would generate a PDF or CSV
        showInfo("Statement download feature will be implemented in the next version.");
    }
    
    private void showChangePasswordDialog() {
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
    
    private void showContactSupport() {
        String message = "Contact Support:\n\n" +
                        "Phone: 1-800-BANKING\n" +
                        "Email: support@smartbank.com\n" +
                        "Hours: 24/7\n\n" +
                        "For immediate assistance, please call our helpline.";
        
        JOptionPane.showMessageDialog(this, message, "Contact Support", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showWelcomeMessage() {
        SwingUtilities.invokeLater(() -> {
            String message = "Welcome, " + currentUser.getFullName() + "!\n" +
                           "You are logged in as a Customer.\n" +
                           "Use the tabs above to manage your accounts and transactions.";
            
            JOptionPane.showMessageDialog(this, message, "Welcome", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void showAboutDialog() {
        String message = "Smart Banking System v1.0\n\n" +
                        "A comprehensive banking application with:\n" +
                        "• Account Management\n" +
                        "• Transaction Processing\n" +
                        "• Fund Transfers\n" +
                        "• Real-time Updates\n" +
                        "• Secure Authentication\n\n" +
                        "Built with Java Swing and MySQL";
        
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
        // Create refresh indicator
        JLabel refreshIndicator = new JLabel("●");
        refreshIndicator.setForeground(Color.GREEN);
        refreshIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        refreshIndicator.setToolTipText("Auto-refresh active");
        
        // Add to menu bar
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            menuBar.add(Box.createHorizontalGlue());
            menuBar.add(new JLabel("Auto-refresh: "));
            menuBar.add(refreshIndicator);
        }
        
        // Auto-refresh with visual feedback
        Timer refreshTimer = new Timer(10000, e -> {
            // Animate refresh indicator
            refreshIndicator.setForeground(Color.ORANGE);
            refreshData();
            
            // Reset indicator color
            Timer resetTimer = new Timer(500, evt -> refreshIndicator.setForeground(Color.GREEN));
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
        refreshTimer.start();
    }
    
    private void setupInteractiveFeatures() {
        // Add keyboard shortcuts
        setupKeyboardShortcuts();
        
        // Add table selection listeners
        setupTableInteractions();
        
        // Add status bar
        setupStatusBar();
    }
    
    private void setupKeyboardShortcuts() {
        // F5 for refresh
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("F5"), "refresh");
        getRootPane().getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
                showStatusMessage("Data refreshed", Color.BLUE);
            }
        });
        
        // Ctrl+D for deposit
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ctrl D"), "deposit");
        getRootPane().getActionMap().put("deposit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDeposit();
            }
        });
        
        // Ctrl+W for withdraw
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ctrl W"), "withdraw");
        getRootPane().getActionMap().put("withdraw", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performWithdrawal();
            }
        });
        
        // Ctrl+T for transfer
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ctrl T"), "transfer");
        getRootPane().getActionMap().put("transfer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(2); // Switch to transfer tab
            }
        });
    }
    
    private void setupTableInteractions() {
        // Double-click on account to view transactions
        accountTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewAccountTransactions();
                }
            }
        });
        
        // Right-click context menu for accounts
        JPopupMenu accountContextMenu = new JPopupMenu();
        JMenuItem depositItem = new JMenuItem("Deposit");
        JMenuItem withdrawItem = new JMenuItem("Withdraw");
        JMenuItem viewTransactionsItem = new JMenuItem("View Transactions");
        
        depositItem.addActionListener(e -> performDeposit());
        withdrawItem.addActionListener(e -> performWithdrawal());
        viewTransactionsItem.addActionListener(e -> viewAccountTransactions());
        
        accountContextMenu.add(depositItem);
        accountContextMenu.add(withdrawItem);
        accountContextMenu.add(viewTransactionsItem);
        
        accountTable.setComponentPopupMenu(accountContextMenu);
    }
    
    private void setupStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        // Update time every second
        Timer timeTimer = new Timer(1000, e -> {
            timeLabel.setText(java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        timeTimer.start();
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        add(statusBar, BorderLayout.SOUTH);
        
        // Store reference for status updates
        this.statusLabel = statusLabel;
    }
    
    private JLabel statusLabel;
    
    private void showStatusMessage(String message, Color color) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setForeground(color);
            
            // Clear after 3 seconds
            Timer clearTimer = new Timer(3000, e -> {
                statusLabel.setText("Ready");
                statusLabel.setForeground(Color.BLACK);
            });
            clearTimer.setRepeats(false);
            clearTimer.start();
        }
    }
}