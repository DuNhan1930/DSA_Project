import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    protected int playerBalance = 100;
    protected int houseBalance = 1000;

    protected int rounds = 0;
    protected int wins = 0;
    protected int losses = 0;
    protected List<String> roundHistory = new ArrayList<>();

    protected Random random = new Random();

    public RoundResult playRound(int bet, String choice) {
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        int dice3 = random.nextInt(6) + 1;
        int sum = dice1 + dice2 + dice3;

        double payoutMultiplier = 0;
        boolean win = false;

        if (sum < 10 && choice.equals("Under")) {
            payoutMultiplier = 2.0;
            win = true;
        } else if (sum == 10 && choice.equals("Equal")) {
            payoutMultiplier = 4.0;
            win = true;
        } else if (sum > 10 && choice.equals("Over")) {
            payoutMultiplier = 1.8;
            win = true;
        }

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

        return new RoundResult(dice1, dice2, dice3, sum, win, resultMessage, bet);
    }

    public boolean isGameOver() {
        return playerBalance <= 0 || houseBalance <= 0;
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public int getHouseBalance() {
        return houseBalance;
    }

    public int getRounds() {
        return rounds;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public List<String> getRoundHistory() {
        return roundHistory;
    }

    // Sorting history (by bet)
    public List<String> getSortedHistory() {
        List<String> sorted = new ArrayList<>(roundHistory);
        mergeSort(sorted, 0, sorted.size() - 1);
        return sorted;
    }

    private void mergeSort(List<String> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<String> list, int left, int mid, int right) {
        List<String> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<String> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            int betLeft = extractBet(leftList.get(i));
            int betRight = extractBet(rightList.get(j));
            if (betLeft <= betRight) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size()) {
            list.set(k++, leftList.get(i++));
        }
        while (j < rightList.size()) {
            list.set(k++, rightList.get(j++));
        }
    }

    private int extractBet(String round) {
        String[] parts = round.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("Bet")) {
                return Integer.parseInt(parts[i + 1].substring(1));
            }
        }
        return 0;
    }

    // Round result structure
    public static class RoundResult {
        public final int dice1, dice2, dice3, sum;
        public final boolean win;
        public final String message;
        public final int bet;

        public RoundResult(int d1, int d2, int d3, int sum, boolean win, String msg, int bet) {
            this.dice1 = d1;
            this.dice2 = d2;
            this.dice3 = d3;
            this.sum = sum;
            this.win = win;
            this.message = msg;
            this.bet = bet;
        }
    }
}
