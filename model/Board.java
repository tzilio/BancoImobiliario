package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 40;  // por exemplo, 40 casas no tabuleiro
    private List<BoardPosition> positions;

    public Board() {
        positions = new ArrayList<>(BOARD_SIZE);
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i % 5 == 0) {
                positions.add(new Property("Property " + i, 200 + i * 10, 50 + i * 5, i)); 
            } else {
                positions.add(new GoSpace(i)); 
            }
        }
    }

    public BoardPosition getSpace(int position) {
        return positions.get(position);
    }
}
