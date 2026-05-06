package com.smartspend.model;

public enum Category {

    PRODUCE(1),     // Fruits, Vegetables, Herbs
    MEAT(2),        // Chicken, Beef, Fish
    DAIRY(3),       // Milk, Butter, Yogurt, Cheese
    FROZEN(4),      // Ice cream, Frozen veggies, Pizza
    BAKERY(5),      // Bread, Muffins, Tortillas
    PANTRY(6),      // Grains, Canned goods, Sauces
    SNACKS(7),      // Chips, Chocolate, Nuts
    HOUSEHOLD(8);   // Cleaning supplies, Detergent

    private final int id;
    Category(int id) { this.id = id; }
    public int getId() { return id; }
}