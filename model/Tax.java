package model;

public class Tax extends BoardPosition {
    private int taxAmount; 

    public Tax(int position, int taxAmount) {
        super(position, "Imposto de renda");
        this.taxAmount = taxAmount;
    }

    @Override
    public void onLand(Player player) {
        player.updateBalance(-taxAmount);
        System.out.println(player.getName() + " parou em " + getName() + " e pagou " + taxAmount + " em impostos.");
    }

    public int getTaxAmount() {
        return taxAmount;
    }
}
