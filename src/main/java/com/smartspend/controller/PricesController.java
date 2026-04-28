package com.smartspend.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class PricesController extends BaseController {
    @FXML private LineChart<String, Number> priceChart;

    @FXML
    public void initialize() {
        XYChart.Series<String, Number> coles = new XYChart.Series<>();
        coles.setName("Coles");
        coles.getData().add(new XYChart.Data<>("Week 1", 3.5));
        coles.getData().add(new XYChart.Data<>("Week 2", 3.2));
        coles.getData().add(new XYChart.Data<>("Week 3", 2.5));
        coles.getData().add(new XYChart.Data<>("Week 4", 3.1));

        XYChart.Series<String, Number> woolworths = new XYChart.Series<>();
        woolworths.setName("Woolworths");
        woolworths.getData().add(new XYChart.Data<>("Week 1", 3.3));
        woolworths.getData().add(new XYChart.Data<>("Week 2", 3.1));
        woolworths.getData().add(new XYChart.Data<>("Week 3", 3.0));
        woolworths.getData().add(new XYChart.Data<>("Week 4", 2.9));

        XYChart.Series<String, Number> aldi = new XYChart.Series<>();
        aldi.setName("Aldi");
        aldi.getData().add(new XYChart.Data<>("Week 1", 2.95));
        aldi.getData().add(new XYChart.Data<>("Week 2", 2.9));
        aldi.getData().add(new XYChart.Data<>("Week 3", 2.85));
        aldi.getData().add(new XYChart.Data<>("Week 4", 2.8));

        priceChart.getData().setAll(coles, woolworths, aldi);
    }
}
