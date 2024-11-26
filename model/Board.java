package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    public static final int BOARD_SIZE = 40;

    // Constantes para posições especiais
    public static final int GO_POSITION = 0;
    public static final int JAIL_POSITION = 10;
    public static final int HOLIDAY_POSITION = 20;
    public static final int GO_TO_JAIL_POSITION = 30;
    public static final int TAX_POSITION = 15;
    public static final int TAX_RETURN_POSITION = 25;

    private static Board instance;

    private ArrayList<BoardPosition> positions;
    private Map<String, List<Property>> propertyCategories;

    // Posições específicas para espaços especiais
    private static final int[] NEWS_POSITIONS = {5, 12, 18, 28, 23, 35};
    private static final int[] SHARE_POSITIONS = {7, 14, 22, 27, 33,};

    // Dados para ShareSpaces
    private static final String[] SHARE_NAMES = {
        "Empreendimentos Tensos",
        "Corretora Vai e Vem",
        "Fundos Quebradinhos",
        "Investimentos Nervosos",
        "Bolsa de Troco",
        "Urubus do PIX"
    };
    private static final double[] SHARE_MULTIPLIERS = {1.5, 2.0, 1.2, 1.8, 1.6, 1.5};
    private static final int[] SHARE_PRICES = {250, 300, 350, 400, 450, 200};

    private static final PropertyData[] PROPERTIES_DATA = {
        // Roxo
        new PropertyData("Beco Sem Fim", 200, 50, 50, "Roxo"),
        new PropertyData("Rua das Dores", 220, 60, 50, "Roxo"),
        new PropertyData("Avenida do Nada", 240, 70, 50, "Roxo"),
    
        // Ciano
        new PropertyData("Travessa Perigosa", 260, 80, 100, "Ciano"),
        new PropertyData("Rua do Queijo", 280, 90, 100, "Ciano"),
        new PropertyData("Beco das Brigas", 300, 100, 100, "Ciano"),
    
        // Rosa
        new PropertyData("Avenida da Farofa", 320, 110, 150, "Rosa"),
        new PropertyData("Travessa do Açaí", 340, 120, 150, "Rosa"),
        new PropertyData("Estrada do Pão", 360, 130, 150, "Rosa"),
    
        // Laranja
        new PropertyData("Rua do Molho", 380, 140, 200, "Laranja"),
        new PropertyData("Praça da Pimenta", 400, 150, 200, "Laranja"),
        new PropertyData("Caminho do Alho", 420, 160, 200, "Laranja"),
    
        // Vermelho
        new PropertyData("Estrada do Tomate", 440, 170, 250, "Vermelho"),
        new PropertyData("Beco do Catchup", 460, 180, 250, "Vermelho"),
        new PropertyData("Rua da Páprica", 480, 190, 250, "Vermelho"),
    
        // Amarelo
        new PropertyData("Travessa da Mostarda", 500, 200, 300, "Amarelo"),
        new PropertyData("Avenida do Mel", 520, 210, 300, "Amarelo"),
        new PropertyData("Estrada da Cúrcuma", 540, 220, 300, "Amarelo"),
    
        // Verde
        new PropertyData("Boulevard da Salsa", 560, 230, 350, "Verde"),
        new PropertyData("Rua da Hortelã", 580, 240, 350, "Verde"),
        new PropertyData("Travessa do Alecrim", 600, 250, 350, "Verde"),
    
        // Azul
        new PropertyData("Estrada do Céu", 620, 260, 400, "Azul"),
        new PropertyData("Avenida da Paz", 640, 270, 400, "Azul"),
    };    

    private Board() {
        positions = new ArrayList<>();
        propertyCategories = new HashMap<>();
        initializeBoard();
    }

    public List<Property> getAllProperties() {
        List<Property> allProperties = new ArrayList<>();
        for (BoardPosition position : positions) {
            if (position instanceof Property) {
                allProperties.add((Property) position);
            }
        }
        return allProperties;
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public BoardPosition getSpace(int position) {
        return positions.get(position % BOARD_SIZE);
    }

    public ArrayList<Property> getPropertiesInCategory(String category) {
        ArrayList<Property> properties = new ArrayList<>();
        for (BoardPosition space : positions) {
            if (space instanceof Property) {
                Property property = (Property) space;
                if (property.getCategory().equals(category)) {
                    properties.add(property);
                }
            }
        }
        return properties;
    }

    public List<Property> getPropertiesByCategory(String category) {
        return propertyCategories.getOrDefault(category, new ArrayList<>());
    }

    public int getJailPosition() {
        return JAIL_POSITION;
    }

    public int getGoToJailPosition() {
        return GO_TO_JAIL_POSITION;
    }

    private void initializeBoard() {
        positions.add(GoSpace.getInstance(GO_POSITION));

        int shareIndex = 0;
        int propertyIndex = 0;
        for (int i = 1; i < BOARD_SIZE; i++) {
            if (i == JAIL_POSITION) {
                positions.add(Prison.getInstance(JAIL_POSITION));
            } else if (i == HOLIDAY_POSITION) {
                positions.add(new Holiday(HOLIDAY_POSITION));
            } else if (i == GO_TO_JAIL_POSITION) {
                positions.add(new GoToJailSpace(GO_TO_JAIL_POSITION));
            } else if (i == TAX_POSITION) {
                positions.add(new Tax(TAX_POSITION, 200));
            } else if (i == TAX_RETURN_POSITION) {
                positions.add(new TaxReturn(TAX_RETURN_POSITION, 150));
            } else if (isInArray(i, NEWS_POSITIONS)) {
                positions.add(new NewsSpace(i));
            } else if (isInArray(i, SHARE_POSITIONS)) {
                String shareName = SHARE_NAMES[shareIndex % SHARE_NAMES.length];
                double multiplier = SHARE_MULTIPLIERS[shareIndex % SHARE_MULTIPLIERS.length];
                int sharePrice = SHARE_PRICES[shareIndex % SHARE_PRICES.length];
                positions.add(new ShareSpace(i, shareName, multiplier, sharePrice));
                shareIndex++;
            } else {
                PropertyData data = PROPERTIES_DATA[propertyIndex];
                Property property = new Property(
                    data.getName(),
                    data.getPrice(),
                    data.getRent(),
                    i,
                    data.getCategory(),
                    data.getHousePrice()
                );

                positions.add(property);
                propertyCategories.computeIfAbsent(data.getCategory(), k -> new ArrayList<>()).add(property);
                propertyIndex++;
            }
        }
    }

    private boolean isInArray(int value, int[] array) {
        for (int val : array) {
            if (val == value) return true;
        }
        return false;
    }
}