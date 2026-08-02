/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import potionprodigy.Brew;
import potionprodigy.Market;
import potionprodigy.Player;
import potionprodigy.Save;
import potionprodigy.ItemStack;
import potionprodigy.Ingredient;
import potionprodigy.Listing;
import potionprodigy.Recipe;
import java.util.ArrayList;

/**
 * transfers information between model and gui
 * controller manages current player, saving, 
 * brewing, transactions, bonuses, and counters
 */
public class Controller {

    private Save save;
    private Market market;
    private Brew brew;

    private Player currentPlayer;
    private boolean loginBonusClaimed;
    private int brewsSinceMarketVisit;
    
    /**
     * constructs controller and initializes its game and session state
     */
    public Controller() {
        save = new Save();
        market = new Market();
        brew = new Brew();

        currentPlayer = null;
        loginBonusClaimed = false;
        brewsSinceMarketVisit = 0;
    }
    /**
     * Determines whether at least one saved game exists
     *
     * @return boolean, true when at least one save exists; otherwise false
     */
    public boolean hasAnySave() {
        return save.hasSave();
    }
    /**
     * Determines whether a save exists for the specified player
     *
     * @param playerName name of the player whose save will be checked
     * @return boolean, true when the player's save exists
     */
    public boolean saveExists(String playerName) {
        return save.saveExists(playerName);
    }
    
