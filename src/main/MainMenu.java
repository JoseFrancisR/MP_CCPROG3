package src.main;

import java.util.Scanner;

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

    /** Scanner for user input. */
    private Scanner scanner = new Scanner(System.in);

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
    	
    	int input;
    	String playerName;
    	while(currentPlayer == null) {
    		if(save.hasSave()) {
    			do {
        			System.out.println("1. New Game");
        		    System.out.println("2. Load Game");
        		    input = scanner.nextInt();

        		} while (input != 1 && input != 2);
        	} else {
        		do {
        			System.out.println("1. New Game");

        			input = scanner.nextInt();

        		} while (input != 1);
        	}
        	
        	if(input==1) {
        		System.out.println("Please enter username: ");
        		playerName = scanner.next();
        		startNewGame(playerName);
        	} else {
        		System.out.println("Please enter save to load: ");
        		playerName = scanner.next();
        		this.currentPlayer = save.loadPlayer(scanner, playerName);
        	}
    	}
    	
    	while(true) {
    		displayMainMenu();
    		input = scanner.nextInt();
    		switch(input) {
    			case 1:
    				 brewConcoction();
    		         break;
    			case 2:
    				checkInventory();
    				break;
    			case 3:
    				checkSpellbook();
    				break;
    			case 4:
    			    visitMarket();
    	            break;
    	        case 5:
    	            blessCauldronPay();
    	            break;
    	        case 6:
    	            claimLoginBonus();
    	            break;
    	        case 7:
    	        	System.out.println("Exitting the game");
    	        	exitGame();
    	            break;
    	        default:
    	            System.out.println("Invalid pick from 1-7");

    		}
    	}
    	
    }

    /**
     * Creates a new player and handles possible overwrite confirmation.
     *
     * @param name player/save name
     */
    public void startNewGame(String name) {
    	if (save.saveExists(name)) {
            System.out.println("A save file with this name already exists. Overwrite? (y/n)");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("y")) {
                System.out.println("New game creation cancelled.");
                return;
            }
        }
        currentPlayer = save.createDefaultPlayer(name);
        save.savePlayer(scanner, currentPlayer);
        System.out.println("New game created for player: " + name);
    }

    /**
     * Loads an existing player.
     *
     * @param name player/save name
     * @return true when a valid save is loaded
     */
    public boolean loadGame(String name) {
        currentPlayer = save.loadPlayer(scanner, name);
        return currentPlayer != null;
    }

    /** Displays the main menu and current crystal balance. */
    public void displayMainMenu() {
        System.out.println("Welcome, " + currentPlayer.getName() + "!");
        System.out.println("Current Crystals: " + currentPlayer.getCrystals());
        System.out.println("Main Menu:");
        System.out.println("1. Brew a concoction");
        System.out.println("2. Check inventory");
        System.out.println("3. Check spellbook");
        System.out.println("4. Visit market");
        System.out.println("5. Bless a cauldron (1000 crystals)");
        System.out.println("6. Claim login bonus (once per session)");
        System.out.println("7. Exit game");
    }

    /** Handles recipe-mode or creative-mode brewing. */
    public void brewConcoction() {
        currentPlayer.getInventory().displayInventory();
        currentPlayer.getRecipeBook().displayUnlockedRecipes();

        // choices
    }

    /** Displays all ingredient quantities and cauldron counts. */
    public void checkInventory() {
        currentPlayer.getInventory().displayInventory();
        System.out.println("Usable Cauldrons: " + currentPlayer.getInventory().countUsableCauldrons());
        System.out.println("Unusable Cauldrons: " + currentPlayer.getInventory().countUnusableCauldrons());
    }

    /** Displays all recipes currently unlocked by the player. */
    public void checkSpellbook() {
        currentPlayer.getRecipeBook().displayUnlockedRecipes();
    }

    /** Handles market refresh checks, buying, selling, and exit. */
    public void visitMarket() {
        market.displayAvailableListings();

        // choices
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
            System.out.print("Cauldron blessed! You now have " + currentPlayer.getInventory().countUsableCauldrons() + " usable cauldrons.");
            System.out.println("You have " + currentPlayer.getCrystals() + " crystals remaining.");
            return true;
        }
        return false;
    }

    /**
     * Gives one random ingredient once per game session.
     *
     * @return true when the bonus is claimed
     */
    public boolean claimLoginBonus() {
        if (!loginBonusClaimed) {
            currentPlayer.getInventory().addItemStack(save.randItem(currentPlayer.getInventory()).getIngredient(), 1);
            loginBonusClaimed = true;
            System.out.println("Login bonus claimed! You received 1 random ingredient.");
            return true;
        }
        return false;
    }

    /** Saves the current player and exits normally. */
    public void exitGame() {
        save.savePlayer(scanner, currentPlayer);
        System.out.println("Game saved. Goodbye!");
        System.exit(0);
    }
}