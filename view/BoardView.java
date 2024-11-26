package view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import model.Board;
import model.BoardPosition;
import model.Observer;
import model.Player;

public class BoardView extends JPanel implements Observer {
    private final Board board;
    private final Map<Integer, SpaceView> spaceViews;
    private final Map<Player, Integer> playerPositions;
    private final int boardSize;
    private final int borderPositions;

    public BoardView(Board board, int boardSize, List<Player> players) {
        if (boardSize < 2) {
            throw new IllegalArgumentException("boardSize deve ser pelo menos 2.");
        }
        this.board = board;
        this.boardSize = boardSize;
        this.borderPositions = (4 * boardSize) - 4;
        this.spaceViews = new HashMap<>();
        this.playerPositions = new HashMap<>();

        setLayout(new GridBagLayout());
        setBackground(new Color(50, 150, 200)); // Fundo azul clássico
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        addSpacesToGrid(gbc);

        // Painel central vazio
        addCenterPanel();

        // Registra o BoardView como observador dos jogadores
        for (Player player : players) {
            player.addObserver(() -> updatePlayerPosition(player, player.getPosition()));
        }
    }

    private void addSpacesToGrid(GridBagConstraints gbc) {
        int position = 0;

        // Ajustando os tamanhos e mantendo a lógica de percorrer o perímetro
        for (int i = 0; i < boardSize; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            addSpaceToGrid(position++, gbc);
        }
        for (int i = 1; i < boardSize - 1; i++) {
            gbc.gridx = boardSize - 1;
            gbc.gridy = i;
            addSpaceToGrid(position++, gbc);
        }
        for (int i = boardSize - 1; i >= 0; i--) {
            gbc.gridx = i;
            gbc.gridy = boardSize - 1;
            addSpaceToGrid(position++, gbc);
        }
        for (int i = boardSize - 2; i > 0; i--) {
            gbc.gridx = 0;
            gbc.gridy = i;
            addSpaceToGrid(position++, gbc);
        }
    }

    public SpaceView getSpaceView(int position) {
        return spaceViews.get(position); // Retorna a SpaceView para a posição dada
    }    

    private void addSpaceToGrid(int position, GridBagConstraints gbc) {
        BoardPosition boardPosition = board.getSpace(position);
        SpaceView spaceView = new SpaceView(boardPosition, position);
        spaceViews.put(position, spaceView);

        // Configurar tamanhos dinâmicos com base na posição
        if (isCornerPosition(position)) {
            spaceView.setPreferredSize(new Dimension(150, 150)); // Quadrados nos cantos
        } else if (isVerticalPosition(position)) {
            spaceView.setPreferredSize(new Dimension(100, 150)); // Verticais
        } else {
            spaceView.setPreferredSize(new Dimension(150, 100)); // Horizontais
        }

        spaceView.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        add(spaceView, gbc);
    }

    private void addCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false); // Deixa transparente para o fundo azul ser visível
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = boardSize / 2; // Centraliza horizontalmente
        centerGbc.gridy = boardSize / 2; // Centraliza verticalmente
        centerGbc.gridwidth = boardSize - 2; // Reduz o tamanho horizontal
        centerGbc.gridheight = boardSize - 2; // Reduz o tamanho vertical
        add(centerPanel, centerGbc);
    }

    private boolean isCornerPosition(int position) {
        return position == 0 ||
               position == (boardSize - 1) ||
               position == (2 * boardSize - 2) ||
               position == (3 * boardSize - 3);
    }

    private boolean isVerticalPosition(int position) {
        return (position >= boardSize && position < (2 * boardSize - 2)) ||
               (position >= (3 * boardSize - 3) && position < (4 * boardSize - 4));
    }

    public void updatePlayerPosition(Player player, int newPosition) {
        if (player == null) {
            System.err.println("Erro: Jogador é nulo");
            return;
        }

        newPosition = newPosition % borderPositions;

        if (playerPositions.containsKey(player)) {
            int previousPosition = playerPositions.get(player);
            SpaceView previousSpace = spaceViews.get(previousPosition);

            if (previousSpace != null) {
                previousSpace.removePlayerToken(player);
            } else {
                System.err.println("Erro: Posição anterior não encontrada - " + previousPosition);
            }
        }

        playerPositions.put(player, newPosition);

        SpaceView currentSpace = spaceViews.get(newPosition);
        if (currentSpace != null) {
            currentSpace.addPlayerToken(player);
        } else {
            System.err.println("Erro: Nova posição não encontrada - " + newPosition);
        }
    }

    public List<SpaceView> getSpaces() {
        return new ArrayList<>(spaceViews.values()); // Converte os valores do Map para uma lista
    }

    @Override
    public void update() {
        for (Map.Entry<Player, Integer> entry : playerPositions.entrySet()) {
            Player player = entry.getKey();
            updatePlayerPosition(player, player.getPosition());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color startColor = new Color(50, 150, 200);
        Color endColor = new Color(200, 230, 250);
        GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
