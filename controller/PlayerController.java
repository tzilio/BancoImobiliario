package controller;

import model.Player;
import model.Property;
import model.Bank;

public class PlayerController {
    private Player player;

    public PlayerController(Player player, Bank bank) {
        this.player = player;
    }

    public void buyProperty(Property property) {
        if (property.isOwned()) {
            System.out.println("Esta propriedade já tem um proprietário.");
        } else if (player.getBalance() >= property.getPrice()) {
            player.updateBalance(-property.getPrice());
            player.addProperty(property);
            property.setOwner(player);
            System.out.println(player.getName() + " comprou a propriedade " + property.getName());
        } else {
            System.out.println("Saldo insuficiente para comprar " + property.getName());
        }
    }

    public void payRent(Property property) {
        Player owner = property.getOwner();
        if (owner != null && owner != player) {
            int rent = property.getRent();
            player.updateBalance(-rent);
            owner.updateBalance(rent);
            System.out.println(player.getName() + " pagou aluguel de " + rent + " para " + owner.getName());
        }
    }

    //public void manageJail() {
    //} sorteiro de dados e os krl
}
