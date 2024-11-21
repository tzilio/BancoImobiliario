package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import model.Board;
import model.BoardPosition;
import model.Player;
import model.PlayerColor;

public class BoardView extends JPanel {
    private Board board;
    private Map<Integer, SpaceView> spaceViews;
    private Map<Player, Integer> playerPositions;
    private int boardSize;         // Número de espaços em cada lado do tabuleiro
    private int totalPositions;    // Número total de posições na borda

    // Cores temáticas
    private final Color boardBackgroundColor = new Color(255, 255, 255); // Verde escuro
    private final Color cellBorderColor = Color.BLACK;

    public BoardView(Board board, int boardSize) {
        if (boardSize < 2) {
            throw new IllegalArgumentException("boardSize deve ser pelo menos 2.");
        }
        this.board = board;
        this.boardSize = boardSize;
        this.totalPositions = (4 * boardSize) - 4; // Calcula dinamicamente
        this.spaceViews = new HashMap<>();
        this.playerPositions = new HashMap<>();

        setLayout(new GridLayout(boardSize, boardSize));
        setBackground(boardBackgroundColor);
        initializeBoard();
    }

    private void initializeBoard() {
        int position = 0; // Variável para acompanhar as posições no contorno

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                // Determinar se a célula está na borda
                if (row == 0 || row == boardSize - 1 || col == 0 || col == boardSize - 1) {
                    addSpaceToGrid(position++, row, col);
                } else {
                    // Espaços centrais vazios ou com logotipo
                    JPanel centerPanel = createCenterPanel();
                    add(centerPanel);
                }
            }
        }

        // Verificação final para garantir que todas as posições foram adicionadas
        for (int pos = 0; pos < totalPositions; pos++) {
            if (!spaceViews.containsKey(pos)) {
                System.err.println("Erro: Posição " + pos + " não foi adicionada ao spaceViews");
            }
        }
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(boardBackgroundColor);
        // Aqui você pode adicionar um logotipo ou deixar vazio
        // Exemplo: adicionar um logotipo central
        // JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png"));
        // panel.add(logoLabel);
        return panel;
    }

    private void addSpaceToGrid(int position, int row, int col) {
        BoardPosition boardPosition = board.getSpace(position);
        SpaceView spaceView = new SpaceView(boardPosition, position);
        spaceViews.put(position, spaceView);

        // Estilização da célula
        spaceView.setBorder(BorderFactory.createLineBorder(cellBorderColor));
        spaceView.setBackground(getBackgroundColorForSpace(boardPosition));
        spaceView.setLayout(new BorderLayout());

        add(spaceView);
    }

    private Color getBackgroundColorForSpace(BoardPosition boardPosition) {
        // Defina cores específicas para tipos de espaços, se necessário
        // Exemplo: espaço de início, propriedades, impostos, etc.
        switch (boardPosition.getType()) {
            case START:
                return new Color(0, 150, 0); // Verde mais claro
            case PROPERTY:
                return new Color(200, 200, 200); // Cinza claro
            case TAX:
                return new Color(255, 0, 0); // Vermelho
            case CHANCE:
                return new Color(255, 215, 0); // Ouro
            case COMMUNITY_CHEST:
                return new Color(0, 191, 255); // Azul
            case JAIL:
                return new Color(128, 128, 128); // Cinza
            case FREE_PARKING:
                return new Color(34, 139, 34); // Verde floresta
            case GO_TO_JAIL:
                return new Color(128, 0, 0); // Marrom escuro
            default:
                return new Color(169, 169, 169); // Cinza
        }
    }

    /**
     * Atualiza a posição de um jogador no tabuleiro.
     *
     * @param player      O jogador a ser movido.
     * @param newPosition A nova posição do jogador.
     */
    public void updatePlayerPosition(Player player, int newPosition) {
        if (player == null) {
            System.err.println("Erro: Jogador é nulo");
            return;
        }

        // Ajustar a nova posição para garantir que está dentro do intervalo
        newPosition = newPosition % totalPositions;

        // Remover o token da posição anterior
        if (playerPositions.containsKey(player)) {
            int previousPosition = playerPositions.get(player);
            SpaceView previousSpace = spaceViews.get(previousPosition);
            if (previousSpace != null) {
                previousSpace.removePlayerToken(player);
            } else {
                System.err.println("Erro: Posição anterior não encontrada - " + previousPosition);
            }
        }

        // Atualizar a posição do jogador
        playerPositions.put(player, newPosition);

        // Adicionar o token na nova posição
        SpaceView currentSpace = spaceViews.get(newPosition);
        if (currentSpace != null) {
            currentSpace.addPlayerToken(player);
        } else {
            System.err.println("Erro: Nova posição não encontrada - " + newPosition);
        }

        // Atualizar a interface
        revalidate();
        repaint();
    }

    /**
     * Reseta todas as posições dos jogadores no tabuleiro.
     */
    public void resetPlayerPositions() {
        for (Map.Entry<Player, Integer> entry : playerPositions.entrySet()) {
            Player player = entry.getKey();
            int position = entry.getValue();
            SpaceView spaceView = spaceViews.get(position);
            if (spaceView != null) {
                spaceView.removePlayerToken(player);
            }
        }
        playerPositions.clear();
        revalidate();
        repaint();
    }
}
