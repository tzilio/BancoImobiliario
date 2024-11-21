package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance = null;
    private List<BoardPosition> positions;

    public static final int TOTAL_POSITIONS = 40; // Total de posições no tabuleiro
    public static final int JAIL_POSITION = 10;
    public static final int GO_TO_JAIL_POSITION = 30;

    // Construtor privado para implementar o padrão Singleton
    private Board() {
        positions = new ArrayList<>();
        initializePositions();
    }

    // Método para obter a única instância de Board
    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    // Inicializa todas as posições no tabuleiro
    private void initializePositions() {
        for (int i = 0; i < TOTAL_POSITIONS; i++) {
            positions.add(createPosition(i));
        }
    }

    // Cria uma posição com base no índice
    private BoardPosition createPosition(int index) {
        // Dependendo do índice, criar diferentes tipos de posições
        // Por exemplo: Início, Propriedade, Imposto, Sorte/Reves, Prisão, etc.
        switch (index) {
            case 0:
                return new BoardPosition(index, "Início", BoardPosition.PositionType.START);
            case JAIL_POSITION:
                return new BoardPosition(index, "Prisão", BoardPosition.PositionType.JAIL);
            case 20:
                return new BoardPosition(index, "Estacionamento Gratuito", BoardPosition.PositionType.FREE_PARKING);
            case GO_TO_JAIL_POSITION:
                return new BoardPosition(index, "Vá para a Prisão", BoardPosition.PositionType.GO_TO_JAIL);
            // Adicione outros casos para posições especiais
            default:
                // Exemplo de propriedade com custo variável
                int cost = 100 + (index * 10); // Exemplo de cálculo de custo
                return new Property(index, "Propriedade " + (index + 1), cost);
        }
    }

    // Retorna o total de posições no tabuleiro
    public int getTotalPositions() {
        return TOTAL_POSITIONS;
    }

    // Obtém uma posição específica pelo índice
    public BoardPosition getSpace(int index) {
        if (index >= 0 && index < TOTAL_POSITIONS) {
            return positions.get(index);
        }
        throw new IndexOutOfBoundsException("Índice de posição inválido: " + index);
    }

    public int getJailPosition() {
        return JAIL_POSITION;
    }

    public int getGoToJailPosition() {
        return GO_TO_JAIL_POSITION;
    }
}
