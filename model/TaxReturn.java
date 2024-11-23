package model;

public class TaxReturn extends BoardPosition {
    private int returnAmount; 

    public TaxReturn(int position, int returnAmount) {
        super(position, "Devolução de imposto");
        this.returnAmount = returnAmount;
    }

    @Override
    public void onLand(Player player) {
        player.updateBalance(returnAmount);
        System.out.println(player.getName() + " parou em " + getName() + " e recebeu " + returnAmount + " como retorno de impostos.");
    }

    public int getReturnAmount() {
        return returnAmount;
    }
}
