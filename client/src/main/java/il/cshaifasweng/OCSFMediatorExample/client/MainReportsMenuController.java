package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.*;

public class MainReportsMenuController {

    @FXML private Button complaint;
    @FXML private Button income;
    @FXML private Button order;

    // לשאר התפקידים: חלון יחיד
    private Stage incomeReportStage = null;
    private Stage ordersHistogramStage = null;
    private Stage complaintsReportStage = null;

    // למנהלת הרשת: מעקב אחרי חלונות פתוחים לכל FXML (מקסימום 2)
    private final Map<String, List<Stage>> openStagesByFxml = new HashMap<>();

    @FXML
    void goBack(ActionEvent event) {
        try {
            App.setRoot("primary",1120,760);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() { }

    private boolean isNetworkManager() {
        return CurrentCustomer.getCurrentEmployee() != null
                && CurrentCustomer.getCurrentEmployee().getPermission() == 4;
    }

    // ---------- עזר: פתיחה עם הגבלה ל-2 ----------
    private void openWindowCapped(String fxml, String title, int width, int height) {
        List<Stage> list = openStagesByFxml.computeIfAbsent(fxml, k -> new ArrayList<>());

        // יש כבר 2? הביאי קדימה אחד קיים (או הציגי התראה)
        if (list.size() >= 2) {
            // אופציה 1: רק להביא קדימה את האחרון
            list.get(list.size() - 1).toFront();

            // אופציה 2 (במקום השורה למעלה): להתריע למשתמש
            // Alert a = new Alert(Alert.AlertType.INFORMATION, "אפשר לפתוח עד שני חלונות מאותו דוח בו-זמנית.");
            // a.setHeaderText(null); a.setTitle("הגבלת חלונות"); a.show();

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));

            // כשנסגר – להסיר מהרשימה
            stage.setOnCloseRequest(e -> {
                List<Stage> l = openStagesByFxml.get(fxml);
                if (l != null) {
                    l.remove(stage);
                    if (l.isEmpty()) openStagesByFxml.remove(fxml);
                }
            });

            list.add(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- עזר: חלון יחיד מודאלי (למי שאינו מנהלת רשת) ----------
    private Stage openSingleModal(Stage holder, String fxml, String title, int width, int height, Window owner) {
        if (holder != null && holder.isShowing()) {
            holder.toFront();
            return holder;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            if (owner != null) {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            }
            stage.setOnCloseRequest(e -> {
                if (title.contains("Income")) {
                    incomeReportStage = null;
                } else if (title.contains("Orders Histogram")) {
                    ordersHistogramStage = null;
                } else if (title.contains("Detailed Complaints Report")) {
                    complaintsReportStage = null;
                }
            });
            stage.show();
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======= הכפתור: דו"ח הכנסות =======
    @FXML
    private void openIncomeReport(ActionEvent event) {
        if (isNetworkManager()) {
            openWindowCapped("IncomeReport.fxml", "Income Report", 1016, 760);
        } else {
            Window owner = ((Stage)((Node)event.getSource()).getScene().getWindow());
            incomeReportStage = openSingleModal(incomeReportStage,
                    "IncomeReport.fxml", "Income Report", 1016, 760, owner);
        }
    }

    // ======= הכפתור: דו"ח תלונות מפורט =======
    @FXML
    private void openDetailedReports(ActionEvent event) {
        if (isNetworkManager()) {
            openWindowCapped("Reports.fxml", "Detailed Complaints Report", 1016, 760);
        } else {
            Window owner = ((Stage)((Node)event.getSource()).getScene().getWindow());
            complaintsReportStage = openSingleModal(complaintsReportStage,
                    "Reports.fxml", "Detailed Complaints Report", 1016, 760, owner);
        }
    }

    // ======= הכפתור: היסטוגרמת הזמנות =======
    @FXML
    private void openOrdersHistogram(ActionEvent event) {
        if (isNetworkManager()) {
            openWindowCapped("OrdersHistogram.fxml", "Orders Histogram", 1016, 760);
        } else {
            Window owner = ((Stage)((Node)event.getSource()).getScene().getWindow());
            ordersHistogramStage = openSingleModal(ordersHistogramStage,
                    "OrdersHistogram.fxml", "Orders Histogram", 1016, 760, owner);
        }
    }
}
