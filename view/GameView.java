package view;

import javax.swing.*;
import model.Player;
import java.awt.*;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;

    public GameView() {
        setupLayout();
        setTitle("Banco Imobiliário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        add(messageLabel, BorderLayout.NORTH);

        diceRollLabel = new JLabel("Resultado dos Dados: ");
        add(diceRollLabel, BorderLayout.CENTER);

        rollDiceButton = new JButton("Rolar Dados");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rollDiceButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void displayMessage(String message) {
        messageLabel.setText(message);
    }

    public void displayDiceRoll(int die1, int die2) {
        diceRollLabel.setText("Resultado dos Dados: " + die1 + " e " + die2);
    }

    public void displayPlayerTurn(Player player) {
        displayMessage("É a vez de " + player.getName());
    }

    public JButton getRollDiceButton() {
        return rollDiceButton;
    }
}
