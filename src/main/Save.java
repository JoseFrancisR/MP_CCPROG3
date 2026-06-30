package src.main;

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
        // code here
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
     * @param name player/save name
     * @return loaded player, or null when loading fails
     */
    public Player loadPlayer(String name) {
        // code here
        return null;
    }

    /**
     * Writes the player's current state to the formatted text save file.
     *
     * @param player player to save
     * @return true when saving succeeds
     */
    public boolean savePlayer(Player player) {
        // code here
        return false;
    }

    /**
     * Reads ingredient and cauldron values under the INVENTORY section.
     *
     * @param scanner save-file scanner
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
     * @param writer save-file writer
     * @param inventory player inventory
     */
    private void writeInventory(PrintWriter writer, Inventory inventory) {
        // code here
    }

    /**
     * Writes comma-separated unlocked recipe IDs.
     *
     * @param writer save-file writer
     * @param recipebook player recipebook
     */
    private void writeRecipebook(PrintWriter writer, RecipeBook recipeBook) {
        // code here
    }
}