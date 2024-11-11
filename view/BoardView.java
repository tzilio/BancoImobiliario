package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import model.Board;
import model.BoardPosition;
import model.Player;

public class BoardView extends JPanel {
    private Board board;
    private Map<Integer, SpaceView> spaceViews;
    private Map<Player, Integer> playerPositions;
    private int boardSize;  // Número de espaços em cada lado do tabuleiro

    public BoardView(Board board, int boardSize) {
        this.board = board;
        this.boardSize = boardSize;
        this.spaceViews = new HashMap<>();
        this.playerPositions = new HashMap<>();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        addSpacesToGrid(gbc);
    }

    private void addSpacesToGrid(GridBagConstraints gbc) {

        // Linha superior
        for (int i = 0; i < boardSize; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            addSpaceToGrid(i, gbc);
        }

        // Coluna direita
        for (int i = 1; i < boardSize - 1; i++) {
            gbc.gridx = boardSize - 1;
            gbc.gridy = i;
            addSpaceToGrid(boardSize + i - 1, gbc);
        }

        // Linha inferior
        for (int i = boardSize - 1; i >= 0; i--) {
            gbc.gridx = i;
            gbc.gridy = boardSize - 1;
            addSpaceToGrid((boardSize * 2 - 2) + (boardSize - 1 - i), gbc);
        }

        // Coluna esquerda
        for (int i = boardSize - 2; i > 0; i--) {
            gbc.gridx = 0;
            gbc.gridy = i;
            addSpaceToGrid((boardSize * 3 - 3) + i, gbc);
        }

        // Espaços centrais (vazio ou logotipo)
        for (int x = 1; x < boardSize - 1; x++) {
            for (int y = 1; y < boardSize - 1; y++) {
                gbc.gridx = x;
                gbc.gridy = y;
                JPanel centerPanel = new JPanel();
                centerPanel.setBackground(Color.WHITE);
                add(centerPanel, gbc);
            }
        }
    }

    private void addSpaceToGrid(int position, GridBagConstraints gbc) {
        BoardPosition boardPosition = board.getSpace(position);
        SpaceView spaceView = new SpaceView(boardPosition, position);
        spaceViews.put(position, spaceView);

        // Ajuste do tamanho dos espaços
        if (isCornerPosition(position)) {
            spaceView.setPreferredSize(new Dimension(80, 80));
        } else {
            if (isVerticalPosition(position)) {
                spaceView.setPreferredSize(new Dimension(80, 60));
            } else {
                spaceView.setPreferredSize(new Dimension(60, 80));
            }
        }

        add(spaceView, gbc);
    }

    private boolean isCornerPosition(int position) {
        return position == 0 || position == boardSize - 1 || position == (boardSize * 2 - 2) || position == (boardSize * 3 - 3);
    }

    private boolean isVerticalPosition(int position) {
        return (position >= boardSize && position < boardSize * 2 - 2) || (position >= boardSize * 3 - 3 && position < boardSize * 4 - 4);
    }

    public void updatePlayerPosition(Player player, int newPosition) {
        // Remover token da posição anterior
        if (playerPositions.containsKey(player)) {
            int previousPosition = playerPositions.get(player);
            SpaceView previousSpace = spaceViews.get(previousPosition);
            previousSpace.removePlayerToken(player);
        }

        // Adicionar token à nova posição
        playerPositions.put(player, newPosition);
        SpaceView currentSpace = spaceViews.get(newPosition);
        currentSpace.addPlayerToken(player);
    }
}
