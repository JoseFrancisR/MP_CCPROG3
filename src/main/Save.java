package src.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads and writes player data using the required plain-text save format.
 */
public class Save {
    /**
     * Checks whether a save file exists.
     *
     * @param name player/save name
     * @return true when the corresponding file exists
     */
    public boolean saveExists(String name) {

        File curDirectory = new File(".").getAbsoluteFile();
        File targetFile = new File(curDirectory.getParentFile(), "data/saves/" + name);

        try {
            File actualFile = targetFile.getCanonicalFile();
            if (actualFile.isFile() && actualFile.exists()) {
                System.out.println("Save exists");
                return true;
            }
        } catch (IOException e) {
            System.out.println("ERROR: in finding the file due to " + e.getMessage());
        }
        System.out.println("Save doesn't exists");
        return false;
    }

    /**
     * Creates a player with the required new-game defaults.
     *
     * @param name player name
     * @return new player
     */
    public Player createDefaultPlayer(String name) {
        int i;
        Player newPlayer = new Player(name);
        // random starting inventory and recipebook
        for (i = 0; i < 5; i++) {
            ItemStack randomStack = randItem(newPlayer.getInventory());
            if (randomStack != null) {
                newPlayer.getInventory().addItemStack(randomStack.getIngredient(), randomStack.getQuantity());
            }
        }
        for (i = 0; i < 5; i++) {
            int randomRecipeId = (int) (Math.random() * 130) + 1; // Random recipe ID between 1 and 130
            if (!newPlayer.getRecipeBook().getUnlockedRecipeIds().contains(randomRecipeId)) {
                newPlayer.getRecipeBook().getUnlockedRecipeIds().add(randomRecipeId);
            }
        }
        return newPlayer;
    }

