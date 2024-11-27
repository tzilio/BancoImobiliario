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
            property.setOwner(null);
            System.out.println(player.getName() + " vendeu a propriedade " + property.getName() + " ao banco por " + sellPrice);
        } else {
            System.out.println("A propriedade " + property.getName() + " não pertence a " + player.getName());
        }
    }
    

    public void chargeFee(Player player, int amount) {
        player.updateBalance(-amount);
        System.out.println(player.getName() + " pagou uma taxa de " + amount);
    }
}
