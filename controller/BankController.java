package controller;

import model.Player;
import model.Property;

public class BankController {
    private static final int PASS_START_REWARD = 200;

    public void givePassStartReward(Player player) {
        player.updateBalance(PASS_START_REWARD);
        System.out.println(player.getName() + " recebeu " + PASS_START_REWARD + " por passar pelo ponto de partida!");
    }

    public void mortgageProperty(Player player, Property property) {
        if (property.getOwner() == player) {
            int mortgageValue = property.getPrice() / 2;
            player.updateBalance(mortgageValue);
            System.out.println(player.getName() + " hipotecou a propriedade " + property.getName() + " e recebeu " + mortgageValue);
        } else {
            System.out.println("A propriedade " + property.getName() + " não pertence a " + player.getName());
        }
    }

    public void chargeFee(Player player, int amount) {
        player.updateBalance(-amount);
        System.out.println(player.getName() + " pagou uma taxa de " + amount);
    }
}