    /**
     * Loads a player from the formatted text save file.
     *
     * @param scanner save-file scanner
     * @param name player/save name
     * @return loaded player, or null when loading fails
     */
    public Player loadPlayer(Scanner scanner, String name) {
        File curDirectory = new File(".").getAbsoluteFile();
        File targetFile = new File(curDirectory.getParentFile(), "data/saves/" + name);
        String playerName = null;
        int playerCrystal = -1;
        Inventory inventory = new Inventory();
        RecipeBook recipe = new RecipeBook();
        
        ArrayList<Integer> unlockedRecipes = new ArrayList<>();
        Ingredient ingredient;
        String section = null;
        String ingredientName;
        int quantity;
        try {
            File actualFile = targetFile.getCanonicalFile();
            if (actualFile.isFile() && actualFile.exists()) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("NAME = ")) {
                        playerName = line.substring(7);
                    } else if (line.startsWith("CRYSTALS = ")) {
                        playerCrystal = Integer.parseInt(line.substring(11));
                    } else if (line.startsWith("[INVENTORY]")) {
                        section = "INVENTORY"; // since it needs to move another line to read the next line, we need to set the section to INVENTORY
                    } else if (line.startsWith("[SPELLBOOK]")) {
                        section = "SPELLBOOK";
                    } else if (line.isEmpty() || section == null){
                        // do nothing
                    } else if (section.equals("INVENTORY")) {
                        // split the line into ingredient name and quantity
                        String[] parts = line.split("=");

                        if (parts.length == 2) {
                            ingredientName = parts[0].trim();
                            quantity = Integer.parseInt(parts[1].trim());

                            if (ingredientName.equals("TOTAL_CAULDRONS")) {
                                inventory.setTotalCauldrons(quantity);
                            } else if (ingredientName.equals("USABLE_CAULDRONS")) {
                                inventory.setUsableCauldrons(quantity);
                            } else {
                                ingredient = Ingredient.findIngredient(ingredientName);
                                inventory.addItemStack(ingredient, quantity);
                            }
                        }
                    } else if (section.equals("SPELLBOOK")) {
                        // split the nums into own string and parse into array list of int
                        String[] recipeIds = line.split(",");

                        for (String id : recipeIds) {
                            unlockedRecipes.add(Integer.parseInt(id.trim()));
                        }
                    }
                }
                recipe.loadUnlockedRecipeIds(unlockedRecipes);
                return new Player(playerName, playerCrystal, inventory, recipe);
            }
        } catch (IOException e) {
            System.out.println("ERROR: in finding the file due to " + e.getMessage());
        }
        return null;
    }

    /**
     * Writes the player's current state to the formatted text save file.
     *
     * @param player player to save
     * @return true when saving succeeds
     */
    public boolean savePlayer(Scanner scanner, Player player) {
        boolean inputCheck = true;
        int input;
        File curDirectory = new File(".").getAbsoluteFile();
        File targetFile = new File(curDirectory.getParentFile(), "data/saves/" + player.getName());
        try {
            File actualFile = targetFile.getCanonicalFile();

            if (saveExists(player.getName())) {
                do {
                    inputCheck = true;
                    System.out.println("Do you want to overwrite the save(0-NO / 1-YES)");
                    input = scanner.nextInt();
                    if (input != 0 && input != 1) {
                        System.out.println("INPUT ONLY 1 or 0");
                        inputCheck = false;
                    }
                } while (inputCheck);
                PrintWriter saveFile = new PrintWriter(player.getName());
            }
            try (PrintWriter saveFile = new PrintWriter(player.getName())) {
                saveFile.println("NAME = " + player.getName() + "\n");
                saveFile.println("CRYSTALS = " + player.getCrystals() + "\n");
                writeInventory(saveFile, player.getInventory());
                writeRecipebook(saveFile, player.getRecipeBook());
            }
        } catch (IOException e) {
            System.out.println("ERROR: in writing the file due to " + e.getMessage());
        }
        return false;
    }

    /**
     * Writes ingredient quantities and cauldron counts.
     *
     * @param writer    save-file writer
     * @param inventory player inventory
     */
    private void writeInventory(PrintWriter writer, Inventory inventory) {
        writer.println("[INVENTORY]");
        for (ItemStack stack : inventory.getIngredientStacks()) {
            writer.println(stack.getIngredient().getName() + " = " + stack.getQuantity());
        }
        writer.println("TOTAL_CAULDRONS = " + inventory.countTotalCauldrons());
        writer.println("USABLE_CAULDRONS = " + inventory.countUsableCauldrons() + "\n");
    }

    /**
     * Writes comma-separated unlocked recipe IDs.
     *
     * @param writer     save-file writer
     * @param recipebook player recipebook
     */
    private void writeRecipebook(PrintWriter writer, RecipeBook recipeBook) {
        writer.println("[SPELLBOOK]");
        ArrayList<Integer> unlockedRecipeIds = recipeBook.getUnlockedRecipeIds();
        for (int i = 0; i < unlockedRecipeIds.size(); i++) {
            writer.print(unlockedRecipeIds.get(i));
            if (i < unlockedRecipeIds.size() - 1) {
                writer.print(",");
            }
        }
    }

    /**
     * Returns a random item stack no duplicates in inventory loaded from list of ingredients at a random quantity between 1 and 5.
     * @param inventory the inventory to get the random item stack from
     * @return a random item stack from the inventory
     */
    private ItemStack randItem(Inventory inventory) {
        ArrayList<Ingredient> ingredients = Ingredient.loadIngredients();
        ArrayList<Ingredient> inventoryIngredients = new ArrayList<>();
        for (ItemStack stack : inventory.getIngredientStacks()) {
            inventoryIngredients.add(stack.getIngredient());
        }
        ingredients.removeAll(inventoryIngredients);
        if (ingredients.isEmpty()) {
            return null; // No more ingredients to add
        }
        int randomIndex = (int) (Math.random() * ingredients.size());
        Ingredient randomIngredient = ingredients.get(randomIndex);
        int randomQuantity = (int) (Math.random() * 5) + 1; // Random quantity between 1 and 5
        return new ItemStack(randomIngredient, randomQuantity);
    }
}