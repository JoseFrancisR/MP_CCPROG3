package src.main;

/**
 * Stores the player's persistent game data.
 */
public class Player {

    /** Player name and save-file name. */
    private String name;

    /** Current number of crystals. */
    private int crystals;

    /** Player inventory. */
    private Inventory inventory;

    /** Player recipebook and unlocked recipes. */
    private RecipeBook recipes;

    /**
     * Creates a player.
     *
     * @param name player name
     * @param crystals starting crystals
     * @param inventory player inventory
     * @param recipes player recipes
     */
    public Player(String name, int crystals, Inventory inventory,
                  RecipeBook recipes) {
        this.name = name;
        this.crystals = crystals;
        this.inventory = inventory;
        this.recipes = recipes;
    }

    /**
     * Creates a default player.
     *
     * @param name player name
     * @param crystals starting crystals
     * @param inventory player inventory
     * @param recipes player recipes
     */
    public Player(String name) {
        this.name = name;
        this.crystals = 5000; // Default starting crystals
        this.inventory = new Inventory(); 
        this.recipes = new RecipeBook();
    }

    /**
     * Adds crystals.
     *
     * @param amount amount to add
     */
    public void addCrystals(int amount) {
        if (amount > 0) {
            this.crystals += amount;
        }
    }

    /**
     * Spends crystals when the player can afford the amount.
     *
     * @param amount amount to spend
     * @return true when payment succeeds
     */
    public boolean spendCrystals(int amount) {
        if (canAfford(amount)) {
            this.crystals -= amount;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the player can afford an amount.
     *
     * @param amount required crystals
     * @return true when enough crystals are available
     */
    public boolean canAfford(int amount) {
        return this.crystals >= amount && amount > 0;
    }

    /** @return player name */
    public String getName() {
        return name;
    }

    /** @return current crystals */
    public int getCrystals() {
        return crystals;
    }

    /** @return player inventory */
    public Inventory getInventory() {
        return inventory;
    }

    /** @return player recipebook */
    public RecipeBook getRecipeBook() {
        return recipes;
    }
}