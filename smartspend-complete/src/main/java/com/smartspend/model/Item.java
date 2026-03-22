package com.smartspend.model;

public class Item {
    private int id;
    private String name;
    private String category;
    private String brand;
    private String defaultUnit;

    public Item(int id, String name, String category, String brand, String defaultUnit) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.defaultUnit = defaultUnit;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getDefaultUnit() { return defaultUnit; }
}
