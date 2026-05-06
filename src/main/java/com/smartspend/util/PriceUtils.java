package com.smartspend.util;

public class PriceUtils {
    public static double calculateUnitPrice(double totalPrice, double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return totalPrice / quantity;
    }

    public static String cheaperByUnit(double price1, double qty1, double price2, double qty2) {
        double unit1 = calculateUnitPrice(price1, qty1);
        double unit2 = calculateUnitPrice(price2, qty2);

        if (unit1 < unit2) return "Option 1";
        if (unit2 < unit1) return "Option 2";
        return "Equal value";
    }
}
