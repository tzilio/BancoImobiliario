package model;

public abstract class BoardPosition {
    private int position;  

    public BoardPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    
    public abstract void onLand(Player player);
}
