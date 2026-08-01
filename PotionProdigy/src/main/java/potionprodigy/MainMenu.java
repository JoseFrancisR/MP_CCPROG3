package potionprodigy;

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
        		    scanner.nextLine(); // take out the buffer
        		} while (input != 1 && input != 2);
        	} else {
        		do {
        			System.out.println("1. New Game");
        			System.out.println("Please enter 1 to make a new game: ");
        			input = scanner.nextInt();
        			scanner.nextLine();

        		} while (input != 1);
        	}
        	
        	if(input==1) {
        		System.out.println("Please enter username: ");
        		playerName = scanner.next();
        		scanner.nextLine();
        		startNewGame(playerName);
        	} else {
        		save.displaySaves();
        		System.out.println("Please enter the name of the save to load in: ");
        		playerName = scanner.next();
        		scanner.nextLine();
        		this.currentPlayer = save.loadPlayer(playerName);
        	}
    	}
    	currentPlayer.getRecipeBook().loadRecipes(); // load all recipes once
    	while(!exit) {
            // wait for confirmation
            displayConfirmation();
            displayBorder();
    		displayMainMenu();
    		input = scanner.nextInt();
    		scanner.nextLine();
    		switch(input) {
    			case 1:
    				// brewConcoction();
    		        break;
    			case 2:
    				// checkInventory();
    				break;
    			case 3:
    				// checkSpellbook();
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
    	        	exit = exitGame();
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
            displayBorder();
            System.out.println("A save file with this name already exists. Overwrite? (y/n)");
            String response = scanner.nextLine().trim();
            if (!response.equalsIgnoreCase("y")) {
                System.out.println("New game creation cancelled.");
            } else {
            	currentPlayer = save.createDefaultPlayer(name);
                save.savePlayer(currentPlayer);
                System.out.println("New game created for player: " + name);
            }
        } else {
            currentPlayer = save.createDefaultPlayer(name);
            save.savePlayer(currentPlayer);
            System.out.println("New game created for player: " + name);
        }
        // create new player and save to file
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
        System.out.println("-----------------------------");
        System.out.print("Choice: ");
    }

    /** Handles market refresh checks, buying, selling, and exit. */
    public void visitMarket() {
        boolean exit = false;
        
        if (!market.hasBeenGenerated()) {
            market.generateListings(Ingredient.loadIngredients());
        } else if (brewsSinceMarketVisit >= 3) {
            market.refresh();
            brewsSinceMarketVisit = 0;
        }
        

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
        boolean flag = false;
        if (currentPlayer.getCrystals() >= 1000 && (currentPlayer.getInventory().countUsableCauldrons() < currentPlayer.getInventory().countTotalCauldrons())) {
            currentPlayer.spendCrystals(1000);
            currentPlayer.getInventory().blessCauldron();
            displayBorder();
            System.out.print("Cauldron blessed! You now have " + currentPlayer.getInventory().countUsableCauldrons() + " usable cauldrons.");
            System.out.println("You have " + currentPlayer.getCrystals() + " crystals remaining.");
            flag = true;
        } else if (currentPlayer.getCrystals() < 1000) {
            displayBorder();
            System.out.println("You don't have enough crystals.");
        } else if (currentPlayer.getInventory().countUsableCauldrons() >= currentPlayer.getInventory().countTotalCauldrons()) {
            displayBorder();
            System.out.println("You have no cauldrons to bless");
        }
        return flag;
    }

    /**
     * Gives one random ingredient once per game session.
     *
     * @return true when the bonus is claimed
     */
    public boolean claimLoginBonus() {
    	boolean claimed = false; 
        if (loginBonusClaimed) {
            displayBorder();
            System.out.println("Login Bonus has already been claimed!");
        } else {
            displayBorder();
        	ItemStack bonus = save.randItem(currentPlayer.getInventory());
            if (bonus == null){
                System.out.println("Bonus item not generated");
            } else {
                currentPlayer.getInventory().addItemStack(bonus.getIngredient(), 1);
                loginBonusClaimed = true;
                System.out.println("Login bonus claimed! You receieved " + 1 + "x " + bonus.getIngredient().getName() + ".");
                claimed = true;
            }
        }

        return claimed;
    }
    
    /** Displays the text when player picks recipeMode
     * 
     */
    public void recipeMode() {
    	int id;
    	boolean loop = true;
    	boolean playerBrew = true;
    	Recipe recipe = null;
    	while(loop) {
            displayBorder();
    		System.out.println("Enter the ID of the (Enter -1 to back out) ");
    		   
        	id = scanner.nextInt();
        	scanner.nextLine(); // to fix the buffer of the nextInt
        	recipe = currentPlayer.getRecipeBook().findRecipeById(id);
        	if(id == -1) {
        		System.out.println("Going back...");
        		loop = false;
        		playerBrew = false;
        	} else {
        		if(recipe == null) {
            		System.out.println("The recipe doesn't exists");
            	} else if(!currentPlayer.getInventory().hasIngredients(recipe)){
            		System.out.println("Player doesn't have the ingredients");
            	} else {
            		loop = false;
            	}
        	}
    	}
    	if(playerBrew == true) {
    		if(brew.brewRecipe(currentPlayer, recipe)) {
                displayBorder();
        		System.out.println("Potion was SUCCESFULLY brewed. You EARNED: " + recipe.getSaleValue());
        	} else {
                displayBorder();
        		System.out.println("Failed to brew the potion.");
        	}
    		brewsSinceMarketVisit++;
    	}
    	
    	
    }
    /** Displays the text when player picks creativeMode
     * 
     */
    public void creativeMode() {
    	int earnedCrystals = 0;
    	String input;
    	int ctr= 1;
    	Ingredient base = null;
    	boolean playerBrew = true;
    	boolean loopBase = true;
    	boolean loopFruit = true;
    	ArrayList<Ingredient> fruits = new ArrayList<>();
    	//Check again whether the player has enough usable cauldrons
    	if(currentPlayer.getInventory().countUsableCauldrons() <= 1) {
    		System.out.println("You can't use creative mode when you dont have enough cauldrons");
    		playerBrew = false;
    		loopBase = false;
    		loopFruit = false;
    	}
    	// Player selects base
    	while(loopBase) {
            displayBorder();
            System.out.println();
    		System.out.println("Enter base from your inventory");
    		System.out.println("If you want to exit type 'BACK'");
    		input = scanner.nextLine().trim();
    		if(input.equalsIgnoreCase("back")) {
    			playerBrew = false; 
    			loopBase = false;
    		} else {
    			Ingredient ingredient = Ingredient.findIngredient(input);
        		
        		if(ingredient == null) {
        			System.out.println("The ingredient doesn't exists");
        		} else if(currentPlayer.getInventory().getQuantity(ingredient) < 1) {
        			System.out.println("You do not have " + ingredient.getName() + " retry...");
        		}
        		else if(!ingredient.isConcoctionBase()) {
        			System.out.println("Please enter a base not a fruit");
        		} else {
        			base = ingredient;
        			loopBase = false;
        		}
    		}
    	}
    	
    	
    	// Player selects fruits if still brewing
    	if (playerBrew) {
            displayBorder();
            System.out.println("Pick fruit/s atleast input one fruit and automatically finishes when you input 3 fruits:");
    	    System.out.println("If you want to exit type 'BACK'");
    	    System.out.println("If you're ready to brew type 'DONE'");
            while(loopFruit) {
                displayBorder();
                System.out.println();
    	    	System.out.println("Enter a name of fruit " + ctr + " : ");
    	    	input = scanner.nextLine().trim();
                
    	    	if(input.equalsIgnoreCase("done")) {
    	    		if(fruits.size() >= 1) {
    	    			loopFruit = false;
    	    		} else {
    	    			System.out.println("Atleast add one fruit...");
    	    		}
    	        } else if(input.equalsIgnoreCase("back")) {
    	    		loopFruit = false;
    	    		playerBrew = false;
    	    	} 
            
    	    	if(loopFruit != false && playerBrew != false) {
    	    		Ingredient ingredient = Ingredient.findIngredient(input);
            		if(ingredient == null) {
            			System.out.println("The ingredient doesn't exists");
            		} else if(currentPlayer.getInventory().getQuantity(ingredient) < 1) {
            			System.out.println("You do not have " + ingredient.getName() + " retry...");
            		} else if(!ingredient.isFruit()) {
            			System.out.println("Please enter a fruit not a base:");
            		} else if(hasSelectedFruit(fruits, ingredient))  {
            			System.out.println("You already selected the fruit");
            		}
            		else {
            			fruits.add(ingredient);
            			ctr++;
            		}
                
            		if(fruits.size() == 3) {
            			System.out.println("MAX is 3 fruits starting the brewing process");
            			loopFruit = false;
            		}
    	    	}
    	    }
        }
        // if still continuing to brew
    	if(playerBrew) {
            displayBorder();
            //Make a recipe variable to find the price when selling it
            Recipe checkRecipe = currentPlayer.getRecipeBook().findRecipe(base, fruits);
    		if(brew.brewCreative(currentPlayer, base, fruits)) {
    			if(checkRecipe != null) {
    				earnedCrystals =checkRecipe.getSaleValue();
    			}
         		System.out.println("Potion was SUCCESFULLY brewed. You EARNED: " + earnedCrystals );
        	} else {
        		System.out.println("Failed to brew the potion.");
        	}
    		brewsSinceMarketVisit++;
    	}
    }

    /** Saves the current player and exits normally. */
    public boolean exitGame() {
        displayBorder();
        save.savePlayer(currentPlayer);
        System.out.println("Game saved. Goodbye!");
        return true;
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
            displayBorder();
            System.out.println("No ingredients available to sell.");
        } else {
            displayBorder();
            System.out.println("============ Sell ===========");
            displaySellable(sellable);
            System.out.println();
            System.out.println("Enter the number and quantity (e.g., 2:1,3:2), or enter 0 to cancel:");
            System.out.print("Selected items: ");

            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                System.out.println("Exiting market selling.");
            } else {
            	String[] selected = input.split(",");
                for (String selection : selected) {
                    String[] parts = selection.split(":"); // seperate quantity and number
                    if (parts.length != 2) {
                        System.out.println("Invalid input format: " + selection + ". Skipping.");
                    } else {
                        try {
                            int index = Integer.parseInt(parts[0].trim()) - 1;
                            int quantity = Integer.parseInt(parts[1].trim());

                            if (index < 0 || index >= sellable.size()) {
                                System.out.println("Invalid item number: " + (index + 1) + ". Skipping.");
                            } else {
                            	ItemStack stackToSell = sellable.get(index);

                                if (quantity <= 0 || quantity > stackToSell.getQuantity()) {
                                    System.out.println("Invalid quantity for " + stackToSell.getIngredient().getName() + ". Skipping.");
                                } else {
                                	currentPlayer.getInventory().removeIngredient(stackToSell.getIngredient(), quantity);
                                    int total = stackToSell.getIngredient().getSellingPrice() * quantity;
                                    currentPlayer.addCrystals(total);
                                    System.out.println("Sold " + quantity + "x " + stackToSell.getIngredient().getName() + " for " + total + " crystals.");
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format in selection: " + selection + ". Skipping.");
                        }
                    }
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
                if (slotNumber < 1 || slotNumber > 8) {
                    System.out.println("Invalid slot number: " + slotNumber);
                } else if (selectedSlots.contains(slotNumber)) {
                    System.out.println("Duplicate slot number: " + slotNumber);
                } else {
                    selectedSlots.add(slotNumber);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + part.trim());
            }
        }

        return selectedSlots;
    }

    /**
     * Finds an available listing by slot number.
     * 
     * @param slotNumber the slot number selected by player
     * @return the listing the was valid/found, if not returns null
     */
    private Listing findAvailableListing(int slotNumber) {
        ArrayList<Listing> listings = market.getAvailableListings();
        Listing targetListing = null;
        for (Listing listing : listings) {
            if (listing.getSlotNumber() == slotNumber) {
                targetListing = listing;
            }
        }
        return targetListing; 
    }

    /**
     * Finds if the key ingredient is found within the ArrayList of fruits
     * @param fruits array list of selected fruits to linear search over
     * @param key key ingredient being checked if contained in list
     * @return boolean, true if found, false if not
     */
    private boolean hasSelectedFruit(ArrayList<Ingredient> fruits, Ingredient key) {
        boolean found = false;

        for (Ingredient fruit : fruits) {
            if (found == false) {
                if (fruit.isEqual(key)) {
                    found = true;
                }
            }
        }

        return found;
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

    /**
     * Displays a confirmation screen before proceeding back to main menu
     */
    private void displayConfirmation() {
        System.out.println("Enter any key to proceed to Main Menu");
        scanner.nextLine();
    }

    /**
     * Displays a border line
     */
    private void displayBorder() {
        System.out.println();
        System.out.println("=============================");
    }
}