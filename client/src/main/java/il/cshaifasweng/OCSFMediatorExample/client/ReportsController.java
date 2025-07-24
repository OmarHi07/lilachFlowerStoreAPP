package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.GetHistogramReportEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.HistogramReportRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.ReportRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
    private TableView<Complain> complainTable;
    @FXML
    private TableColumn<Complain, Integer> idColumn;
    @FXML
    private TableColumn<Complain, String> textColumn;
    @FXML
    private TableColumn<Complain, LocalDate> dateColumn;


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




    @FXML
    public void initialize() {
        System.out.println("initialize1111111");

        EventBus.getDefault().register(this);

        branchColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        complainTextColumn.setCellValueFactory(new PropertyValueFactory<>("complain_text"));
        complainDateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); // תואם ל-getDate()
        System.out.println("initialize22222222");

    }


    @FXML
    private void loadComplaintsHistogram() throws IOException {
        LocalDate from = LocalDate.now().minusMonths(3);
        LocalDate to = LocalDate.now();

        System.out.println("נשלחת בקשה לדוח (" + from + " עד " + to + ")");

        SimpleClient.getClient().sendToServer(new HistogramReportRequest(from, to));

    }


    @Subscribe
    public void onGetHistogramReportEvent(GetHistogramReportEvent event) {
        System.out.println("onGetHistogramReportEvent");
        System.out.println("aa " + event.getType());
        System.out.println("bb " + event.getData());
        if (event.getType().equals("Complaints")) {
            Platform.runLater(() -> {
                List<HistogramEntry> entries = new ArrayList<>();
                for (Map.Entry<String, Long> entry : event.getData().entrySet()) {
                    entries.add(new HistogramEntry(entry.getKey(), entry.getValue()));
                }
                histogramTable.getItems().setAll(entries);
            });
        }
    }
    public void onReportReceived(GetReportEvent event) {
        System.out.println("onReportReceived");

        if (!"complain".equals(event.getReportType())) {
            System.out.println("Skipped event, not a complain report.");
            return;
        }

        Object data = event.getReportData();

        if (data == null) {
            System.out.println("❌ reportData is null");
            return;
        }

        System.out.println("Class of reportData: " + data.getClass().getName());

        if (data instanceof List<?>) {
            List<?> rawList = (List<?>) data;

            if (!rawList.isEmpty()) {
                System.out.println("First element class: " + rawList.get(0).getClass().getName());
            } else {
                System.out.println("⚠️ reportData list is empty");
            }

            try {
                @SuppressWarnings("unchecked")
                List<Complain> complaints = (List<Complain>) rawList;

                Platform.runLater(() -> {
                    complainTable.setItems(FXCollections.observableArrayList(complaints));
                });
            } catch (ClassCastException e) {
                System.out.println("❌ Failed to cast to List<Complain>");
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ reportData is NOT a List");
        }
    }
    @FXML
    private void loadDetailedComplaintsReport() throws IOException {
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate to = LocalDate.now();
        int branchId = 1; // או -1 אם אתה מנהל רשת

        ReportRequest request = new ReportRequest("complain", from, to, branchId);
        SimpleClient.getClient().sendToServer(request);
    }




}
