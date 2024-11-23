package model;

public class ShareSpace extends BoardPosition {
    private double multiplier; 

    public ShareSpace(int position, String name, double multiplier) {
        super(position, name);
        this.multiplier = multiplier;
    }

    @Override
    public void onLand(Player player) {
        Dice dice = Dice.getInstance();
        int diceRoll = dice.getDiceSum(); 
        int marketEffect = (int) (diceRoll * multiplier); 

        player.updateBalance(-marketEffect);
        System.out.println(player.getName() + " parou em " + getName() + " e teve um impacto de " + marketEffect +
                " baseado no resultado dos dados: " + diceRoll);
    }

    public double getMultiplier() {
        return multiplier;
    }
}
