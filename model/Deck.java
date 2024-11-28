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
        cards.add(new Card("Pague uma taxa de 100", -100));
        cards.add(new Card("Avance três espaços", 0, 3, false));
        cards.add(new Card("Volte dois espaços", 0, -2, false));
        cards.add(new Card("Receba um bônus por serviços prestados! Ganhe 150.", 150));
        cards.add(new Card("Pague uma multa por infração de trânsito. Perda de 50.", -50));
        cards.add(new Card("Você encontrou dinheiro perdido na rua! Ganhe 100.", 100));
        cards.add(new Card("Você comprou um presente caro. Perda de 200.", -200));
        cards.add(new Card("Avance até a propriedade mais próxima e pague aluguel.", 0, 0, true)); // Comportamento especial
        cards.add(new Card("Vá para a cadeia! Não receba dinheiro ao passar pelo ponto de partida.", 0, 0, true)); // Enviar para um estado especial
        cards.add(new Card("Avance cinco espaços.", 0, 5, false));
        cards.add(new Card("Volte três espaços.", 0, -3, false));
        cards.add(new Card("Você ganhou na loteria! Receba 300.", 300));
        cards.add(new Card("Avance para o ponto de partida e ganhe 200!", 200, 0, true));
        cards.add(new Card("Uma despesa médica inesperada surgiu. Pague 150.", -150));
        cards.add(new Card("Você comprou ações e teve lucro! Ganhe 250.", 250));
        cards.add(new Card("Uma manutenção no carro custou caro. Pague 100.", -100));
        cards.add(new Card("Avance até a próxima estação de trem.", 0, 0, true)); // Movimento especial
        cards.add(new Card("Receba um reembolso de impostos. Ganhe 200.", 200));
        cards.add(new Card("Doe 50 para caridade.", -50));
        cards.add(new Card("Avance até o próximo hotel e pague 50 por noite.", 0, 0, true)); // Movimento especial
        cards.add(new Card("Receba dividendos de investimentos. Ganhe 100.", 100));
        cards.add(new Card("Perdeu um objeto valioso. Perda de 75.", -75));
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

    public String drawCard(Player player) {
        Card card = next();
        card.applyEffect(player);
        return card.getDescription();
    }
}
