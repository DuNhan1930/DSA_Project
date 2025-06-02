import javax.swing.*;

public class UnderOverDiceGame extends JFrame {
    private MainMenu menu;

    public UnderOverDiceGame() {
        setTitle("Under or Over Dice Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        showMenu();
    }

    public void showMenu() {
        menu = new MainMenu(e -> startGame());
        setContentPane(menu);
        revalidate();
    }

    private void startGame() {
        String playerName = menu.getPlayerName();
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name!");
            return;
        }

        setContentPane(new GamePanel(playerName, this));
        revalidate();
    }

    public void showStatistics(String playerName, int rounds, int wins, int losses, int playerBalance, int houseBalance, java.util.List<String> history) {
        StringBuilder stats = new StringBuilder();
        stats.append("Game Over!\n\n");
        stats.append("Player: ").append(playerName).append("\n");
        stats.append("Rounds: ").append(rounds).append("\n");
        stats.append("Wins: ").append(wins).append("\n");
        stats.append("Losses: ").append(losses).append("\n");
        stats.append("Final Player Balance: $").append(playerBalance).append("\n");
        stats.append("Final House Balance: $").append(houseBalance).append("\n\n");
        stats.append("Round History:\n");
        for (String round : history) {
            stats.append(round).append("\n");
        }

        JOptionPane.showMessageDialog(this, stats.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
        showMenu();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UnderOverDiceGame().setVisible(true));
    }
}
