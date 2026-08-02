/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.components;

/**
 *
 * @author ikoyg
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import potionprodigy.ItemStack;

public class SellItemCard extends JPanel {

    private final ItemStack itemStack;
    private Runnable quantityChangeListener;
    private int selectedQuantity = 0;

    private JLabel lblIcon;
    private JLabel lblName;
    private JLabel lblPrice;
    private JLabel lblSelectedQty;
    private JButton btnMinus;
    private JButton btnPlus;

    public SellItemCard(ItemStack itemStack) {
        this.itemStack = itemStack;
        initializeCard();
    }

    private void initializeCard() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(140, 180));

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        int unitPrice = itemStack.getIngredient().getSellingPrice();
        lblPrice = new JLabel(unitPrice + " Crystals per unit", SwingConstants.CENTER);
        lblPrice.setOpaque(true);
    
        lblPrice.setBackground(Color.LIGHT_GRAY); 
        lblPrice.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        lblPrice.setForeground(Color.BLACK);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pricePanel.setOpaque(false);
        pricePanel.add(lblPrice);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        String iconPath;
        if (itemStack.getIngredient().isFruit()) {
            iconPath = "/images/fruits.png";
        } else {
            iconPath = "/images/bases.png";
        }
        ImageIcon itemIcon = loadIcon(iconPath, 60, 60);

        if (itemIcon != null) {
            lblIcon.setIcon(itemIcon);
        } else {
            lblIcon.setText("[ PLACEHOLDER ]");
        }

        lblName = new JLabel(itemStack.getIngredient().getName(), SwingConstants.CENTER);
        lblName.setForeground(Color.BLACK);

        centerPanel.add(lblIcon, BorderLayout.CENTER);
        centerPanel.add(lblName, BorderLayout.SOUTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
        controlPanel.setOpaque(false);

        btnMinus = new JButton("-");
        btnPlus = new JButton("+");
        btnMinus.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnPlus.setMargin(new java.awt.Insets(0, 0, 0, 0));

        btnMinus.setPreferredSize(new Dimension(38, 22)); //[cite: 9]
        btnPlus.setPreferredSize(new Dimension(38, 22));

        lblSelectedQty = new JLabel("0/" + itemStack.getQuantity(), SwingConstants.CENTER);
        lblSelectedQty.setPreferredSize(new Dimension(45, 22));
        lblSelectedQty.setForeground(Color.BLACK);

        btnMinus.addActionListener(e -> updateQuantity(-1));
        btnPlus.addActionListener(e -> updateQuantity(1));

        controlPanel.add(btnMinus);
        controlPanel.add(lblSelectedQty);
        controlPanel.add(btnPlus);

        add(pricePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void updateQuantity(int change) {
        int newQty = selectedQuantity + change;
        if (newQty >= 0 && newQty <= itemStack.getQuantity()) {
            selectedQuantity = newQty;
            lblSelectedQty.setText(selectedQuantity + "/" + itemStack.getQuantity());
            if (selectedQuantity > 0) {
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                setBackground(new Color(220, 235, 255));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                setBackground(Color.WHITE);
            }

            if (quantityChangeListener != null) {
                quantityChangeListener.run();
            }
            repaint();
        }
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
    
    public void setQuantityChangeListener(Runnable listener) {
        this.quantityChangeListener = listener;
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