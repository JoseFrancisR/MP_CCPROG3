package potionprodigy;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
     * @return true when all recipes are loaded successfully
     */
    public boolean loadRecipes() {
        File targetFile = new File("src/data/recipeCatalog.csv");
        ArrayList<Recipe> loadedRecipes = new ArrayList<>();
        int id = 0, saleValue = 0;
        String name = null;
        Ingredient base = null;
        ArrayList<Ingredient> fruits = null;
        boolean success = false;
        try {
            File actualFile = targetFile.getCanonicalFile();
            if (actualFile.exists() && actualFile.isFile()) {
                Scanner scanner = new Scanner(actualFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()){
                        String[] parts = line.split(",", -1); // -1 to include trailing empty strings
                        id = Integer.parseInt(parts[0].trim());
                        name = parts[1].trim();
                        base = Ingredient.findIngredient(parts[2].trim());
                        saleValue = Integer.parseInt(parts[3].trim());
                        fruits = new ArrayList<>();
                        for (int i = 4; i <= 6; i++) {
                            String fruitName = parts[i].trim();
                            if (!fruitName.isEmpty()) {
                                Ingredient fruit = Ingredient.findIngredient(fruitName);
                                if (fruit != null) {
                                    fruits.add(fruit);
                                }
                            }
                        }
                        if (base != null) {
                            loadedRecipes.add(new Recipe(id, name, base, fruits, saleValue));
                        } else {
                            System.out.println("Base error");
                        }
                    }
                }
                this.recipes = loadedRecipes;
                scanner.close();
                success = true;
            } else {
                System.out.println("ERROR: Recipe catalog file not found at " + actualFile.getAbsolutePath());
            }

        } catch (Exception e) {
            System.out.println("ERROR: Failed to load recipes from " + targetFile.getAbsolutePath() + ": " + e.getMessage());
        }
        return success;
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
        Recipe found = null;
        for (Recipe recipe : recipes) {
            if (recipe.getConcoctionId() == id) {
                found = recipe;
            }
        }
        return found;
    }

    /**
     * Searches for a recipe matching the selected base and fruits.
     *
     * @param base   selected base
     * @param fruits selected unique fruits
     * @return matching recipe, or null when the mixture is invalid
     */
    public Recipe findRecipe(Ingredient base, ArrayList<Ingredient> fruits) {
        Recipe found = null;
        for (Recipe recipe : recipes) {
            if (recipe.matches(base, fruits)) {
                found = recipe;
            }
        }
        return found;
    }

    /**
     * Adds a recipe ID to the unlocked list when it is not yet present.
     *
     * @param id concoction ID
     * @return true when the recipe becomes newly unlocked
     */
    public boolean unlockRecipe(int id) {
        boolean flag = false;
        if (!isUnlocked(id)) {
            unlockedRecipeIds.add(id);
            flag = true;
        }
        return flag;
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
    public ArrayList<Recipe> getUnlockedRecipes() {
        ArrayList<Recipe> unlockedRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (isUnlocked(recipe.getConcoctionId())) {
                unlockedRecipes.add(recipe);
            }
        }
        return unlockedRecipes;
    }
}
