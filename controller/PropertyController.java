package controller;

import java.util.ArrayList;

import model.Player;
import model.Property;
import model.ShareSpace;
import view.GameView;

public class PropertyController {
    private GameView view;

    public PropertyController(GameView view) {
        this.view = view;
    }

    public void transferProperties(Player player) {
        for (Property property : new ArrayList<>(player.getProperties())) {
            player.removeProperty(property);
            property.setOwner(null);
            view.displayMessage(player.getName() + " transferiu " + property.getName() + " para o banco.");
        }

        for (ShareSpace share : new ArrayList<>(player.getShares())) {
            player.getShares().remove(share);
            share.setOwner(null);
            view.displayMessage(player.getName() + " transferiu " + share.getName() + " para o banco.");
        }
    }

    public void buyProperty(Player player, Property property) {
        if (player.getBalance() >= property.getPrice()) {
            player.updateBalance(-property.getPrice());
            property.setOwner(player);
            player.addProperty(property);
            view.displayMessage(player.getName() + " comprou " + property.getName() + " por " + property.getPrice());
            view.enableBuyPropertyButton(false);
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para comprar " + property.getName());
        }
    }

    public void buyShare(Player player, ShareSpace shareSpace) {
        int price = shareSpace.getPrice();
        if (player.getBalance() >= price) {
            player.updateBalance(-price);
            shareSpace.setOwner(player);
            player.addShare(shareSpace);
            view.displayMessage(player.getName() + " comprou " + shareSpace.getName() + " por " + price);
            view.enableBuyPropertyButton(false);
            view.updatePlayerInfo(null); // Atualiza as informações na interface gráfica
        } else {
            view.displayMessage(player.getName() + " não tem saldo suficiente para comprar " + shareSpace.getName());
        }
    }
}
