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

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

import potionprodigy.ItemStack;

public class ItemCard extends JPanel {

    private final ItemStack itemStack;
    
    private JLabel lblIcon;
    private JLabel lblName;
    private JLabel lblQuantity;
    
    private boolean selected;

    public ItemCard(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.selected = false;

        initializeCard();
    }
    
    private void initializeCard() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(140, 150));
        setBackground(Color.WHITE);

        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        lblQuantity = new JLabel(String.valueOf(itemStack.getQuantity()), SwingConstants.CENTER);

        lblQuantity.setOpaque(true);
        lblQuantity.setBackground(Color.LIGHT_GRAY);
        lblQuantity.setPreferredSize(new Dimension(30, 30));

        lblQuantity.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        quantityPanel.setOpaque(false);
        quantityPanel.add(lblQuantity);

        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        String iconPath;
        // check if fruit/base
        if (itemStack.getIngredient().isFruit()) {
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
                itemStack.getIngredient().getName(),
                SwingConstants.CENTER
        );

        add(quantityPanel, BorderLayout.NORTH);
        add(lblIcon, BorderLayout.CENTER);
        add(lblName, BorderLayout.SOUTH);
    }
    
    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isSelectedCard() {
        return selected;
    }
    
    public void setSelectedCard(boolean selected) {
        this.selected = selected;

        if (selected) {
            setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

            setBackground(new Color(220, 235, 255));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            setBackground(Color.WHITE);
        }

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