package model;

public class NewsSpace extends BoardPosition {

    public NewsSpace(int position) {
        super(position, "Sorte ou revés");
    }

    @Override
    public void onLand(Player player) {
        Deck deck = Deck.getInstance();
        deck.drawCard(player);
    }
}
