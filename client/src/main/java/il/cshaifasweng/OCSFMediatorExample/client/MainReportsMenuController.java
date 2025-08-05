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

public class MainReportsMenuController {

    @FXML
    void goBack(ActionEvent event) {
        try {
            App.setRoot("Primary", 1006, 750);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openIncomeReport(ActionEvent event) {
        openNewWindow("IncomeReport.fxml", "Income Report", 1016, 760, event);
    }

    @FXML
    private void openDetailedReports(ActionEvent event) {
        openNewWindow("Reports.fxml", "Detailed Complaints Report", 1016, 760, event);
    }

    @FXML
    private void openOrdersHistogram(ActionEvent event) {
        openNewWindow("OrdersHistogram.fxml", "Orders Histogram", 1016, 760, event);
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
