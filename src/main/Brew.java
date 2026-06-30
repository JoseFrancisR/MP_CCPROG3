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
        // code here
        return false;
    }

    /**
     * Attempts to discover and brew a creative-mode recipe.
     *
     * @param player active player
     * @param base selected concoction base
     * @param fruits one to three selected fruits
     * @return true when the mixture matches a valid recipe
     */
    public boolean brewCreative(Player player, Ingredient base, ArrayList<Ingredient> fruits) {
        // code here
        return false;
    }

    /**
     * Checks the one-to-three-fruit rule and prevents duplicate fruits.
     *
     * @param fruits selected fruits
     * @return true when the selection is valid
     */
    private boolean validateUniqueFruits(ArrayList<Ingredient> fruits) {
        // code here
        return false;
    }

    /**
     * Checks whether the inventory contains the required ingredients.
     *
     * @param player active player
     * @param recipe selected recipe
     * @return true when enough ingredients are available
     */
    private boolean hasEnoughIngredients(Player player, Recipe recipe) {
        // code here
        return false;
    }

    /**
     * Removes the required ingredients after brewing is confirmed.
     *
     * @param player active player
     * @param recipe brewed recipe
     */
    private void consumeIngredients(Player player, Recipe recipe) {
        // code here
    }

    /**
     * Automatically sells a successful concoction.
     *
     * @param player active player
     * @param recipe brewed recipe
     */
    private void sellConcoction(Player player, Recipe recipe) {
        // code here
    }

    /**
     * Makes one cauldron unusable after a failed creative experiment.
     *
     * @param player active player
     */
    private void damageCauldron(Player player) {
        // code here
    }
}