package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 40; 
    private static final int JAIL_POSITION = 10;
    private static final int GO_TO_JAIL_POSITION = 30;
    
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
    
        int[] newsPositions = {3, 12, 18, 24, 33, 37};
        int[] sharePositions = {5, 14, 20, 28, 36, 39};
        Double[] shareMultipliers = {10.0, 20.0, 15.0, 5.0, 10.0, 15.0};
    
        String[] shareSpaceNames = {
            "Empreendimentos Baratinhos",
            "Investimentos Quebradinhos S.A.",
            "Corretora Tudo ou Nada",
            "Bolsa de Troco LTDA",
            "Fundos Vai que Cola",
            "Capital Arriscado Inc."
        };
        int shareIndex = 0;
    
        for (int i = 1; i < BOARD_SIZE; i++) {
            if (i == getJailPosition()) {
                positions.add(Prison.getInstance(i));
            } else if (i == GO_TO_JAIL_POSITION) {
                positions.add(new GoToJailSpace(i));
            } else if (isInArray(i, newsPositions)) {
                positions.add(new NewsSpace(i));
            } else if (isInArray(i, sharePositions)) {
                String shareName = shareSpaceNames[shareIndex % shareSpaceNames.length];
                positions.add(new ShareSpace(i, shareName, shareMultipliers[shareIndex % shareSpaceNames.length]));
                shareIndex++;
            } else {
                positions.add(new Property("Property " + i, 200 + i * 10, 50 + i * 5, i));
            }
        }
    }
    
    
    private boolean isInArray(int value, int[] array) {
        for (int item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
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

    public int getGoToJailPosition() {
        return GO_TO_JAIL_POSITION;
    }
}
