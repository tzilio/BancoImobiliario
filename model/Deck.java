package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private int currentIndex;
    private static Deck instance;

    private Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
        currentIndex = 0;
    }

    public static Deck getInstance() {
        if (instance == null) {
            instance = new Deck();
        }
        return instance;
    }

    private void initializeDeck() {
        cards.add(new Card("Avance para o ponto de partida e ganhe 200!", 200));
        cards.add(new Card("Pague uma taxa de 100", -100));
        cards.add(new Card("Avance três espaços", 0, 3));
        cards.add(new Card("Volte dois espaços", 0, -2));
    }

    public void shuffle() {
        Collections.shuffle(cards);
        currentIndex = 0;
    }

    public Card next() {
        Card card = cards.get(currentIndex);
        currentIndex = (currentIndex + 1) % cards.size(); // Garantia do comportamento circular
        return card;
    }

    public boolean hasNext() {
        return true; //lista circular
    }

    public void drawCard(Player player) {
        Card card = next();
        System.out.println(card.getDescription()); 
        card.applyEffect(player);
    }
}
