package potionprodigy;

import java.util.ArrayList;
import java.util.Stack;

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
        this.totalCauldrons = 0;
        this.usableCauldrons = 0;
    }

    /**
     * Adds an ingredient quantity.
     *
     * @param item ingredient to add
     * @param qty number of units
     */
    public void addItemStack(Ingredient item, int qty) {
        if (!(item == null || qty <= 0)) {
            ItemStack stack = findStack(item);

            if (stack == null) {
                this.ingredientStacks.add(new ItemStack(item, qty));
            } else if (qty > 0) {
                stack.add(qty);
            }
        }
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
        boolean flag = false;
        
        if (stack != null) {
            flag = stack.remove(qty);
        }
        return flag;
    }

    /**
     * Gets the quantity of an ingredient.
     *
     * @param item ingredient to search
     * @return quantity owned
     */
    public int getQuantity(Ingredient item) {
        ItemStack stack = findStack(item);
        int qty = 0;

        if (stack != null) {
            qty = stack.getQuantity();
        }
        return qty;
    }

    /**
     * Checks whether every ingredient required by a recipe is available.
     *
     * @param recipe recipe to check
     * @return true when all required ingredients are available
     */
    public boolean hasIngredients(Recipe recipe) {
        boolean flag = true;

        for (Ingredient item : recipe.getRequiredIngredients()) {
            if (getQuantity(item) < recipe.getRequiredQuantity(item)) {
                flag = false;
            }
        }
        return flag;
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
        boolean flag = false;

        if (this.usableCauldrons > 0) {
            this.usableCauldrons--;
            flag = true;
        }
        return flag;
    }

    /**
     * Converts one unusable cauldron back into a usable cauldron.
     *
     * @return true when a cauldron was blessed, false if all cauldrons are already usable
     */
    public boolean blessCauldron() {
        boolean flag = false;
        
        if (this.totalCauldrons > this.usableCauldrons) {
            this.usableCauldrons++;
            flag = true;
        }
        return flag;
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

    /**
     * Sets the total number of cauldrons.
     * @param totalCauldrons total number of cauldrons to be set
     */
    public void setTotalCauldrons(int totalCauldrons) {
        this.totalCauldrons = totalCauldrons;
    }

    /**
     * Sets the number of usable cauldrons.
     * @param usableCauldrons number of usable cauldrons to be set
     */
    public void setUsableCauldrons(int usableCauldrons) {
        this.usableCauldrons = usableCauldrons;
    }

    /** @return the ingredient needed null if not found */
    private ItemStack findStack(Ingredient item) {
        ItemStack found = null;
        
        for (ItemStack stack : ingredientStacks) {
            if (stack.getIngredient().isEqual(item)) {
                found = stack;
            }
        }
        return found;
    }

    /** Displays the current inventory. */
    public void displayInventory() {
        Stack<String> baseIngredients = new Stack<>();
        
        System.out.println("Current Inventory:");
        for (ItemStack stack : ingredientStacks) {
            if (stack.getIngredient().getName().contains("BASE"))
                baseIngredients.push(stack.getIngredient().getName() + " = " + stack.getQuantity());
            else
                System.out.println(stack.getIngredient().getName() + " = " + stack.getQuantity());
        }
        System.out.println();
        while (!baseIngredients.isEmpty()) {
            System.out.println(baseIngredients.pop());
        }
    }
}