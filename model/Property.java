package model;

import java.util.ArrayList;

public class Property extends BoardPosition {
    private static final long serialVersionUID = 1L;

    private int price;
    private int rent;
    private Player owner;
    private String category;
    private int houses;
    private boolean hasHotel; // Indica se a propriedade possui um hotel
    private int housePrice;
    private int hotelPrice;

    public Property(String name, int price, int rent, int position, String category, int housePrice) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.owner = null;
        this.category = category;
        this.houses = 0; // Nenhuma casa no início
        this.hasHotel = false; // Nenhum hotel no início
        this.housePrice = housePrice;
        this.hotelPrice = housePrice * 2; // Exemplo: hotel custa 5x o preço de uma casa
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
        if (hasHotel) {
            return rent * 5; // Aluguel com hotel é 5x maior
        }
        return rent + (houses * rent / 2); // Aluguel aumenta com cada casa
    }

    public int getHouses() {
        return houses;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public boolean hasHotel() {
        return hasHotel;
    }

    public boolean canBuildHouse(Player player, ArrayList<Property> propertiesInCategory) {
        if (hasHotel) {
            System.out.println("Já possui hotel: " + getName());
            return false; // Não pode construir casas se já houver um hotel
        }
    
        // Total de propriedades na categoria
        int totalPropertiesInCategory = propertiesInCategory.size();
    
        // Propriedades da categoria que o jogador possui
        int ownedPropertiesInCategory = 0;
        for (Property property : propertiesInCategory) {
            if (player.equals(property.getOwner())) {
                ownedPropertiesInCategory++;
            } else {
                System.out.println("Propriedade " + property.getName() + " pertence a " +
                                   (property.getOwner() != null ? property.getOwner().getName() : "ninguém"));
            }
        }
    
        // Debug
        System.out.println("Propriedades da categoria " + getCategory() + ": " + totalPropertiesInCategory);
        System.out.println("Propriedades da categoria que o jogador possui: " + ownedPropertiesInCategory);
    
        if (ownedPropertiesInCategory < totalPropertiesInCategory) {
            System.out.println("Jogador não possui todas as propriedades da categoria: " + getCategory());
            return false;
        }
    
        // Verifica o balanceamento da construção
        boolean balanced = propertiesInCategory.stream()
                .allMatch(p -> p.getHouses() <= this.getHouses());
    
        if (!balanced) {
            System.out.println("Construção não está balanceada para a propriedade: " + getName());
            return false;
        }
    
        return true;
    }     

    public void buildHouse(Player player, ArrayList<Property> propertiesInCategory) {
        if (canBuildHouse(player, propertiesInCategory) && player.getBalance() >= housePrice) {
            player.updateBalance(-housePrice);
            houses++;
            System.out.println(player.getName() + " construiu uma casa em " + getName());
        } else {
            System.out.println(player.getName() + " não pode construir uma casa em " + getName());
        }
    }

    public boolean canBuildHotel(Player player, ArrayList<Property> propertiesInCategory) {
        return owner == player &&
                houses == 4 && // A propriedade deve ter 4 casas
                !hasHotel && // Não pode já ter um hotel
                player.getBalance() >= hotelPrice; // Jogador tem saldo suficiente
    }

    public void buildHotel(Player player) {
        if (canBuildHotel(player, new ArrayList<>())) {
            player.updateBalance(-hotelPrice);
            houses = 0; // Remove as 4 casas
            hasHotel = true;
            System.out.println(player.getName() + " construiu um hotel em " + getName());
        } else {
            System.out.println(player.getName() + " não pode construir um hotel em " + getName());
        }
    }

    @Override
    public void onLand(Player player) {
        if (isOwned() && owner != player) {
            int rentToPay = getRent();
            player.updateBalance(-rentToPay);
            owner.updateBalance(rentToPay);
            System.out.println(player.getName() + " pagou " + rentToPay + " para " + owner.getName());
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