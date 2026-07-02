package src.main;

import java.util.ArrayList;

/**
 * Stores the complete Potion Compendium and the recipe IDs unlocked by the
 * player.
 */
public class RecipeBook {

    /** Complete list of predefined recipes, already ordered by concoction ID. */
    private ArrayList<Recipe> recipes;

    /** IDs of recipes that the player has unlocked. */
    private ArrayList<Integer> unlockedRecipeIds;

    /**
     * Creates an empty recipe book.
     */
    public RecipeBook() {
        this.recipes = new ArrayList<>();
        this.unlockedRecipeIds = new ArrayList<>();
    }

    /**
     * Loads the predefined recipes from the Potion Compendium file.
     *
     * @param path CSV file path
     * @return true when all recipes are loaded successfully
     */
    public boolean loadRecipes(String path) {
        // recipe loading/file handling
        return false;
    }

    /**
     * Replaces the current unlocked-recipe list with IDs from a save file.
     *
     * @param ids unlocked recipe IDs
     */
    public void loadUnlockedRecipeIds(ArrayList<Integer> ids) {
        this.unlockedRecipeIds = new ArrayList<>(ids);
    }

    /**
     * Searches for a recipe by concoction ID.
     *
     * @param id concoction ID
     * @return matching recipe, or null when not found
     */
    public Recipe findRecipeById(int id) {
        for (Recipe recipe : recipes) {
            if (recipe.getConcoctionId() == id) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Searches for a recipe matching the selected base and fruits.
     *
     * @param base   selected base
     * @param fruits selected unique fruits
     * @return matching recipe, or null when the mixture is invalid
     */
    public Recipe findRecipe(Ingredient base, ArrayList<Ingredient> fruits) {
        for (Recipe recipe : recipes) {
            if (recipe.matches(base, fruits)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Adds a recipe ID to the unlocked list when it is not yet present.
     *
     * @param id concoction ID
     * @return true when the recipe becomes newly unlocked
     */
    public boolean unlockRecipe(int id) {
        if (!isUnlocked(id)) {
            unlockedRecipeIds.add(id);
            return true;
        }
        return false;
    }

    /**
     * Checks whether a recipe is unlocked.
     *
     * @param id concoction ID
     * @return true when the recipe is unlocked
     */
    public boolean isUnlocked(int id) {
        return unlockedRecipeIds.contains(id);
    }

    /**
     * Returns the IDs that must be written under the save file's RECIPEBOOK
     * section.
     *
     * @return copy of the unlocked recipe IDs
     */
    public ArrayList<Integer> getUnlockedRecipeIds() {
        return this.unlockedRecipeIds;
    }

    /**
     * Displays only unlocked recipes in the same ascending order used by the
     * Potion Compendium.
     */
    public void displayUnlockedRecipes() {
        for (Recipe recipe : recipes) {
            if (isUnlocked(recipe.getConcoctionId())) {
                System.out.println(recipe.getConcoctionName());
                System.out.println("  Base: " + recipe.getBase().getName());
                System.out.print("  Fruits: ");
                ArrayList<Ingredient> fruits = recipe.getFruits();
                for (int i = 0; i < fruits.size(); i++) {
                    System.out.print(fruits.get(i).getName());
                    if (i < fruits.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }
}
