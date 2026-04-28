package com.smartspend.service;

import com.smartspend.util.BudgetUtils;

import java.util.List;

public class BudgetService {
    public double getBasketTotal(List<Double> prices) {
        return BudgetUtils.calculateBasketTotal(prices);
    }

    public double getRemainingBudget(double budget, double basketTotal) {
        return BudgetUtils.remainingBudget(budget, basketTotal);
    }

    public boolean overBudget(double budget, double basketTotal) {
        return BudgetUtils.isOverBudget(budget, basketTotal);
    }
}
