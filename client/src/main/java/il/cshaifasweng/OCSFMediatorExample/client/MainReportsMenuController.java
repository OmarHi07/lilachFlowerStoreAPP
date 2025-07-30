package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainReportsMenuController {

    @FXML
    private void openIncomeReport() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IncomeReport.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Income Report");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void openDetailedReports() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reports.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Detailed Reports");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void openOrdersHistogram() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OrdersHistogram.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Orders Histogram");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
