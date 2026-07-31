package potionprodigy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents one fruit ingredient or concoction base available in the game.
 */
public abstract class Ingredient {

    /** Name used by the inventory, recipes, market, and save file. */
    private String name;

    /** Number of crystals required to buy one unit. */
    private int buyingPrice;

    /** Number of crystals received when selling one unit. */
    private int sellingPrice;

    /**
     * Creates an ingredient.
     *
     * @param name ingredient name
     * @param buyingPrice market buying price
     * @param sellingPrice market selling price
     */
    public Ingredient(String name, 
                      int buyingPrice, int sellingPrice) {
        this.name = name;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    /** @return ingredient name */
    public String getName() {
        return name;
    }

    /** @return true when the ingredient is a fruit */
    public boolean isFruit() {
        return this instanceof Fruit;
    }

    /** @return true when the ingredient is a concoction base */
    public boolean isConcoctionBase() {
    	return this instanceof Fruit;
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
     * Compares this ingredient with another ingredient by name.
     *
     * @param other ingredient to compare
     * @return true if both ingredients have the same name
     */
    public boolean isEqual(Ingredient other) {
        boolean equal = false;

        if (other != null) {
            if (this.name.equalsIgnoreCase(other.getName())) {
                equal = true;
            }
        }

        return equal;
    }

    /**
     * @return array list of ingredient information in the order of name, buyingPrice, sellingPrice
     */
    public static ArrayList<Ingredient> loadIngredients() {
        File targetFile = new File("src/data/ingredients.csv");
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
                    Ingredient ingredient;
                    if(fruitIngredient) {
                    	ingredient = new Fruit(name, buyingPrice, sellingPrice);
                    } else {
                    	ingredient = new ConcoctionBase(name, buyingPrice, sellingPrice);
                    }
                    
                    ingredients.add(ingredient);
                }
                scanner.close();
            }
        } catch (IOException e) {
            System.out.println("ERROR: in finding the file due to " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ERROR: invalid number in file due to " + e.getMessage());
        }

        return ingredients;
    }

    /** 
     * find an ingredient from the ingredients.csv file by its name
     * 
     * @param name the name of the ingredient to be found
     * @return the ingredient from the arraylist needed to be loaded from the ingredients.csv file
     */
    public static Ingredient findIngredient(String name) {
        ArrayList<Ingredient> ingredients = loadIngredients();
        Ingredient found = null;

        if (name != null) {
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getName().equalsIgnoreCase(name)) {
                    found = ingredient;
                }
            }
        }
        
        return found;
    }
}