package com.smartspend.util;

import java.util.List;

public class BudgetUtils {
    public static double calculateBasketTotal(List<Double> prices) {
        double total = 0.0;
        for (double price : prices) {
            total += price;
        }
        return total;
    }

    public static double remainingBudget(double budget, double basketTotal) {
        return budget - basketTotal;
    }

    public static boolean isOverBudget(double budget, double basketTotal) {
        return basketTotal > budget;
    }
}
