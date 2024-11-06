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


        rollDiceButton = new JButton("Rolar Dados");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rollDiceButton);
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        diceRollLabel = new JLabel("Resultado dos dados: ");
        centerPanel.add(diceRollLabel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(5, 5));
        int casas = 0;
        int limit = (int)(Math.sqrt(board.BOARD_SIZE));
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                if (i == 0 || i == limit - 1 || j == 0 || j == limit - 1) {
                    JButton spaceButton = new JButton("Espaço " + casas);
                    spaceButton.setEnabled(false); 
                    boardPanel.add(spaceButton);
                    casas++;
                } else {
                    boardPanel.add(new JLabel());
                }
            }
        }

        centerPanel.add(boardPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
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
