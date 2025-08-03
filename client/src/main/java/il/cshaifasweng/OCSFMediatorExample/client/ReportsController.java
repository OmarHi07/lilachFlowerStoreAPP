package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.scene.chart.XYChart;
import java.util.TreeMap;
import java.util.stream.Collectors;
import il.cshaifasweng.OCSFMediatorExample.entities.GetReportEvent;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.HistogramReportRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ReportsController {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private ComboBox<String> branchComboBox;


    @FXML
    private TableView<Complain> complainTable;
    @FXML
    private TableColumn<Complain, Integer> idColumn;
    @FXML
    private TableColumn<Complain, String> textColumn;
    @FXML
    private TableColumn<Complain, LocalDate> dateColumn;
    @FXML
    private BarChart<String, Number> dailyComplaintsChart;
    @FXML
    private TableColumn<Complain, String> complainTextColumn;
    @FXML
    private TableColumn<Complain, LocalDate> complainDateColumn;
    @FXML
    private TableColumn<HistogramEntry, String> branchColumn;
    @FXML
    private TableColumn<HistogramEntry, Integer> countColumn;
    @FXML
    private TableView<HistogramEntry> histogramTable;
    private Collectors Collectors;
    @FXML
    public void initialize() {
        branchComboBox.getItems().addAll("All", "1", "2");
        branchComboBox.setValue(("All")); // ברירת מחדל
        EventBus.getDefault().register(this);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("branch.id"));

        countColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        complainTextColumn.setCellValueFactory(new PropertyValueFactory<>("complain_text"));
        complainDateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); // תואם ל-getDate()
    }

    private int parseBranchId() {
        String v = branchComboBox.getValue();
        if (v == null || v.equalsIgnoreCase("All")) return -1;   // ← תואם לשרת
        try { return Integer.parseInt(v.trim()); } catch (NumberFormatException e) { return -1; }
    }

    @FXML
    private void loadOrdersHistogram(){
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();

        // טיפול במקרה שלא נבחרו תאריכים
        if (from == null || to == null) {
            System.out.println("⚠️ יש לבחור טווח תאריכים לפני שליחה לשרת");
            return;
        }

        int branchId = parseBranchId();

        try {
            SimpleClient.getClient().sendToServer(new HistogramReportRequest("complain", from, to, branchId));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onReportReceived(GetReportEvent event) {
        if (!"complain".equals(event.getReportType())) {
            System.out.println("Skipped event, not a complain report.");
            return;
        }
        Object data = event.getReportData();
        if (data == null) {
            System.out.println("❌ reportData is null");
            return;
        }
        if (!(data instanceof List<?>)) {
            System.out.println("❌ reportData is NOT a List");
            return;
        }
        List<?> rawList = (List<?>) data;
        if (rawList.isEmpty()) {
            System.out.println("⚠️ reportData list is empty");
            return;
        }
        try {
            @SuppressWarnings("unchecked")
            List<Complain> complaints = (List<Complain>) rawList;
            Map<LocalDate, Long> countsByDate = complaints.stream()
                    .collect(Collectors.groupingBy(
                            Complain::getDate,
                            TreeMap::new,
                            Collectors.counting()
                    ));
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<LocalDate, Long> entry : countsByDate.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
            Platform.runLater(() -> {
                complainTable.setItems(FXCollections.observableArrayList(complaints));
                dailyComplaintsChart.getData().clear();
                dailyComplaintsChart.getData().add(series);
            });
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}