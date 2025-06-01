import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JTextField nameField;
    private JLabel diceLabel1, diceLabel2, diceLabel3;

    public MainMenu(ActionListener startListener) {
        setBackground(new Color(59, 104, 1));
        setLayout(new BorderLayout(10, 60));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel titleLabel = new JLabel("Under or Over Dice Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(203, 193, 34));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(59, 104, 1));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel dicePanel = new JPanel(new FlowLayout());
        dicePanel.setBackground(new Color(59, 104, 1));
        diceLabel1 = createDiceLabel();
        diceLabel2 = createDiceLabel();
        diceLabel3 = createDiceLabel();
        dicePanel.add(diceLabel1);
        dicePanel.add(diceLabel2);
        dicePanel.add(diceLabel3);
        centerPanel.add(dicePanel);

        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        nameLabel.setForeground(Color.WHITE);

        nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        centerPanel.add(nameLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(nameField);

        add(centerPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(50, 150, 50));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.addActionListener(startListener);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(59, 104, 1));
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.SOUTH);

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

    public String getPlayerName() {
        return nameField.getText().trim();
    }
}
