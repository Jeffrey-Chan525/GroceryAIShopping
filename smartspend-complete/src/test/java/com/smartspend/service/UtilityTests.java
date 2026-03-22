package com.smartspend.service;

import com.smartspend.util.BudgetUtils;
import com.smartspend.util.PriceUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilityTests {

    @Test
    void shouldCalculateBasketTotalCorrectly() {
        double total = BudgetUtils.calculateBasketTotal(List.of(3.20, 2.90, 5.40));
        assertEquals(11.50, total, 0.001);
    }

    @Test
    void shouldDetectOverBudget() {
        assertTrue(BudgetUtils.isOverBudget(10.0, 12.5));
        assertFalse(BudgetUtils.isOverBudget(15.0, 12.5));
    }

    @Test
    void shouldRecommendCheaperUnitOption() {
        String result = PriceUtils.cheaperByUnit(2.40, 200, 4.50, 500);
        assertEquals("Option 2", result);
    }
}
