package src.main;

import java.util.ArrayList;

/**
 * Manages market generation, buying, and selling.
 */
public class Market {

    /** Up to eight listings shown during the current market visit. */
    private ArrayList<Listing> listings;

    /** True after the market has been generated during the current session. */
    private boolean generatedThisSession;

    /** Creates an empty market. */
    public Market() {
        listings = new ArrayList<>();
        generatedThisSession = false;
    }

    /**
     * Generates eight random market slots.
     *
     * @param ingredients complete list of fruits and bases
     */
    public void generateListings(ArrayList<Ingredient> ingredients) {
        // code here
    }

    /** Replaces the existing listings when a refresh condition is met. */
    public void refresh() {
        // code here
    }

    /**
     * Buys several selected market slots in one procedure.
     *
     * @param player purchasing player
     * @param slotNumbers selected slot numbers
     */
    public void buyMultiple(Player player, ArrayList<Integer> slotNumbers) {
        // code here
    }

    /**
     * Sells several selected ingredient stacks in one procedure.
     *
     * @param player selling player
     * @param items selected ingredients and quantities
     */
    public void sellMultiple(Player player, ArrayList<ItemStack> items) {
        // code here
    }

    /**
     * Returns listings that have not yet been purchased.
     *
     * @return available listings
     */
    public ArrayList<Listing> getAvailableListings() {
        // code here
        return null;
    }
}