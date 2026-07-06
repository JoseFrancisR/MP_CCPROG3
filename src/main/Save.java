package src.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

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
        File targetFile = getSaveFile(name);
        boolean flag = false;

        try {
            File actualFile = targetFile.getCanonicalFile();
            if (actualFile.isFile() && actualFile.exists()) {
                flag = true;
            }
        } catch (IOException e) {
            System.out.println("ERROR: in finding the file due to " + e.getMessage());
        }
        return flag;
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
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("STRAWBERRY"), 3);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("ORANGE"), 2);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("LEMON"), 2);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("BANANA"), 3);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("MANGO"), 1);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("KIWI"), 1);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("BLUEBERRY"), 3);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("SYRUP BASE"), 3);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("BUBBLE BASE"), 3);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("PERFUME BASE"), 1);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("MILK BASE"), 2);
        newPlayer.getInventory().addItemStack(Ingredient.findIngredient("LOTION BASE"), 2);

        ArrayList<Integer> defRecipes = new ArrayList<>();
        defRecipes.add(1);
        defRecipes.add(2);
        defRecipes.add(16);
        defRecipes.add(17);
        defRecipes.add(36);
        defRecipes.add(37);
        defRecipes.add(55);
        defRecipes.add(56);

        newPlayer.getRecipeBook().loadRecipes();
        newPlayer.getRecipeBook().loadUnlockedRecipeIds(defRecipes);
        
        return newPlayer;
    }

    /**
     * Loads a player from the formatted text save file.
     *
     * @param scanner save-file scanner
     * @param name    player/save name
     * @return loaded player, or null when loading fails
     */
    public Player loadPlayer(String name) {
        File targetFile = getSaveFile(name);
        String playerName = null;
        int playerCrystal = -1;
        Inventory inventory = new Inventory();
        RecipeBook recipe = new RecipeBook();

        ArrayList<Integer> unlockedRecipes = new ArrayList<>();
        Ingredient ingredient;
        String section = null;
        String ingredientName;
        int quantity;

        Player save = null;

        if (!targetFile.exists()) {
            System.out.println("Save file does not exist.");
        } else {
            try {
                File actualFile = targetFile.getCanonicalFile();
                Scanner fileScanner = null;
                
                try {
                    fileScanner = new Scanner(actualFile);

                    if (!actualFile.exists()) {
                        System.out.println("Save file does not exist.");
                        return null;
                    }
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        if (line.startsWith("NAME = ")) {
                            playerName = line.substring(7);
                        } else if (line.startsWith("CRYSTALS = ")) {
                            playerCrystal = Integer.parseInt(line.substring(11));
                        } else if (line.startsWith("[INVENTORY]")) {
                            section = "INVENTORY"; // since it needs to move another line to read the next line, we need to
                                                   // set the section to INVENTORY
                        } else if (line.startsWith("[SPELLBOOK]")) {
                            section = "SPELLBOOK";
                        } else if (line.isEmpty() || section == null) {
                            // do nothing
                        } else if (section.equals("INVENTORY")) {
                            // split the line into ingredient name and quantity
                            String[] parts = line.split("=");

                            if (parts.length == 2) {
                                ingredientName = parts[0].trim();
                                quantity = Integer.parseInt(parts[1].trim());

                                if (ingredientName.equals("TOTAL CAULDRONS")) {
                                    inventory.setTotalCauldrons(quantity);
                                } else if (ingredientName.equals("USABLE CAULDRONS")) {
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
                    recipe.loadRecipes();

                    recipe.loadUnlockedRecipeIds(unlockedRecipes);
                    save = new Player(playerName, playerCrystal, inventory, recipe);
                } finally {
                    if (fileScanner != null) {
                        fileScanner.close();
                    }
                }
            } catch (IOException e) {
                System.out.println("ERROR: in finding the file due to " + e.getMessage());
            }
        }
        
        return save;
    }

    /**
     * Writes the player's current state to the formatted text save file.
     *
     * @param player player to save
     * @return true when saving succeeds
     */
    public boolean savePlayer(Player player) {
        File targetFile = getSaveFile(player.getName());
        PrintWriter saveFile = null;
        boolean flag = true;

        try {
            saveFile = new PrintWriter(targetFile);

            saveFile.println("NAME = " + player.getName() + "\n");
            saveFile.println("CRYSTALS = " + player.getCrystals() + "\n");
            writeInventory(saveFile, player.getInventory());
            writeRecipebook(saveFile, player.getRecipeBook());
        } catch (IOException e) {
            System.out.println("ERROR: in writing the file due to " + e.getMessage());
            flag = false;
        } finally {
            if (saveFile != null) {
                saveFile.close();
            }
        }
        return flag;
    }

    /**
     * Writes ingredient quantities and cauldron counts.
     *
     * @param writer    save-file writer
     * @param inventory player inventory
     */
    private void writeInventory(PrintWriter writer, Inventory inventory) {
        Stack<String> baseIngredients = new Stack<>();
        writer.println("[INVENTORY]");
        for (ItemStack stack : inventory.getIngredientStacks()) {
            if (stack.getIngredient().getName().contains("BASE")) 
                
                baseIngredients.push(stack.getIngredient().getName() + " = " + stack.getQuantity());
            else
                writer.println(stack.getIngredient().getName() + " = " + stack.getQuantity());
        }
        writer.println();
        while (!baseIngredients.isEmpty()) {
            writer.println(baseIngredients.pop());
        }
        writer.println();
        writer.println("TOTAL CAULDRONS = " + inventory.countTotalCauldrons());
        writer.println("USABLE CAULDRONS = " + inventory.countUsableCauldrons() + "\n");
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
     * Returns a random item stack no duplicates in inventory loaded from list of
     * ingredients at a random quantity between 1 and 5.
     * 
     * @param inventory the inventory to get the random item stack from
     * @return a random item stack from the inventory
     */
    public ItemStack randItem(Inventory inventory) {
        ArrayList<Ingredient> ingredients = Ingredient.loadIngredients();
        ArrayList<Ingredient> inventoryIngredients = new ArrayList<>();
        ItemStack random = null;

        for (ItemStack stack : inventory.getIngredientStacks()) {
            inventoryIngredients.add(stack.getIngredient());
        }

        // filters existing/owned
        ArrayList<Ingredient> filtered = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            boolean owned = false;

            for (Ingredient inventoryIngredient : inventoryIngredients) {
                if (owned == false) {
                    if (ingredient.isEqual(inventoryIngredient)) {
                        owned = true;
                    }
                }
            }

            if (owned == false) {
                filtered.add(ingredient);
            }
        }

        ingredients = filtered;

        if (!ingredients.isEmpty()) {
            int randomIndex = (int) (Math.random() * ingredients.size());
            Ingredient randomIngredient = ingredients.get(randomIndex);
            int randomQuantity = (int) (Math.random() * 5) + 1; // Random quantity between 1 and 5
            
            random = new ItemStack(randomIngredient, randomQuantity);
        }
        
        return random;
    }
    
    /**
     * Returns a boolean on whether there is a save that exists in the directory 
     */
    public boolean hasSave() {
    	File saveFolder = new File("src/data/saves/");
        File[] files = saveFolder.listFiles();
        boolean flag = false;
        if(files != null) {
            for(File file: files) {
        	    if(file.isFile()&& file.getName().endsWith(".txt")) {
        	    	flag = true;
        	    }
            }
        }
        return flag;
    }

    /**
     * constructs save file path for a given player name
     * 
     * @param name player/save name
     * @return the path to the save file for the given player name
     */
    private File getSaveFile(String name) {
        return new File("src/data/saves/" + name + ".txt");
    }
    
    /**
     * Displays all available saves
     * 
     */
    public void displaySaves() {
    	int ctr = 1;
    	File saveFolder = new File("src/data/saves/");
        File[] files = saveFolder.listFiles();
        boolean found = true;
        
        if(files == null) {
        	System.out.println("Save folder not found");
        	found = false;
        }
        
        System.out.println("Availabe Saves: ");
        if (found){
            for(File file: files) {
        	    if(file.isFile()&& file.getName().endsWith(".txt")) {
        	    	System.out.println(ctr + " " + file.getName());
        	    	ctr++;
        	    }
            }
        }

        if(ctr==1) {
        	System.out.println("No save is available");
        }
    }
}