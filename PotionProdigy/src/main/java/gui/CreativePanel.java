/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.util.ArrayList;
import gui.components.ItemCard;
import gui.components.Theme;
import potionprodigy.Inventory;
import potionprodigy.Player;
import potionprodigy.ItemStack;
import potionprodigy.Ingredient;
import potionprodigy.Recipe;
import controller.Controller;
import java.util.Stack;


/**
 * Allows the player to select one concoction base and one to three unique fruits for an experimental brew.
 */
public class CreativePanel extends javax.swing.JPanel {
    private MainFrame mainFrame;
    private Controller controller;
    
    private Ingredient selectedBase = null;
    private ItemCard selectedBaseCard = null;
    
    private ArrayList<Ingredient> selectedFruits = new ArrayList<>();
    private ArrayList<ItemCard> selectedFruitCards = new ArrayList<>();
    
    /**
     * Creates a creative-mode panel connected to the application.
     *
     * @param mainFrame application window used for navigation
     * @param controller controller used for creative brewing
     */
    public CreativePanel(MainFrame mainFrame, Controller controller) {
        initComponents();
        inventoryItemsPanel.setLayout(new java.awt.GridLayout(0, 4, 12, 12));
        inventoryItemsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        inventoryScrollPane.getVerticalScrollBar().setUnitIncrement(15);
        
        this.mainFrame = mainFrame;
        this.controller = controller;
    }
    /**
     * Clears previous selections and rebuilds the selectable ingredient cards using the current player's inventory.
     */
    public void refreshDisplay() {
        selectedBase = null;
        selectedBaseCard = null;
        selectedFruits.clear();
        selectedFruitCards.clear();
        
        inventoryItemsPanel.removeAll();
        Stack<ItemStack> bases = new Stack<>();

        Player player = controller.getCurrentPlayer();

        if (player != null) {
            for (ItemStack stack : player.getInventory().getIngredientStacks()) {
                if (stack.getQuantity() > 0 && stack.getIngredient().getName().contains("BASE")) {
                    bases.push(stack);
                } else if (stack.getQuantity() > 0 && !stack.getIngredient().getName().contains("BASE")) {
                    ItemCard card = new ItemCard(stack);
                    Theme.apply(card);

                    card.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent event) {
                            handleItemSelection(card);
                        }
                    });
                    
                    inventoryItemsPanel.add(card);
                }
            }
            
            for (ItemStack stack : bases) {
                ItemCard card = new ItemCard(stack);
                
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent event) {
                        handleItemSelection(card);
                    }
                });
                
                inventoryItemsPanel.add(card);
            }
        }
        Inventory inventory = player.getInventory();

        lblCauldrons.setText("Cauldrons: " + inventory.countUsableCauldrons()
                + " usable / " + inventory.countUnusableCauldrons() + " unusable");

        inventoryItemsPanel.revalidate();
        inventoryItemsPanel.repaint();
    }
    /**
     * Routes a clicked card to either base-selection or fruit-selection logic.
     *
     * @param card ingredient card clicked by the player
     */
    private void handleItemSelection(ItemCard card) {
        Ingredient ingredient = card.getItemStack().getIngredient();
    
        if (ingredient.isConcoctionBase()) {
            handleBaseSelection(card, ingredient);
        } else {
            handleFruitSelection(card, ingredient);
        }
    }
    /**
     * Selects or deselects a fruit while enforcing the three-fruit limit.
     *
     * @param card card representing the selected fruit
     * @param fruit fruit represented by the card
     */
    private void handleFruitSelection(ItemCard card, Ingredient fruit) {
        int existingIndex = findSelectedFruitIndex(fruit);
        
        if (existingIndex >= 0) {
            selectedFruits.remove(fruit);
            ItemCard removedCard = selectedFruitCards.remove(existingIndex);
            removedCard.setSelectedCard(false);
            card.setSelectedCard(false);
        } else if (selectedFruits.size() >= 3) {
            javax.swing.JOptionPane.showMessageDialog(this, "You may only select up to three fruits.",
                "Invalid Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
            selectedFruits.add(fruit);
            selectedFruitCards.add(card);
            card.setSelectedCard(true);
        }
    }
    /**
     * Selects a base or clears the currently selected base.
     *
     * @param card card representing the selected base
     * @param base base represented by the card
     */
    private void handleBaseSelection(ItemCard card, Ingredient base) {
        if (selectedBase == base) {
            card.setSelectedCard(false);
            selectedBase = null;
            selectedBaseCard = null;
        } else {
            if (selectedBaseCard != null) {
                selectedBaseCard.setSelectedCard(false);
            }
            selectedBaseCard = card;
            selectedBase = base;
            
            card.setSelectedCard(true);
        } 
    }
    /**
     * Finds the selected-fruit index corresponding to an ingredient.
     *
     * @param ingredient ingredient to locate
     * @return selected-fruit index, or {@code -1} when not selected
     */
    private int findSelectedFruitIndex(Ingredient ingredient) {
        int foundIndex = -1;
        
        for (int i = 0; i < selectedFruits.size() && foundIndex == -1; i++) {
            if (selectedFruits.get(i).isEqual(ingredient)) {
                foundIndex = i;
            }
        }
        
        return foundIndex;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inventoryScrollPane = new javax.swing.JScrollPane();
        inventoryItemsPanel = new javax.swing.JPanel();
        lblCauldrons = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnBrew = new javax.swing.JButton();
        lblText = new javax.swing.JLabel();

        inventoryScrollPane.setBorder(null);

        inventoryItemsPanel.setLayout(new java.awt.GridLayout(1, 0));
        inventoryScrollPane.setViewportView(inventoryItemsPanel);

        lblCauldrons.setText("Cauldrons");

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        btnBack.setText("Back");
        btnBack.addActionListener(this::btnBackActionPerformed);
        jPanel1.add(btnBack);

        btnBrew.setText("Brew a Concoction");
        btnBrew.addActionListener(this::btnBrewActionPerformed);
        jPanel1.add(btnBrew);

        lblText.setText("Select your ingredients and base.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inventoryScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
                        .addComponent(lblCauldrons)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCauldrons)
                    .addComponent(lblText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Returns to the brewing-mode selection screen.
     *
     * @param evt generated button action event
     */
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        mainFrame.showBrew();
    }//GEN-LAST:event_btnBackActionPerformed
    /**
     * Validates the selected ingredients and attempts an experimental brew.
     *
     * @param evt generated button action event
     */
    private void btnBrewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrewActionPerformed
        Player player = controller.getCurrentPlayer();
        boolean success = false;
        Recipe recipe = null;
        int confirmation;
        
        if (player != null) {
            if (player.getInventory().countUsableCauldrons() > 1) {
                if (selectedBase != null) {
                    if (!selectedFruits.isEmpty()) {
                        ArrayList<Ingredient> fruits = new ArrayList<>(selectedFruits);
                        StringBuilder fruitNames = new StringBuilder();
                        
                        for (int i = 0; i < fruits.size(); i++) {
                            if (i > 0) {
                                fruitNames.append(", ");
                            }
                            fruitNames.append(fruits.get(i).getName());
                        }
                        
                        confirmation = javax.swing.JOptionPane.showConfirmDialog(this, "Continue with this experiment?\n\n" + "Base: " 
                                + selectedBase.getName() + "\nFruits: " + fruitNames + "\n\nAn invalid combination will damage a cauldron.",
                                "Confirm Brewing", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                        
                        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
                            recipe = player.getRecipeBook().findRecipe(selectedBase, fruits);
                            success = controller.brewCreative(selectedBase, fruits);

                            if (success && recipe != null) {
                                javax.swing.JOptionPane.showMessageDialog(this, recipe.getConcoctionName() + " was successfully brewed!\n" + 
                                        "You earned " + recipe.getSaleValue() + " crystals.\n" + "The recipe is now unlocked!", 
                                        "Brewing Successful", javax.swing.JOptionPane.WARNING_MESSAGE);
                            } else if (recipe == null) {
                                javax.swing.JOptionPane.showMessageDialog(this, "The ingredients did not create a valid concoction.\n" + 
                                        "One usable cauldron was damaged. D:", 
                                        "Brewing Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
                            } else if (!success) {
                                javax.swing.JOptionPane.showMessageDialog(this, "The concoction could not be brewed.", 
                                        "Brewing Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        refreshDisplay();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this, "Please select at least one fruit.", 
                                "No Fruit Selected", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Please select one concoction base.", 
                            "No Base Selected", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Creative Mode requires more than one usable cauldron.", 
                        "Not Enough Cauldrons", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnBrewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBrew;
    private javax.swing.JPanel inventoryItemsPanel;
    private javax.swing.JScrollPane inventoryScrollPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCauldrons;
    private javax.swing.JLabel lblText;
    // End of variables declaration//GEN-END:variables
}
