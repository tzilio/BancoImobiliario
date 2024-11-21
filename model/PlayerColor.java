package model;

import java.awt.Color;

public enum PlayerColor {
    VERMELHO("Vermelho", new Color(220, 20, 60)),
    AZUL("Azul", new Color(30, 144, 255)),
    VERDE("Verde", new Color(34, 139, 34)),
    AMARELO("Amarelo", new Color(255, 215, 0)),
    BRANCO("Branco", Color.WHITE),
    PRETO("Preto", Color.BLACK);
    // Adicione mais cores conforme necessário

    private final String name;
    private final Color color;

    PlayerColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    // Método para obter o PlayerColor a partir do nome
    public static PlayerColor fromName(String name) {
        for (PlayerColor pc : PlayerColor.values()) {
            if (pc.getName().equalsIgnoreCase(name)) {
                return pc;
            }
        }
        throw new IllegalArgumentException("Cor inválida: " + name);
    }
}
