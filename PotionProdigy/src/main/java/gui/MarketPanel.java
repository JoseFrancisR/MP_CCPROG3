/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import gui.components.MarketItemCard;
import controller.Controller;
import javax.swing.ButtonGroup;
import gui.components.MarketItemCard;
import javax.swing.JOptionPane;
import gui.components.Theme;
import java.util.ArrayList;
import gui.components.SellItemCard;
import java.util.List;

/**
 * Displays the market's buying and selling interfaces and manages the user's temporary market selections.
 */
public class MarketPanel extends javax.swing.JPanel {
    private MainFrame mainFrame;
    private Controller controller;
    private ArrayList<Integer> selectedSlotNumbers = new ArrayList<>();
    private ArrayList<SellItemCard> sellCards = new ArrayList<>();
    
    /**
     * Creates a market panel connected to the application.
     *
     * @param mainFrame application window used for navigation
     * @param controller controller used for market transactions
     */
    public MarketPanel(MainFrame mainFrame, Controller controller) {
        initComponents();

        this.mainFrame = mainFrame;
        this.controller = controller;

        javax.swing.ButtonGroup modeGroup = new javax.swing.ButtonGroup();
        modeGroup.add(radioBtnBuy);
        modeGroup.add(radioBtnSell);
        radioBtnBuy.setSelected(true);
        
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(15);
    }
    /**
     * Refreshes either the buying or selling interface according to the selected market mode
     */
    public void refreshDisplay() {
        if (radioBtnBuy.isSelected()) {
            refreshBuyView();
        } else {
            refreshSellView();
        }
    }
    /**
     * Rebuilds the available market listings and restores their selection states
     */
    public void refreshBuyView() {
        if (controller != null && controller.getMarket() != null) {
            java.util.ArrayList<potionprodigy.Listing> listings = controller.getAvailableListings();
            javax.swing.JPanel[] buySlots = {buySlot1, buySlot2, buySlot3, buySlot4, buySlot5, buySlot6, buySlot7, buySlot8};

            //Clear the slots first
            for (int i = 0; i < buySlots.length; i++) {
                buySlots[i].removeAll();
                buySlots[i].setLayout(new java.awt.BorderLayout());
            }

            for (potionprodigy.Listing listing : listings) {
                int slotNumber = listing.getSlotNumber(); 
                int slotIndex = slotNumber - 1;           

                if(slotIndex >= 0 && slotIndex < buySlots.length) {
                    MarketItemCard card = new MarketItemCard(listing);
                    Theme.apply(card);

                    if(controller.isSlotAvailable(slotNumber)) {
                        card.setSelectedCard(selectedSlotNumbers.contains(slotNumber));

                        card.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent e) {
                                toggleSlotSelection(slotNumber);
                            }
                        });
                    } else { // when the slot isn't available
                        card.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1));
                    }
                    buySlots[slotIndex].add(card, java.awt.BorderLayout.CENTER);
                }
            }

            // Revalidate and repaint all slot panels
            for (int i = 0; i < buySlots.length; i++) {
                buySlots[i].revalidate();
                buySlots[i].repaint();
            }

            int totalCost = controller.calculateTotalCost(new ArrayList<>(selectedSlotNumbers));
            totalCostLabel.setText("Total: " + totalCost + " Crystals");
        }
    }
    /**
     * Selects or deselects a market slot.
     *
     * @param slotNumber one-based slot number to toggle
     */
    private void toggleSlotSelection(int slotNumber) {
        if (selectedSlotNumbers.contains(slotNumber)) {
            selectedSlotNumbers.remove(Integer.valueOf(slotNumber));
        } else {
            selectedSlotNumbers.add(slotNumber);
        }
        refreshDisplay();
    }
    /**
     * Validates and processes the currently selected purchases.
     */
    private void handleConfirmPurchase() {
        int totalCost, currentCrystals;
        if(!selectedSlotNumbers.isEmpty()) {
            totalCost = controller.calculateTotalCost(new ArrayList<>(selectedSlotNumbers));
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to buy the selected items for " + totalCost + " Crystals?",
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if(confirm == JOptionPane.YES_OPTION){
                ArrayList<Integer> selectedSlots = new ArrayList<>(selectedSlotNumbers);
                ArrayList<Integer> results = controller.buyItems(selectedSlots);

                boolean allSucceeded = true;
                boolean insufficientCrystals = false;

                for(int status : results) {
                    if(status == -3) {
                        insufficientCrystals = true;
                        allSucceeded = false;
                    } else if(status != 1) {
                        allSucceeded = false;
                    }
                }

                if(allSucceeded) {
                    JOptionPane.showMessageDialog(this, 
                        "Successfully purchased all selected items!", 
                        "Purchase Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                    selectedSlotNumbers.clear();
                    refreshDisplay();
                } else if(insufficientCrystals) {
                    currentCrystals = controller.getCurrentPlayer().getCrystals();
            
                    JOptionPane.showMessageDialog(this, 
                        "Transaction failed! Total cost is " + totalCost + " crystals, but you only have " + currentCrystals + " crystals.", 
                        "Insufficient Crystals", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Transaction failed. One or more items are no longer available.", 
                        "Purchase Failed", 
                        JOptionPane.ERROR_MESSAGE);
                    selectedSlotNumbers.clear();
                    refreshDisplay();
                }
            } 
        } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select at least one item to purchase.", 
                    "No Items Selected", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    /**
     * Rebuilds the selling interface using the player's current inventory
     */
    public void refreshSellView() {
        jPanel2.removeAll();

        jPanel2.setLayout(new java.awt.GridLayout(0, 4, 10, 10));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(15);
    
        // Clear previously tracked sell cards
        sellCards.clear();

        if(controller != null) {
            potionprodigy.Player player = controller.getCurrentPlayer();
            if(player != null && player.getInventory() != null) {
                for(potionprodigy.ItemStack stack : player.getInventory().getIngredientStacks()) {
                    if(stack.getQuantity() > 0) {
                        SellItemCard card = new gui.components.SellItemCard(stack);
                        Theme.apply(card);
                        card.setQuantityChangeListener(() -> updateTotalSellEarnings());
                        sellCards.add(card);
                        jPanel2.add(card);
                    }
                }
            }
        }

        updateTotalSellEarnings();
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    /**
     * Recalculates and displays the total value of all selected sell quantities
     */
    private void updateTotalSellEarnings() {
        int totalEarnings = 0;

        if(controller != null) {
        java.util.ArrayList<potionprodigy.ItemStack> selectedStacks = new java.util.ArrayList<>();

            for(gui.components.SellItemCard card : sellCards) {
                int qty = card.getSelectedQuantity();
                if(qty > 0) {
                    selectedStacks.add(new potionprodigy.ItemStack(card.getItemStack().getIngredient(), qty));
                }
            }
            totalEarnings = controller.calculateTotalSellValue(selectedStacks);
        }

        totalCostLabel.setText("Total Earnings: " + totalEarnings + " Crystals");
    }
    /**
     * Validates and processes the items selected for sale.
     */
    private void handleConfirmSell() {
        int qty, earnings;
        ArrayList<potionprodigy.ItemStack> itemsToSell = new ArrayList<>();

        for(gui.components.SellItemCard card : sellCards) {
            qty = card.getSelectedQuantity();
            if (qty > 0) {
                itemsToSell.add(new potionprodigy.ItemStack(card.getItemStack().getIngredient(), qty));
            }
        }

        if (itemsToSell.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one item to sell.",
                "No Items Selected",
                JOptionPane.WARNING_MESSAGE);
        } else {
            earnings = controller.calculateTotalSellValue(itemsToSell);
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to sell the selected items for " + earnings + " Crystals?",
                "Confirm Sale",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if(confirm == JOptionPane.YES_OPTION) {
                controller.sellItems(itemsToSell);

                JOptionPane.showMessageDialog(this,
                    "Successfully sold items for " + earnings + " crystals!",
                    "Sale Successful",
                    JOptionPane.INFORMATION_MESSAGE);

                refreshDisplay();
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        Header = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        radioBtnBuy = new javax.swing.JRadioButton();
        radioBtnSell = new javax.swing.JRadioButton();
        centerCardPanel = new javax.swing.JPanel();
        buyItemSlots = new javax.swing.JPanel();
        buySlot1 = new javax.swing.JPanel();
        buySlot2 = new javax.swing.JPanel();
        buySlot3 = new javax.swing.JPanel();
        buySlot4 = new javax.swing.JPanel();
        buySlot5 = new javax.swing.JPanel();
        buySlot6 = new javax.swing.JPanel();
        buySlot7 = new javax.swing.JPanel();
        buySlot8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        Footer = new javax.swing.JPanel();
        totalCostLabel = new javax.swing.JLabel();
        bottomButtonsContainer = new javax.swing.JPanel();
        btnConfirm = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(710, 550));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        Header.setPreferredSize(new java.awt.Dimension(242, 50));
        Header.setLayout(new javax.swing.BoxLayout(Header, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Market");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setAlignmentX(0.5F);
        Header.add(jLabel1);

        radioBtnBuy.setText("Buy");
        radioBtnBuy.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        radioBtnBuy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        radioBtnBuy.addActionListener(this::radioBtnBuyActionPerformed);
        jPanel14.add(radioBtnBuy);

        radioBtnSell.setText("Sell");
        radioBtnSell.addActionListener(this::radioBtnSellActionPerformed);
        jPanel14.add(radioBtnSell);

        Header.add(jPanel14);

        jPanel1.add(Header, java.awt.BorderLayout.NORTH);

        centerCardPanel.setLayout(new java.awt.CardLayout());

        buyItemSlots.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 20));
        buyItemSlots.setLayout(new java.awt.GridLayout(2, 4, 10, 10));

        buySlot1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot1);

        buySlot2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot2);

        buySlot3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot3);

        buySlot4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot4);

        buySlot5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot5);

        buySlot6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot6);

        buySlot7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot7);

        buySlot8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        buyItemSlots.add(buySlot8);

        centerCardPanel.add(buyItemSlots, "card2");

        jScrollPane1.setBorder(null);

        jPanel2.setLayout(new java.awt.GridLayout(2, 4, 10, 0));
        jScrollPane1.setViewportView(jPanel2);

        centerCardPanel.add(jScrollPane1, "card4");

        jPanel1.add(centerCardPanel, java.awt.BorderLayout.CENTER);

        Footer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        Footer.setPreferredSize(new java.awt.Dimension(200, 100));
        Footer.setLayout(new java.awt.BorderLayout());

        totalCostLabel.setText("Total:");
        Footer.add(totalCostLabel, java.awt.BorderLayout.CENTER);

        bottomButtonsContainer.setPreferredSize(new java.awt.Dimension(171, 33));

        btnConfirm.setText("Confirm");
        btnConfirm.addActionListener(this::btnConfirmActionPerformed);
        bottomButtonsContainer.add(btnConfirm);

        jButton2.setText("Back");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        bottomButtonsContainer.add(jButton2);

        Footer.add(bottomButtonsContainer, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(Footer, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Processes the current buying or selling confirmation
     *
     * @param evt generated button action event
     */
    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        if (radioBtnBuy.isSelected()) {
            handleConfirmPurchase();
        } else {
            handleConfirmSell();
        }
    }//GEN-LAST:event_btnConfirmActionPerformed
    /**
     * Returns to the main menu and clears temporary market selections
     *
     * @param evt generated button action event
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (mainFrame != null) {
            mainFrame.showMainMenu();
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    /**
     * Switches the market interface to buying mode
     *
     * @param evt generated radio-button action event
     */
    private void radioBtnBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBtnBuyActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) centerCardPanel.getLayout();
        cl.show(centerCardPanel, "card2"); // Opens Buy view
    
        refreshDisplay();
    }//GEN-LAST:event_radioBtnBuyActionPerformed
    /**
     * Switches the market interface to selling mode
     *
     * @param evt generated radio-button action event
     */
    private void radioBtnSellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBtnSellActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) centerCardPanel.getLayout();
        cl.show(centerCardPanel, "card4"); // Opens Buy view
    
        refreshDisplay();
    }//GEN-LAST:event_radioBtnSellActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Footer;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel bottomButtonsContainer;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JPanel buyItemSlots;
    private javax.swing.JPanel buySlot1;
    private javax.swing.JPanel buySlot2;
    private javax.swing.JPanel buySlot3;
    private javax.swing.JPanel buySlot4;
    private javax.swing.JPanel buySlot5;
    private javax.swing.JPanel buySlot6;
    private javax.swing.JPanel buySlot7;
    private javax.swing.JPanel buySlot8;
    private javax.swing.JPanel centerCardPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton radioBtnBuy;
    private javax.swing.JRadioButton radioBtnSell;
    private javax.swing.JLabel totalCostLabel;
    // End of variables declaration//GEN-END:variables
}
