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

    private void toggleDetails() {
        expanded = !expanded;
        DETAILS_PANEL.setVisible(expanded);
        
        String arrow = expanded ? "▼ " : "▶ ";
        
        DROPDOWN_BUTTON.setText(arrow + String.format("#%03d ", RECIPE.getConcoctionId()) + RECIPE.getConcoctionName());
        
        revalidate();
        repaint();
    }

    public Recipe getRecipe() {
        return this.RECIPE;
    }

    public JRadioButton getRecipeRadioButton() {
        return this.RECIPE_RADIO_BUTTON;
    }
    
    public boolean isSelectedCard() {
        return this.selected;
    }
    
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
    
    @Override
    public Dimension getMaximumSize() {
        Dimension preferred = getPreferredSize();
        
        return new Dimension(Integer.MAX_VALUE, preferred.height);
    }
}