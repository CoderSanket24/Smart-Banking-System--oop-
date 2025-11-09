package utils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern UI Components Factory
 * Creates beautiful, consistent UI elements
 */
public class ModernUIComponents {
    
    /**
     * Create a modern styled button
     */
    public static JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.FONT_NORMAL_BOLD);
        button.setForeground(UIConstants.TEXT_ON_PRIMARY);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Create a primary button (blue)
     */
    public static JButton createPrimaryButton(String text) {
        return createModernButton(text, UIConstants.PRIMARY_COLOR, UIConstants.PRIMARY_HOVER);
    }
    
    /**
     * Create a secondary button (green)
     */
    public static JButton createSecondaryButton(String text) {
        return createModernButton(text, UIConstants.SECONDARY_COLOR, UIConstants.SECONDARY_HOVER);
    }
    
    /**
     * Create a success button
     */
    public static JButton createSuccessButton(String text) {
        return createModernButton(text, UIConstants.SUCCESS_COLOR, UIConstants.darker(UIConstants.SUCCESS_COLOR, 0.1f));
    }
    
    /**
     * Create a danger button
     */
    public static JButton createDangerButton(String text) {
        return createModernButton(text, UIConstants.ERROR_COLOR, UIConstants.darker(UIConstants.ERROR_COLOR, 0.1f));
    }
    
    /**
     * Create an outlined button
     */
    public static JButton createOutlinedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.FONT_NORMAL_BOLD);
        button.setForeground(color);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(13, 28, 13, 28)
        ));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIConstants.lighter(color, 0.9f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern text field
     */
    public static JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(UIConstants.FONT_NORMAL);
        field.setForeground(UIConstants.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIConstants.BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create a modern password field
     */
    public static JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(UIConstants.FONT_NORMAL);
        field.setForeground(UIConstants.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIConstants.BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create a modern card panel
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(UIConstants.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE
            )
        ));
        return card;
    }
    
    /**
     * Create a stat card with icon and value
     */
    public static JPanel createStatCard(String title, String value, String icon, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(UIConstants.SPACING_MEDIUM, UIConstants.SPACING_MEDIUM));
        card.setBackground(UIConstants.CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 3),
            BorderFactory.createEmptyBorder(
                UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE
            )
        ));
        
        // Icon panel
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconPanel.setBackground(UIConstants.CARD_BACKGROUND);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 32));
        iconPanel.add(iconLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIConstants.CARD_BACKGROUND);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_MEDIUM);
        titleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIConstants.FONT_XLARGE);
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        contentPanel.add(valueLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create a section title label
     */
    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_TITLE);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a subtitle label
     */
    public static JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SUBTITLE);
        label.setForeground(UIConstants.TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Create a field label
     */
    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_NORMAL_BOLD);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a modern panel with title
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            UIConstants.FONT_SUBTITLE,
            UIConstants.PRIMARY_COLOR
        ));
        return panel;
    }
    
    /**
     * Create a separator
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(UIConstants.BORDER_COLOR);
        return separator;
    }
    
    /**
     * Show a modern success message
     */
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Success " + UIConstants.ICON_SUCCESS,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show a modern error message
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Error " + UIConstants.ICON_ERROR,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Show a modern warning message
     */
    public static void showWarningMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Warning " + UIConstants.ICON_WARNING,
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Show a modern info message
     */
    public static void showInfoMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Information " + UIConstants.ICON_INFO,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Create a modern confirmation dialog
     */
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Add a subtle shadow effect to a component
     */
    public static void addShadow(JComponent component) {
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.SHADOW_COLOR, 1),
            component.getBorder()
        ));
    }
    
    /**
     * Create a loading indicator
     */
    public static JLabel createLoadingIndicator() {
        JLabel label = new JLabel("Loading...");
        label.setFont(UIConstants.FONT_NORMAL);
        label.setForeground(UIConstants.TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Create a badge label
     */
    public static JLabel createBadge(String text, Color bgColor) {
        JLabel badge = new JLabel(text);
        badge.setFont(UIConstants.FONT_SMALL);
        badge.setForeground(UIConstants.TEXT_ON_PRIMARY);
        badge.setBackground(bgColor);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        return badge;
    }
    
    private ModernUIComponents() {
        // Private constructor to prevent instantiation
    }
}