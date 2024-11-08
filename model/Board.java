package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 40; 
    private static final int JAIL_POSITION = 10;
    private static final int GO_TO_JAIL_POSITION = 10;
    
    private List<BoardPosition> positions;

    private static Board instance;

    private Board() {
        positions = new ArrayList<>(BOARD_SIZE);
        initializeBoard();
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    private void initializeBoard() {
        positions.add(GoSpace.getInstance(0)); 
        for (int i = 1; i < BOARD_SIZE; i++) {
            if (i == getJailPosition()) {
                positions.add(Prison.getInstance(i));
            } else if (i == GO_TO_JAIL_POSITION) {
                positions.add(new GoToJailSpace(i));
            } else {
                positions.add(new Property("Property " + i, 200 + i * 10, 50 + i * 5, i)); 
            } 
        }
    }

    public BoardPosition getSpace(int position) {
        if (position >= BOARD_SIZE || position < 0) {
            System.out.println("estorvo");
            return null;
        }
        return positions.get(position);
    }

    public int getJailPosition() {
        return JAIL_POSITION;
    }
}
