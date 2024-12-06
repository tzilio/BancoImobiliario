package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.Player;
import model.Board;
import model.Dice;
import model.Bank;
import model.SaveGameManager;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JButton buyPropertyButton;
    private JButton quitButton;
    private BoardView boardView;
    private JPanel playerInfoPanel;
    private JButton passTurnButton;
    private JPanel playerInfoContainer;
    private JButton pauseMenuButton;
    private PauseMenuView pauseMenu;

    private Map<Player, PlayerInfoView> playerInfoViews = new HashMap<>();


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
        messageLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(messageLabel, BorderLayout.CENTER);

        pauseMenuButton = new JButton("Menu");
        headerPanel.add(pauseMenuButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void setupMainLayout(Board board, List<Player> players) {
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        // Painel de informações dos jogadores
        setupPlayerInfoPanel(players);
        JPanel playerInfoWithSpacing = new JPanel(new BorderLayout());
        playerInfoWithSpacing.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST); // Espaçamento
        playerInfoWithSpacing.add(playerInfoContainer, BorderLayout.CENTER);
        mainPanel.add(playerInfoWithSpacing, BorderLayout.WEST);
    
        // Tabuleiro central
        setupCenter(board, players);
        JPanel boardWithSpacing = new JPanel(new BorderLayout());
        boardWithSpacing.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.NORTH); // Espaçamento acima
        boardWithSpacing.add(boardView, BorderLayout.CENTER);
        boardWithSpacing.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH); // Espaçamento abaixo
        mainPanel.add(boardWithSpacing, BorderLayout.CENTER);
    
        // Painel de botões
        JPanel buttonPanel = setupButtonPanel();
        JPanel buttonsWithSpacing = new JPanel(new BorderLayout());
        buttonsWithSpacing.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST); // Espaçamento
        buttonsWithSpacing.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsWithSpacing, BorderLayout.EAST);
    
        // Adicionar o painel dos dados abaixo do tabuleiro
        JPanel dicePanel = setupDicePanel();
        mainPanel.add(dicePanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }
    

    private void setupCenter(Board board, List<Player> players) {
        boardView = new BoardView(board, 11, players);
    
        boardView.setPreferredSize(new Dimension(1200, 1200)); // Tamanho total do tabuleiro
        boardView.setMinimumSize(new Dimension(1200, 1200));
        boardView.setMaximumSize(new Dimension(1200, 1200));
    
        diceRollLabel = new JLabel("Resultado dos Dados: ");
        diceRollLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        diceRollLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        // Configuração para exibir os dados
        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Espaçamento entre os componentes
    
        // Ajuste do tamanho das imagens dos dados
        int diceImageSize = 100; // Tamanho das imagens dos dados
        JLabel diceOneLabel = new JLabel(new ImageIcon(
                new ImageIcon("resources/dices/dice1.png").getImage().getScaledInstance(diceImageSize, diceImageSize, Image.SCALE_SMOOTH)));
        JLabel diceTwoLabel = new JLabel(new ImageIcon(
                new ImageIcon("resources/dices/dice1.png").getImage().getScaledInstance(diceImageSize, diceImageSize, Image.SCALE_SMOOTH)));
    
        // Botão de Rolar Dados
        JButton rollDiceButton = new JButton("Rolar Dados");
        rollDiceButton.setFont(new Font("Verdana", Font.BOLD, 16));
        rollDiceButton.setPreferredSize(new Dimension(150, 50)); // Ajuste do tamanho do botão
    
        dicePanel.add(diceOneLabel);
        dicePanel.add(diceTwoLabel);
        dicePanel.add(rollDiceButton);
    
        // Ação do botão
        rollDiceButton.addActionListener(e -> {
            Dice dice = Dice.getInstance();
            dice.roll();
    
            int dice1 = dice.getDice1();
            int dice2 = dice.getDice2();
    
            diceOneLabel.setIcon(new ImageIcon(
                    new ImageIcon("resources/dices/dice" + dice1 + ".png").getImage().getScaledInstance(diceImageSize, diceImageSize, Image.SCALE_SMOOTH)));
            diceTwoLabel.setIcon(new ImageIcon(
                    new ImageIcon("resources/dices/dice" + dice2 + ".png").getImage().getScaledInstance(diceImageSize, diceImageSize, Image.SCALE_SMOOTH)));
    
            diceRollLabel.setText("Resultado dos Dados: " + dice1 + " e " + dice2);
        });
    
        // Painel centralizado para o tabuleiro e o painel dos dados
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(diceRollLabel, BorderLayout.NORTH);
        centerPanel.add(boardView, BorderLayout.CENTER);
    
        // Painel para centralizar os dados e o botão em um GridLayout
        JPanel diceAndButtonPanel = new JPanel(new BorderLayout());
        JPanel diceContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // Dados centralizados
        diceContainer.add(diceOneLabel);
        diceContainer.add(diceTwoLabel);
        diceAndButtonPanel.add(diceContainer, BorderLayout.CENTER);
        diceAndButtonPanel.add(rollDiceButton, BorderLayout.EAST); // Botão ao lado
    
        centerPanel.add(diceAndButtonPanel, BorderLayout.SOUTH);
    
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel setupDicePanel() {
        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
        JLabel diceOneLabel = new JLabel(new ImageIcon(
                new ImageIcon("resources/dices/dice1.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        JLabel diceTwoLabel = new JLabel(new ImageIcon(
                new ImageIcon("resources/dices/dice1.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    
        Dimension buttonSize = new Dimension(200, 50); // Largura e altura dos botões
    
        // Botão de Rolar Dados
        rollDiceButton = new JButton("Rolar Dados");
        rollDiceButton.setFont(new Font("Verdana", Font.BOLD, 14));
        rollDiceButton.setBackground(new Color(70, 130, 180)); // Azul
        rollDiceButton.setForeground(Color.WHITE);
        rollDiceButton.setFocusPainted(false);
        rollDiceButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rollDiceButton.setPreferredSize(buttonSize);
        rollDiceButton.setMinimumSize(buttonSize);
        rollDiceButton.setMaximumSize(buttonSize);

        dicePanel.add(diceOneLabel);
        dicePanel.add(diceTwoLabel);
        dicePanel.add(rollDiceButton);
    
        rollDiceButton.addActionListener(e -> {
            rollDiceButton.setEnabled(false); // Desabilitar o botão
            Dice dice = Dice.getInstance();
        
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < 1000) {
                    Random rand = new Random();
                    int animDice1 = rand.nextInt(6) + 1;
                    int animDice2 = rand.nextInt(6) + 1;
        
                    SwingUtilities.invokeLater(() -> {
                        diceOneLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + animDice1 + ".png")
                                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                        diceTwoLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + animDice2 + ".png")
                                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    });
        
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
        
                SwingUtilities.invokeLater(() -> {
                    int dice1 = dice.getDice1();
                    int dice2 = dice.getDice2();
        
                    diceOneLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + dice1 + ".png").getImage()
                            .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                    diceTwoLabel.setIcon(new ImageIcon(new ImageIcon("resources/dices/dice" + dice2 + ".png").getImage()
                            .getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        
                    diceRollLabel.setText("Resultado dos Dados: " + dice1 + " e " + dice2);
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
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void setupPlayerInfoPanel(List<Player> players) {
        // Inicializa o painel de informações dos jogadores
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setPreferredSize(new Dimension(300, getHeight()));
        playerInfoPanel.setBorder(BorderFactory.createTitledBorder("Informações dos Jogadores"));
    
        // Adiciona a exibição de informações de cada jogador
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoViews.put(player, playerInfoView);
            playerInfoPanel.add(playerInfoView);
            playerInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

    
        // Contêiner para o painel lateral
        playerInfoContainer = new JPanel(new BorderLayout());
        playerInfoContainer.setPreferredSize(new Dimension(300, getHeight()));
        playerInfoContainer.add(new JScrollPane(playerInfoPanel), BorderLayout.CENTER);
    }
    
    private transient java.util.List<java.util.function.Consumer<Integer>> movePlayerListeners = new ArrayList<>();

    public void addMovePlayerListener(java.util.function.Consumer<Integer> listener) {
        movePlayerListeners.add(listener);
    }

    private transient java.util.List<java.util.function.Consumer<Void>> quitPlayerListeners = new ArrayList<>();

    public void addQuitPlayerListener(java.util.function.Consumer<Void> listener) {
        quitPlayerListeners.add(listener);
    }
    
    private void fireQuitPlayerEvent() {
        for (var listener : quitPlayerListeners) {
            listener.accept(null);
        }
    }
    

    private JPanel setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(250, getHeight()));
    
        Dimension buttonSize = new Dimension(200, 50); // Largura e altura dos botões
    
        // Botão de Comprar Propriedade
        buyPropertyButton = new JButton("Comprar Propriedade");
        buyPropertyButton.setFont(new Font("Verdana", Font.BOLD, 14));
        buyPropertyButton.setBackground(new Color(46, 139, 87)); // Verde
        buyPropertyButton.setForeground(Color.WHITE);
        buyPropertyButton.setFocusPainted(false);
        buyPropertyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buyPropertyButton.setPreferredSize(buttonSize);
        buyPropertyButton.setMinimumSize(buttonSize);
        buyPropertyButton.setMaximumSize(buttonSize);
        buyPropertyButton.setEnabled(false);
    
        // Botão de Passar Turno
        passTurnButton = new JButton("Passar Turno");
        passTurnButton.setFont(new Font("Verdana", Font.BOLD, 14));
        passTurnButton.setBackground(new Color(255, 165, 0)); // Laranja
        passTurnButton.setForeground(Color.WHITE);
        passTurnButton.setFocusPainted(false);
        passTurnButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passTurnButton.setPreferredSize(buttonSize);
        passTurnButton.setMinimumSize(buttonSize);
        passTurnButton.setMaximumSize(buttonSize);
        passTurnButton.setEnabled(false);
    
        // Botão de Desistência
        quitButton = new JButton("Desistir");
        quitButton.setFont(new Font("Verdana", Font.BOLD, 14));
        quitButton.setBackground(new Color(178, 34, 34)); // Vermelho escuro
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        quitButton.setPreferredSize(buttonSize);
        quitButton.setMinimumSize(buttonSize);
        quitButton.setMaximumSize(buttonSize);
        quitButton.addActionListener(e -> fireQuitPlayerEvent());
    
        // Centraliza e espaça os botões
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espaçamento entre botões
        buttonPanel.add(centerButton(buyPropertyButton));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(centerButton(passTurnButton));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(centerButton(quitButton));
    
        return buttonPanel;
    }
    
    // Método auxiliar para centralizar botões
    private JPanel centerButton(JButton button) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        panel.add(button);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }    
    
    private void openPauseMenu(List<Player> players, Bank bank) {
        pauseMenu.addResumeButtonListener(e -> pauseMenu.hideMenu());
        handleSaveGame(players);
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


    public void removePlayerPanel(Player player) {
        PlayerInfoView playerInfoView = playerInfoViews.remove(player);
        if (playerInfoView != null) {
            playerInfoPanel.remove(playerInfoView);
        }
    
        playerInfoPanel.revalidate();
        playerInfoPanel.repaint();
    }
    

    public void updatePlayerInfo(List<Player> players) {
        playerInfoPanel.removeAll();
    
        for (Player player : players) {
            PlayerInfoView playerInfoView = getPlayerInfoView(player);
            playerInfoView.update(); // Atualiza os detalhes de cada jogador
            playerInfoPanel.add(playerInfoView);
        }
    
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

    public JPanel getPlayerInfoPanel() {
        return playerInfoPanel;
    }

    public PlayerInfoView getPlayerInfoView(Player player) {
        return playerInfoViews.get(player);
    }
    

    public void displayWinnerScreen(List<Player> ranking, GameView gameView) {
        gameView.setVisible(false);
        JFrame winnerFrame = new JFrame("Fim do Jogo!");
        winnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        winnerFrame.setSize(400, 300);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        JLabel titleLabel = new JLabel("Ranking Final");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        int position = 1;
        for (Player player : ranking) {
            JLabel playerLabel = new JLabel(position + "º: " + player.getName() +
                    (player.getBalance() <= 0 ? "(Eliminado)" : " (Vencedor)"));
            playerLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
            playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(playerLabel);
            position++;
        }
    
        JButton closeButton = new JButton("Voltar ao Menu");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> {
            winnerFrame.dispose(); // Fecha a tela do ranking
            gameView.dispose(); // Fecha a tela do jogo
            SwingUtilities.invokeLater(() -> {
                MenuView menuView = new MenuView(); // Abre o menu principal
                menuView.setVisible(true);
            });
        });
    
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(closeButton);
    
        winnerFrame.add(panel);
        winnerFrame.setLocationRelativeTo(null); // Centraliza a janela
        winnerFrame.setVisible(true);
    }    
}
