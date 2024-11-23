package model;

public class GoSpace extends BoardPosition {
    private static final int START_REWARD = 200;  
    private static GoSpace instance;  

    private GoSpace(int position) {
        super(position, "Início");
    }

    public static GoSpace getInstance(int position) {
        if (instance == null) {
            instance = new GoSpace(position);
        }
        return instance;
    }

    @Override
    public void onLand(Player player) {
        player.updateBalance(START_REWARD);
        System.out.println(player.getName() + " recebeu " + START_REWARD + " por passar pelo ponto de partida!");
    }
}
