package src.main;

import java.util.Scanner;
import java.util.ArrayList;

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
    	int input;
        boolean exit = false;
    	String playerName;
    	System.out.println("WELCOME TO THE ");
    	while(currentPlayer == null) {
    		if(save.hasSave()) {
    			do {
    				
        			System.out.println("1. New Game");
        		    System.out.println("2. Load Game");
        		    System.out.println("Please choose 1 or 2 to continue: ");
        		    input = scanner.nextInt();

        		} while (input != 1 && input != 2);
        	} else {
        		do {
        			System.out.println("1. New Game");
        			System.out.println("Please enter 1 to make a new game: ");
        			input = scanner.nextInt();

        		} while (input != 1);
        	}
        	
        	if(input==1) {
        		System.out.println("Please enter username: ");
        		playerName = scanner.next();
        		startNewGame(playerName);
        	} else {
        		System.out.println("Please enter the name of the save to load in: ");
        		playerName = scanner.next();
        		this.currentPlayer = save.loadPlayer(playerName);
        	}
    	}
    	
    	while(!exit) {
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
    	        	exit = true;
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
            String temp = scanner.nextLine(); // consume the newline character
            String response = scanner.nextLine().trim();
            if (!response.equalsIgnoreCase("y")) {
                System.out.println("New game creation cancelled.");
                return;
            }
        }
        // create new player and save to file
        currentPlayer = save.createDefaultPlayer(name);
        save.savePlayer(currentPlayer);
        System.out.println("New game created for player: " + name);
    }

    /**
     * Loads an existing player.
     *
     * @param name player/save name
     * @return true when a valid save is loaded
     */
    public boolean loadGame(String name) {
        currentPlayer = save.loadPlayer(name);
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
    	boolean loop = true;
    	int input;
        currentPlayer.getInventory().displayInventory();
        if(currentPlayer.getInventory().countUsableCauldrons()>1) {
        	while(loop) {
        		System.out.println("1. Recipe Mode");
        		System.out.println("2. Creative Mode");
        		System.out.println("3. Back");
        		input = scanner.nextInt();
        		switch(input) {
        			case 1:
        				recipeMode();
        				break;
        			case 2: 
        				creativeMode();
        				break;
        			case 3:
        				loop = false;
        				break;
        		}
        	}
        } else {
        	while(loop) {
        		System.out.println("1. Recipe Mode");
        		System.out.println("2. Back");
        		input = scanner.nextInt();
        		switch(input) {
        			case 1:
        				recipeMode();
        				break;
        			case 2: 
        				loop = false;
        				break;
        		}
        	}
        }

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
        boolean exit = false;
        
        if (!market.hasBeenGenerated()) {
            market.generateListings(Ingredient.loadIngredients());
        } else if (brewsSinceMarketVisit >= 3) {
            market.refresh();
        }
        brewsSinceMarketVisit = 0;

        scanner.nextLine(); // read remaining newline character from previous input

        do {
            System.out.println();
            System.out.println("=========== Market ==========");
            System.out.println("Current Crystals: " + currentPlayer.getCrystals());
            System.out.println("1. Buy ingredients/cauldrons");
            System.out.println("2. Sell ingredients");
            System.out.println("3. Leave market");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    buyFromMarket();
                    break;
                case "2":
                    sellToMarket();
                    break;
                case "3":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        } while (!exit);
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
    
    /** Displays the text when player picks recipeMode
     * 
     */
    public void recipeMode() {
    	int id;
    	currentPlayer.getRecipeBook().displayUnlockedRecipes();
    	System.out.println("Enter the ID of the ");
    	id = scanner.nextInt();
    	Recipe recipe = currentPlayer.getRecipeBook().findRecipeById(id);
    	if(recipe != null) {
    		if(brew.brewRecipe(currentPlayer, recipe)) {
        		System.out.println("Potion was SUCCESFULLY brewed.");
        		brewsSinceMarketVisit++;
        	} else {
        		System.out.println("Failed to brew the potion.");
        	}
    	} else {
    		System.out.println("Recipe does not exists");
    	}
    }
    /** Displays the text when player picks creativeMode
     * 
     */
    public void creativeMode() {
    	System.out.println("Pick base");
    	
    	if(brew.brewCreative(currentPlayer, null, null)) {// STILL NOT DONE
    		System.out.println("Potion was SUCCESFULLY brewed.");
    		brewsSinceMarketVisit++;
    	} else {
    		System.out.println("Failed to brew the potion.");
    	}
    }

    /** Saves the current player and exits normally. */
    public void exitGame() {
        save.savePlayer(currentPlayer);
        System.out.println("Game saved. Goodbye!");
        System.exit(0);
    }

    /**
     * buying from market
     * 
     * @param player
     */
    private void buyFromMarket() {
        ArrayList<Listing> listings = market.getAvailableListings();

        if (listings.isEmpty()) {
            System.out.println("No listings available in the market.");
            return;
        }

        System.out.println();
        System.out.println("Available listings:");
        market.displayAvailableListings();
        System.out.println();
        System.out.println("Enter the slot numbers to buy (comma-separated), or enter 0 to cancel:");
        System.out.print("Selected items: ");

        String input = scanner.nextLine().trim();
        if (input.equals("0")) {
            System.out.println("Exiting market buying.");
            return;
        }

        ArrayList<Integer> selectedSlots = parseSelectedSlots(input);

        if (selectedSlots.isEmpty()) {
            System.out.println("No valid slot numbers selected. Exiting market buying.");
            return;
        }

        int crystals = currentPlayer.getCrystals();
        int total = 0, purchases = 0;

        for (Integer slot : selectedSlots) {
            Listing listing = findAvailableListing(slot);

            if (listing == null) {
                System.out.println("Invalid slot number: " + slot + ". Skipping.");
            } else {
                String itemName = listing.isCauldronListing() ? "Cauldron" : listing.getIngredient().getName();
                int cost = listing.getUnitPrice() * listing.getQuantity();

                if (listing.purchase(currentPlayer)){
                    purchases++;
                    total += cost;
                    System.out.println("Purchased " + listing.getQuantity() + "x " + itemName + " for " + cost + " crystals.");
                    System.out.println("Remaining Crystals: " + currentPlayer.getCrystals());
                } else {
                    System.out.println("Failed to purchase " + listing.getQuantity() + "x " + itemName + ". Not enough crystals/item unavailable.");
                }
            }
        }

        if (purchases > 0) {
            System.out.println("Total spent: " + total + " crystals.");
        } else {
            System.out.println("No purchases were made.");
        }
    }

    /**
     * selling ingredients to market
     * 
     * @param player
     */
    private void sellToMarket() {
        ArrayList<ItemStack> inventoryStacks = currentPlayer.getInventory().getIngredientStacks();
        ArrayList<ItemStack> sellable = new ArrayList<>();

        for (ItemStack stack : inventoryStacks) {
            if (stack.getQuantity() > 0) {
                sellable.add(stack);
            }
        }

        if (sellable.isEmpty()) {
            System.out.println("No ingredients available to sell.");
            return;
        }

        System.out.println();
        System.out.println("=========== Sell ==========");
        displaySellable(sellable);
        System.out.println();
        System.out.println("Enter the number and quantity (e.g., 2:1,3:2), or enter 0 to cancel:");
        System.out.print("Selected items: ");

        String input = scanner.nextLine().trim();
        if (input.equals("0")) {
            System.out.println("Exiting market selling.");
            return;
        }

        String[] selected = input.split(",");

        for (String selection : selected) {
            String[] parts = selection.split(":"); // seperate quantity and number
            if (parts.length != 2) {
                System.out.println("Invalid input format: " + selection + ". Skipping.");
                continue;
            } else {
                try {
                    int index = Integer.parseInt(parts[0].trim()) - 1;
                    int quantity = Integer.parseInt(parts[1].trim());

                    if (index < 1 || index > sellable.size()) {
                        System.out.println("Invalid item number: " + (index + 1) + ". Skipping.");
                        // exit
                    }

                    ItemStack stackToSell = sellable.get(index);

                    if (quantity <= 0 || quantity > stackToSell.getQuantity()) {
                        System.out.println("Invalid quantity for " + stackToSell.getIngredient().getName() + ". Skipping.");
                        // exit
                    }
                    
                    currentPlayer.getInventory().removeIngredient(stackToSell.getIngredient(), quantity);
                    int total = stackToSell.getIngredient().getSellingPrice() * quantity;
                    currentPlayer.addCrystals(total);
                    System.out.println("Sold " + quantity + "x " + stackToSell.getIngredient().getName() + " for " + total + " crystals.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in selection: " + selection + ". Skipping.");
                }
            }
        }
    }

    /**
     * parses the user input for selected slots and returns a list of valid slot numbers.
     * 
     * @param input user input string
     * @return list of valid slot numbers
     */
    private ArrayList<Integer> parseSelectedSlots(String input) {
        ArrayList<Integer> selectedSlots = new ArrayList<>();
        String[] parts = input.split(",");

        for (String part : parts) {
            try {
                int slotNumber = Integer.parseInt(part.trim());
                if (slotNumber >= 1 && slotNumber <= market.getAvailableListings().size()) {
                    selectedSlots.add(slotNumber);
                } else {
                    System.out.println("Invalid slot number: " + slotNumber);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + part.trim());
            }
        }

        return selectedSlots;
    }

    /**
     * Finds an available listing by slot number.
     */
    private Listing findAvailableListing(int slotNumber) {
        ArrayList<Listing> listings = market.getAvailableListings();
        for (Listing listing : listings) {
            if (listing.getSlotNumber() == slotNumber) {
                return listing;
            }
        }
        return null; // No available listing found for the given slot number
    }

    /**
     * Displays the sellable ingredients with their quantities and selling prices.
     * 
     * @param sellable list of sellable ItemStacks
     */
    private void displaySellable(ArrayList<ItemStack> sellable) {
        for (int i = 0; i < sellable.size(); i++) {
            ItemStack stack = sellable.get(i);
            System.out.println((i + 1) + ". " + stack.getIngredient().getName() + " - Quantity: " + stack.getQuantity() + " - Selling Price: " + stack.getIngredient().getSellingPrice());
        }
    }
}