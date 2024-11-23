package model;

public class ShareSpace extends BoardPosition {
    private double multiplier; 
    private int price;        
    private Player owner;     

    public ShareSpace(int position, String name, double multiplier, int price) {
        super(position, name);
        this.multiplier = multiplier;
        this.price = price;
        this.owner = null;
    }

    @Override
    public void onLand(Player player) {
        Dice dice = Dice.getInstance();
        int diceRoll = dice.getDiceSum(); 

        if (isOwned() && !owner.equals(player)) {
            int marketEffect = (int) (diceRoll * multiplier);
            player.updateBalance(-marketEffect);
            owner.updateBalance(marketEffect);

            System.out.println(player.getName() + " parou em " + getName() + " e pagou " + marketEffect +
                    " a " + owner.getName() + " com base no resultado dos dados: " + diceRoll);
        } else if (!isOwned()) {
            System.out.println(player.getName() + " pode comprar " + getName() + " por " + price + ".");
        } else {
            System.out.println(player.getName() + " parou em sua própria ação: " + getName() + ".");
        }
    }

    public boolean isOwned() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getPrice() {
        return price;
    }
}
