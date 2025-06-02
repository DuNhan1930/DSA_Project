import java.util.LinkedList;
import java.util.Queue;

public class HarderGameController extends GameController {
    private int currentLossStreak = 0;
    private int maxLossStreak = getRandomMaxLossStreak();

    private int currentWinStreak = 0;
    private int maxWinStreak = getRandomMaxWinStreak();

    private int betStreakCounter = 0;
    private Queue<Integer> lastThreeBets = new LinkedList<>();
    private boolean firstRound = true;

    private boolean forcedLossActive = false; // New: track forced loss mode

    @Override
    public RoundResult playRound(int bet, String choice) {
        // Track bet size
        if ((double) bet / playerBalance >= 0.5) {
            lastThreeBets.add(bet);
            if (lastThreeBets.size() > 2) lastThreeBets.poll();
            betStreakCounter++;
        } else {
            lastThreeBets.clear();
            betStreakCounter = 0;
        }

        boolean forceLoss = false;
        boolean forceWin = false;

        //if player balance > 70% of house, enter forced loss mode
        if (!forcedLossActive && playerBalance > 0.7 * houseBalance) {
            forcedLossActive = true;
        }
        //if forced loss active & player balance drops below 25%, exit forced loss mode
        if (forcedLossActive && playerBalance < 0.25 * houseBalance) {
            forcedLossActive = false;
        }

        //If forced loss mode active, player always loses
        if (forcedLossActive) {
            forceLoss = true;
        } else if (firstRound) {
            forceWin = true;
        } else if (currentLossStreak >= maxLossStreak) {
            forceWin = true;
        } else if (currentWinStreak >= maxWinStreak) {
            forceLoss = true;
        } else if (betStreakCounter == 3 && wins >= 2) {
            forceLoss = true;
        }

        int dice1, dice2, dice3, sum;
        if (forceLoss) {
            // Generate losing sum
            do {
                dice1 = random.nextInt(6) + 1;
                dice2 = random.nextInt(6) + 1;
                dice3 = random.nextInt(6) + 1;
                sum = dice1 + dice2 + dice3;
            } while (checkWin(sum, choice));
            currentLossStreak++;
            currentWinStreak = 0;
            maxWinStreak = getRandomMaxWinStreak(); // re-randomize after forced loss
        } else if (forceWin) {
            // Generate winning sum
            do {
                dice1 = random.nextInt(6) + 1;
                dice2 = random.nextInt(6) + 1;
                dice3 = random.nextInt(6) + 1;
                sum = dice1 + dice2 + dice3;
            } while (!checkWin(sum, choice));
            currentLossStreak = 0;
            currentWinStreak++;
            maxLossStreak = getRandomMaxLossStreak(); // re-randomize after forced win
        } else {
            // Normal play
            dice1 = random.nextInt(6) + 1;
            dice2 = random.nextInt(6) + 1;
            dice3 = random.nextInt(6) + 1;
            sum = dice1 + dice2 + dice3;

            if (checkWin(sum, choice)) {
                currentWinStreak++;
                currentLossStreak = 0;
                maxLossStreak = getRandomMaxLossStreak(); // reset after normal win
            } else {
                currentLossStreak++;
                currentWinStreak = 0;
                maxWinStreak = getRandomMaxWinStreak(); // reset after normal loss
            }
        }

        double payoutMultiplier = 0;
        boolean win = checkWin(sum, choice);
        if (sum < 10 && choice.equals("Under")) payoutMultiplier = 2.0;
        else if (sum == 10 && choice.equals("Equal")) payoutMultiplier = 4.0;
        else if (sum > 10 && choice.equals("Over")) payoutMultiplier = 1.8;

        String resultMessage;
        if (win) {
            int winnings = (int) (bet * payoutMultiplier);
            playerBalance += winnings;
            houseBalance -= winnings;
            wins++;
            resultMessage = "You won! Sum: " + sum + " | Payout: $" + winnings;
        } else {
            playerBalance -= bet;
            houseBalance += bet;
            losses++;
            resultMessage = "You lost! Sum: " + sum;
        }

        rounds++;
        roundHistory.add("Round " + rounds + ": Bet $" + bet + " on " + choice + " | " + resultMessage);

        firstRound = false;
        return new RoundResult(dice1, dice2, dice3, sum, win, resultMessage, bet);
    }

    private boolean checkWin(int sum, String choice) {
        return (sum < 10 && choice.equals("Under"))
                || (sum == 10 && choice.equals("Equal"))
                || (sum > 10 && choice.equals("Over"));
    }

    private int getRandomMaxLossStreak() {
        return 2 + random.nextInt(4); // random 2–5
    }

    private int getRandomMaxWinStreak() {
        return 1 + random.nextInt(4); // random 1–4
    }
}
