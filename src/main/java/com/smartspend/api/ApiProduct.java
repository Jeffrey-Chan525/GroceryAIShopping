package com.smartspend.api;

public class ApiProduct {
    private final String code;
    private final String productName;
    private final String brands;
    private final String quantity;
    private final String categories;

    public ApiProduct(String code, String productName, String brands, String quantity, String categories) {
        this.code = code;
        this.productName = productName;
        this.brands = brands;
        this.quantity = quantity;
        this.categories = categories;
    }

    public String getCode() {
        return code;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrands() {
        return brands;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        String brandText = (brands == null || brands.isBlank()) ? "Unknown brand" : brands;
        return productName + " — " + brandText;
    }
}
