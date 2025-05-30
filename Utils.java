package utils;

import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    /**
     * Rolls a single six-sided die.
     *
     * @return A number between 1 and 6
     */
    public static int rollDie() {
        return random.nextInt(6) + 1; // Returns 1–6
    }

    /**
     * Rolls 3 dice and returns the total.
     *
     * @return Sum of 3 dice (between 3 and 18)
     */
    public static int rollThreeDice() {
        return rollDie() + rollDie() + rollDie();
    }

    /**
     * Format currency-style output for balance.
     *
     * @param amount Balance or bet
     * @return Formatted string
     */
    public static String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }

    /**
     * (Optional advanced) Biased die roll: makes over slightly more likely.
     * @return A number between 1 and 6 with slight bias
     */
    public static int biasedRollDie() {
        int[] biasedValues = {2, 3, 4, 5, 5, 6}; // slightly higher avg
        return biasedValues[random.nextInt(biasedValues.length)];
    }
}
