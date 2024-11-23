package model;

public abstract class BoardPosition {
    private int position;  
    private String name;

    public BoardPosition(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }
    
    public abstract void onLand(Player player);
}
