package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 40; 
    private static final int JAIL_POSITION = 10;
    private static final int GO_TO_JAIL_POSITION = 30;
    private static final int HOLIDAY_POSITION = 20;
    private static final int TAX_POSITION = 15;
    private static final int TAX_RETURN_POSITION = 25;
    private static final int[] NEWS_POSITIONS = {3, 12, 18, 24, 33, 37};
    private static final int[] SHARE_POSITIONS = {5, 14, 19, 28, 36, 39};
    private static final Double[] SHARE_MULTIPLIERS = {10.0, 20.0, 15.0, 5.0, 10.0, 15.0};
    private static final int[] SHARE_PRICES = {300, 500, 450, 200, 350, 400};
    private static final String[] SHARE_SPACE_NAMES = {
        "Empreendimentos Baratinhos",
        "Investimentos Quebradinhos S.A.",
        "Corretora Tudo ou Nada",
        "Bolsa de Troco LTDA",
        "Fundos Vai que Cola",
        "Capital Arriscado Inc."
    };
    
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
    
        int shareIndex = 0;
    
        for (int i = 1; i < BOARD_SIZE; i++) {
            if (i == getJailPosition()) {
                positions.add(Prison.getInstance(i)); 
            } else if (i == GO_TO_JAIL_POSITION) {
                positions.add(new GoToJailSpace(i));
            } else if (i == HOLIDAY_POSITION) {
                positions.add(new Holiday(i));
            } else if (i == TAX_POSITION) {
                positions.add(new Tax(i, 200)); // Posição de imposto
            } else if (i == TAX_RETURN_POSITION) {
                positions.add(new TaxReturn(i, 150)); // Posição de devolução de imposto
            } else if (isInArray(i, NEWS_POSITIONS)) {
                positions.add(new NewsSpace(i)); 
            } else if (isInArray(i, SHARE_POSITIONS)) {
                String shareName = SHARE_SPACE_NAMES[shareIndex % SHARE_SPACE_NAMES.length];
                double multiplier = SHARE_MULTIPLIERS[shareIndex % SHARE_MULTIPLIERS.length];
                int price = SHARE_PRICES[shareIndex % SHARE_PRICES.length];
                positions.add(new ShareSpace(i, shareName, multiplier, price));
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
