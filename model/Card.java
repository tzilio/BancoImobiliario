package model;

public class Card {
    private String description;
    private int balanceChange; 
    private int positionChange;  

    public Card(String description, int balanceChange) {
        this.description = description;
        this.balanceChange = balanceChange;
        this.positionChange = 0;
    }

    public Card(String description, int balanceChange, int positionChange) {
        this.description = description;
        this.balanceChange = balanceChange;
        this.positionChange = positionChange;
    }

    public String getDescription() {
        return description;
    }

    public int getBalanceChange() {
        return balanceChange;
    }

    public int getPositionChange() {
        return positionChange;
    }
    
    public void applyEffect(Player player) {
        player.updateBalance(balanceChange);
        player.move(positionChange);
    }
}
