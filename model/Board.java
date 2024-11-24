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

    private List<BoardPosition> positions;
    private Map<String, List<Property>> propertyCategories;

    private static final String[] CATEGORIES = {"Roxo", "Ciano", "Rosa", "Laranja", "Vermelho", "Amarelo", "Verde", "Azul"};
    private static final int[] HOUSE_PRICES = {50, 50, 100, 100, 150, 150, 200, 200};
    private static final int PROPERTIES_PER_CATEGORY = 3;

    // Posições específicas para espaços especiais
    private static final int[] NEWS_POSITIONS = {5, 12, 18, 28, 35};
    private static final int[] SHARE_POSITIONS = {7, 14, 22, 27, 33};

    // Dados para ShareSpaces
    private static final String[] SHARE_NAMES = {
        "Empreendimentos Tensos",
        "Corretora Vai e Vem",
        "Fundos Quebradinhos",
        "Investimentos Nervosos",
        "Bolsa de Troco"
    };
    private static final double[] SHARE_MULTIPLIERS = {1.5, 2.0, 1.2, 1.8, 1.6};
    private static final int SHARE_PRICE = 300;

    // Nomes engraçados para propriedades
    private static final String[] FUN_PROPERTY_NAMES = {
        "Beco Sem Fim",
        "Rua das Dores",
        "Avenida do Nada",
        "Travessa Perigosa",
        "Rua do Queijo",
        "Beco das Brigas",
        "Avenida da Farofa",
        "Travessa do Açaí",
        "Estrada do Pão",
        "Viela do Desespero",
        "Alameda da Sorte",
        "Rua dos Lamentos",
        "Praça das Promessas",
        "Boulevard Sem Saída",
        "Passagem do Açúcar",
        "Caminho das Pedras",
        "Avenida dos Sonhos",
        "Travessa do Medo",
        "Rua do Abraço",
        "Praça da Felicidade",
        "Beco do Troco",
        "Rua da Prosperidade",
        "Estrada do Silêncio",
        "Avenida da Alegria",
        "Travessa da Lua",
        "Rua das Estrelas",
        "Caminho do Sol",
        "Praça da Paz",
        "Beco da Fortuna",
        "Estrada do Horizonte",
        "Rua dos Ventos",
        "Travessa das Águas",
        "Avenida do Céu",
        "Caminho do Sorriso",
        "Praça do Arco-Íris",
        "Rua dos Anjos",
        "Boulevard das Flores",
        "Passagem das Ondas",
        "Viela da Harmonia",
        "Alameda do Sucesso"
    };    

    private Board() {
        positions = new ArrayList<>();
        propertyCategories = new HashMap<>();
        initializeBoard();
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
        positions.add(GoSpace.getInstance(GO_POSITION)); // "GO" na posição inicial

        int categoryIndex = 0;
        int propertiesInCategory = 0;
        int shareIndex = 0;

        for (int i = 1; i < BOARD_SIZE; i++) {
            if (i == JAIL_POSITION) {
                positions.add(Prison.getInstance(JAIL_POSITION));
            } else if (i == HOLIDAY_POSITION) {
                positions.add(new Holiday(HOLIDAY_POSITION));
            } else if (i == GO_TO_JAIL_POSITION) {
                positions.add(new GoToJailSpace(GO_TO_JAIL_POSITION));
            } else if (i == TAX_POSITION) {
                positions.add(new Tax(TAX_POSITION, 200)); // Imposto fixo
            } else if (i == TAX_RETURN_POSITION) {
                positions.add(new TaxReturn(TAX_RETURN_POSITION, 150)); // Retorno de imposto
            } else if (isInArray(i, NEWS_POSITIONS)) {
                positions.add(new NewsSpace(i));
            } else if (isInArray(i, SHARE_POSITIONS)) {
                String shareName = SHARE_NAMES[shareIndex % SHARE_NAMES.length];
                double multiplier = SHARE_MULTIPLIERS[shareIndex % SHARE_MULTIPLIERS.length];
                positions.add(new ShareSpace(i, shareName, multiplier, SHARE_PRICE));
                shareIndex++;
            } else {
                String category = CATEGORIES[categoryIndex];
                int housePrice = HOUSE_PRICES[categoryIndex];
                String propertyName = FUN_PROPERTY_NAMES[(i - 1) % FUN_PROPERTY_NAMES.length];
                Property property = new Property(
                    propertyName,
                    200 + i * 10,
                    50 + i * 5,
                    i,
                    category,
                    housePrice
                );

                positions.add(property);

                // Adiciona a propriedade na categoria
                propertyCategories.computeIfAbsent(category, k -> new ArrayList<>()).add(property);

                // Verifica se deve mudar de categoria
                propertiesInCategory++;
                if (propertiesInCategory == PROPERTIES_PER_CATEGORY) {
                    propertiesInCategory = 0;
                    categoryIndex++;
                }
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
