package src.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

    /**
     * @return array list of ingredient information in the order of name, buyingPrice, sellingPrice
     */
    public static ArrayList<Ingredient> loadIngredients() {
        File curDirectory = new File(".").getAbsoluteFile();
        File targetFile = new File(curDirectory.getParentFile(), "data/ingredients.csv");
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        try {
            File actualFile = targetFile.getCanonicalFile();
            if (actualFile.isFile() && actualFile.exists()) {
                Scanner scanner = new Scanner(actualFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    String name = parts[0];
                    boolean fruitIngredient = Boolean.parseBoolean(parts[1]);
                    int buyingPrice = Integer.parseInt(parts[2]);
                    int sellingPrice = Integer.parseInt(parts[3]);
                    Ingredient ingredient = new Ingredient(name, fruitIngredient, buyingPrice, sellingPrice);
                    ingredients.add(ingredient);
                }
                scanner.close();
            }
        } catch (IOException e) {
            System.out.println("ERROR: in finding the file due to " + e.getMessage());
        }

        return ingredients;
    }
}