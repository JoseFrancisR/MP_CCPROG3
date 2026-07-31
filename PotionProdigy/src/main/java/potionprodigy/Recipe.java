package potionprodigy;

import java.util.ArrayList;

/**
 * Represents one predefined concoction from the Potion Compendium.
 */
public class Recipe {

    /** Numeric concoction ID. Display it with leading zeroes when needed. */
    private int concoctionId;

    /** Name of the resulting concoction. */
    private String concoctionName;

    /** Required concoction base. */
    private Ingredient base;

    /** Required fruits; the specification allows one to three unique fruits. */
    private ArrayList<Ingredient> fruits;

    /** Crystals earned when the concoction is successfully brewed and sold. */
    private int saleValue;

    /**
     * Creates a recipe.
     *
     * @param concoctionId concoction ID
     * @param concoctionName concoction name
     * @param base required base
     * @param fruits required fruits
     * @param saleValue automatic sale value
     */
    public Recipe(int concoctionId, String concoctionName, Ingredient base,
                  ArrayList<Ingredient> fruits, int saleValue) {
        this.concoctionId = concoctionId;
        this.concoctionName = concoctionName;
        this.base = base;
        this.fruits = new ArrayList<>(fruits);
        this.saleValue = saleValue;
    }

    /**
     * Checks whether the supplied base and fruits match this recipe.
     * Fruit order must not affect the result.
     *
     * @param base selected base
     * @param fruits selected fruits
     * @return true when the ingredients match this recipe
     */
    public boolean matches(Ingredient base, ArrayList<Ingredient> fruits) {
        boolean match = true;

        if (base == null || fruits == null) {
            match = false;
        }
        
        if (match) {
            if (!this.base.isEqual(base)) {
                match = false;
            }
        }
        
        if (match) {
            if (this.fruits.size() != fruits.size()) {
                match = false;
            }
        }
        
        if (match) {
            for (Ingredient fruit : fruits) {
                if (!containsFruit(fruit)) {
                    match = false;
                }
            }
        }
        
        return match;
    }

    /**
     * Returns all ingredients needed by this recipe.
     *
     * @return list containing the base and required fruits
     */
    public ArrayList<Ingredient> getRequiredIngredients() {
        ArrayList<Ingredient> required = new ArrayList<>();
        required.add(base);
        required.addAll(fruits);
        return required;
    }

    /** @return concoction ID */
    public int getConcoctionId() {
        return concoctionId;
    }

    /** @return concoction name */
    public String getConcoctionName() {
        return concoctionName;
    }

    /** @return required base */
    public Ingredient getBase() {
        return base;
    }

    /** @return defensive copy of the required fruits */
    public ArrayList<Ingredient> getFruits() {
        return new ArrayList<>(fruits);
    }

    /** @return automatic sale value */
    public int getSaleValue() {
        return saleValue;
    }

    /**
     * Checks whether the inputted ingredient is needed for a recipe
     * 
     * @param item the item being checked if needed
     * @return 1 or 0 depending if ingredient is required or not
     */
    public int getRequiredQuantity(Ingredient item) {
        int val = 0;
        
        if (item != null && (item.isEqual(base) || containsFruit(item))) {
            val = 1;
        }

        return val;
    }

    /**
     * Checks if it contains the Ingredient fruit
     * @return boolean
     */
    public boolean containsFruit(Ingredient other) {
        boolean contains = false;

        if (other != null) {
            for (Ingredient fruit : fruits) {
                if (contains == false) {
                    if (fruit.isEqual(other)) {
                        contains = true;
                    }
                }
            }
        }
        
        return contains;
    }
}