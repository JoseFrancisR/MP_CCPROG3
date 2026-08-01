/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import potionprodigy.Listing;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
/**
 *
 * @author ikoyg
 */
public class ItemCard extends JPanel {

    private JLabel lblName;
    private JLabel lblQuantity;
    private JLabel lblCost;

    public ItemCard(Listing listing) {
        initComponents();
        
        if (listing != null && listing.getIngredient() != null) {
            lblName.setText(listing.getIngredient().getName());
            lblQuantity.setText("Qty: " + listing.getQuantity());
            lblCost.setText(listing.getUnitPrice() * listing.getQuantity() + " Crystals");
        }
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));

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

        add(lblName);
        add(lblQuantity);
        add(lblCost);
    }
}
