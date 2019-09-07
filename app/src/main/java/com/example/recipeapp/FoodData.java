package com.example.recipeapp;

public class FoodData {

    private String itemName;
    private String itemDescription;
    private String itemPrice;
    private String itemImage;
    private String key;

    public FoodData() {
    }

    public FoodData(String itemName, String itemDescription, String itemPrice, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
