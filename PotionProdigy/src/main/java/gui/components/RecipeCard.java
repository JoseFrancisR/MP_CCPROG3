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
    private final Recipe recipe;
    private final Inventory inventory;

    private final JRadioButton recipeRadioButton;
    private final JButton dropdownButton;
    private final JLabel craftableLabel;
    private final JLabel valueLabel;
    private final JPanel detailsPanel;

    private boolean expanded;
    private boolean selected;

    public RecipeCard(Recipe recipe, Inventory inventory, ButtonGroup recipeGroup, boolean selectable) {

        this.recipe = recipe;
        this.inventory = inventory;
        this.expanded = false;
        this.selected = false;

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Theme.SECONDARY);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        boolean craftable = inventory.hasIngredients(recipe);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        recipeRadioButton = new JRadioButton();
        recipeRadioButton.setVisible(selectable);
        recipeRadioButton.setEnabled(craftable);

        recipeRadioButton.addItemListener(event -> {
            setSelectedCard(
                recipeRadioButton.isSelected()
            );
        });
        
        if (recipeGroup != null) {
            recipeGroup.add(recipeRadioButton);
        }

        dropdownButton = new JButton("▶ " + String.format("#%03d ", recipe.getConcoctionId()) + recipe.getConcoctionName());

        dropdownButton.setHorizontalAlignment(JButton.LEFT);

        leftHeaderPanel.add(recipeRadioButton);
        leftHeaderPanel.add(dropdownButton);

        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        craftableLabel = new JLabel(craftable ? "Craftable |" : "Not Craftable |");

        valueLabel = new JLabel(
                recipe.getSaleValue() + " crystals"
        );

        rightHeaderPanel.add(craftableLabel);
        rightHeaderPanel.add(valueLabel);

        headerPanel.add(leftHeaderPanel, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        
        // allowing selection to be anywhere on the recipe card
        if (selectable && craftable) {
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent event) {
                    recipeRadioButton.setSelected(true);
                }
            });
        }
        
        headerPanel.setOpaque(false);
        leftHeaderPanel.setOpaque(false);
        rightHeaderPanel.setOpaque(false);
        
        recipeRadioButton.setOpaque(false);
        
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 35, 10, 10));

        createIngredientCheckboxes();
        detailsPanel.setVisible(false);
        detailsPanel.setOpaque(false);

        dropdownButton.addActionListener(event -> {
            toggleDetails();}
        );

        add(headerPanel, BorderLayout.NORTH);
        add(detailsPanel, BorderLayout.CENTER);
    }

    private void createIngredientCheckboxes() {
        detailsPanel.removeAll();

        for (Ingredient ingredient : recipe.getRequiredIngredients()) {
            int required = recipe.getRequiredQuantity(ingredient);

            int owned = inventory.getQuantity(ingredient);

            JCheckBox ingredientCheckBox = new JCheckBox(ingredient.getName() + "    " + owned + "/" + required);
            
            ingredientCheckBox.setSelected(owned >= required);
            
            ingredientCheckBox.setEnabled(false);

            detailsPanel.add(ingredientCheckBox);
        }
    }

    private void toggleDetails() {
        expanded = !expanded;
        detailsPanel.setVisible(expanded);
        
        String arrow = expanded ? "▼ " : "▶ ";
        
        dropdownButton.setText(arrow + String.format("#%03d ", recipe.getConcoctionId()) + recipe.getConcoctionName());
        
        revalidate();
        repaint();
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public JRadioButton getRecipeRadioButton() {
        return this.recipeRadioButton;
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