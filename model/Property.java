package model;

public class Property extends BoardPosition {
    private String name;
    private int price;
    private int rent;
    private Player owner;

    public Property(String name, int price, int rent, int position) {
        super(position);
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null; 
    }

    public String getName() {
        return name;
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
            player.updateBalance(-rent);  // Player pays rent to the owner
            owner.updateBalance(rent);    // Owner receives the rent
            System.out.println(player.getName() + " pagou " + rent + " para " + owner.getName());
        } else if (!isOwned()) {
            System.out.println(player.getName() + " pode comprar " + name + " por " + price);
        } else {
            System.out.println(player.getName() + " já é o proprietário de " + name);
        }
    }
}
