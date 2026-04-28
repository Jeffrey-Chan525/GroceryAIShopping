package com.smartspend.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ShoppingListEntry shoppingListEntry = (ShoppingListEntry) object;
        boolean isListItemIDEqual = this.listItemId == shoppingListEntry.listItemId;
        boolean isUserIDEqual = this.userId == shoppingListEntry.userId;
        boolean isQuantityEqual = this.quantity == shoppingListEntry.quantity;
        boolean isUnitEqual = this.unit.equals(shoppingListEntry.unit);
        boolean isCompletedValueEqual = this.completed == shoppingListEntry.completed;
        boolean isAddedDateEqual = this.addedDate.equals(shoppingListEntry.addedDate);
        return isListItemIDEqual && isUserIDEqual && isQuantityEqual && isUnitEqual && isCompletedValueEqual && isAddedDateEqual;
    }

    @Override
    public int hashCode(){
        return Objects.hash(listItemId);
    }
}
