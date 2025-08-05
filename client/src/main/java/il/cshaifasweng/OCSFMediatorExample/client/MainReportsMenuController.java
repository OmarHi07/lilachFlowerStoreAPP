package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainReportsMenuController {

    @FXML
    void goBack(ActionEvent event) {
        try {
            App.setRoot("Primary", 1006, 750);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openIncomeReport() throws IOException {
        try {
            App.setRoot("IncomeReport",1016, 760);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openDetailedReports() throws IOException {
        try {
            App.setRoot("Reports",1016, 760);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openOrdersHistogram() throws IOException {
        try {
            App.setRoot("OrdersHistogram", 1016, 760);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
