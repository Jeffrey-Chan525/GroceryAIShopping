package com.smartspend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ShoppingList {

    private final List<ShoppingListEntry> entries;
    private double weeklyBudget;

    public ShoppingList() {
        this.entries = new ArrayList<>();
        this.weeklyBudget = 0.0;
    }

    public ShoppingList(double weeklyBudget) {
        this.entries = new ArrayList<>();
        this.weeklyBudget = weeklyBudget;
    }

    public ShoppingList(List<ShoppingListEntry> entries, double weeklyBudget) {
        this.entries = (entries != null) ? new ArrayList<>(entries) : new ArrayList<>();
        this.weeklyBudget = weeklyBudget;
    }

    public List<ShoppingListEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void setEntries(List<ShoppingListEntry> entries) {
        this.entries.clear();
        if (entries != null) {
            this.entries.addAll(entries);
        }
    }

    public double getWeeklyBudget() {
        return weeklyBudget;
    }

    public void setWeeklyBudget(double weeklyBudget) {
        this.weeklyBudget = weeklyBudget;
    }

    public void addEntry(ShoppingListEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    public boolean editEntry(ShoppingListEntry updatedEntry) {
        if (updatedEntry == null) {
            return false;
        }

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getListItemId() == updatedEntry.getListItemId()) {
                entries.set(i, updatedEntry);
                return true;
            }
        }

        return false;
    }

    public boolean deleteEntry(int listItemId) {
        return entries.removeIf(entry -> entry.getListItemId() == listItemId);
    }

    public boolean markCompleted(int listItemId) {
        for (int i = 0; i < entries.size(); i++) {
            ShoppingListEntry entry = entries.get(i);
            if (entry.getListItemId() == listItemId) {
                if (!entry.isCompleted()) {
                    entries.set(i, new ShoppingListEntry(
                            entry.getListItemId(),
                            entry.getUserId(),
                            entry.getItemId(),
                            entry.getQuantity(),
                            entry.getUnit(),
                            true,
                            entry.getAddedDate()
                    ));
                }
                return true;
            }
        }
        return false;
    }

    public double calculateTotal() {
        return entries.size();
    }

    public double getRemainingBudget(double currentTotal) {
        return weeklyBudget - currentTotal;
    }

    public boolean isOverBudget(double currentTotal) {
        return currentTotal > weeklyBudget;
    }

    public ShoppingListEntry findEntryById(int listItemId) {
        for (ShoppingListEntry entry : entries) {
            if (entry.getListItemId() == listItemId) {
                return entry;
            }
        }
        return null;
    }

    public boolean containsItem(int itemId) {
        for (ShoppingListEntry entry : entries) {
            if (entry.getItemId() == itemId) {
                return true;
            }
        }
        return false;
    }
}