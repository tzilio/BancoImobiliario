package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int balance;
    private int position;
    private List<Property> properties;
    private boolean inJail;
    
    // Lista de observadores
    private List<Observer> observers;

    public Player(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.inJail = false;
        this.observers = new ArrayList<>(); // Inicializa a lista de observadores
    }

    // Métodos para gerenciar observadores
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Getters e setters
    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }
    
    public void setPosition(int position) {
        this.position = position;
        notifyObservers();  // Notifica os observadores ao atualizar a posição
    }

    public void updateBalance(int amount) {
        this.balance += amount;
        notifyObservers();  // Notifica os observadores ao atualizar o saldo
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        this.position = (position + steps) % Board.BOARD_SIZE;
        notifyObservers();  // Notifica os observadores ao mover o jogador
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
        notifyObservers();  // Notifica os observadores ao adicionar uma propriedade
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
        notifyObservers();  // Notifica os observadores ao atualizar o estado de prisão
    }
}
