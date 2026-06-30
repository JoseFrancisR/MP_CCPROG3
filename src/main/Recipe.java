package src.main;

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
        if (!this.base.equals(base)) {
            return false;
        }
        if (this.fruits.size() != fruits.size()) {
            return false; 
        }
        for (Ingredient fruit : fruits) {
            if (!this.fruits.contains(fruit)) {
                return false;
            }
        }
        return true;
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

    public int getRequiredQuantity(Ingredient item) {
        if (item.equals(base)) {
            return 1;
        } else if (fruits.contains(item)) {
            return 1;
        } else {
            return 0;
        }
    }
}