package model;

public abstract class BoardPosition {
    private int position;  

    public Space(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    // Método abstrato para definir ações específicas de cada tipo de espaço
    public abstract void onLand(Player player);
}
