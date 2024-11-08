package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck implements Iterator<Card> {
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

    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("O baralho está vazio!");
            return null;
        }
        return cards.remove(0);
    }

    @Override
    public boolean hasNext() {
        return currentIndex < cards.size();
    }

    @Override
    public Card next() {
        if (!hasNext()) {
            throw new IllegalStateException("Não há mais cartas no baralho.");
        }
        return cards.get(currentIndex++);
    }
}
