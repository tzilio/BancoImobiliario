package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Player;
import model.Board;

public class GameView extends JFrame {
    private JLabel messageLabel;
    private JLabel diceRollLabel;
    private JButton rollDiceButton;
    private JButton buyPropertyButton;
    private BoardView boardView;
    private JPanel playerInfoPanel;

    // Cores temáticas do jogo
    private final Color primaryColor = new Color(34, 139, 34); // Verde floresta
    private final Color secondaryColor = new Color(255, 255, 255); // Verde escuro
    private final Color accentColor = new Color(255, 215, 0); // Ouro

    public GameView(Board board, List<Player> players) {
        setupLayout(board, players);
        setTitle("BANCO IMOBILIÁRIO");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Tela cheia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupLayout(Board board, List<Player> players) {
        setLayout(new BorderLayout());

        // Painel Superior para mensagens
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(primaryColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageLabel = new JLabel("Bem-vindo ao Banco Imobiliário!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messageLabel.setForeground(Color.WHITE);

        diceRollLabel = new JLabel("Resultado dos Dados: ", SwingConstants.CENTER);
        diceRollLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        diceRollLabel.setForeground(Color.WHITE);

        topPanel.add(messageLabel);
        topPanel.add(diceRollLabel);
        add(topPanel, BorderLayout.NORTH);

        // Painel Central para o Tabuleiro
        boardView = new BoardView(board, 11); // Supondo boardSize = 11
        add(boardView, BorderLayout.CENTER);

        // Painel Inferior para Botões e Informações dos Jogadores
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(primaryColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel para Botões de Ação
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(primaryColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

        rollDiceButton = createStyledButton("Rolar Dados", accentColor);
        buyPropertyButton = createStyledButton("Comprar Propriedade", new Color(70, 130, 180)); // Azul aço
        buyPropertyButton.setEnabled(false);

        buttonPanel.add(rollDiceButton);
        buttonPanel.add(buyPropertyButton);

        // Painel para Informações dos Jogadores com Scroll
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setBackground(primaryColor);
        playerInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Adiciona informações de cada jogador ao painel de informações
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoView.setMaximumSize(new Dimension(300, 100));
            playerInfoView.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerInfoPanel.add(playerInfoView);
            playerInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre jogadores
        }

        JScrollPane scrollPane = new JScrollPane(playerInfoPanel);
        scrollPane.setPreferredSize(new Dimension(320, 100));
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Informações dos Jogadores"));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(scrollPane, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método auxiliar para criar botões estilizados
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito de hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // Métodos para atualizar a interface
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

    /**
     * Atualiza as informações dos jogadores no painel.
     *
     * @param players Lista atualizada de jogadores.
     */
    public void updatePlayerInfo(List<Player> players) {
        playerInfoPanel.removeAll();
        for (Player player : players) {
            PlayerInfoView playerInfoView = new PlayerInfoView(player);
            playerInfoView.setMaximumSize(new Dimension(300, 100));
            playerInfoView.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerInfoPanel.add(playerInfoView);
            playerInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        playerInfoPanel.revalidate();
        playerInfoPanel.repaint();
    }
}
