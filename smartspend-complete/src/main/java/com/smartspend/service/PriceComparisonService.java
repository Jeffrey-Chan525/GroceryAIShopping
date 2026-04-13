package com.smartspend.service;

import java.util.Map;

public class PriceComparisonService {

    public String findCheapestStore(Map<String, Double> totalsByStore) {
        String cheapestStore = null;
        double cheapestPrice = Double.MAX_VALUE;

        for (Map.Entry<String, Double> entry : totalsByStore.entrySet()) {
            if (entry.getValue() < cheapestPrice) {
                cheapestPrice = entry.getValue();
                cheapestStore = entry.getKey();
            }
        }
        return cheapestStore;
    }

    public Map<String, Double> getStoreTotals(
            List<ShoppingListEntry> entries,
            List<Price> prices) {

        Map<String, Double> totals = new HashMap<>();

        for (ShoppingListEntry entry : entries) {
            for (Price price : prices) {
                if (price.getItemId() == entry.getItemID()) {
                    String store = price.getStore();
                    double cost = price.getPrice() * entry.getQuantity();
                    totals.put(store, totals.getOrDefault(store, 0.0) + cost);
                }
            }
        }
        return totals;
    }

    public Price getCheapestPriceForItem(int itemId, List<Price> prices) {
        Price cheapest = null;

        for (Price price : prices) {
            if (price.getItemId() == itemId) {
                if (cheapest == null || price.getPrice() < cheapest.getPrice()) {
                    cheapest = price;
                }
            }
        }
        return cheapest;
    }
}
