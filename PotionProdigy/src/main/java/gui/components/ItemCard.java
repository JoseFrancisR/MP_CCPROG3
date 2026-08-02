/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
// image handling
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

import potionprodigy.ItemStack;

public class ItemCard extends JPanel {

    private final ItemStack ITEM_STACK;
    
    private JLabel lblIcon;
    private JLabel lblName;
    private JLabel lblQuantity;
    
    private boolean selected;

    public ItemCard(ItemStack ITEM_STACK) {
        this.ITEM_STACK = ITEM_STACK;
        this.selected = false;

        initializeCard();
    }
    
    private void initializeCard() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(140, 140));
        setOpaque(true);
        setBackground(Theme.SECONDARY);
        setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));

        lblQuantity = new JLabel(String.valueOf(ITEM_STACK.getQuantity()), SwingConstants.CENTER);

        lblQuantity.setOpaque(true);
        lblQuantity.setBackground(Theme.DARK);
        lblQuantity.setForeground(Theme.TEXT);
        lblQuantity.setPreferredSize(new Dimension(30, 30));

        lblQuantity.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        quantityPanel.setOpaque(false);
        quantityPanel.add(lblQuantity);

        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        String iconPath;
        // check if fruit/base
        if (ITEM_STACK.getIngredient().isFruit()) {
            iconPath = "/images/fruits.png";
        } else {
            iconPath = "/images/bases.png";
        }
        
        ImageIcon itemIcon = loadIcon(iconPath, 80, 80);
        
        if (itemIcon != null) {
            lblIcon.setIcon(itemIcon);
        } else {
            lblIcon.setText("[ PLACEHOLDER ]");
        }

        /*
         * Ingredient name.
         */
        lblName = new JLabel(
                ITEM_STACK.getIngredient().getName(),
                SwingConstants.CENTER
        );
        lblName.setForeground(Theme.TEXT);

        add(quantityPanel, BorderLayout.NORTH);
        add(lblIcon, BorderLayout.CENTER);
        add(lblName, BorderLayout.SOUTH);
    }
    
    public ItemStack getItemStack() {
        return ITEM_STACK;
    }

    public boolean isSelectedCard() {
        return selected;
    }
    
    public void setSelectedCard(boolean selected) {
        this.selected = selected;

        if (selected) {
            setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 3));
            setBackground(new Color(64, 68, 90));
        } else {
            setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
            setBackground(Theme.SECONDARY);
        }
        
        revalidate();
        repaint();
    }
    
    private ImageIcon loadIcon(String resourcePath, int width, int height) {
        URL imageUrl = getClass().getResource(resourcePath);
        Image scaledImage = null;
    
        if (imageUrl != null) {
            Image originalImage = new ImageIcon(imageUrl).getImage();
    
            scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
    
        return new ImageIcon(scaledImage);
    }
}