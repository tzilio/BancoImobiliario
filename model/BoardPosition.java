package model;

public class BoardPosition {
    public enum PositionType {
        START,
        PROPERTY,
        TAX,
        CHANCE,
        COMMUNITY_CHEST,
        JAIL,
        FREE_PARKING,
        GO_TO_JAIL,
        // Adicione outros tipos conforme necessário
    }

    private int index;
    private String name;
    private PositionType type;

    public BoardPosition(int index, String name, PositionType type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public PositionType getType() {
        return type;
    }

    // Método que define o que acontece quando um jogador pousa nesta posição
    public void onLand(Player player) {
        /*switch (type) {
            case START:
                player.receiveSalary();
                break;
            case PROPERTY:
                // Implementar lógica específica para propriedades
                break;
            case TAX:
                // Implementar lógica específica para impostos
                break;
            case CHANCE:
                // Implementar lógica específica para Chance
                break;
            case COMMUNITY_CHEST:
                // Implementar lógica específica para Caixa Comunitária
                break;
            case JAIL:
                // Implementar lógica específica para Prisão
                break;
            case FREE_PARKING:
                // Implementar lógica específica para Estacionamento Gratuito
                break;
            case GO_TO_JAIL:
                player.goToJail();
                break;
            // Adicione outros casos conforme necessário
            default:
                break;
            }
        */
    }
}
