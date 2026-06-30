package src.main;

/**
 * Stores an ingredient together with the quantity owned by the player.
 */
public class ItemStack {

    /** Ingredient represented by this stack. */
    private Ingredient ingredient;

    /** Current number of units in the stack. */
    private int quantity;

    /**
     * Creates an ingredient stack.
     *
     * @param ingredient ingredient represented by the stack
     * @param quantity starting quantity
     */
    public ItemStack(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    /**
     * Adds units to the stack.
     *
     * @param qty number of units to add
     */
    public void add(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Cannot add non-positive quantity");
        } else {
            quantity += qty;
        }
    }

    /**
     * Removes units from the stack when enough units are available.
     *
     * @param qty number of units to remove
     * @return true when the removal succeeds
     */
    public boolean remove(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Cannot remove non-positive quantity");
        } else if (qty > quantity) {
            return false;
        } else {
            quantity -= qty;
            return true;
        }
    }

    /** @return current quantity */
    public int getQuantity() {
        return quantity;
    }

    /** @return ingredient stored by this stack */
    public Ingredient getIngredient() {
        return ingredient;
    }
}