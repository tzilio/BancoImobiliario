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
    private JButton passTurnButton;

    public GameView(Board board, List<Player> players) {
        setupLayout(board, players);
        setTitle("Banco Imobiliário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupHeader() {
        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        add(messageLabel, BorderLayout.NORTH);
    }    

    private void setupCenter(Board board, List<Player> players) {
        diceRollLabel = new JLabel("Resultado dos Dados: ");
        add(diceRollLabel, BorderLayout.CENTER);
    
        boardView = new BoardView(board, 11, players);
        add(boardView, BorderLayout.CENTER);
    }    

    private void setupBottomPanel(List<Player> players) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
    
        JPanel buttonPanel = setupButtonPanel();
    
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
    
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoPanel.add(playerInfoView);
        }
    
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        bottomPanel.add(playerInfoPanel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
    
        rollDiceButton = new JButton("Rolar Dados");
        buyPropertyButton = new JButton("Comprar Propriedade");
        passTurnButton = new JButton("Passar Turno");
    
        buyPropertyButton.setEnabled(false);
        passTurnButton.setEnabled(false);
    
        buttonPanel.add(rollDiceButton);
        buttonPanel.add(buyPropertyButton);
        buttonPanel.add(passTurnButton);
    
        return buttonPanel;
    }    

    private void setupLayout(Board board, List<Player> players) {
        setLayout(new BorderLayout());
    
        setupHeader();
        setupCenter(board, players);
        setupBottomPanel(players);
    }    

    public JButton getPassTurnButton() {
        return passTurnButton;
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
