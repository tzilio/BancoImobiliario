package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private int balance;
    private int position;
    private List<Property> properties;
    private boolean inJail;
    private String color; // Cor do peão

    // Lista de observadores (transient, pois não deve ser serializada)
    private transient List<Observer> observers;

    // Construtor principal
    public Player(String name, int initialBalance, String color) {
        this.name = name;
        this.balance = initialBalance;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.inJail = false;
        this.color = color;
        this.observers = new ArrayList<>();
    }

    // Construtor simplificado para compatibilidade com instâncias antigas
    public Player(String name, int initialBalance) {
        this(name, initialBalance, "Default");
    }

    // Métodos para gerenciar observadores
    public void addObserver(Observer observer) {
        if (observers == null) { // Garantia caso observers seja null após desserialização
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    public void notifyObservers() {
        if (observers != null) {
            for (Observer observer : observers) {
                observer.update();
            }
        }
    }

    // Getters e setters
    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        notifyObservers();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyObservers();
    }

    public void updateBalance(int amount) {
        this.balance += amount;
        notifyObservers();
    }

    public void move(int steps) {
        this.position = (position + steps) % Board.BOARD_SIZE;
        notifyObservers();
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
        notifyObservers();
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
        notifyObservers();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyObservers();
    }

    // Método para restaurar estado após desserialização
    private Object readResolve() {
        this.observers = new ArrayList<>();
        return this;
    }
}