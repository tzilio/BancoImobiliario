package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int balance;
    private PlayerColor color;
    private int position;
    private boolean inJail;
    private List<Property> properties;
    private List<Observer> observers;

    public Player(String name, int balance, PlayerColor color) {
        this.name = name;
        this.balance = balance;
        this.color = color;
        this.position = 0; // Posição inicial
        this.inJail = false;
        this.properties = new ArrayList<>();
        this.observers = new ArrayList<>();
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

    public int getPosition() {
        return position;
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

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
        notifyObservers(); // Notifica os observadores ao alterar a cor
    }



    public void addProperty(BoardPosition position) {
        if (position instanceof Property) { // Verifica se a posição é uma propriedade
            Property property = (Property) position;
            properties.add(property); // Adiciona à lista de propriedades do jogador
            System.out.println("Propriedade " + property.getName() + " foi adicionada ao jogador.");
            notifyObservers(); // Notifica observadores sobre a mudança no estado do jogador
        } else {
            throw new IllegalArgumentException("A posição fornecida não é uma propriedade.");
        }
    }
    

    /**
     * Move o jogador no tabuleiro.
     *
     * @param steps Número de passos a mover.
     */
    public void move(int steps) {
        Board board = Board.getInstance();
        position = (position + steps) % board.getTotalPositions();
        BoardPosition currentPosition = board.getSpace(position);
        currentPosition.onLand(this);
        notifyObservers();
    }

    /**
     * Rola os dados para o jogador.
     *
     * @return Soma dos dois dados.
     */
    public int rollDice() {
        int die1 = (int) (Math.random() * 6) + 1;
        int die2 = (int) (Math.random() * 6) + 1;
        int total = die1 + die2;
        notifyObservers();
        return total;
    }

    public void updateBalance(int amount) {
        balance += amount;
        notifyObservers();
    }

    // Implementação do padrão Observer
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


}
