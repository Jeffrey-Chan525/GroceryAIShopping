package com.smartspend.controller;

import com.smartspend.service.PriceComparisonService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComparisonController extends BaseController {

    public static class ComparisonRow {
        private final SimpleStringProperty item;
        private final SimpleStringProperty coles;
        private final SimpleStringProperty woolworths;
        private final SimpleStringProperty aldi;
        private final SimpleStringProperty bestStore;
        private final SimpleStringProperty reason;

        public ComparisonRow(String item, double coles, double woolworths, double aldi,
                             String bestStore, String reason) {
            this.item = new SimpleStringProperty(item);
            this.coles = new SimpleStringProperty(String.format("$%.2f", coles));
            this.woolworths = new SimpleStringProperty(String.format("$%.2f", woolworths));
            this.aldi = new SimpleStringProperty(String.format("$%.2f", aldi));
            this.bestStore = new SimpleStringProperty(bestStore);
            this.reason = new SimpleStringProperty(reason);
        }

        public String getItem() {
            return item.get();
        }

        public String getColes() {
            return coles.get();
        }

        public String getWoolworths() {
            return woolworths.get();
        }

        public String getAldi() {
            return aldi.get();
        }

        public String getBestStore() {
            return bestStore.get();
        }

        public String getReason() {
            return reason.get();
        }
    }

    @FXML private Label lowestStoreLabel;
    @FXML private Label lowestBasketLabel;
    @FXML private Label bestDealLabel;
    @FXML private Label bestDealSubLabel;
    @FXML private Label splitShopLabel;
    @FXML private Label splitShopSubLabel;

    @FXML private TableView<ComparisonRow> comparisonTable;
    @FXML private TableColumn<ComparisonRow, String> itemColumn;
    @FXML private TableColumn<ComparisonRow, String> colesColumn;
    @FXML private TableColumn<ComparisonRow, String> woolworthsColumn;
    @FXML private TableColumn<ComparisonRow, String> aldiColumn;
    @FXML private TableColumn<ComparisonRow, String> bestStoreColumn;
    @FXML private TableColumn<ComparisonRow, String> reasonColumn;

    private final PriceComparisonService priceComparisonService = new PriceComparisonService();

    @FXML
    public void initialize() {
        setupTable();
        loadComparisonData();
    }

    private void setupTable() {
        itemColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem()));
        colesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColes()));
        woolworthsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWoolworths()));
        aldiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAldi()));
        bestStoreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBestStore()));
        reasonColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReason()));
    }

    private void loadComparisonData() {
        ComparisonRow milk = buildRow("Whole milk 2L", 3.20, 3.10, 2.80);
        ComparisonRow chicken = buildRow("Chicken breast 500g", 6.49, 7.00, 7.20);
        ComparisonRow pasta = buildRow("Pasta 500g", 1.80, 1.60, 1.20);
        ComparisonRow yoghurt = buildRow("Greek yoghurt 1kg", 4.50, 3.90, 3.90);

        comparisonTable.setItems(FXCollections.observableArrayList(
                milk,
                chicken,
                pasta,
                yoghurt
        ));

        Map<String, Double> basketTotals = new LinkedHashMap<>();
        basketTotals.put("Coles", 3.20 + 6.49 + 1.80 + 4.50);
        basketTotals.put("Woolworths", 3.10 + 7.00 + 1.60 + 3.90);
        basketTotals.put("Aldi", 2.80 + 7.20 + 1.20 + 3.90);

        String cheapestBasketStore = priceComparisonService.findCheapestStore(basketTotals);
        double cheapestBasketTotal = basketTotals.get(cheapestBasketStore);

        lowestStoreLabel.setText(cheapestBasketStore);
        lowestBasketLabel.setText(String.format("$%.2f for the full basket", cheapestBasketTotal));

        bestDealLabel.setText("Pasta at Aldi");
        bestDealSubLabel.setText("Cheapest individual item compared across stores");

        splitShopLabel.setText("Aldi + Coles");
        splitShopSubLabel.setText("Aldi is best overall, but Coles has the best chicken price");
    }

    private ComparisonRow buildRow(String item, double coles, double woolworths, double aldi) {
        Map<String, Double> prices = new LinkedHashMap<>();
        prices.put("Coles", coles);
        prices.put("Woolworths", woolworths);
        prices.put("Aldi", aldi);

        String cheapestStore = priceComparisonService.findCheapestStore(prices);
        double cheapestPrice = prices.get(cheapestStore);

        String reason = String.format("%s has the lowest price at $%.2f.", cheapestStore, cheapestPrice);

        return new ComparisonRow(item, coles, woolworths, aldi, cheapestStore, reason);
    }
}