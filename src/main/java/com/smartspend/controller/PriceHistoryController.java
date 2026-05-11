package com.smartspend.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * No functionality yet hard values
 */
public class PriceHistoryController {

    @FXML private LineChart<String, Number> chart;
    @FXML private ComboBox<String> itemCombo;
    @FXML private ComboBox<String> rangeCombo;

    @FXML private TableView<PriceHistorySnapshot> priceTable;
    @FXML private TableColumn<PriceHistorySnapshot, String> colWeek;
    @FXML private TableColumn<PriceHistorySnapshot, String> colAldi;
    @FXML private TableColumn<PriceHistorySnapshot, String> colColes;
    @FXML private TableColumn<PriceHistorySnapshot, String> colWoolies;
    @FXML private TableColumn<PriceHistorySnapshot, String> colLowest;
    @FXML private TableColumn<PriceHistorySnapshot, String> colChange;

    @FXML
    public void initialize() {
        itemCombo.setItems(FXCollections.observableArrayList(
                "Whole milk 2L", "Chicken breast 500g", "Pasta 500g", "Greek yoghurt 1kg"));
        itemCombo.getSelectionModel().selectFirst();

        rangeCombo.setItems(FXCollections.observableArrayList(
                "Last month", "Last 3 months", "Last 6 months"));
        rangeCombo.getSelectionModel().select("Last 3 months");
    }
}
