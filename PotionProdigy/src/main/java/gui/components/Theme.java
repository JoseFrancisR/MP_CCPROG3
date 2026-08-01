/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.components;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JViewport;

public class Theme {
    public static final Color BACKGROUND = new Color(49, 51, 56);
    public static final Color SECONDARY = new Color(43, 45, 49);
    public static final Color DARK = new Color(30, 31, 34);
    public static final Color TEXT = new Color(242, 243, 245);
    public static final Color MUTED_TEXT = new Color(181, 186, 193);
    public static final Color ACCENT = new Color(88, 101, 242);
    public static final Color INPUT = new Color(30, 31, 34);
    public static final Color BORDER = new Color(64, 66, 73);
    public static final Color SELECTED_BACKGROUND = new Color(64, 68, 90);
    
    public static void configureDefaults() {
        UIManager.put("Panel.background", BACKGROUND);

        UIManager.put("Label.foreground", TEXT);

        UIManager.put("Button.background", SECONDARY);
        UIManager.put("Button.foreground", TEXT);

        UIManager.put("RadioButton.background", BACKGROUND);
        UIManager.put("RadioButton.foreground", TEXT);

        UIManager.put("CheckBox.background", BACKGROUND);
        UIManager.put("CheckBox.foreground", TEXT);

        UIManager.put("ScrollPane.background", BACKGROUND);

        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("OptionPane.foreground", TEXT);
        UIManager.put("OptionPane.messageForeground", TEXT);
    }
    
    public static void apply(Component component) {
        boolean customCard =
            component instanceof gui.components.ItemCard
            || component instanceof gui.components.MarketItemCard
            || component instanceof gui.components.SellItemCard
            || component instanceof gui.components.RecipeCard;
        
        if (component instanceof JPanel panel && !customCard) {
            panel.setBackground(BACKGROUND);
            panel.setOpaque(true);
        }
        if (component instanceof JLabel label) {
            label.setForeground(TEXT);
        }
        if (component instanceof JButton button) {
            styleButton(button);
        } else if (component instanceof JRadioButton radioButton) {
            styleToggleButton(radioButton);
        } else if (component instanceof JCheckBox checkBox) {
            styleToggleButton(checkBox);
        }
        if (component instanceof JScrollPane scroll) {
            scroll.setBackground(BACKGROUND);
            scroll.setBorder(BorderFactory.createLineBorder(BORDER));
            JViewport viewport = scroll.getViewport();
            viewport.setBackground(BACKGROUND);
        }
    }
    
    private static void styleButton(JButton button) {
        button.setBackground(SECONDARY);
        button.setForeground(TEXT);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(7, 12, 7, 12)));
    }
    
    private static void styleToggleButton(AbstractButton button) { // abstract button = interface for all buttons
        button.setBackground(BACKGROUND);
        button.setForeground(TEXT);
        button.setFocusPainted(false);
        button.setOpaque(true);
    }
}
