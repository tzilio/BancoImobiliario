package model;

import javax.swing.Timer;

public class Card {
    private String description;   // Descrição da carta
    private int balanceChange;    // Mudança no saldo
    private int positionChange;   // Mudança relativa na posição
    private Integer goToPosition; // Posição absoluta (null se não aplicável)

    // Construtor
    public Card(String description, int balanceChange, Integer positionValue, boolean isAbsolutePosition) {
        this.description = description;
        this.balanceChange = balanceChange;

        if (isAbsolutePosition) {
            this.goToPosition = positionValue;
            this.positionChange = 0;
        } else {
            this.goToPosition = null;
            this.positionChange = positionValue;
        }
    }

    public Card(String description, int balanceChange) {
        this(description, balanceChange, 0, false);
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public int getBalanceChange() {
        return balanceChange;
    }

    public int getPositionChange() {
        return positionChange;
    }

    public Integer getGoToPosition() {
        return goToPosition;
    }

    public void applyEffect(Player player) {
        player.updateBalance(balanceChange);
    
        // timer pra atrasar mudança de posição
        Timer timer = new Timer(2000, e -> {
            if (goToPosition != null) {
                player.setPosition(goToPosition);
            } else {
                player.move(positionChange);
            }
            ((Timer) e.getSource()).stop(); 
        });
    
        timer.setRepeats(false);
        timer.start();
    }
}
