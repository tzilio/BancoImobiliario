package view;

import controller.GameController;
import model.Board;
import model.Player;
import model.Bank;
import model.SaveGameManager;
import model.Property;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuView extends JFrame {
    private JComboBox<Integer> playerCountComboBox;
    private List<JTextField> playerNameFields = new ArrayList<>();
    private List<PlayerColorSelector> playerColorSelectors = new ArrayList<>();
    private JPanel playersPanel;
    private JButton startButton, loadButton, exitButton;

    private final String[] colors = { "Vermelho", "Azul", "Verde", "Amarelo", "Branco", "Preto" };
    private final Color[] colorValues = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.WHITE, Color.BLACK };
    private final String backgroundPath = "resources/background_menu.jpg";

    // Conjunto para rastrear cores já selecionadas
    private Set<String> selectedColors = new HashSet<>();

    public MenuView() {
        setTitle("Configuração do Jogo - Banco Imobiliário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLayout(null); // Usar posicionamento absoluto com JLayeredPane

        // Dimensões da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Criação do JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, screenWidth, screenHeight);
        add(layeredPane);

        // Imagem de fundo
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
        ImageIcon originalBackground = new ImageIcon(backgroundPath);
        Image scaledBackground = originalBackground.getImage().getScaledInstance(screenWidth, screenHeight,
                Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledBackground));
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER); // Adiciona no fundo

        // Título
        JLabel titleLabel = new JLabel("BANCO IMOBILIÁRIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(screenWidth / 2 - 300, 20, 600, 50); // Centralizado
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(titleLabel, JLayeredPane.PALETTE_LAYER); // Adiciona acima do fundo

        // Painel de configurações
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setOpaque(false); // Transparente
        configPanel.setBounds(screenWidth / 4, screenHeight / 8 + 50, screenWidth / 2, screenHeight / 2);

        // Seleção de número de jogadores
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        JLabel playerCountLabel = new JLabel("Quantidade de Jogadores: ");
        playerCountLabel.setForeground(Color.WHITE); // Texto branco
        playerCountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playerCountComboBox = new JComboBox<>(new Integer[] { 2, 3, 4, 5, 6 });
        playerCountComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        playerCountComboBox.addActionListener(this::updatePlayerInputs);
        topPanel.add(playerCountLabel);
        topPanel.add(playerCountComboBox);
        configPanel.add(topPanel);

        // Painel central: Configurações dos jogadores
        playersPanel = new JPanel();
        playersPanel.setOpaque(false);
        playersPanel.setLayout(new GridLayout(6, 1, 15, 15)); // Configurado para até 6 jogadores
        configPanel.add(playersPanel);
        layeredPane.add(configPanel, JLayeredPane.PALETTE_LAYER); // Adiciona acima do fundo

        // Painel de botões
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(screenWidth / 3, screenHeight - 250, screenWidth / 3, 150);

        startButton = new RoundedButton("Iniciar Jogo", new Color(60, 179, 113), 20);
        startButton.addActionListener(e -> onStartGame());
        buttonPanel.add(startButton);

        loadButton = new RoundedButton("Carregar Jogo", new Color(30, 144, 255), 20);
        loadButton.addActionListener(e -> onLoadGame());
        buttonPanel.add(loadButton);

        exitButton = new RoundedButton("Sair", new Color(220, 20, 60), 20);
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER); // Adiciona acima do fundo

        updatePlayerInputs(null); // Inicializa os campos
        setVisible(true);
    }

    private void updatePlayerInputs(ActionEvent e) {
        playersPanel.removeAll();
        playerNameFields.clear();
        playerColorSelectors.clear();
        selectedColors.clear();

        int playerCount = (int) playerCountComboBox.getSelectedItem();

        for (int i = 0; i < playerCount; i++) {
            JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            playerPanel.setOpaque(false);

            // Nome do Jogador
            JTextField nameField = new JTextField("Jogador " + (i + 1));
            nameField.setFont(new Font("Arial", Font.PLAIN, 14));
            nameField.setPreferredSize(new Dimension(150, 30));
            nameField.setBorder(new LineBorder(Color.BLUE, 1, true));
            playerNameFields.add(nameField);
            playerPanel.add(nameField);

            // Seletor de Cor
            PlayerColorSelector colorSelector = new PlayerColorSelector(i);
            playerColorSelectors.add(colorSelector);
            playerPanel.add(colorSelector.getPanel());

            playersPanel.add(playerPanel);
        }

        playersPanel.revalidate();
        playersPanel.repaint();
    }

    private void onStartGame() {
        int playerCount = (int) playerCountComboBox.getSelectedItem();
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            String playerName = playerNameFields.get(i).getText();
            String playerColorName = playerColorSelectors.get(i).getSelectedColor();
            if (playerColorName == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma cor para o " + playerName + "!");
                return;
            }

            // Adiciona o jogador diretamente com o nome da cor
            players.add(new Player(playerName, 1500, playerColorName));
        }

        // Configurar o tabuleiro e a interface do jogo
        Board board = Board.getInstance();

        SwingUtilities.invokeLater(() -> {
            GameView gameView = new GameView(board, players, null);
            gameView.setVisible(true);

            GameController gameController = new GameController(players, gameView);
            gameController.startGame();
        });

        setVisible(false); // Oculta o menu
    }

    private void onLoadGame() {
        File savegamesDir = new File("savegames");
    
        if (savegamesDir.exists() && savegamesDir.isDirectory()) {
            File[] saveFiles = savegamesDir.listFiles((dir, name) -> name.endsWith("_savegame.dat"));
    
            if (saveFiles == null || saveFiles.length == 0) {
                JOptionPane.showMessageDialog(this, "Nenhum arquivo de savegame encontrado!");
                return;
            }
    
            String[] saveFileNames = new String[saveFiles.length];
            for (int i = 0; i < saveFiles.length; i++) {
                saveFileNames[i] = saveFiles[i].getName().replace("_savegame.dat", "");
            }
    
            String selectedFile = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecione um savegame para carregar:",
                    "Carregar Jogo",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    saveFileNames,
                    saveFileNames[0]);
    
            if (selectedFile != null) {
                String fullFileName = selectedFile + "_savegame.dat";
                File chosenSaveFile = new File(savegamesDir, fullFileName);
                loadSelectedGame(chosenSaveFile);
            }
        } else {
            JOptionPane.showMessageDialog(this, "A pasta de savegames não foi encontrada!");
        }
    }
    
    private void loadSelectedGame(File saveFile) {
        Object[] loadedData = SaveGameManager.loadGame(saveFile.getPath());
        if (loadedData != null) {
            Bank loadedBank = (Bank) loadedData[0];
            @SuppressWarnings("unchecked")
            List<Player> loadedPlayers = (List<Player>) loadedData[1];
    
            // Atualiza a instância do banco
            Bank.setInstance(loadedBank);
    
            // Obtém a instância do tabuleiro
            Board board = Board.getInstance();
    
            SwingUtilities.invokeLater(() -> {
                // Cria a nova visão do jogo
                GameView gameView = new GameView(board, loadedPlayers, loadedBank);
                gameView.setVisible(true);
    
                // Atualiza as posições dos jogadores no tabuleiro
                for (Player player : loadedPlayers) {
                    gameView.getBoardView().updatePlayerPosition(player, player.getPosition());
    
                    // Atualiza as casas e hotéis das propriedades do jogador
                    for (Property property : player.getProperties()) {
                        SpaceView spaceView = gameView.getBoardView().getSpaceView(property.getPosition());
                        if (spaceView != null) {
                            spaceView.updateHouses(property.getHouses(), property.hasHotel());
                        } else {
                            System.err.println("SpaceView não encontrado para a posição: " + property.getPosition());
                        }
                    }
                }
    
                // Atualiza as informações dos jogadores na interface
                gameView.updatePlayerInfo(loadedPlayers);
    
                // Cria o controlador do jogo e inicia o jogo
                GameController gameController = new GameController(loadedPlayers, gameView);
                gameController.startGame();
            });
    
            setVisible(false); // Oculta o menu principal
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o jogo!");
        }
    }    

    // Classe personalizada para botões arredondados
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
            setPreferredSize(new Dimension(200, 40));
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

    // Classe auxiliar para seleção de cores por jogador
    private class PlayerColorSelector {
        private JPanel panel;
        private String selectedColor = null;
        private List<JButton> colorButtons = new ArrayList<>();

        public PlayerColorSelector(int playerIndex) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(false);

            for (int i = 0; i < colors.length; i++) {
                String colorName = colors[i];
                Color colorValue = colorValues[i];
                JButton colorButton = new JButton();
                colorButton.setBackground(colorValue);
                colorButton.setPreferredSize(new Dimension(30, 30));
                colorButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                colorButton.setOpaque(true);
                colorButton.setToolTipText(colorName);
                colorButton.addActionListener(e -> handleColorSelection(colorName, colorButton));
                colorButtons.add(colorButton);
                panel.add(colorButton);
            }
        }

        public JPanel getPanel() {
            return panel;
        }

        public String getSelectedColor() {
            return selectedColor;
        }

        private void handleColorSelection(String colorName, JButton clickedButton) {
            if (colorName.equals(selectedColor)) {
                // Desseleciona a cor se o mesmo botão for clicado novamente
                selectedColors.remove(colorName);
                selectedColor = null;
                clickedButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            } else {
                // Se já havia uma cor selecionada, removê-la das cores selecionadas
                if (selectedColor != null) {
                    selectedColors.remove(selectedColor);
                }
                selectedColor = colorName;
                selectedColors.add(colorName);
            }
            updateAllColorButtons();
        }

        // Atualiza o estado dos botões de cor em todos os seletores
        private void updateAllColorButtons() {
            for (PlayerColorSelector selector : playerColorSelectors) {
                for (JButton button : selector.colorButtons) {
                    String btnColorName = getColorNameByButton(button);
                    if (selectedColors.contains(btnColorName) && !btnColorName.equals(selector.selectedColor)) {
                        button.setEnabled(false);
                    } else {
                        button.setEnabled(true);
                    }

                    // Atualiza a borda para indicar seleção
                    if (btnColorName.equals(selector.selectedColor)) {
                        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                    } else {
                        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    }
                }
            }
        }

        private String getColorNameByButton(JButton button) {
            for (int i = 0; i < colorValues.length; i++) {
                if (button.getBackground().equals(colorValues[i])) {
                    return colors[i];
                }
            }
            return null;
        }
    }
}