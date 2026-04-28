package com.smartspend.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Just a basic row so far */
public class PriceHistorySnapshot {
    private final StringProperty week = new SimpleStringProperty();
    private final StringProperty aldi = new SimpleStringProperty();
    private final StringProperty coles = new SimpleStringProperty();
    private final StringProperty woolworths = new SimpleStringProperty();
    private final StringProperty lowest = new SimpleStringProperty();
    private final StringProperty change = new SimpleStringProperty();

    public PriceHistorySnapshot(String week, String aldi, String coles, String woolworths, String lowest, String change) {
        this.week.set(week);
        this.aldi.set(aldi);
        this.coles.set(coles);
        this.woolworths.set(woolworths);
        this.lowest.set(lowest);
        this.change.set(change);
    }

    public StringProperty weekProperty() { return week; }
    public StringProperty aldiProperty() { return aldi; }
    public StringProperty colesProperty() { return coles; }
    public StringProperty woolworthsProperty() { return woolworths; }
    public StringProperty lowestProperty() { return lowest; }
    public StringProperty changeProperty() { return change; }
}
