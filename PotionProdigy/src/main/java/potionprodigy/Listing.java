package potionprodigy;

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
     * @return int on the status of the purchase
     */
    public int purchase(Player player) {
        int status = 0;
        int cost;
        if (!available) {
            status = -1;
        } else {
            if (isCauldron) {
                cost = unitPrice;
            } else {
                cost = unitPrice * quantity;
            }

            if(player.getCrystals()< cost){
                status = 0;
            } else {
                if(isCauldron){
                    player.getInventory().addCauldron();
                } else {
                    player.getInventory().addItemStack(ingredient, quantity);
                }
                player.spendCrystals(cost);
                markSold();
                status = 1;
            }
        }
            
        
        return status;
    }

    /** @return Gets the total price including its quantity*/
    public int getTotalPrice() {
        if (isCauldron) {
            return unitPrice;
        }
        return unitPrice * quantity;
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