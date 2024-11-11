package view;

import javax.swing.*;
import java.awt.*;
import model.Player;
import model.Board;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JButton buyPropertyButton;
    private BoardView boardView;

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
        buyPropertyButton = new JButton("Comprar Propriedade");
        buyPropertyButton.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rollDiceButton);
        bottomPanel.add(buyPropertyButton);
        add(bottomPanel, BorderLayout.SOUTH);

        boardView = new BoardView(board, 10);
        add(boardView, BorderLayout.CENTER);
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

    public JButton getBuyPropertyButton() {
        return buyPropertyButton;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void enableBuyPropertyButton(boolean enable) {
        buyPropertyButton.setEnabled(enable);
    }
}
