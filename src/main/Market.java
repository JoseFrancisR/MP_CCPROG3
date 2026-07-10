package src.main;

import java.util.ArrayList;
import java.util.Random;

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
        listings.clear();
        Random rand = new Random();
        int i, qty, price;
        boolean cauldronExists = false;
        for (i = 0; i < 8; i++) {
            if (rand.nextInt(8) == 0 && !cauldronExists) { // Randomly generates a cauldron
                Listing listing = new Listing(i + 1, null, 1, 3000, true);
                cauldronExists = true;
                listings.add(listing);
            } else {
                if (!ingredients.isEmpty()){
                    Ingredient ingredient = ingredients.get(rand.nextInt(ingredients.size()));
                    qty = rand.nextInt(5) + 1;
                    price = ingredient.getBuyingPrice();
                    Listing listing = new Listing(i + 1, ingredient, qty, price, false);
                    listings.add(listing);
                }
            }
        }
        generatedThisSession = true;
    }

    /** Replaces the existing listings when a refresh condition is met. */
    public void refresh() {
        listings.clear();
        generateListings(Ingredient.loadIngredients());
    }

    /**
     * Buys several selected market slots in one procedure.
     *
     * @param player      purchasing player
     * @param slotNumbers selected slot numbers
     */
    public void buyMultiple(Player player, ArrayList<Integer> slotNumbers) {
        for (Integer slot : slotNumbers) {
            if (slot >= 1 && slot <= listings.size()) {
                Listing listing = listings.get(slot - 1);
                listing.purchase(player);
            } // else invalid slot number, do nothing
        }
    }

    /**
     * Sells several selected ingredient stacks in one procedure.
     *
     * @param player selling player
     * @param items  selected ingredients and quantities
     */
    public void sellMultiple(Player player, ArrayList<ItemStack> items) {
        Inventory inventory = player.getInventory();
        for (ItemStack stack : items) {
            Ingredient ingredient = stack.getIngredient();
            int quantity = stack.getQuantity();

            if (inventory.getQuantity(ingredient) >= quantity) {
                inventory.removeIngredient(ingredient, quantity);
                int total = ingredient.getSellingPrice() * quantity;
                player.addCrystals(total);
            }
        }
    }

    /**
     * Returns listings that have not yet been purchased.
     *
     * @return available listings
     */
    public ArrayList<Listing> getAvailableListings() {
        ArrayList<Listing> availListing = new ArrayList<>();
        for (Listing listing : listings) {
            if (listing.isAvailable()) {
                availListing.add(listing);
            }
        }
        return availListing;
    }

    /** 
     * display available listings 
     */
    public void displayAvailableListings() {
        System.out.println("Available Listings:");
        for (Listing listing : getAvailableListings()) {
            if (listing.isCauldronListing()) {
                System.out.println(listing.getSlotNumber() + ": " + listing.getQuantity() + "x Cauldron for " + listing.getUnitPrice() + " crystals");
            } else {
                System.out.println(listing.getSlotNumber() + ": " + listing.getQuantity() + "x " + listing.getIngredient().getName() + " for " + (listing.getIngredient().getBuyingPrice() * listing.getQuantity()) + " crystals");
            }
        }
    }

    /**
     * Returns true when the market has been generated during the current session.
     *
     * @return true when the market has been generated
     */
    public boolean hasBeenGenerated() {
        return generatedThisSession;
    }
}