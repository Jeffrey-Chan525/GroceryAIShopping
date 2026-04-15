package com.smartspend.model;

public class ShoppingListEntry {
    private int listItemId;
    private int userId;
    private int itemId;
    private double quantity;
    private String unit;
    private boolean completed;
    private String addedDate;

    public ShoppingListEntry(int listItemId, int userId, int itemId, double quantity, String unit, boolean completed, String addedDate) {
        this.listItemId = listItemId;
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unit = unit;
        this.completed = completed;
        this.addedDate = addedDate;
    }

    public int getListItemId() { return listItemId; }
    public int getUserId() { return userId; }
    public int getItemId() { return itemId; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public boolean isCompleted() { return completed; }
    public String getAddedDate() { return addedDate; }
}
