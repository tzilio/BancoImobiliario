package model;

public class GoToJailSpace extends BoardPosition {

    public GoToJailSpace(int index) {
        super(index, "Go to Jail", PositionType.GO_TO_JAIL);
    }

    @Override
    public void onLand(Player player) {
        Prison prison = Prison.getInstance(Board.getInstance().getJailPosition());
        prison.sendToJail(player);
    }
}
