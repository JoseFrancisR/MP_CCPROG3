package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

    public RecipeCard(Recipe recipe, Inventory inventory, ButtonGroup recipeGroup, boolean selectable) {

        this.recipe = recipe;
        this.inventory = inventory;
        this.expanded = false;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        boolean craftable = inventory.hasIngredients(recipe);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        recipeRadioButton = new JRadioButton();
        recipeRadioButton.setVisible(selectable);
        recipeRadioButton.setEnabled(craftable);

        if (recipeGroup != null) {
            recipeGroup.add(recipeRadioButton);
        }

        dropdownButton = new JButton("▶ " + String.format("#%03d ", recipe.getConcoctionId()) + recipe.getConcoctionName());

        dropdownButton.setHorizontalAlignment(JButton.LEFT);

        leftHeaderPanel.add(recipeRadioButton);
        leftHeaderPanel.add(dropdownButton);

        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        craftableLabel = new JLabel(craftable ? "Craftable" : "Not Craftable");

        valueLabel = new JLabel(
                recipe.getSaleValue() + " crystals"
        );

        rightHeaderPanel.add(craftableLabel);
        rightHeaderPanel.add(valueLabel);

        headerPanel.add(leftHeaderPanel, BorderLayout.CENTER);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 35, 10, 10));

        createIngredientCheckboxes();
        detailsPanel.setVisible(false);

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
            
            ingredientCheckBox.setSelected(
                    owned >= required
            );
            
            ingredientCheckBox.setEnabled(false);

            detailsPanel.add(ingredientCheckBox);
        }
    }

    private void toggleDetails() {
        expanded = !expanded;
        detailsPanel.setVisible(expanded);

        if (expanded) {
            dropdownButton.setText("▼ " + String.format("#%03d ", recipe.getConcoctionId()) + recipe.getConcoctionName());
        } else {
            dropdownButton.setText("▶ " + String.format("#%03d ",recipe.getConcoctionId()) + recipe.getConcoctionName());
        }

        revalidate();
        repaint();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public JRadioButton getRecipeRadioButton() {
        return recipeRadioButton;
    }
}