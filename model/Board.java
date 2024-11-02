package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 40;  // por exemplo, 40 casas no tabuleiro
    private List<Space> spaces;

    // Construtor
    public Board() {
        spaces = new ArrayList<>(BOARD_SIZE);
        initializeBoard();
    }

    // Método para inicializar o tabuleiro com diferentes tipos de espaços
    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i % 5 == 0) {
                spaces.add(new Property("Property " + i, 200 + i * 10, 50 + i * 5));
            } else {
                spaces.add(new GoSpace(i));
            }
        }
    }

    public Space getSpace(int position) {
        return spaces.get(position);
    }
}
