package model;

import java.util.ArrayList;

public class Property extends BoardPosition {
    private int price;
    private int rent;
    private Player owner;
    private String category;
    private int houses; 
    private int housePrice; 

    public Property(String name, int price, int rent, int position, String category, int housePrice) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.owner = null;
        this.category = category;
        this.houses = 0; // Nenhuma casa no início
        this.housePrice = housePrice;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }    

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    public int getRent() {
        return rent;
    }

    public int getHouses() {
        return houses;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public void buildHouse(Player player) {
        if (player.getBalance() >= housePrice) {
            player.updateBalance(-housePrice); 
            houses++;
            rent *= 2; 
            System.out.println(player.getName() + " construiu uma casa em " + getName());
        } else {
            System.out.println(player.getName() + " não tem saldo suficiente para construir uma casa em " + getName());
        }
    }

    public boolean canBuildHouse(Player player, ArrayList<Property> propertiesInCategory) {
        return owner == player &&
               propertiesInCategory.stream().allMatch(p -> p.getOwner() == player);
    }

    @Override
    public void onLand(Player player) {
        if (isOwned() && owner != player) {
            int effectiveRent = rent + (houses * rent / 2); // Aluguel aumenta com casas
            player.updateBalance(-effectiveRent);
            owner.updateBalance(effectiveRent);
            System.out.println(player.getName() + " pagou " + effectiveRent + " para " + owner.getName());
        } else if (!isOwned()) {
            System.out.println(player.getName() + " pode comprar " + getName() + " por " + price);
        } else {
            System.out.println(player.getName() + " já é o proprietário de " + getName());
        }
    }

    public boolean isOwned() {
        return owner != null;
    }   

    public Player getOwner() {
        return owner;
    }   
}
