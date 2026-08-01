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
import java.util.ArrayList;

public class Controller {

    private Save save;
    private Market market;
    private Brew brew;

    private Player currentPlayer;
    private boolean loginBonusClaimed;
    private int brewsSinceMarketVisit;

    public Controller() {
        save = new Save();
        market = new Market();
        brew = new Brew();

        currentPlayer = null;
        loginBonusClaimed = false;
        brewsSinceMarketVisit = 0;
    }

    public boolean hasAnySave() {
        return save.hasSave();
    }

    public boolean saveExists(String playerName) {
        return save.saveExists(playerName);
    }

    public Player createNewGame(String playerName) {
        currentPlayer = save.createDefaultPlayer(playerName);
        save.savePlayer(currentPlayer);

        loginBonusClaimed = false;
        brewsSinceMarketVisit = 0;

        return currentPlayer;
    }

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

    public boolean saveGame() {
        boolean success = false;

        if (currentPlayer != null) {
            success = save.savePlayer(currentPlayer);
        }

        return success;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Market getMarket() {
        return market;
    }

    public Brew getBrew() {
        return brew;
    }

    public boolean isLoginBonusClaimed() {
        return loginBonusClaimed;
    }

    public void setLoginBonusClaimed(boolean loginBonusClaimed) {
        this.loginBonusClaimed = loginBonusClaimed;
    }

    public int getBrewsSinceMarketVisit() {
        return brewsSinceMarketVisit;
    }

    public void recordSuccessfulBrew() {
        brewsSinceMarketVisit++;
    }

    public void resetMarketBrewCounter() {
        brewsSinceMarketVisit = 0;
    }

    public void generateMarket(){
        if (!market.hasBeenGenerated()) {
            market.generateListings(Ingredient.loadIngredients());
        } else if (getBrewsSinceMarketVisit() >= 3) {
            market.refresh();
            resetMarketBrewCounter();
        }
    }

    public ArrayList<Listing> getAvailableListings(){
        return market.getAvailableListings();
    }

    public int buyListing(Listing listing) {
        return listing.purchase(currentPlayer);
    }

    public void sellItems(ArrayList<ItemStack> items){
        if (currentPlayer != null && items != null) {
            market.sellMultiple(currentPlayer, items);
        }
    }


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

    public int claimLoginBonus() {

    	int status = 0;
        if(!loginBonusClaimed) {
            ItemStack bonus = save.randItem(currentPlayer.getInventory());
            if (bonus == null){
                status = -1;
            } else {
                currentPlayer.getInventory().addItemStack(bonus.getIngredient(), 1);
                loginBonusClaimed = true;
                status = 1;
            }
        }
        return status;
    }



}
