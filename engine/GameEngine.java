package engine;

import model.Player;
import model.House;
import utils.Utils;

public class GameEngine {
    private House house;

    public GameEngine(House house) {
        this.house = house;
    }

    /**
     * Plays one round of the game for the given player.
     * @param player The player placing a bet
     * @return The outcome message
     */
    public String playRound(Player player) {
        // Check balance
        if (!player.hasEnoughBalance()) {
            return "Insufficient balance to place the bet.";
        }

        // Roll the dice
        int diceTotal;
        if (house.isBiasEnabled()) {
            diceTotal = house.rollBiasedDice();
        } else {
            diceTotal = Utils.rollThreeDice();
        }

        // Determine win/loss
        boolean isOver = diceTotal > 10;
        boolean playerWins = (isOver && player.getBetChoice().equalsIgnoreCase("over"))
                || (!isOver && player.getBetChoice().equalsIgnoreCase("under"));

        if (playerWins) {
            player.winBet();
            house.loseMoney(player.getBetAmount());
            return "🎉 You win! Dice total: " + diceTotal;
        } else {
            player.loseBet();
            house.winMoney(player.getBetAmount());
            return "💸 You lose. Dice total: " + diceTotal;
        }
    }
}
