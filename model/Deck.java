package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        cards.add(new Card("Avance para o ponto de partida e ganhe 200!", 200));
        cards.add(new Card("Pague uma taxa de 100", -100));
        cards.add(new Card("Avance três espaços", 0, 3));
        cards.add(new Card("Volte dois espaços", 0, -2));
        // adicionar cartas
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("O baralho está vazio!");
            return null;
        }
        return cards.remove(0);
    }
}
