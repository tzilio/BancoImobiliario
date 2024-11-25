package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Observable {
    private String name;
    private int balance;
    private int position;
    private List<Property> properties;
    private boolean inJail;
    private String color; // Cor representada como String
    
    // Lista de observadores
    private List<Observer> observers = new ArrayList<>();

    // Mapa de cores pré-definidas (simula o antigo enum)
    private static final Map<String, Color> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("Vermelho", new Color(220, 20, 60));
        COLOR_MAP.put("Azul", new Color(30, 144, 255));
        COLOR_MAP.put("Verde", new Color(34, 139, 34));
        COLOR_MAP.put("Amarelo", new Color(255, 215, 0));
        COLOR_MAP.put("Branco", Color.WHITE);
        COLOR_MAP.put("Preto", Color.BLACK);
        // Adicione mais cores conforme necessário
    }

    // Construtor principal
    public Player(String name, int initialBalance, String color) {
        this.name = name;
        this.balance = initialBalance;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.inJail = false;
        setColor(color); // Valida e inicializa a cor do peão
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

    public void notifyObservers() {
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
        if (!COLOR_MAP.containsKey(color)) {
            throw new IllegalArgumentException("Cor inválida: " + color);
        }
        this.color = color;
        notifyObservers(); // Notifica os observadores ao alterar a cor
    }

    public Color getColorAsAwtColor() {
        if (COLOR_MAP.containsKey(color)) {
            return COLOR_MAP.get(color);
        }
        throw new IllegalStateException("Cor não mapeada para: " + color);
    }

    // Método para validar se a cor é válida
    public static boolean isValidColor(String color) {
        return COLOR_MAP.containsKey(color);
    }

    // Método para obter todas as cores disponíveis
    public static List<String> getAvailableColors() {
        return new ArrayList<>(COLOR_MAP.keySet());
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", position=" + position +
                ", properties=" + properties +
                ", inJail=" + inJail +
                ", color='" + color + '\'' +
                '}';
    }
}