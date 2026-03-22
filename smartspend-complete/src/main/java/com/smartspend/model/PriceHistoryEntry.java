package com.smartspend.model;

public class PriceHistoryEntry {
    private int historyId;
    private int itemId;
    private String storeName;
    private double price;
    private String recordedDate;
    private boolean onSale;

    public PriceHistoryEntry(int historyId, int itemId, String storeName, double price, String recordedDate, boolean onSale) {
        this.historyId = historyId;
        this.itemId = itemId;
        this.storeName = storeName;
        this.price = price;
        this.recordedDate = recordedDate;
        this.onSale = onSale;
    }

    public int getHistoryId() { return historyId; }
    public int getItemId() { return itemId; }
    public String getStoreName() { return storeName; }
    public double getPrice() { return price; }
    public String getRecordedDate() { return recordedDate; }
    public boolean isOnSale() { return onSale; }
}
