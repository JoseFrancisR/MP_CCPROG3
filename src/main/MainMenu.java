package src.main;

/**
 * Coordinates menus, session-only values, and the major game services.
 */
public class MainMenu {

    /** Player currently loaded into the game. */
    private Player currentPlayer;

    /** Market used for ingredient and cauldron transactions. */
    private Market market;

    /** Service responsible for loading and saving player files. */
    private Save save;

    /** Service responsible for brewing logic. */
    private Brew brew;

    /** True after the login bonus has been claimed during this session. */
    private boolean loginBonusClaimed;

    /** Number of successful brews since the market counter last reset. */
    private int brewsSinceMarketVisit;

    /** Creates the game's coordinating objects. */
    public MainMenu() {
        currentPlayer = null;
        market = new Market();
        save = new Save();
        brew = new Brew();
        loginBonusClaimed = false;
        brewsSinceMarketVisit = 0;
    }

    /** Starts the new-game/load-game menu and main program loop. */
    public void start() {
        // code here
        // check if there is save file, if yes, load it, if no, create new player
        // start new game or load game, then display main menu
        // loop until exit game
    }

    /**
     * Creates a new player and handles possible overwrite confirmation.
     *
     * @param name player/save name
     */
    public void startNewGame(String name) {
        // code here
    }

    /**
     * Loads an existing player.
     *
     * @param name player/save name
     * @return true when a valid save is loaded
     */
    public boolean loadGame(String name) {
        // code here
        return false;
    }

    /** Displays the main menu and current crystal balance. */
    public void displayMainMenu() {
        // code here
    }

    /** Handles recipe-mode or creative-mode brewing. */
    public void brewConcoction() {
        // code here
    }

    /** Displays all ingredient quantities and cauldron counts. */
    public void checkInventory() {
        // code here
    }

    /** Displays all recipes currently unlocked by the player. */
    public void checkSpellbook() {
        // code here
    }

    /** Handles market refresh checks, buying, selling, and exit. */
    public void visitMarket() {
        // code here
    }

    /**
     * Blesses one unusable cauldron for 1000 crystals.
     *
     * @return true when the transaction succeeds
     */
    public boolean blessCauldronPay() {
        if (currentPlayer.getCrystals() >= 1000 && (currentPlayer.getInventory().countUsableCauldrons() < currentPlayer.getInventory().countTotalCauldrons())) {
            currentPlayer.spendCrystals(1000);
            currentPlayer.getInventory().blessCauldron();
            return true;
            System.out.println("Cauldron blessed! You now have " + currentPlayer.getInventory().countUsableCauldrons() + " usable cauldrons.");
        }
        return false;
    }

    /**
     * Gives one random ingredient once per game session.
     *
     * @return true when the bonus is claimed
     */
    public boolean claimLoginBonus() {
        // code here
        return false;
    }

    /** Saves the current player and exits normally. */
    public void exitGame() {
        // code here
    }
}