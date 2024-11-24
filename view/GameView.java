package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Player;
import model.Board;
import model.Property;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JButton buyPropertyButton;
    private JButton buildHouseButton; // Botão para construir casa
    private JComboBox<Property> propertySelectionBox; // Caixa para selecionar propriedade
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
        buildHouseButton = new JButton("Construir Casa"); // Botão para construir casa
        passTurnButton = new JButton("Passar Turno");
    
        buyPropertyButton.setEnabled(false);
        buildHouseButton.setEnabled(false); // Inicia desabilitado
        passTurnButton.setEnabled(false);
    
        propertySelectionBox = new JComboBox<>(); // Caixa de seleção de propriedades
        propertySelectionBox.setEnabled(false); // Inicia desabilitada
    
        buttonPanel.add(rollDiceButton);
        buttonPanel.add(buyPropertyButton);
        buttonPanel.add(propertySelectionBox);
        buttonPanel.add(buildHouseButton);
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

    public JButton getBuildHouseButton() {
        return buildHouseButton; // Retorna o botão para construir casa
    }

    public JComboBox<Property> getPropertySelectionBox() {
        return propertySelectionBox; // Retorna a caixa de seleção de propriedades
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void enableBuyPropertyButton(boolean enable) {
        buyPropertyButton.setEnabled(enable);
    }

    public void enableBuildHouseButton(boolean enable) {
        buildHouseButton.setEnabled(enable); // Habilita/desabilita o botão para construir casa
        propertySelectionBox.setEnabled(enable); // Habilita/desabilita a seleção de propriedades
    }

    public void updatePropertySelectionBox(List<Property> properties) {
        propertySelectionBox.removeAllItems();
        for (Property property : properties) {
            propertySelectionBox.addItem(property);
        }
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
