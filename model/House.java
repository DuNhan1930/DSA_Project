package model;

import utils.Utils;

public class House {
    private double balance;
    private boolean biasEnabled;

    public House(double initialBalance, boolean biasEnabled) {
        this.balance = initialBalance;
        this.biasEnabled = biasEnabled;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isBiasEnabled() {
        return biasEnabled;
    }

    public void setBiasEnabled(boolean biasEnabled) {
        this.biasEnabled = biasEnabled;
    }

    public void winMoney(double amount) {
        this.balance += amount;
    }

    public void loseMoney(double amount) {
        this.balance -= amount;
    }

    /**
     * Rolls 3 biased dice using Utils
     * @return Total sum of biased rolls
     */
    public int rollBiasedDice() {
        return Utils.biasedRollDie() + Utils.biasedRollDie() + Utils.biasedRollDie();
    }

    @Override
    public String toString() {
        return "House{" +
                "balance=" + balance +
                ", biasEnabled=" + biasEnabled +
                '}';
    }
}
