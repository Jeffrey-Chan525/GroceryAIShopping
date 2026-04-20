package com.smartspend.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Item item = (Item) object;
        return (this.id == item.id) && this.name.equals(item.name) && this.category.equals(item.category) && (this.brand.equals(item.brand)) && this.defaultUnit.equals(item.defaultUnit);
    }

    @Override
    public int hashCode(){
        // generates a hashcode based on the id
        return Objects.hash(id);
    }
}
