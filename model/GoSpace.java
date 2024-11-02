package model;

public class GoSpace extends BoardPosition { //posicao de inicio do jogo
    private static final int START_REWARD = 200;  

    public GoSpace(int position) {
        super(position);
    }

    @Override
    public void onLand(Player player) {
        player.updateBalance(START_REWARD);
        System.out.println(player.getName() + " recebeu " + START_REWARD + " por passar pelo ponto de partida!");
    }
}
