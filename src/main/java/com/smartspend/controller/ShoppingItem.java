package com.smartspend.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Basic row without functionality yet */
public class ShoppingItem {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty qty = new SimpleStringProperty();
    private final StringProperty bestPrice = new SimpleStringProperty();
    private final StringProperty store = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public ShoppingItem(String name, String qty, String bestPrice, String store, String status) {
        this.name.set(name);
        this.qty.set(qty);
        this.bestPrice.set(bestPrice);
        this.store.set(store);
        this.status.set(status);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty qtyProperty() { return qty; }
    public StringProperty bestPriceProperty() { return bestPrice; }
    public StringProperty storeProperty() { return store; }
    public StringProperty statusProperty() { return status; }
}
