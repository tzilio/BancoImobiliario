package model;

public class PropertyData {
    private String name;
    private int price;
    private int rent;
    private int housePrice;
    private String category;

    public PropertyData(String name, int price, int rent, int housePrice, String category) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.housePrice = housePrice;
        this.category = category;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public String getCategory() {
        return category;
    }
}
