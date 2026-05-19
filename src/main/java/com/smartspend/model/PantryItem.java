package com.smartspend.model;

import java.util.Objects;

/**
 * Represent a single item in the user's pantry
 */
public class PantryItem {

    private int pantryId;
    private int userId;
    private String name;
    private String quantity;
    private String category;
    private String expiryDate;
    private boolean isLowStock;

    /**
     * Constructor for PantryItem with all fields.
     *
     * @param pantryId
     * @param userId
     * @param name
     * @param quantity
     * @param category
     * @param expiryDate
     * @param isLowStock
     */
    public PantryItem(int pantryId, int userId, String name, String quantity, String category, String expiryDate, boolean isLowStock) {
        this.pantryId = pantryId;
        this.userId = userId;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.expiryDate = expiryDate;
        this.isLowStock = isLowStock;
    }

    // Getters

    /**
     * @return the unique pantry entry ID
     */
    public int getPantryId() {
        return pantryId;
    }

    /**
     * @return the ID of the user who owns this item
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the quantity at home
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @return the food category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the best before date string
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @return true if the item is running low
     */
    public boolean isLowStock() {
        return isLowStock;
    }

    // Setters

    /**
     * @param quantity the updated quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * @param isLowStock true if the item is running low
     */
    public void setLowStock(boolean isLowStock) {
        this.isLowStock = isLowStock;
    }

    /**
     * @param expiryDate the updated best before date
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    // Overides

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        PantryItem other = (PantryItem) object;
        return pantryId == other.pantryId
                && userId == other.userId
                && isLowStock == other.isLowStock
                && Objects.equals(name, other.name)
                && Objects.equals(quantity, other.quantity)
                && Objects.equals(category, other.category)
                && Objects.equals(expiryDate, other.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pantryId);
    }

    @Override
    public String toString() {
        return "PantryItem{" +
                "pantryId=" + pantryId +
                ", name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", category='" + category + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", isLowStock=" + isLowStock +
                '}';
    }
}
