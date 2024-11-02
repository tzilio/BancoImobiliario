package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int balance;
    private int position;  
    private List<Property> properties;
    private boolean inJail; 

    public Player(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.position = 0; 
        this.properties = new ArrayList<>();
        this.inJail = false;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void updateBalance(int amount) {
        this.balance += amount;
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        this.position = (position + steps) % Board.BOARD_SIZE;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }
}
