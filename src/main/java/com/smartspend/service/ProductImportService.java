package com.smartspend.service;

import com.smartspend.api.ApiProduct;
import com.smartspend.model.Item;

public class ProductImportService {

    public Item mapApiProductToItem(ApiProduct apiProduct) {
        String name = blankToDefault(apiProduct.getProductName(), "Unknown Product");
        String category = extractFirstCategory(apiProduct.getCategories());
        String brand = blankToDefault(apiProduct.getBrands(), "Unknown Brand");
        String defaultUnit = blankToDefault(apiProduct.getQuantity(), "unit");

        return new Item(0, name, category, brand, defaultUnit);
    }

    private String extractFirstCategory(String categories) {
        if (categories == null || categories.isBlank()) {
            return "Uncategorized";
        }

        String[] parts = categories.split(",");
        if (parts.length == 0 || parts[0].isBlank()) {
            return "Uncategorized";
        }
        return parts[0].trim();
    }

    private String blankToDefault(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