    /**
     * creates and saves a new player using the default starting data
     *
     * @param playerName name assigned to the new player
     * @return boolean, true the newly created player
     */
    public Player createNewGame(String playerName) {
        currentPlayer = save.createDefaultPlayer(playerName);
        save.savePlayer(currentPlayer);

        loginBonusClaimed = false;
        brewsSinceMarketVisit = 0;

        return currentPlayer;
    }
    /**
     * loads a player and resets all session-only values
     *
     * @param playerName name of the player to load
     * @return boolean, true when the player was successfully loaded
     */
    public boolean loadGame(String playerName) {
        Player loadedPlayer = save.loadPlayer(playerName);
        boolean success = false;

        if (loadedPlayer != null) {
            currentPlayer = loadedPlayer;
            loginBonusClaimed = false;
            brewsSinceMarketVisit = 0;
            success = true;
        }

        return success;
    }
    /**
     * writes the current player's data to save file, txt
     *
     * @return boolean, true when the save operation succeeds
     */
    public boolean saveGame() {
        boolean success = false;

        if (currentPlayer != null) {
            success = save.savePlayer(currentPlayer);
        }

        return success;
    }
    /**
     * returns the player currently loaded into the game
     *
     * @return the current player, or {@code null} when no player is loaded
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * returns the market managed by this controller
     *
     * @return the current market
     */
    public Market getMarket() {
        return market;
    }
    /**
     * returns the brewing service managed by this controller
     *
     * @return the brewing service
     */
    public Brew getBrew() {
        return brew;
    }
    /**
     * determines whether the login bonus has been claimed during this session
     *
     * @return boolean true when the login bonus has already been claimed
     */
    public boolean isLoginBonusClaimed() {
        return loginBonusClaimed;
    }
    /**
     * updates the login-bonus state for the current session
     *
     * @param loginBonusClaimed new login-bonus state
     */
    public void setLoginBonusClaimed(boolean loginBonusClaimed) {
        this.loginBonusClaimed = loginBonusClaimed;
    }
    /**
     * returns the number of brews performed since the last market refresh
     *
     * @return number of brews since the last market refresh
     */
    public int getBrewsSinceMarketVisit() {
        return brewsSinceMarketVisit;
    }
    /**
     * resets the market-refresh brewing counter to zero
     */
    public void resetMarketBrewCounter() {
        brewsSinceMarketVisit = 0;
    }
    /**
     * attempts to brew an unlocked recipe for the current player
     *
     * @param recipe recipe selected by the player
     * @return boolean, true when the concoction is brewed successfully
     */
    public boolean brewRecipe(Recipe recipe) {
        boolean success = false;

        if (currentPlayer != null && recipe != null) {
            success = brew.brewRecipe(currentPlayer, recipe);
            
            if (success) {
                brewsSinceMarketVisit++;
            }
        }

        return success;
    }
    /**
     * attempts to brew a concoction from a selected base and fruits
     *
     * @param base selected concoction base
     * @param fruits selected unique fruits
     * @return boolean, true when the ingredients match a valid recipe
     */
    public boolean brewCreative(Ingredient base, ArrayList<Ingredient> fruits) {
        boolean success = false;
        
        if (currentPlayer != null && base != null && fruits != null) {
            success = brew.brewCreative(currentPlayer, base, new ArrayList<>(fruits));
            
            brewsSinceMarketVisit++;
        }
        
        return success;
    }
    /**
     * generates the market for the first visit or refreshes it after the required
     * number of brewing attempts
     */
    public void generateMarket(){
        if (!market.hasBeenGenerated()) {
            market.generateListings(Ingredient.loadIngredients());
        } else if (getBrewsSinceMarketVisit() >= 3) {
            market.refresh();
            resetMarketBrewCounter();
        }
    }
    /**
     * returns the market listings that have not been purchased
     *
     * @return currently available market listings
     */
    public ArrayList<Listing> getAvailableListings(){
        return market.getAvailableListings();
    }
    /**
     * attempts to purchase one market listing for the current player
     *
     * @param listing listing selected for purchase
     * @return transaction status returned by the listing
     */
    public int buyListing(Listing listing) {
        return listing.purchase(currentPlayer);
    }
    /**
     * sells the specified item quantities for the current player
     *
     * @param items item stacks containing the quantities to sell
     */
    public void sellItems(ArrayList<ItemStack> items){
        if (currentPlayer != null && items != null) {
            market.sellMultiple(currentPlayer, items);
        }
    }
    /**
     * calculates the total price of selected market slots
     *
     * @param slotNumbers selected one-based market slot numbers
     * @return total purchase price in crystals
     */
    public int calculateTotalCost(ArrayList<Integer> slotNumbers) {
        int totalCost = 0;
        if (slotNumbers != null) {
            for (Integer slot : slotNumbers) {
                for (Listing listing : market.getAvailableListings()) {
                    if (listing.getSlotNumber() == slot) {
                        totalCost += listing.getTotalPrice();
                    }
                }
            }
        }
        return totalCost;
    }
    /**
     * calculates the total value of the selected items
     *
     * @param items selected items and quantities selected from inventory
     * @return total purchase price in crystals
     */
    public int calculateTotalSellValue(ArrayList<ItemStack> items) {
        int total = 0;
        if(items != null) {
            for(ItemStack stack : items) {
                if (stack.getIngredient() != null) {
                    total += stack.getIngredient().getSellingPrice() * stack.getQuantity();
                }
            }
        }
        return total;
    }
    /**
     * determines whether the specified market slot is available
     *
     * @param slotNumber one-based market slot number
     * @return boolean, true when the slot is available
     */
    public boolean isSlotAvailable(int slotNumber) {
        boolean available = false;
        int i = 0;
        if (market != null) {
            ArrayList<Listing> availableListings = market.getAvailableListings();
        

            while (i < availableListings.size() && !available) {
                if (availableListings.get(i).getSlotNumber() == slotNumber) {
                    available = true;
                }
                i++;
            }
        }

        return available;
    }
    /**
     * attempts to purchase all selected market slots
     *
     * @param slotNumbers selected one-based market slot numbers
     * @return transaction result code for each selected slot
     */
    public ArrayList<Integer> buyItems(ArrayList<Integer> slotNumbers) {
        ArrayList<Integer> results = new ArrayList<>();
        int totalCost = calculateTotalCost(slotNumbers);

        if (currentPlayer != null && currentPlayer.getCrystals() >= totalCost) {
            results = market.buyMultiple(currentPlayer, slotNumbers);
        } else { //when the player cant afford the selected items
            for (int i = 0; i < slotNumbers.size(); i++) {
                results.add(-3);
            }
        }
        return results;
    }
    /**
     * attempts to restore one unusable cauldron for 1000 crystals
     *
     * @return 1 when successful, 0 when the player cannot afford
     *         the blessing, or -1 when no cauldron requires blessing
     */
    public int blessCauldronPay() {
        int status = 0;
        if (currentPlayer.getCrystals() >= 1000 && (currentPlayer.getInventory().countUsableCauldrons() < currentPlayer.getInventory().countTotalCauldrons())) {
            currentPlayer.spendCrystals(1000);
            currentPlayer.getInventory().blessCauldron();
            status = 1;
        } else if (currentPlayer.getCrystals() < 1000) {
            status = 0;
        } else if (currentPlayer.getInventory().countUsableCauldrons() >= currentPlayer.getInventory().countTotalCauldrons()) {
            status = -1;
        }
        return status;
    }
    /**
     * Attempts to claim the session's login bonus.
     *
     * @return the awarded ingredient name, "claimed" when already claimed,
     *         or "error" when no bonus can be awarded
     */
    public String claimLoginBonus() {

    	String status = "claimed";
        if(!loginBonusClaimed) {
            Ingredient bonus = save.randItem(currentPlayer.getInventory());
            if (bonus == null){
                status = "error";
            } else {
                currentPlayer.getInventory().addItemStack(bonus, 1);
                loginBonusClaimed = true;
                status = bonus.getName();
            }
        }
        return status;
    }
}
