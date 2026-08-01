/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.components;

import potionprodigy.Listing;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;
/**
 *
 * @author ikoyg
 */
public class MarketItemCard extends JPanel {

    private JLabel lblIcon;
    private JLabel lblName;
    private JLabel lblQuantity;
    private JLabel lblCost;

    public MarketItemCard(Listing listing) {
        initComponents();
        
        if (listing != null) {
            //First check if its a cauldron
            if(listing.isCauldronListing()){
                lblName.setText("Cauldron");
                lblQuantity.setText("Qty: 1");
                lblCost.setText(listing.getUnitPrice() + " Crystals");
            
                ImageIcon cauldronIcon = loadIcon("/images/cauldron.png", 80, 80);
                if (cauldronIcon != null) {
                    lblIcon.setIcon(cauldronIcon);
                    lblIcon.setText("");
                } else {
                    lblIcon.setText("[ CAULDRON ]");
                }
            }
            else if(listing.getIngredient() != null) {
                lblName.setText(listing.getIngredient().getName());
                lblQuantity.setText("Qty: " + listing.getQuantity());
                lblCost.setText(listing.getUnitPrice() * listing.getQuantity() + " Crystals");

                String iconPath;
                if(listing.getIngredient().isFruit()) {
                    iconPath = "/images/fruits.png";
                } else if (listing.isCauldronListing()){
                    iconPath = "/images/cauldron.png";
                } else {
                    iconPath = "/images/bases.png";
                }

                ImageIcon itemIcon = loadIcon(iconPath, 80, 80);
                if (itemIcon != null) {
                    lblIcon.setIcon(itemIcon);
                    lblIcon.setText("");
                } else { //When cant load the icon
                    lblIcon.setText("[ PLACEHOLDER ]");
                }
            }
        } 
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));

        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblName = new JLabel("Item Name");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblQuantity = new JLabel("Qty: 0");
        lblQuantity.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblCost = new JLabel("0 Crystals");
        lblCost.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblCost.setForeground(new Color(180, 100, 0));
        lblCost.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblIcon);
        add(lblName);
        add(lblQuantity);
        add(lblCost);
    }

    private ImageIcon loadIcon(String resourcePath, int width, int height) {
        URL imageUrl = getClass().getResource(resourcePath);
        if (imageUrl != null) {
            Image originalImage = new ImageIcon(imageUrl).getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
}