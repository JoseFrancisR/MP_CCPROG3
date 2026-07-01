package src.main;

import java.util.ArrayList;

/**
 * Stores the player's ingredient quantities and aggregate cauldron counts.
 */
public class Inventory {

    /** Ingredient quantities owned by the player. */
    private ArrayList<ItemStack> ingredientStacks;

    /** Total number of usable and unusable cauldrons owned. */
    private int totalCauldrons;

    /** Number of cauldrons that can currently be used for brewing. */
    private int usableCauldrons;

    /**
     * Creates an inventory.
     *
     * @param totalCauldrons starting total cauldrons
     * @param usableCauldrons starting usable cauldrons
     */
    public Inventory(int totalCauldrons, int usableCauldrons) {
        ingredientStacks = new ArrayList<>();
        this.totalCauldrons = totalCauldrons;
        this.usableCauldrons = usableCauldrons;
    }

    /** Creates an inventory */
    public Inventory() {
        ingredientStacks = new ArrayList<>();
        this.totalCauldrons = -1;
        this.usableCauldrons = -1;
    }

    /**
     * Adds an ingredient quantity.
     *
     * @param item ingredient to add
     * @param qty number of units
     */
    public void addIngredient(Ingredient item, int qty) {
        this.ingredientStacks.add(new ItemStack(item, qty));
    }

    /**
     * Removes an ingredient quantity.
     *
     * @param item ingredient to remove
     * @param qty number of units
     * @return true when enough units were available
     */
    public boolean removeIngredient(Ingredient item, int qty) {
        ItemStack stack = findStack(item);
        if (stack != null) {
            return stack.remove(qty);
        }
        return false;
    }

    /**
     * Gets the quantity of an ingredient.
     *
     * @param item ingredient to search
     * @return quantity owned
     */
    public int getQuantity(Ingredient item) {
        ItemStack stack = findStack(item);
        if (stack != null) {
            return stack.getQuantity();
        }
        return 0;
    }

    /**
     * Checks whether every ingredient required by a recipe is available.
     *
     * @param recipe recipe to check
     * @return true when all required ingredients are available
     */
    public boolean hasIngredients(Recipe recipe) {
        for (Ingredient item : recipe.getRequiredIngredients()) {
            if (getQuantity(item) < recipe.getRequiredQuantity(item)) {
                return false;
            }
        }
        return true;
    }

    /** Adds one new usable cauldron. */
    public void addCauldron() {
        this.totalCauldrons++;
        this.usableCauldrons++;
    }

    /**
     * Converts one usable cauldron into an unusable cauldron.
     *
     * @return true when a usable cauldron was damaged
     */
    public boolean damageCauldron() {
        if (this.usableCauldrons > 0) {
            this.usableCauldrons--;
            return true;
        }
        return false;
    }

    /**
     * Converts one unusable cauldron back into a usable cauldron.
     *
     * @return true when a cauldron was blessed, false if all cauldrons are already usable
     */
    public boolean blessCauldron() {
        if (this.totalCauldrons > this.usableCauldrons) {
            this.usableCauldrons++;
            return true;
        }
        return false;
    }

    /** @return total cauldrons */
    public int countTotalCauldrons() {
        return totalCauldrons;
    }

    /** @return usable cauldrons */
    public int countUsableCauldrons() {
        return usableCauldrons;
    }

    /** @return unusable cauldrons */
    public int countUnusableCauldrons() {
        return totalCauldrons - usableCauldrons;
    }

    /** @return copy of ingredient stacks */
    public ArrayList<ItemStack> getIngredientStacks() {
        return new ArrayList<>(ingredientStacks);
    }

    /** @return the ingredient needed null if not found */
    private ItemStack findStack(Ingredient item) {
        for (ItemStack stack : ingredientStacks) {
            if (stack.getIngredient().equals(item)) {
                return stack;
            }
        }
        return null;
    }
}