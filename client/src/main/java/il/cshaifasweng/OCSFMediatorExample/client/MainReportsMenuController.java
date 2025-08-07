package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import javafx.scene.control.Button;

public class MainReportsMenuController {

    @FXML
    private Button complaint;

    @FXML
    private Button income;

    @FXML
    private Button order;


    private Stage incomeReportStage = null;
    private Stage ordersHistogramStage = null;


    @FXML
    void goBack(ActionEvent event) {
        try {
            App.setRoot("Primary", 1006, 750);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        if (CurrentCustomer.getCurrentEmployee() != null) {
            if(CurrentCustomer.getCurrentEmployee().getPermission()==2){
                order.setDisable(true);
                complaint.setDisable(false);
                income.setDisable(true);
            }
           else {
                order.setDisable(false);
                complaint.setDisable(true);
                income.setDisable(false);
            }
        }

    }


    @FXML
    private void openIncomeReport(ActionEvent event) {
        if (incomeReportStage == null || !incomeReportStage.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("IncomeReport.fxml"));
                Parent root = loader.load();
                incomeReportStage = new Stage();
                incomeReportStage.setTitle("Income Report");
                incomeReportStage.setScene(new Scene(root, 1016, 760));
                incomeReportStage.initOwner(((Stage)((Node)event.getSource()).getScene().getWindow()));
                incomeReportStage.initModality(Modality.WINDOW_MODAL);
                incomeReportStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            incomeReportStage.toFront(); // אם כבר פתוח – מביא קדימה
        }
    }


    @FXML
    private void openDetailedReports(ActionEvent event) {
        openNewWindow("Reports.fxml", "Detailed Complaints Report", 1016, 760, event);
    }

    @FXML
    private void openOrdersHistogram(ActionEvent event) {
        if (ordersHistogramStage == null || !ordersHistogramStage.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("OrdersHistogram.fxml"));
                Parent root = loader.load();
                ordersHistogramStage = new Stage();
                ordersHistogramStage.setTitle("Orders Histogram");
                ordersHistogramStage.setScene(new Scene(root, 1016, 760));
                ordersHistogramStage.initOwner(((Stage)((Node)event.getSource()).getScene().getWindow()));
                ordersHistogramStage.initModality(Modality.WINDOW_MODAL);
                ordersHistogramStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ordersHistogramStage.toFront(); // אם כבר פתוח – מביא קדימה
        }
    }
    private void openNewWindow(String fxmlFile, String title, int width, int height, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.setScene(new Scene(root, width, height));
            popupStage.initOwner(((Stage)((Node)event.getSource()).getScene().getWindow())); // מקשר לחלון האב
            popupStage.initModality(Modality.WINDOW_MODAL); // אם רוצים שיהיה modal
            popupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
