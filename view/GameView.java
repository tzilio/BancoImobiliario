package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.Player;
import model.Board;
import model.Dice;
import model.Property;
import model.Bank;
import model.SaveGameManager;

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
    private JPanel playerInfoContainer;
    private JButton pauseMenuButton;
    private PauseMenuView pauseMenu;

    public GameView(Board board, List<Player> players, Bank bank) {
        setTitle("Banco Imobiliário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        pauseMenu = new PauseMenuView();

        setupHeader();
        setupMainLayout(board, players);

        // Configuração do botão de menu
        pauseMenuButton.addActionListener(e -> openPauseMenu(players, bank));
    }

    private void setupHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(messageLabel, BorderLayout.CENTER);

        pauseMenuButton = new JButton("Menu");
        headerPanel.add(pauseMenuButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void setupMainLayout(Board board, List<Player> players) {
        JPanel mainPanel = new JPanel(new BorderLayout());

        setupPlayerInfoPanel(players);
        mainPanel.add(playerInfoContainer, BorderLayout.WEST);

        setupCenter(board, players);
        mainPanel.add(boardView, BorderLayout.CENTER);

        JPanel buttonPanel = setupButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        // Adicionar o painel dos dados abaixo do tabuleiro
        JPanel dicePanel = setupDicePanel();
        mainPanel.add(dicePanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupCenter(Board board, List<Player> players) {
        boardView = new BoardView(board, 11, players);

        diceRollLabel = new JLabel("Resultado dos Dados: ");
        diceRollLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        diceRollLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Configuração para exibir os dados
        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel diceOneLabel = new JLabel(new ImageIcon("resources/dices/dice1.png"));
        JLabel diceTwoLabel = new JLabel(new ImageIcon("resources/dices/dice1.png"));
        JButton rollDiceButton = new JButton("Rolar Dados");

        dicePanel.add(diceOneLabel);
        dicePanel.add(diceTwoLabel);
        dicePanel.add(rollDiceButton);

        // Ação do botão
        rollDiceButton.addActionListener(e -> {
            Dice dice = Dice.getInstance();
            dice.roll();

            int dice1 = dice.getDice1();
            int dice2 = dice.getDice2();

            diceOneLabel.setIcon(new ImageIcon("resources/dices/dice" + dice1 + ".png"));
            diceTwoLabel.setIcon(new ImageIcon("resources/dices/dice" + dice2 + ".png"));

            diceRollLabel.setText("Resultado dos Dados: " + dice1 + " e " + dice2);
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(diceRollLabel, BorderLayout.NORTH);
        centerPanel.add(boardView, BorderLayout.CENTER);
        centerPanel.add(dicePanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel setupDicePanel() {
        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel diceOneLabel = new JLabel(new ImageIcon(
                new ImageIcon("resources/dices/dice1.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        JLabel diceTwoLabel = new JLabel(new ImageIcon(
                new ImageIcon("resources/dices/dice1.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

        dicePanel.add(diceOneLabel);
        dicePanel.add(diceTwoLabel);

        rollDiceButton.addActionListener(e -> {
            rollDiceButton.setEnabled(false); 
            Dice dice = Dice.getInstance();

            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                Random rand = new Random();

                while ((System.currentTimeMillis() - startTime) < 2000) { 
                    int animDice1 = rand.nextInt(6) + 1;
                    int animDice2 = rand.nextInt(6) + 1;

                    SwingUtilities.invokeLater(() -> {
                        diceOneLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + animDice1 + ".png")
                                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                        diceTwoLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + animDice2 + ".png")
                                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    });

                    try {
                        Thread.sleep(60); // Intervalo entre animações
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    dice.roll();
                    int dice1 = dice.getDice1();
                    int dice2 = dice.getDice2();

                    diceOneLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + dice1 + ".png").getImage()
                            .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    diceTwoLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + dice2 + ".png").getImage()
                            .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

                    diceRollLabel.setText("Resultado dos Dados: " + dice1 + " e " + dice2);
                    rollDiceButton.setEnabled(true);
                });
            }).start();
        });

        return dicePanel;
    }

    public void displayNewsPopup(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Sorte ou Revés",
            JOptionPane.INFORMATION_MESSAGE
        );
    }    

    private void setupPlayerInfoPanel(List<Player> players) {
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setPreferredSize(new Dimension(300, getHeight()));

        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoPanel.add(playerInfoView);
        }

        JButton togglePlayerInfoButton = new JButton("⮟ Mostrar Jogadores");
        togglePlayerInfoButton.addActionListener(e -> {
            boolean isVisible = playerInfoPanel.isVisible();
            playerInfoPanel.setVisible(!isVisible);
            togglePlayerInfoButton.setText(isVisible ? "⮝ Ocultar Jogadores" : "⮟ Mostrar Jogadores");
            revalidate();
            repaint();
        });

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        togglePanel.add(togglePlayerInfoButton);

        playerInfoContainer = new JPanel(new BorderLayout());
        playerInfoContainer.setPreferredSize(new Dimension(300, getHeight()));
        playerInfoContainer.add(togglePanel, BorderLayout.NORTH);
        playerInfoContainer.add(playerInfoPanel, BorderLayout.CENTER);
    }

    private JPanel setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(200, getHeight()));

        rollDiceButton = new JButton("Rolar Dados");
        buyPropertyButton = new JButton("Comprar Propriedade");
        buildHouseButton = new JButton("Construir Casa");
        passTurnButton = new JButton("Passar Turno");

        buyPropertyButton.setEnabled(false);
        buildHouseButton.setEnabled(false);
        passTurnButton.setEnabled(false);

        propertySelectionBox = new JComboBox<>();
        propertySelectionBox.setEnabled(false);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(rollDiceButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(buyPropertyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(buildHouseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(passTurnButton);

        return buttonPanel;
    }

    private void openPauseMenu(List<Player> players, Bank bank) {
        pauseMenu.addResumeButtonListener(e -> pauseMenu.hideMenu());
        handleSaveGame(players);
        pauseMenu.addLoadGameButtonListener(e -> handleLoadGame(players, bank));
        pauseMenu.addExitButtonListener(e -> handleExitGame());

        pauseMenu.showMenu();
    }

    private void handleSaveGame(List<Player> players) {
        pauseMenu.addSaveGameButtonListener(fileName -> {
            if (!fileName.endsWith("_savegame.dat")) {
                fileName += "_savegame.dat";
            }

            try {
                SaveGameManager.saveGame("savegames/" + fileName, Bank.getInstance(), players);
                displayMessage("Jogo salvo com sucesso como: " + fileName);
            } catch (Exception ex) {
                displayMessage("Erro ao salvar o jogo: " + ex.getMessage());
            }
        });
    }

    private void handleLoadGame(List<Player> players, Bank bank) {
        File savegamesDir = new File("savegames");

        File[] saveFiles = savegamesDir.listFiles((dir, name) -> name.endsWith("_savegame.dat"));

        if (saveFiles == null || saveFiles.length == 0) {
            displayMessage("Nenhum arquivo de salvamento encontrado.");
            return;
        }

        String[] saveFileNames = Arrays.stream(saveFiles)
                .map(file -> file.getName().replace("_savegame.dat", ""))
                .toArray(String[]::new);

        String selectedFile = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o jogo salvo para carregar:",
                "Carregar Jogo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                saveFileNames,
                saveFileNames[0]);

        if (selectedFile != null) {
            String fileNameWithExtension = selectedFile + "_savegame.dat";
            Object[] loadedData = SaveGameManager.loadGame("savegames/" + fileNameWithExtension);
            if (loadedData != null) {
                updateGameState(loadedData, players, bank);
                displayMessage("Jogo carregado com sucesso!");
            } else {
                displayMessage("Erro ao carregar o jogo.");
            }
        } else {
            displayMessage("Nenhum jogo foi selecionado.");
        }
    }

    private void handleExitGame() {
        String[] options = { "Menu Principal", "Sair do Jogo", "Cancelar" };
        int choice = JOptionPane.showOptionDialog(
                this,
                "Você deseja sair para o menu principal ou encerrar o jogo?",
                "Confirmar Saída",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) { // "Menu Principal"
            pauseMenu.hideMenu();
            pauseMenu.dispose();

            this.setVisible(false);
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                MenuView menuView = new MenuView();
                menuView.setVisible(true);
            });
        } else if (choice == 1) {
            System.exit(0);
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

    public void updatePlayerInfo(List<Player> players) {
        // Remove todos os componentes atuais do painel de informações
        playerInfoPanel.removeAll();

        // Adiciona as informações atualizadas de cada jogador
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoPanel.add(playerInfoView);
        }

        // Atualiza o layout do painel
        playerInfoPanel.revalidate();
        playerInfoPanel.repaint();
    }

    public void displayMessage(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
    }

    public void displayDiceRoll(int die1, int die2) {
        if (diceRollLabel != null) {
            diceRollLabel.setText("Resultado dos Dados: " + die1 + " e " + die2);
        }
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public JButton getRollDiceButton() {
        return rollDiceButton;
    }

    public JButton getBuyPropertyButton() {
        return buyPropertyButton;
    }

    public JButton getPassTurnButton() {
        return passTurnButton;
    }

    public void enableBuyPropertyButton(boolean enable) {
        buyPropertyButton.setEnabled(enable);
    }

    public void displayPlayerTurn(Player player) {
        if (messageLabel != null) {
            messageLabel.setText("É a vez de " + player.getName());
        }
    }
}
