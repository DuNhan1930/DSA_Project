import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel {
    private String playerName;
    private UnderOverDiceGame mainFrame;
    private GameController gameController = new GameController();

    private JLabel balanceLabel, houseLabel;
    private JLabel diceLabel1, diceLabel2, diceLabel3;
    private JTextField betField;
    private JComboBox<String> choiceBox;
    private JButton rollButton;

    private Random random = new Random();

    public GamePanel(String playerName, UnderOverDiceGame mainFrame) {
        this.playerName = playerName;
        this.mainFrame = mainFrame;

        setBackground(new Color(59, 104, 1));
        setLayout(new BorderLayout(10, 100));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Place A Bet, " + playerName + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        welcomeLabel.setForeground(Color.WHITE);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 40));
        centerPanel.setBackground(new Color(59, 104, 1));

        // Dice panel
        JPanel dicePanel = new JPanel(new FlowLayout());
        dicePanel.setBackground(new Color(59, 104, 1));
        diceLabel1 = createDiceLabel();
        diceLabel2 = createDiceLabel();
        diceLabel3 = createDiceLabel();
        dicePanel.add(diceLabel1);
        dicePanel.add(diceLabel2);
        dicePanel.add(diceLabel3);
        centerPanel.add(dicePanel, BorderLayout.NORTH);

        // Balance panel
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        balancePanel.setBackground(new Color(59, 104, 1));
        balanceLabel = new JLabel("Your Balance: $" + gameController.getPlayerBalance());
        houseLabel = new JLabel("House Balance: $" + gameController.getHouseBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        houseLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setForeground(new Color(203, 193, 34));
        houseLabel.setForeground(new Color(203, 193, 34));
        balancePanel.add(balanceLabel);
        balancePanel.add(houseLabel);

        // Payout panel (new and separate!)
        JPanel payoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        payoutPanel.setBackground(new Color(59, 104, 1));

        JLabel underLabel = new JLabel("Under 10: x2");
        underLabel.setFont(new Font("Arial", Font.BOLD, 16));
        underLabel.setForeground(Color.WHITE);

        JLabel equalLabel = new JLabel("Equal 10: x4");
        equalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        equalLabel.setForeground(Color.WHITE);

        JLabel overLabel = new JLabel("Over 10: x1.8");
        overLabel.setFont(new Font("Arial", Font.BOLD, 16));
        overLabel.setForeground(Color.WHITE);

        payoutPanel.add(underLabel);
        payoutPanel.add(equalLabel);
        payoutPanel.add(overLabel);

        // A panel to hold both balance and payout (vertical stacking)
        JPanel balanceAndPayoutPanel = new JPanel();
        balanceAndPayoutPanel.setLayout(new BoxLayout(balanceAndPayoutPanel, BoxLayout.Y_AXIS));
        balanceAndPayoutPanel.setBackground(new Color(59, 104, 1));
        balanceAndPayoutPanel.add(balancePanel);
        balanceAndPayoutPanel.add(Box.createVerticalStrut(5)); // gap
        balanceAndPayoutPanel.add(payoutPanel);

        centerPanel.add(balanceAndPayoutPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(59, 104, 1));
        betField = new JTextField(5);
        choiceBox = new JComboBox<>(new String[]{"Under", "Over", "Equal"});
        rollButton = new JButton("Roll!");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.setBackground(new Color(50, 150, 50));
        rollButton.setForeground(Color.WHITE);
        rollButton.setFocusPainted(false);
        rollButton.addActionListener(e -> playRound());
        controlPanel.add(new JLabel("Bet: $") {{
            setForeground(Color.WHITE);
        }});
        controlPanel.add(betField);
        controlPanel.add(new JLabel("Choice:") {{
            setForeground(Color.WHITE);
        }});
        controlPanel.add(choiceBox);
        controlPanel.add(rollButton);

        add(controlPanel, BorderLayout.SOUTH);

        updateDiceIcons(1, 1, 1);
    }

    private JLabel createDiceLabel() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(80, 80));
        label.setOpaque(true);
        label.setBackground(new Color(220, 220, 220));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
        return label;
    }

    private void updateDiceIcons(int val1, int val2, int val3) {
        diceLabel1.setIcon(resizeIcon(new ImageIcon("resources/dice" + val1 + ".png"), 80, 80));
        diceLabel2.setIcon(resizeIcon(new ImageIcon("resources/dice" + val2 + ".png"), 80, 80));
        diceLabel3.setIcon(resizeIcon(new ImageIcon("resources/dice" + val3 + ".png"), 80, 80));
    }

    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void playRound() {
        int betAmount;
        try {
            betAmount = Integer.parseInt(betField.getText());
            if (betAmount <= 0 || betAmount > gameController.getPlayerBalance()) {
                JOptionPane.showMessageDialog(this, "Invalid bet amount!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number!");
            return;
        }

        String betChoice = (String) choiceBox.getSelectedItem();

        Timer animationTimer = new Timer(100, null);
        final int[] count = {0};
        animationTimer.addActionListener(e -> {
            updateDiceIcons(random.nextInt(6) + 1, random.nextInt(6) + 1, random.nextInt(6) + 1);
            count[0]++;
            if (count[0] == 10) {
                animationTimer.stop();

                GameController.RoundResult result = gameController.playRound(betAmount, betChoice);
                updateDiceIcons(result.dice1, result.dice2, result.dice3);
                updateLabels();
                JOptionPane.showMessageDialog(this, result.message);

                if (gameController.isGameOver()) {
                    mainFrame.showStatistics(
                            playerName,
                            gameController.getRounds(),
                            gameController.getWins(),
                            gameController.getLosses(),
                            gameController.getPlayerBalance(),
                            gameController.getHouseBalance(),
                            gameController.getSortedHistory()
                    );
                }
            }
        });
        animationTimer.start();
    }

    private void updateLabels() {
        balanceLabel.setText("Your Balance: $" + gameController.getPlayerBalance());
        houseLabel.setText("House Balance: $" + gameController.getHouseBalance());
    }
}
