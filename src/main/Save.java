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
        // code here
        return new Player(name);
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
        String playerName;
        int playerCrystal;
        Inventory inventory = new Inventory();
        RecipeBook recipe = new RecipeBook();

        try {
            File actualFile = targetFile.getCanonicalFile();
            if (actualFile.isFile() && actualFile.exists()) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("NAME = ")) {
                        playerName = line.substring(7);
                    } else if (line.startsWith("CRYSTALS = ")) {
                        playerCrystal = Integer.parseInt(line.substring(11));
                    }
                    // code for reading player name and crystals
                    readInventory(scanner, inventory);
                    recipe.loadUnlockedRecipeIds(readUnlockedRecipeIds(scanner));
                }
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
                // writeInventory(PrintWriter writer, Inventory inventory) NOT DONEEE
                // void writeRecipebook(PrintWriter writer, RecipeBook recipeBook
            }
        } catch (IOException e) {
            System.out.println("ERROR: in writing the file due to " + e.getMessage());
        }
        return false;
    }

    /**
     * Reads ingredient and cauldron values under the INVENTORY section.
     *
     * @param scanner   save-file scanner
     * @param inventory inventory being populated
     */
    private void readInventory(Scanner scanner, Inventory inventory) {
        // code here
    }

    /**
     * Reads comma-separated recipe IDs under the SPELLBOOK section.
     *
     * @param scanner save-file scanner
     * @return unlocked recipe IDs
     */
    private ArrayList<Integer> readUnlockedRecipeIds(Scanner scanner) {
        // code here
        return new ArrayList<>();
    }

    /**
     * Writes ingredient quantities and cauldron counts.
     *
     * @param writer    save-file writer
     * @param inventory player inventory
     */
    private void writeInventory(PrintWriter writer, Inventory inventory) {
        // code here
    }

    /**
     * Writes comma-separated unlocked recipe IDs.
     *
     * @param writer     save-file writer
     * @param recipebook player recipebook
     */
    private void writeRecipebook(PrintWriter writer, RecipeBook recipeBook) {
        // code here
    }
}