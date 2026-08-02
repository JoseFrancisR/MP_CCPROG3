package gui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import potionprodigy.Ingredient;
import potionprodigy.Inventory;
import potionprodigy.Recipe;
/**
 * Expandable visual card representing a recipe and whether the player owns its required ingredients.
 */
public class RecipeCard extends JPanel {
    private final Recipe RECIPE;
    private final Inventory INVENTORY;

    private final JRadioButton RECIPE_RADIO_BUTTON;
    private final JButton DROPDOWN_BUTTON;
    private final JLabel CRAFTABLE_LABEL;
    private final JLabel VALUE_LABEL;
    private final JPanel DETAILS_PANEL;

    private boolean expanded;
    private boolean selected;
    /**
     * Creates a recipe card.
     *
     * @param recipe recipe displayed by the card
     * @param inventory inventory used to check ingredient availability
     * @param recipeGroup group used to enforce single recipe selection
     * @param selectable whether the recipe may be selected for brewing
     */
    public RecipeCard(Recipe RECIPE, Inventory INVENTORY, ButtonGroup recipeGroup, boolean selectable) {

        this.RECIPE = RECIPE;
        this.INVENTORY = INVENTORY;
        this.expanded = false;
        this.selected = false;

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Theme.SECONDARY);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        boolean craftable = INVENTORY.hasIngredients(RECIPE);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        RECIPE_RADIO_BUTTON = new JRadioButton();
        RECIPE_RADIO_BUTTON.setVisible(selectable);
        RECIPE_RADIO_BUTTON.setEnabled(craftable);

        RECIPE_RADIO_BUTTON.addItemListener(event -> {
            setSelectedCard(
                RECIPE_RADIO_BUTTON.isSelected()
            );
        });
        
        if (recipeGroup != null) {
            recipeGroup.add(RECIPE_RADIO_BUTTON);
        }

        DROPDOWN_BUTTON = new JButton("▶ " + String.format("#%03d ", RECIPE.getConcoctionId()) + RECIPE.getConcoctionName());

        DROPDOWN_BUTTON.setHorizontalAlignment(JButton.LEFT);

        leftHeaderPanel.add(RECIPE_RADIO_BUTTON);
        leftHeaderPanel.add(DROPDOWN_BUTTON);

        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CRAFTABLE_LABEL = new JLabel(craftable ? "Craftable |" : "Not Craftable |");

        VALUE_LABEL = new JLabel(
                RECIPE.getSaleValue() + " crystals"
        );

        rightHeaderPanel.add(CRAFTABLE_LABEL);
        rightHeaderPanel.add(VALUE_LABEL);

        headerPanel.add(leftHeaderPanel, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        
        // allowing selection to be anywhere on the recipe card
        if (selectable && craftable) {
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent event) {
                    RECIPE_RADIO_BUTTON.setSelected(true);
                }
            });
        }
        
        headerPanel.setOpaque(false);
        leftHeaderPanel.setOpaque(false);
        rightHeaderPanel.setOpaque(false);
        
        RECIPE_RADIO_BUTTON.setOpaque(false);
        
        DETAILS_PANEL = new JPanel();
        DETAILS_PANEL.setLayout(new BoxLayout(DETAILS_PANEL, BoxLayout.Y_AXIS));
        DETAILS_PANEL.setBorder(BorderFactory.createEmptyBorder(5, 35, 10, 10));

        createIngredientCheckboxes();
        DETAILS_PANEL.setVisible(false);
        DETAILS_PANEL.setOpaque(false);

        DROPDOWN_BUTTON.addActionListener(event -> {
            toggleDetails();}
        );

        add(headerPanel, BorderLayout.NORTH);
        add(DETAILS_PANEL, BorderLayout.CENTER);
    }
    /**
     * Rebuilds the disabled ingredient checkboxes and displays the owned and required quantities for each ingredient.
     */
    private void createIngredientCheckboxes() {
        DETAILS_PANEL.removeAll();

        for (Ingredient ingredient : RECIPE.getRequiredIngredients()) {
            int required = RECIPE.getRequiredQuantity(ingredient);

            int owned = INVENTORY.getQuantity(ingredient);

            JCheckBox ingredientCheckBox = new JCheckBox(ingredient.getName() + "    " + owned + "/" + required);
            
            ingredientCheckBox.setSelected(owned >= required);
            
            ingredientCheckBox.setEnabled(false);

            DETAILS_PANEL.add(ingredientCheckBox);
        }
    }
    /**
     * Expands or collapses the ingredient-details area.
     */
    private void toggleDetails() {
        expanded = !expanded;
        DETAILS_PANEL.setVisible(expanded);
        
        String arrow = expanded ? "▼ " : "▶ ";
        
        DROPDOWN_BUTTON.setText(arrow + String.format("#%03d ", RECIPE.getConcoctionId()) + RECIPE.getConcoctionName());
        
        revalidate();
        repaint();
    }
    /**
     * Returns the recipe represented by this card.
     *
     * @return displayed recipe
     */
    public Recipe getRecipe() {
        return this.RECIPE;
    }
    /**
     * Returns the radio button used to select this recipe.
     *
     * @return recipe-selection radio button
     */
    public JRadioButton getRecipeRadioButton() {
        return this.RECIPE_RADIO_BUTTON;
    }
    /**
     * Determines whether this recipe card is visually selected.
     *
     * @return true when selected
    */
    public boolean isSelectedCard() {
        return this.selected;
    }
    /**
     * Updates the recipe card's selection state and appearance.
     *
     * @param selected true to display the selected style
     */
    public void setSelectedCard(boolean selected) {
        this.selected = selected;
        
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 3));
        } else {
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            setBackground(Theme.SECONDARY);
        }
        
        revalidate();
        repaint();
    }
    /**
     * Allows the card to expand horizontally while preserving its preferred
     * height in a vertical list.
     *
     * @return maximum size used by the parent layout
     */
    @Override
    public Dimension getMaximumSize() {
        Dimension preferred = getPreferredSize();
        
        return new Dimension(Integer.MAX_VALUE, preferred.height);
    }
}