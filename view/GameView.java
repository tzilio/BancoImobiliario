package view;

import javax.swing.*;
import java.awt.*;
import model.Player;
import model.Board;
import java.util.List;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JButton buyPropertyButton;
    private BoardView boardView;
    private JPanel playerInfoPanel; // Painel para exibir informações dos jogadores

    public GameView(Board board, List<Player> players) {
        setupLayout(board, players);
        setTitle("Banco Imobiliário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupLayout(Board board, List<Player> players) {
        setLayout(new BorderLayout());

        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        add(messageLabel, BorderLayout.NORTH);

        diceRollLabel = new JLabel("Resultado dos Dados: ");
        add(diceRollLabel, BorderLayout.CENTER);

        rollDiceButton = new JButton("Rolar Dados");
        buyPropertyButton = new JButton("Comprar Propriedade");
        buyPropertyButton.setEnabled(false);

        // Painel inferior para botões e informações dos jogadores
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Painel para botões de ação
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rollDiceButton);
        buttonPanel.add(buyPropertyButton);
        
        // Painel para exibir informações dos jogadores
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        
        // Adiciona informações de cada jogador ao painel de informações
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoPanel.add(playerInfoView);
        }
        
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(playerInfoPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        boardView = new BoardView(board, 11);
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

    public void updatePlayerInfo(List<Player> players) {
        playerInfoPanel.removeAll();
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoPanel.add(playerInfoView);
        }
        playerInfoPanel.revalidate();
        playerInfoPanel.repaint();
    }
}
