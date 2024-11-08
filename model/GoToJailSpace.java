package model;

public class GoToJailSpace extends BoardPosition {

    public GoToJailSpace(int position) {
        super(position);
    }

    @Override
    public void onLand(Player player) {
        Prison prison = Prison.getInstance(Board.getInstance().getJailPosition());
        prison.sendToJail(player);
    }
}
