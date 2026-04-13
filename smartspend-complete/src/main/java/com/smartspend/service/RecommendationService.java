package com.smartspend.service;

public class RecommendationService {
    public String buildFrugalBriefing(String cheapestStore, double basketTotal, double budget) {
        double remaining = budget - basketTotal;
        if (remaining >= 0) {
            return "Your basket is cheapest at " + cheapestStore + ". You are $" + String.format("%.2f", remaining) + " under budget.";
        }
        return "Your basket is cheapest at " + cheapestStore + ", but you are $" + String.format("%.2f", Math.abs(remaining)) + " over budget.";
    }

    // View Past prices method
    public boolean predictSaleSoon(int itemId, String storeName, List<PriceHistoryEntry> history) {
        for (PriceHistoryEntry entry : history) {
            if (entry.getItemId() == itemId
                && entry.getStoreName().equals(storeName)
                && entry.isOnSale()) {
                return true;
            }
        }
        return false;
    }

    // Let user know two price options for the same item
    public String getSuggestion(Price option1, Price option2) {
        String result = PriceUtils.cheaperByUnit(
                option1.getPrice(), option1.getPackageQuantity(),
                option2.getPrice(), option2.getPackageQuantity()
        );

        if (result.equals("Option 1")) {
            return option1.getStoreName() + " offers better value per unit.";
        } else if (result.equals("Option 2")) {
            return option2.getStoreName() + " offers better value per unit.";
        } else {
            return "Both stores offer equal value per unit.";
        }
    }
}

