package model;

public class Property extends BoardPosition {
    private String name;
    private int price;
    private int rent;
    private Player owner;

    public Property(int index, String name, int cost) {
        super(index, name, PositionType.PROPERTY);
        this.price = price;
        this.rent = rent;
        this.owner = null; // Inicialmente, sem dono
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
            player.updateBalance(-rent); 
            owner.updateBalance(rent);   
            System.out.println(player.getName() + " pagou " + rent + " para " + owner.getName());
        } else if (!isOwned()) {
            System.out.println(player.getName() + " pode comprar " + name + " por " + price);
        } else {
            System.out.println(player.getName() + " já é o proprietário de " + name);
        }
    }

    private int calculateRent() {
        // Implementar lógica para calcular o aluguel com base em diferentes fatores
        // Por simplicidade, retornaremos um valor fixo
        return price / 10;
    }
}
