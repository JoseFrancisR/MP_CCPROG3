package src.main;

/**
 * Represents one of the eight market slots.
 */
public class Listing {

    /** One-based slot number shown to the player. */
    private int slotNumber;

    /** Ingredient offered by this listing; null for a cauldron listing. */
    private Ingredient ingredient;

    /** Number of units available; cauldron listings always use one. */
    private int quantity;

    /** Price per unit. */
    private int unitPrice;

    /** True while the slot can still be purchased. */
    private boolean available;

    /** True when the listing sells a cauldron rather than an ingredient. */
    private boolean isCauldron;

    /**
     * Creates a market listing.
     *
     * @param slotNumber market slot
     * @param ingredient offered ingredient, or null for a cauldron
     * @param quantity quantity offered
     * @param unitPrice price per unit
     * @param isCauldron true for a cauldron listing
     */
    public Listing(int slotNumber, Ingredient ingredient, int quantity,
                         int unitPrice, boolean isCauldron) {
        this.slotNumber = slotNumber;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.available = true;
        this.isCauldron = isCauldron;
    }

    /**
     * Attempts to purchase this listing.
     *
     * @param player purchasing player
     * @return true when the transaction succeeds
     */
    public boolean purchase(Player player) {
        // code here
        return false;
    }

    /** Marks the slot as unavailable after purchase. */
    public void markSold() {
        this.available = false;
    }

    /** @return true when this slot can still be purchased */
    public boolean isAvailable() {
        return available;
    }

    /** @return true when the slot sells an ingredient */
    public boolean isIngredientListing() {
        return !isCauldron;
    }

    /** @return true when the slot sells one cauldron */
    public boolean isCauldronListing() {
        return isCauldron;
    }

    /** @return slot number */
    public int getSlotNumber() {
        return slotNumber;
    }

    /** @return offered ingredient, or null for a cauldron */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /** @return quantity offered */
    public int getQuantity() {
        return quantity;
    }

    /** @return price per unit */
    public int getUnitPrice() {
        return unitPrice;
    }
}