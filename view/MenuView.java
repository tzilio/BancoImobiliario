package view;

import controller.GameController;
import model.Bank;
import model.Board;
import model.Player;
import model.SaveGameManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class MenuView extends JFrame {
    private JComboBox<Integer> playerCountComboBox;
    private List<JTextField> playerNameFields = new ArrayList<>();
    private List<JComboBox<String>> playerColorSelectors = new ArrayList<>();
    private JPanel playersPanel;
    private JButton startButton, loadButton, exitButton;

    private final String[] colors = { "Vermelho", "Azul", "Verde", "Amarelo", "Branco", "Preto" };
    // private final String iconPath = "path/to/image.png"; // Substitua pelo
    // caminho correto da imagem
    private final String backgroundPath = "view/assets/background_menu.jpg"; // Substitua pelo caminho correto da imagem
                                                                             // de fundo

    public MenuView() {
        setTitle("Configuração do Jogo - Banco Imobiliário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        setUndecorated(true); // Remove bordas
        setLayout(null); // Posicionamento absoluto

        // Dimensões da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Imagem de fundo dimensionada para 100% da tela
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);

        ImageIcon originalBackground = new ImageIcon(backgroundPath);
        Image scaledBackground = originalBackground.getImage().getScaledInstance(screenWidth, screenHeight,
                Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledBackground));
        add(backgroundLabel);

        // Título
        JLabel titleLabel = new JLabel("BANCO IMOBILIÁRIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(screenWidth / 2 - 300, 20, 600, 50); // Centralizado
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Painel principal transparente para configurações
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setOpaque(false); // Transparente
        configPanel.setBounds(screenWidth / 4, screenHeight / 8 + 50, screenWidth / 2, screenHeight / 2);

        // Seleção de número de jogadores
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        JLabel playerCountLabel = new JLabel("Quantidade de Jogadores: ");
        playerCountLabel.setForeground(Color.WHITE); // Texto branco
        playerCountLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Fonte maior
        playerCountComboBox = new JComboBox<>(new Integer[] { 2, 3, 4, 5, 6 });
        playerCountComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        playerCountComboBox.addActionListener(this::updatePlayerInputs);
        topPanel.add(playerCountLabel);
        topPanel.add(playerCountComboBox);
        configPanel.add(topPanel);

        // Painel central: Configurações dos jogadores
        playersPanel = new JPanel();
        playersPanel.setOpaque(false);
        playersPanel.setLayout(new GridLayout(6, 3, 15, 15)); // Configurado para até 6 jogadores
        configPanel.add(playersPanel);

        // Botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Botões em uma coluna com espaçamento
        buttonPanel.setBounds(screenWidth / 3, screenHeight - 200, screenWidth / 3, 150); // Movidos para baixo

        // Botão Iniciar Jogo
        startButton = new RoundedButton("Iniciar Jogo", new Color(60, 179, 113), 20);
        startButton.addActionListener(e -> onStartGame());
        buttonPanel.add(startButton);

        // Botão Carregar Jogo
        loadButton = new RoundedButton("Carregar Jogo", new Color(30, 144, 255), 20);
        loadButton.addActionListener(e -> onLoadGame());
        buttonPanel.add(loadButton);

        // Botão Sair
        exitButton = new RoundedButton("Sair", new Color(220, 20, 60), 20);
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        add(buttonPanel);

        // Adicionar o painel de configurações e o fundo
        add(configPanel);
        add(backgroundLabel);

        updatePlayerInputs(null); // Inicializa os campos
        setVisible(true);
    }

    private void updatePlayerInputs(ActionEvent e) {
        playersPanel.removeAll();
        playerNameFields.clear();
        playerColorSelectors.clear();

        int playerCount = (int) playerCountComboBox.getSelectedItem();
        for (int i = 0; i < playerCount; i++) {
            // Nome do Jogador
            JTextField nameField = new JTextField("Jogador " + (i + 1));
            nameField.setFont(new Font("Arial", Font.PLAIN, 14));
            nameField.setPreferredSize(new Dimension(150, 30));
            nameField.setBorder(new LineBorder(Color.BLUE, 1, true));
            playerNameFields.add(nameField);

            // Cor do Peão
            JComboBox<String> colorSelector = new JComboBox<>(colors);
            colorSelector.setFont(new Font("Arial", Font.PLAIN, 14));
            playerColorSelectors.add(colorSelector);

            playersPanel.add(nameField);
            playersPanel.add(colorSelector);
        }

        playersPanel.revalidate();
        playersPanel.repaint();
    }

    private void onStartGame() {
        int playerCount = (int) playerCountComboBox.getSelectedItem();
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            String playerName = playerNameFields.get(i).getText();
            String playerColor = (String) playerColorSelectors.get(i).getSelectedItem();
            players.add(new Player(playerName, 1500, playerColor));
        }

        // Configurar o tabuleiro e a interface do jogo
        Board board = Board.getInstance();

        SwingUtilities.invokeLater(() -> {
            GameView gameView = new GameView(board, players, Bank.getInstance());
            gameView.setVisible(true);

            GameController gameController = new GameController(players, gameView);
            gameController.startGame();
        });

        setVisible(false); // Oculta o menu
    }

    private void onLoadGame() {
        Object[] loadedData = SaveGameManager.loadGame("BANQUIMOBILHARIO");
        if (loadedData != null) {
            Bank loadedBank = (Bank) loadedData[0];
            @SuppressWarnings("unchecked")
            List<Player> loadedPlayers = (List<Player>) loadedData[1];

            Bank.setInstance(loadedBank);

            Board board = Board.getInstance();
            SwingUtilities.invokeLater(() -> {
                GameView gameView = new GameView(board, loadedPlayers, loadedBank);
                gameView.setVisible(true);

                // Atualiza a posição dos jogadores no tabuleiro
                for (Player player : loadedPlayers) {
                    gameView.getBoardView().updatePlayerPosition(player, player.getPosition());
                }

                // Atualiza informações dos jogadores
                gameView.updatePlayerInfo(loadedPlayers);

                // Inicializa o controlador com o estado carregado
                GameController gameController = new GameController(loadedPlayers, gameView);
                gameController.startGame();
            });

            setVisible(false); // Oculta o menu principal
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o jogo!");
        }
    }

    private static class RoundedButton extends JButton {
        private final Color backgroundColor;
        private final int borderRadius;

        public RoundedButton(String text, Color backgroundColor, int borderRadius) {
            super(text);
            this.backgroundColor = backgroundColor;
            this.borderRadius = borderRadius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fundo do botão
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

            // Texto
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor.darker());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
            g2.dispose();
        }
    }
}
