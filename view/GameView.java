package view;

import javax.swing.*;
import java.awt.*;
import model.Player;
import model.Board;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JPanel boardPanel; 

    public GameView(Board board) {
        setupLayout(board);
        setTitle("Banco Imobiliário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    private void setupLayout(Board board) {
        setLayout(new BorderLayout());

        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        add(messageLabel, BorderLayout.NORTH);

        diceRollLabel = new JLabel("Resultado dos Dados: ");
        add(diceRollLabel, BorderLayout.CENTER);

        rollDiceButton = new JButton("Rolar Dados");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rollDiceButton);
        add(bottomPanel, BorderLayout.SOUTH);

        boardPanel = new JPanel(new GridLayout(4, 10)); 
        for (int i = 0; i < board.BOARD_SIZE; i++) {
            JButton spaceButton = new JButton("Espaço " + i);
            spaceButton.setEnabled(false); 
            boardPanel.add(spaceButton);
        }
        add(boardPanel, BorderLayout.CENTER);
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
