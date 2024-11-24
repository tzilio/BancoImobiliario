package view;

import javax.swing.*;
import java.awt.*;
import model.Player;
import model.Bank;
import model.Board;
import model.Property;
import model.SaveGameManager;

import java.util.List;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JButton buyPropertyButton;
    private JButton buildHouseButton;
    private JComboBox<Property> propertySelectionBox;
    private BoardView boardView;
    private JPanel playerInfoPanel;
    private JButton passTurnButton;
    private JButton pauseMenuButton;

    public GameView(Board board, List<Player> players, Bank bank) {
        setupLayout(board, players);
        pauseMenuButton.addActionListener(e -> openPauseMenu(players, bank));
        setTitle("Banco Imobiliário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        headerPanel.add(messageLabel, BorderLayout.WEST);

        pauseMenuButton = new JButton("Menu");
        headerPanel.add(pauseMenuButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void setupCenter(Board board, List<Player> players) {
        JPanel centerPanel = new JPanel(new BorderLayout());

        boardView = new BoardView(board, 11, players);
        centerPanel.add(boardView, BorderLayout.CENTER);

        JPanel dicePanel = new JPanel();
        diceRollLabel = new JLabel("Resultado dos Dados: ");
        dicePanel.add(diceRollLabel);

        centerPanel.add(dicePanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void openPauseMenu(List<Player> players, Bank bank) {
        PauseMenuView pauseMenu = new PauseMenuView();

        pauseMenu.addResumeButtonListener(e -> pauseMenu.hideMenu());
        pauseMenu.addSaveGameButtonListener(e -> handleSaveGame(players));
        pauseMenu.addLoadGameButtonListener(e -> handleLoadGame(players, bank));
        pauseMenu.addExitButtonListener(e -> handleExitGame());

        pauseMenu.showMenu();
    }

    private void handleSaveGame(List<Player> players) {
            SaveGameManager.saveGame("BANQUIMOBILHARIO", Bank.getInstance(), players);
        displayMessage("Jogo salvo com sucesso!");
    }

    private void handleLoadGame(List<Player> players, Bank bank) {
        Object[] loadedData = SaveGameManager.loadGame("BANQUIMOBILHARIO");
        if (loadedData != null) {
            updateGameState(loadedData, players, bank);
            displayMessage("Jogo carregado com sucesso!");
        } else {
            displayMessage("Erro ao carregar o jogo.");
        }
    }

    private void handleExitGame() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Você tem certeza que deseja sair do jogo?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0); // Fecha o jogo
        }
    }

    private void updateGameState(Object[] loadedData, List<Player> players, Bank bank) {
        Bank loadedBank = (Bank) loadedData[0];
        @SuppressWarnings("unchecked")
        List<Player> loadedPlayers = (List<Player>) loadedData[1];
    
        Bank.setInstance(loadedBank);
    
        players.clear();
        players.addAll(loadedPlayers);
    
        boardView.getSpaces().forEach(space -> space.clearPlayerTokens());
    
        for (Player player : players) {
            boardView.updatePlayerPosition(player, player.getPosition());
        }
    
        updatePlayerInfo(players);
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
        buildHouseButton = new JButton("Construir Casa");
        passTurnButton = new JButton("Passar Turno");

        buyPropertyButton.setEnabled(false);
        buildHouseButton.setEnabled(false);
        passTurnButton.setEnabled(false);

        propertySelectionBox = new JComboBox<>();
        propertySelectionBox.setEnabled(false);

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

    // Funções para habilitar/desabilitar botões
    public void enableBuyPropertyButton(boolean enable) {
        buyPropertyButton.setEnabled(enable);
    }

    public void enableBuildHouseButton(boolean enable) {
        buildHouseButton.setEnabled(enable);
        propertySelectionBox.setEnabled(enable);
    }

    // Atualizar informações de jogadores
    public void updatePlayerInfo(List<Player> players) {
        playerInfoPanel.removeAll();
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoPanel.add(playerInfoView);
        }
        playerInfoPanel.revalidate();
        playerInfoPanel.repaint();
    }

    public void updatePropertySelectionBox(List<Property> properties) {
        propertySelectionBox.removeAllItems();
        for (Property property : properties) {
            propertySelectionBox.addItem(property);
        }
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

    // Getters para botões e componentes
    public JButton getRollDiceButton() {
        return rollDiceButton;
    }

    public JButton getBuyPropertyButton() {
        return buyPropertyButton;
    }

    public JButton getBuildHouseButton() {
        return buildHouseButton;
    }

    public JComboBox<Property> getPropertySelectionBox() {
        return propertySelectionBox;
    }

    public JButton getPassTurnButton() {
        return passTurnButton;
    }

    public BoardView getBoardView() {
        return boardView;
    }
}
