package model;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private int totalMoney;  
    private Map<Property, Boolean> mortgagedProperties; 

    public Bank() {
        this.totalMoney = 100000000; 
        this.mortgagedProperties = new HashMap<>();
    }

    public void payPlayer(Player player, int amount) {
        player.updateBalance(amount);
        totalMoney -= amount;
        System.out.println("O banco pagou " + amount + " para " + player.getName());
    }

    public void chargePlayer(Player player, int amount) {
        player.updateBalance(-amount);
        totalMoney += amount;
        System.out.println(player.getName() + " pagou " + amount + " ao banco.");
    }

    public void mortgageProperty(Player player, Property property) {
        if (property.getOwner() == player && !isMortgaged(property)) {
            int mortgageValue = property.getPrice() / 2;
            player.updateBalance(mortgageValue);
            mortgagedProperties.put(property, true);  
            System.out.println(player.getName() + " hipotecou " + property.getName() + " por " + mortgageValue);
        } else {
            System.out.println("A propriedade " + property.getName() + " não pode ser hipotecada.");
        }
    }

    public void liftMortgage(Player player, Property property) {
        if (property.getOwner() == player && isMortgaged(property)) {
            int mortgageLiftCost = (int) (property.getPrice() * 0.55);  
            if (player.getBalance() >= mortgageLiftCost) {
                player.updateBalance(-mortgageLiftCost);
                mortgagedProperties.put(property, false);  
                System.out.println(player.getName() + " pagou " + mortgageLiftCost + " para quitar a hipoteca de " + property.getName());
            } else {
                System.out.println(player.getName() + " não tem saldo suficiente para quitar a hipoteca de " + property.getName());
            }
        } else {
            System.out.println("A propriedade " + property.getName() + " não está hipotecada ou não pertence a " + player.getName());
        }
    }

    public boolean isMortgaged(Property property) {
        return mortgagedProperties.getOrDefault(property, false);
    }

    public int getTotalMoney() {
        return totalMoney;
    }
}
