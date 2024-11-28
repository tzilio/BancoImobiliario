package controller;

import model.Bank;
import model.Player;
import model.Property;

public class BankController {
    private static final int PASS_START_REWARD = 200;

    public void givePassStartReward(Player player) {
        player.updateBalance(PASS_START_REWARD);
    }

    public void mortgageProperty(Player player, Property property) {
        Bank bank = Bank.getInstance();
        if (property.getOwner() == player && !bank.isMortgaged(property)) {
            int mortgageValue = property.getPrice() / 2;
            bank.mortgageProperty(player, property);
            System.out.println(player.getName() + " hipotecou a propriedade " + property.getName());
        } else if (bank.isMortgaged(property)) {
            System.out.println("A propriedade " + property.getName() + " já está hipotecada.");
        } else {
            System.out.println("A propriedade " + property.getName() + " não pertence a " + player.getName());
        }
    }

    public void liftMortgage(Player player, Property property) {
        Bank bank = Bank.getInstance();
        if (property.getOwner() == player && bank.isMortgaged(property)) {
            bank.liftMortgage(player, property);
        } else {
            System.out.println("Não é possível quitar a hipoteca da propriedade " + property.getName());
        }
    }
    
    public void sellPropertyToBank(Player player, Property property) {
        if (property.getOwner() == player) {
            int sellPrice = property.getPrice() / 2;
            player.updateBalance(sellPrice);
            player.removeProperty(property); // Remove a propriedade do jogador
            property.setOwner(null); // Desassocia a propriedade de qualquer dono
    
            System.out.println(player.getName() + " vendeu a propriedade " + property.getName() + " ao banco por " + sellPrice);
        } else {
            System.out.println("A propriedade " + property.getName() + " não pertence a " + player.getName());
        }
    }
    
    
    public void repurchaseProperty(Player player, Property property) {
        Bank bank = Bank.getInstance();
    
        if (property.getOwner() == player && bank.isMortgaged(property)) {
            int repurchaseCost = (int) (property.getPrice() * 1.2); // 120% do preço original
            if (player.getBalance() >= repurchaseCost) {
                player.updateBalance(-repurchaseCost); // Deduz o custo do jogador
                bank.liftMortgage(player, property); // Remove a hipoteca
                System.out.println(player.getName() + " recomprou a propriedade " + property.getName() + " por " + repurchaseCost);
            } else {
                System.out.println(player.getName() + " não tem saldo suficiente para recomprar " + property.getName());
            }
        } else {
            System.out.println("A propriedade " + property.getName() + " não está hipotecada ou não pertence a " + player.getName());
        }
    }

    public void chargeFee(Player player, int amount) {
        player.updateBalance(-amount);
        System.out.println(player.getName() + " pagou uma taxa de " + amount);
    }
}
