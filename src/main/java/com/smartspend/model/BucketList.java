package com.smartspend.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketList {

    private final List<ShoppingListEntry> pantryItems;

    // Constructor
    public BucketList() {
        this.pantryItems = new ArrayList<>();
    }

    // Method to add a completed item to the pantry
    public void addToPantry(ShoppingListEntry entry) {
        if (entry != null && entry.isCompleted()) {
            pantryItems.add(entry);
        }
    }

    // Check if an item is already in the pantry
    public boolean containsItem(int itemId) {
        for (ShoppingListEntry entry : pantryItems) {
            if (entry.getItemId() == itemId) {
                return true;
            }
        }
        return false;
    }

    // Remove an item
    public boolean removeItem(int itemId) {
        return pantryItems.removeIf(entry -> entry.getItemId() == itemId);
    }

    // Get all pantry items
    public List<ShoppingListEntry> getPantryItems() {
        return Collections.unmodifiableList(pantryItems);
    }
}