package com.smartspend.service;

public class RecommendationService {
    public String buildFrugalBriefing(String cheapestStore, double basketTotal, double budget) {
        double remaining = budget - basketTotal;
        if (remaining >= 0) {
            return "Your basket is cheapest at " + cheapestStore + ". You are $" + String.format("%.2f", remaining) + " under budget.";
        }
        return "Your basket is cheapest at " + cheapestStore + ", but you are $" + String.format("%.2f", Math.abs(remaining)) + " over budget.";
    }
}
