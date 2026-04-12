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
}
