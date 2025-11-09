package utils;

import java.awt.*;

/**
 * Modern UI Constants for Smart Banking System
 * Professional color scheme and styling constants
 */
public class UIConstants {
    
    // ==================== COLOR SCHEME ====================
    
    // Primary Colors - Modern Banking Blue
    public static final Color PRIMARY_COLOR = new Color(25, 118, 210);        // Material Blue
    public static final Color PRIMARY_DARK = new Color(13, 71, 161);          // Darker Blue
    public static final Color PRIMARY_LIGHT = new Color(100, 181, 246);       // Light Blue
    public static final Color PRIMARY_HOVER = new Color(30, 136, 229);        // Hover Blue
    
    // Secondary Colors - Accent Green
    public static final Color SECONDARY_COLOR = new Color(67, 160, 71);       // Material Green
    public static final Color SECONDARY_DARK = new Color(46, 125, 50);        // Dark Green
    public static final Color SECONDARY_LIGHT = new Color(129, 199, 132);     // Light Green
    public static final Color SECONDARY_HOVER = new Color(76, 175, 80);       // Hover Green
    
    // Status Colors
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);         // Green
    public static final Color WARNING_COLOR = new Color(255, 152, 0);         // Orange
    public static final Color ERROR_COLOR = new Color(244, 67, 54);           // Red
    public static final Color INFO_COLOR = new Color(33, 150, 243);           // Blue
    
    // Background Colors
    public static final Color BACKGROUND_COLOR = new Color(250, 251, 252);    // Light Gray
    public static final Color CARD_BACKGROUND = Color.WHITE;                   // White
    public static final Color PANEL_BACKGROUND = new Color(245, 247, 250);    // Very Light Gray
    public static final Color HOVER_BACKGROUND = new Color(240, 242, 245);    // Hover Gray
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);           // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);      // Medium Gray
    public static final Color TEXT_DISABLED = new Color(189, 189, 189);       // Light Gray
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;                   // White on colored backgrounds
    
    // Border Colors
    public static final Color BORDER_COLOR = new Color(224, 224, 224);        // Light Border
    public static final Color BORDER_FOCUS = PRIMARY_COLOR;                    // Focus Border
    public static final Color BORDER_ERROR = ERROR_COLOR;                      // Error Border
    
    // Shadow Colors
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 20);          // Subtle Shadow
    public static final Color SHADOW_DARK = new Color(0, 0, 0, 40);           // Darker Shadow
    
    // ==================== FONTS ====================
    
    public static final String FONT_FAMILY = "Segoe UI";
    public static final String FONT_FAMILY_MONO = "Consolas";
    
    // Font Sizes
    public static final int FONT_SIZE_HUGE = 36;
    public static final int FONT_SIZE_XLARGE = 28;
    public static final int FONT_SIZE_LARGE = 22;
    public static final int FONT_SIZE_TITLE = 20;
    public static final int FONT_SIZE_SUBTITLE = 18;
    public static final int FONT_SIZE_NORMAL = 16;
    public static final int FONT_SIZE_MEDIUM = 14;
    public static final int FONT_SIZE_SMALL = 12;
    
    // Font Styles
    public static final Font FONT_HUGE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_HUGE);
    public static final Font FONT_XLARGE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_XLARGE);
    public static final Font FONT_LARGE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_LARGE);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_TITLE);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_SUBTITLE);
    public static final Font FONT_NORMAL = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_NORMAL);
    public static final Font FONT_NORMAL_BOLD = new Font(FONT_FAMILY, Font.BOLD, FONT_SIZE_NORMAL);
    public static final Font FONT_MEDIUM = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_MEDIUM);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE_SMALL);
    public static final Font FONT_MONO = new Font(FONT_FAMILY_MONO, Font.PLAIN, FONT_SIZE_MEDIUM);
    
    // ==================== DIMENSIONS ====================
    
    // Button Sizes
    public static final Dimension BUTTON_LARGE = new Dimension(200, 55);
    public static final Dimension BUTTON_MEDIUM = new Dimension(150, 45);
    public static final Dimension BUTTON_SMALL = new Dimension(120, 40);
    
    // Input Field Sizes
    public static final Dimension INPUT_LARGE = new Dimension(450, 50);
    public static final Dimension INPUT_MEDIUM = new Dimension(350, 45);
    public static final Dimension INPUT_SMALL = new Dimension(250, 40);
    
    // Spacing
    public static final int SPACING_HUGE = 40;
    public static final int SPACING_XLARGE = 30;
    public static final int SPACING_LARGE = 25;
    public static final int SPACING_MEDIUM = 20;
    public static final int SPACING_NORMAL = 15;
    public static final int SPACING_SMALL = 10;
    public static final int SPACING_TINY = 5;
    
    // Padding
    public static final int PADDING_HUGE = 40;
    public static final int PADDING_XLARGE = 30;
    public static final int PADDING_LARGE = 25;
    public static final int PADDING_MEDIUM = 20;
    public static final int PADDING_NORMAL = 15;
    public static final int PADDING_SMALL = 10;
    
    // Border Radius
    public static final int RADIUS_LARGE = 12;
    public static final int RADIUS_MEDIUM = 8;
    public static final int RADIUS_SMALL = 4;
    
    // ==================== ICONS & SYMBOLS ====================
    
    public static final String ICON_USER = "👤";
    public static final String ICON_LOCK = "🔒";
    public static final String ICON_MONEY = "💰";
    public static final String ICON_CARD = "💳";
    public static final String ICON_TRANSFER = "↔️";
    public static final String ICON_DEPOSIT = "⬇️";
    public static final String ICON_WITHDRAW = "⬆️";
    public static final String ICON_SUCCESS = "✓";
    public static final String ICON_ERROR = "✗";
    public static final String ICON_INFO = "ℹ";
    public static final String ICON_WARNING = "⚠";
    public static final String ICON_REFRESH = "⟳";
    public static final String ICON_SETTINGS = "⚙";
    public static final String ICON_LOGOUT = "⎋";
    public static final String ICON_DASHBOARD = "📊";
    public static final String ICON_REPORT = "📈";
    
    // ==================== ANIMATION SETTINGS ====================
    
    public static final int ANIMATION_DURATION_FAST = 150;
    public static final int ANIMATION_DURATION_NORMAL = 300;
    public static final int ANIMATION_DURATION_SLOW = 500;
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Create a lighter version of a color
     */
    public static Color lighter(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    /**
     * Create a darker version of a color
     */
    public static Color darker(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Create a color with transparency
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    private UIConstants() {
        // Private constructor to prevent instantiation
    }
}