/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import potionprodigy.Brew;
import potionprodigy.Market;
import potionprodigy.Player;
import potionprodigy.Save;

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
}
