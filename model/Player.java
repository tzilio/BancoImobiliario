package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int balance;
    private int position;
    private List<Property> properties;
    private boolean inJail;
    private String color; // Nova propriedade para a cor do peão
    
    // Lista de observadores
    private List<Observer> observers;

    // Construtor principal
    public Player(String name, int initialBalance, String color) {
        this.name = name;
        this.balance = initialBalance;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.inJail = false;
        this.color = color; // Inicializa a cor do peão
        this.observers = new ArrayList<>();
    }

    // Construtor simplificado para compatibilidade com instâncias antigas
    public Player(String name, int initialBalance) {
        this(name, initialBalance, "Default");
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
        notifyObservers(); // Notifica os observadores ao atualizar a posição
    }

    public void updateBalance(int amount) {
        this.balance += amount;
        notifyObservers(); // Notifica os observadores ao atualizar o saldo
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        this.position = (position + steps) % Board.BOARD_SIZE;
        notifyObservers(); // Notifica os observadores ao mover o jogador
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
        notifyObservers(); // Notifica os observadores ao adicionar uma propriedade
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
        notifyObservers(); // Notifica os observadores ao atualizar o estado de prisão
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyObservers(); // Notifica os observadores ao alterar a cor
    }
}
