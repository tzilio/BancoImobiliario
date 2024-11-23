package model;

public class Holiday extends BoardPosition {

    public Holiday(int position) {
        super(position, "Feriado");
    }

    @Override
    public void onLand(Player player) {
        System.out.println(player.getName() + " parou em " + getName() + ". Aproveite o descanso!");
    }
}
