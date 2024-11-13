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
    private int borderPositions; // Número total de posições na borda(posições que vão ser usadas durante o jogo).

    public BoardView(Board board, int boardSize) {
        if (boardSize < 2) {
            throw new IllegalArgumentException("boardSize deve ser pelo menos 2.");
        }
        this.board = board;
        this.boardSize = boardSize;
        this.borderPositions = (4 * boardSize) - 4; // Calcula dinamicamente
        this.spaceViews = new HashMap<>();
        this.playerPositions = new HashMap<>();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        addSpacesToGrid(gbc);
    }

    private void addSpacesToGrid(GridBagConstraints gbc) {
        int position = 0; // Variável para acompanhar as posições no contorno

        // Linha superior (posições 0 a boardSize-1)
        for (int i = 0; i < boardSize; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            addSpaceToGrid(position++, gbc);  // Posições 0 a boardSize-1
        }

        // Coluna direita (posições boardSize a 2*boardSize-3)
        for (int i = 1; i < boardSize - 1; i++) {
            gbc.gridx = boardSize - 1;
            gbc.gridy = i;
            addSpaceToGrid(position++, gbc);  // Posições seguintes
        }

        // Linha inferior (posições 2*boardSize-2 a 3*boardSize-3)
        for (int i = boardSize - 1; i >= 0; i--) {
            gbc.gridx = i;
            gbc.gridy = boardSize - 1;
            addSpaceToGrid(position++, gbc);  // Posições na linha inferior
        }

        // Coluna esquerda (posições 3*boardSize-3 a 4*boardSize-5)
        for (int i = boardSize - 2; i > 0; i--) {
            gbc.gridx = 0;
            gbc.gridy = i;
            addSpaceToGrid(position++, gbc);  // Posições na coluna esquerda
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

        // Verificação final para garantir que todas as posições foram adicionadas
        for (int pos = 0; pos < borderPositions; pos++) {
            if (!spaceViews.containsKey(pos)) {
                System.err.println("Erro: Posição " + pos + " não foi adicionada ao spaceViews");
            } else {
                System.out.println("Posição " + pos + " adicionada corretamente.");
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

        // Ajustar a nova posição para garantir que está dentro do intervalo
        newPosition = newPosition % borderPositions;

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
    }
}
