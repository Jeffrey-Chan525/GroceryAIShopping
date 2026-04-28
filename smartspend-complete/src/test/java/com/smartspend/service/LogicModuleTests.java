package com.smartspend.service;

import com.smartspend.model.Price;
import com.smartspend.model.PriceHistoryEntry;
import com.smartspend.model.ShoppingListEntry;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LogicModuleTests {

    @Test
    void shouldCalculateCorrectStoreTotals() {

        ShoppingListEntry entry = new ShoppingListEntry(1, 1, 1, 2.0, "L", false, "2026-04-13");
        Price colesPrice = new Price(1, 1, "Coles", 3.20, 2, "L", "2026-04-13", false);
        Price aldiPrice  = new Price(2, 1, "Aldi",  2.85, 2, "L", "2026-04-13", false);

        PriceComparisonService service = new PriceComparisonService();

        Map<String, Double> totals = service.getStoreTotals(
                List.of(entry), List.of(colesPrice, aldiPrice)
        );

        assertEquals(6.40, totals.get("Coles"), 0.001);
        assertEquals(5.70, totals.get("Aldi"),  0.001);
    }

    @Test
    void shouldFindCheapestStore() {

        Map<String, Double> totals = new HashMap<>();
        totals.put("Coles", 11.50);
        totals.put("Woolworths", 10.90);
        totals.put("Aldi", 10.25);

        PriceComparisonService service = new PriceComparisonService();


        String cheapest = service.findCheapestStore(totals);


        assertEquals("Aldi", cheapest);
    }

    @Test
    void shouldFindCheapestPriceForItem() {

        Price coles = new Price(1, 1, "Coles", 3.20, 2, "L", "2026-04-13", false);
        Price aldi  = new Price(2, 1, "Aldi",  2.85, 2, "L", "2026-04-13", false);

        PriceComparisonService service = new PriceComparisonService();


        Price cheapest = service.getCheapestPriceForItem(1, List.of(coles, aldi));


        assertEquals("Aldi", cheapest.getStoreName());
        assertEquals(2.85, cheapest.getPrice(), 0.001);
    }

    //RecommendationService

    @Test
    void shouldDetectPastSaleInHistory() {

        PriceHistoryEntry noSale = new PriceHistoryEntry(1, 1, "Coles", 3.50, "2026-03-01", false);
        PriceHistoryEntry onSale = new PriceHistoryEntry(2, 1, "Coles", 2.50, "2026-03-15", true);

        RecommendationService service = new RecommendationService();


        assertTrue(service.predictSaleSoon(1, "Coles", List.of(noSale, onSale)));
    }

    @Test
    void shouldReturnFalseWhenNoSaleHistory() {

        PriceHistoryEntry noSale = new PriceHistoryEntry(1, 1, "Coles", 3.50, "2026-03-01", false);

        RecommendationService service = new RecommendationService();


        assertFalse(service.predictSaleSoon(1, "Coles", List.of(noSale)));
    }

    @Test
    void shouldSuggestCheaperUnitOption() {
        // ARRANGE
        Price coles = new Price(1, 1, "Coles", 3.20, 2, "L", "2026-04-13", false);
        Price aldi  = new Price(2, 1, "Aldi",  2.85, 2, "L", "2026-04-13", false);

        RecommendationService service = new RecommendationService();

        String suggestion = service.getSuggestion(coles, aldi);

        assertEquals("Aldi offers better value per unit.", suggestion);
    }

    @Test
    void shouldReportEqualValueWhenPricesMatch() {
        // ARRANGE: same unit price ($1.60/L each)
        Price coles = new Price(1, 1, "Coles", 3.20, 2, "L", "2026-04-13", false);
        Price aldi  = new Price(2, 1, "Aldi",  3.20, 2, "L", "2026-04-13", false);

        RecommendationService service = new RecommendationService();


        String suggestion = service.getSuggestion(coles, aldi);

        assertEquals("Both stores offer equal value per unit.", suggestion);
    }
}
