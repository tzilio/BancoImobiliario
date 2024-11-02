package controller;

import model.Card;
import model.Deck;
import model.Player;

public class CardController {
    private Deck deck;

    public CardController(Deck deck) {
        this.deck = deck;
    }

    public void drawCard(Player player) {
        Card card = deck.drawCard();
        if (card != null) {
            System.out.println(player.getName() + " puxou a carta: " + card.getDescription());
            card.applyEffect(player);
        } else {
            System.out.println("O baralho está vazio, não há mais cartas.");
        }
    }

    public void shuffleDeck() {
        deck.shuffle();
    }
}
