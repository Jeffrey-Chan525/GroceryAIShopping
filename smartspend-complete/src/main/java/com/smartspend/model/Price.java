package com.smartspend.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Price priceObject = (Price) object;
        boolean isPriceIDEqual = this.priceId == priceObject.priceId;
        boolean isItemIDEqual = this.itemId == priceObject.itemId;
        boolean isStoreNameEqual = this.storeName.equals(priceObject.storeName);
        boolean isPriceEqual = this.price == priceObject.price;
        boolean isPackageQuantityEqual = this.packageQuantity == priceObject.packageQuantity;
        boolean isPackageUnitEqual = this.packageUnit.equals(priceObject.packageUnit);
        boolean isLastUpdatedTimeEqual = this.lastUpdated.equals(priceObject.lastUpdated);
        boolean isonSaleValueEqual = this.onSale == priceObject.onSale;
        return isPriceIDEqual && isItemIDEqual && isStoreNameEqual && isPriceEqual && isPackageQuantityEqual && isPackageUnitEqual && isLastUpdatedTimeEqual && isonSaleValueEqual;
    }

    @Override
    public int hashCode(){return Objects.hash(priceId);};
}
