package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.scene.chart.BarChart;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ComplaintController {

    public static ComplaintController instance;

    @FXML
    TableView<Complain> complaintTable;

    @FXML
    private TableColumn<Complain, String> customerColumn;

    @FXML
    private TableColumn<Complain, String> branchColumn;

    @FXML
    private TableColumn<Complain, String> dateColumn;

    @FXML
    private TableColumn<Complain, String> timeColumn;

    @FXML
    private TableColumn<Complain, String> textColumn;

    @FXML
    private TableColumn<Complain, String> answerColumn;

    @FXML
    private TableColumn<Complain, String> statusColumn;

    @FXML
    public void initialize() {
        instance = this;

        customerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer().getUsername()));

        branchColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBranch().getAddress()));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTime().toString()));

        textColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getComplain_text()));

        answerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAnswer_text()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus() ? "Handled" : "Pending"));
    }

    public static void handleComplaintList(List<Complain> complaints, ComplaintController controller) {
        Platform.runLater(() -> {
            ObservableList<Complain> observableList = FXCollections.observableArrayList(complaints);
            controller.complaintTable.setItems(observableList);
        });
    }
    @FXML
    private BarChart<String, Number> complaintBarChart;

    public static void updateBarChart(List<Complain> complaints, ComplaintController controller) {
        long handledCount = complaints.stream().filter(Complain::getStatus).count();
        long pendingCount = complaints.size() - handledCount;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Handled", handledCount));
        series.getData().add(new XYChart.Data<>("Pending", pendingCount));

        Platform.runLater(() -> {
            controller.complaintBarChart.getData().clear();
            controller.complaintBarChart.getData().add(series);
        });
    }

}
