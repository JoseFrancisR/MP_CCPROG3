package src.main;

/**
 * Represents one fruit ingredient or concoction base available in the game.
 */
public class Ingredient {

    /** Name used by the inventory, recipes, market, and save file. */
    private String name;

    /** True when this ingredient is a fruit; false when it is a concoction base. */
    private boolean fruitIngredient;

    /** Number of crystals required to buy one unit. */
    private int buyingPrice;

    /** Number of crystals received when selling one unit. */
    private int sellingPrice;

    /**
     * Creates an ingredient.
     *
     * @param name ingredient name
     * @param fruitIngredient true for fruit; false for a concoction base
     * @param buyingPrice market buying price
     * @param sellingPrice market selling price
     */
    public Ingredient(String name, boolean fruitIngredient,
                      int buyingPrice, int sellingPrice) {
        this.name = name;
        this.fruitIngredient = fruitIngredient;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    /** @return ingredient name */
    public String getName() {
        return name;
    }

    /** @return true when the ingredient is a fruit */
    public boolean isFruit() {
        return fruitIngredient;
    }

    /** @return true when the ingredient is a concoction base */
    public boolean isConcoctionBase() {
        return !fruitIngredient;
    }

    /** @return market buying price */
    public int getBuyingPrice() {
        return buyingPrice;
    }

    /** @return market selling price */
    public int getSellingPrice() {
        return sellingPrice;
    }

    /**
     * Compares two ingredients using their names.
     *
     * @param other ingredient to compare
     * @return true when both ingredients represent the same item
     */
    public boolean equals(Ingredient other) {
        boolean equal = false;

        if (other != null) {
            equal = this.name.equals(other.name);
        }

        return equal;
    }
}