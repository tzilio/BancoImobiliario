package model;

public class Property extends BoardPosition {
    private int price;
    private int rent;
    private Player owner;

    public Property(String name, int price, int rent, int position) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.owner = null; 
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isOwned() {
        return owner != null;
    }

    @Override
    public void onLand(Player player) {
        if (isOwned() && owner != player) {
            player.updateBalance(-rent); 
            owner.updateBalance(rent);   
            System.out.println(player.getName() + " pagou " + rent + " para " + owner.getName());
        } else if (!isOwned()) {
            System.out.println(player.getName() + " pode comprar " + getName() + " por " + price);
        } else {
            System.out.println(player.getName() + " já é o proprietário de " + getName());
        }
    }
}
