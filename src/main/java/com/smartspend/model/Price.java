package com.smartspend.model;

public class Price {
    private int priceId;
    private int itemId;
    private String storeName;
    private double price;
    private double packageQuantity;
    private String packageUnit;
    private String lastUpdated;
    private boolean onSale;

    public Price(int priceId, int itemId, String storeName, double price, double packageQuantity, String packageUnit, String lastUpdated, boolean onSale) {
        this.priceId = priceId;
        this.itemId = itemId;
        this.storeName = storeName;
        this.price = price;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.lastUpdated = lastUpdated;
        this.onSale = onSale;
    }

    public int getPriceId() { return priceId; }
    public int getItemId() { return itemId; }
    public String getStoreName() { return storeName; }
    public double getPrice() { return price; }
    public double getPackageQuantity() { return packageQuantity; }
    public String getPackageUnit() { return packageUnit; }
    public String getLastUpdated() { return lastUpdated; }
    public boolean isOnSale() { return onSale; }
}
