package src.main;

import java.util.ArrayList;

/**
 * Contains the processing rules for recipe-mode and creative-mode brewing.
 */
public class Brew {

    /**
     * Brews an already-unlocked recipe.
     *
     * @param player active player
     * @param recipe selected recipe
     * @return true when brewing succeeds
     */
    public boolean brewRecipe(Player player, Recipe recipe) {
        boolean success = true;
        
        if(!hasEnoughIngredients(player, recipe)) {
            success = false;
        }
        
        if(!player.getRecipeBook().isUnlocked(recipe.getConcoctionId())) {
            success = false;
        }

        if (success){
            consumeIngredients(player, recipe);
            sellConcoction(player, recipe);
        }

        return success;
    }

    /**
     * Attempts to discover and brew a creative-mode recipe.
     *
     * @param player active player
     * @param base   selected concoction base
     * @param fruits one to three selected fruits
     * @return true when the mixture matches a valid recipe
     */
    public boolean brewCreative(Player player, Ingredient base, ArrayList<Ingredient> fruits) {
        boolean success = true;
        boolean validFruits = validateUniqueFruits(fruits);

        if(!validFruits) {
            success = false;
        }

        Recipe checkRecipe = player.getRecipeBook().findRecipe(base, fruits);
        
        if(checkRecipe == null) {
            damageCauldron(player);
            success = false;
        }
        if(checkRecipe != null && !hasEnoughIngredients(player, checkRecipe)) {
            success = false;
        }
        
        if (success) {
            consumeIngredients(player, checkRecipe);
            sellConcoction(player, checkRecipe);
            player.getRecipeBook().unlockRecipe(checkRecipe.getConcoctionId());
        }
        
        return success;
    }

    /**
     * Checks the one-to-three-fruit rule and prevents duplicate fruits.
     *
     * @param fruits selected fruits
     * @return true when the selection is valid
     */
    private boolean validateUniqueFruits(ArrayList<Ingredient> fruits) {
        int i, j;
        boolean success = true;

        if(fruits.size() < 1 || fruits.size() > 3) {
            success = false;
        }

        for(i = 0; i < fruits.size(); i++) {
            for(j = i + 1; j < fruits.size(); j++) {
                if(fruits.get(i).isEqual(fruits.get(j))) {
                    success = false;
                }
            }
        }

        return success;
    }

    /**
     * Checks whether the inventory contains the required ingredients.
     *
     * @param player active player
     * @param recipe selected recipe
     * @return true when enough ingredients are available
     */
    private boolean hasEnoughIngredients(Player player, Recipe recipe) {
        return player.getInventory().hasIngredients(recipe);
    }

    /**
     * Removes the required ingredients after brewing is confirmed.
     *
     * @param player active player
     * @param recipe brewed recipe
     */
    private void consumeIngredients(Player player, Recipe recipe) {
        Inventory inventory = player.getInventory();

        for(Ingredient item : recipe.getRequiredIngredients()) {
            inventory.removeIngredient(item, recipe.getRequiredQuantity(item));
        }
    }

    /**
     * Automatically sells a successful concoction.
     *
     * @param player active player
     * @param recipe brewed recipe
     */
    private void sellConcoction(Player player, Recipe recipe) {
        player.addCrystals(recipe.getSaleValue());
    }

    /**
     * Makes one cauldron unusable after a failed creative experiment.
     *
     * @param player active player
     */
    private void damageCauldron(Player player) {
        player.getInventory().damageCauldron();
    }
}