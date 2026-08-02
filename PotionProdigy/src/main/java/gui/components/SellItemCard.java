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
/**
 * Visual card used to select how many units of an ingredient will be sold.
 */
public class SellItemCard extends JPanel {

    private final ItemStack ITEM_STACK;
    private Runnable quantityChangeListener;
    private int selectedQuantity = 0;

    private JLabel lblIcon;
    private JLabel lblName;
    private JLabel lblPrice;
    private JLabel lblSelectedQty;
    private JButton btnMinus;
    private JButton btnPlus;
    /**
     * Creates a selling card for an inventory stack.
     *
     * @param itemStack inventory stack available for sale
     */
    public SellItemCard(ItemStack ITEM_STACK) {
        this.ITEM_STACK = ITEM_STACK;
        initializeCard();
    }
    /**
     * Creates and arranges the selling controls, labels, price, and icon.
     */
    private void initializeCard() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(140, 180));

        setOpaque(true);
        setBackground(Theme.SECONDARY);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        int unitPrice = ITEM_STACK.getIngredient().getSellingPrice();
        lblPrice = new JLabel(unitPrice + " Crystals per unit", SwingConstants.CENTER);
        lblPrice.setOpaque(true);
        lblPrice.setBackground(Theme.DARK);
        lblPrice.setForeground(Theme.TEXT);
        lblPrice.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pricePanel.setOpaque(false);
        pricePanel.add(lblPrice);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        String iconPath;
        if (ITEM_STACK.getIngredient().isFruit()) {
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

        lblName = new JLabel(ITEM_STACK.getIngredient().getName(), SwingConstants.CENTER);
        lblName.setForeground(Theme.TEXT);

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

        lblSelectedQty = new JLabel("0/" + ITEM_STACK.getQuantity(), SwingConstants.CENTER);
        lblSelectedQty.setPreferredSize(new Dimension(45, 22));
        lblSelectedQty.setForeground(Theme.TEXT);

        btnMinus.addActionListener(e -> updateQuantity(-1));
        btnPlus.addActionListener(e -> updateQuantity(1));

        controlPanel.add(btnMinus);
        controlPanel.add(lblSelectedQty);
        controlPanel.add(btnPlus);

        add(pricePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    /**
     * Increases or decreases the selected quantity while enforcing its valid
     * range.
     *
     * @param change quantity adjustment, normally {@code -1} or {@code 1}
     */
    private void updateQuantity(int change) {
        int newQty = selectedQuantity + change;
        if (newQty >= 0 && newQty <= ITEM_STACK.getQuantity()) {
            selectedQuantity = newQty;
            lblSelectedQty.setText(selectedQuantity + "/" + ITEM_STACK.getQuantity());
            
            setSelectedCard(selectedQuantity > 0);

            if (quantityChangeListener != null) {
                quantityChangeListener.run();
            }
        }
    }
    /**
     * Returns the quantity currently selected for sale.
     *
     * @return selected selling quantity
     */
    public int getSelectedQuantity() {
        return selectedQuantity;
    }
    /**
     * Returns the inventory stack represented by this card.
     *
     * @return represented item stack
     */
    public ItemStack getItemStack() {
        return ITEM_STACK;
    }
    /**
     * Registers a callback that runs whenever the selected quantity changes.
     *
     * @param listener callback used to update the market's total earnings
     */
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
    /**
     * Updates the card's appearance according to whether at least one unit is selected.
     *
     * @param selected {@code true} to display the selected style
     */
    public void setSelectedCard(boolean selected) {
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 3));
            setBackground(Theme.SELECTED_BACKGROUND);
        } else {
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            setBackground(Theme.SECONDARY);
        }
        
        revalidate();
        repaint();
    }
}