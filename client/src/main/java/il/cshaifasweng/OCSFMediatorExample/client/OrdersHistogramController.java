package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class OrdersHistogramController {

    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<String> branchComboBox;

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> idColumn;
    @FXML
    private TableColumn<Order, String> addressColumn;
    @FXML
    private TableColumn<Order, LocalDate> dateOrderColumn;
    @FXML
    private TableColumn<Order, LocalDate> dateReceiveColumn;
    @FXML
    private TableColumn<Order, String> greetingColumn;
    @FXML
    private TableColumn<Order, String> nameReceivesColumn;
    @FXML
    private TableColumn<Order, String> phoneReceivesColumn;
    @FXML
    private TableColumn<Order, Integer> statusColumn;
    @FXML
    private TableColumn<Order, Double> sumColumn;
    @FXML
    private TableColumn<Order, String> typeColumn;
    @FXML
    private TableColumn<Order, Integer> branchIdColumn;
    @FXML
    private TableColumn<Order, Integer> customerIdColumn;
    @FXML
    private TableColumn<Order, LocalTime> timeOrderColumn;
    @FXML
    private TableColumn<Order, LocalTime> timeReceiveColumn;
    @FXML
    private BarChart<String, Number> ordersBarChart;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private ComboBox<String> quarter;

    @FXML
    public void initialize() {
        branchComboBox.getItems().clear();
        branchComboBox.getItems().addAll("All", "Haifa", "TelAviv");
        branchComboBox.setValue("All");

        EventBus.getDefault().register(this);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("dateOrder"));
        dateReceiveColumn.setCellValueFactory(new PropertyValueFactory<>("dateReceive"));
        greetingColumn.setCellValueFactory(new PropertyValueFactory<>("greeting"));
        nameReceivesColumn.setCellValueFactory(new PropertyValueFactory<>("nameReceives"));
        phoneReceivesColumn.setCellValueFactory(new PropertyValueFactory<>("phoneReceives"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerIdColumn.setCellValueFactory(cd -> {
            Customer c = cd.getValue().getCustomer();
            Integer val = (c != null) ? c.getId() : null;   // ודא שלCustomer יש getId()
            return new ReadOnlyObjectWrapper<>(val);
        });

        // עמודה מקוננת: branch -> id
        branchIdColumn.setCellValueFactory(cd -> {
            Branch b = cd.getValue().getBranch();
            Integer val = (b != null) ? b.getId() : null;   // ודא שלBranch יש getId()
            return new ReadOnlyObjectWrapper<>(val);
        });
        timeOrderColumn.setCellValueFactory(new PropertyValueFactory<>("timeOrder"));
        timeReceiveColumn.setCellValueFactory(new PropertyValueFactory<>("timeReceive"));

        if (CurrentCustomer.getCurrentEmployee().getPermission() == 3) {
            BranchManager man = (BranchManager) CurrentCustomer.getCurrentEmployee();
            if (man.getBranch().getAddress().equals("Haifa")) {
                branchComboBox.getItems().clear();
                branchComboBox.setValue("Haifa");
            } else {
                branchComboBox.getItems().clear();
                branchComboBox.setValue("TelAviv");
            }
        }
    }

    private int parseBranchId() {
        String v = branchComboBox.getValue();
        if (v == null || v.equalsIgnoreCase("All")) return -1;
        if (v.equals("Haifa")) {
            return 1;
        } else {
            return 2;
        }
    }

    @FXML
    private void loadOrdersHistogram() {
        ordersBarChart.getData().clear();
        ordersTable.getItems().clear();  // רק אם רלוונטי

        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();

        // טיפול במקרה שלא נבחרו תאריכים
        if (from == null || to == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Date Range");
            alert.setHeaderText("Date range not selected");
            alert.setContentText("Please select both start and end dates before generating the report.");
            alert.showAndWait();
            return;
        }
        if (from.isAfter(to)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Date Range");
            alert.setHeaderText("Incorrect date range");
            alert.setContentText("Start date cannot be after the end date.");
            alert.showAndWait();
            return;
        }

        int branchId = parseBranchId();
        try {
            SimpleClient.getClient().sendToServer(new HistogramReportRequest("orders", from, to, branchId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void onReportReceived(GetReportEvent event) {
        if (!"orders".equals(event.getReportType())) {
            System.out.println("Skipped event, not an orders report.");
            return;
        }
        Object data = event.getReportData();
        if (data == null || !(data instanceof List<?>)) {
            Platform.runLater(() -> showError("No Data", "The report is empty or invalid. Try a different date range."));
            System.out.println("⚠️ Invalid or empty report data");
            return;
        }

        List<?> rawList = (List<?>) data;
        if (rawList.isEmpty()) {
            Platform.runLater(() -> showWarn("No Data", "The report is empty."));
            System.out.println("⚠️ empty report data");
            return;
        }

        try {
            List<Order> orders = rawList.stream().filter(obj -> obj instanceof Order).map(obj -> (Order) obj)
                    .filter(order ->
                            order.getDateReceive() != null &&
                                    order.getSum() != 0 &&
                                    order.getCustomer() != null &&
                                    order.getBranch() != null &&
                                    order.getStatus() != 0
                    )
                    .collect(Collectors.toList());

            Map<LocalDate, Double> incomeByDate = orders.stream()
                    .collect(Collectors.groupingBy(
                            Order::getDateReceive,
                            TreeMap::new,
                            Collectors.summingDouble(Order::getSum)
                    ));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<LocalDate, Double> entry : incomeByDate.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }

            double totalIncome = orders.stream().mapToDouble(Order::getSum).sum();

            Platform.runLater(() -> {
                ordersTable.setItems(FXCollections.observableArrayList(orders));
                ordersBarChart.getData().clear();
                ordersBarChart.getData().add(series);
                if (totalIncomeLabel != null) {
                    totalIncomeLabel.setText(String.format("Total Income: ₪%.2f", totalIncome));
                }
            });
        } catch (ClassCastException e) {
            System.out.println("❌ Failed to cast to List<Order>");
            e.printStackTrace();
        }
    }

    @Subscribe
    public void quarter(javafx.event.ActionEvent actionEvent) {
        String selectedQuarter = quarter.getValue();
        int year = LocalDate.now().getYear();

        switch (selectedQuarter) {
            case "Q1 (Jan - Mar)":
                fromDatePicker.setValue(LocalDate.of(year, 1, 1));
                toDatePicker.setValue(LocalDate.of(year, 3, 31));
                break;
            case "Q2 (Apr - Jun)":
                fromDatePicker.setValue(LocalDate.of(year, 4, 1));
                toDatePicker.setValue(LocalDate.of(year, 6, 30));
                break;
            case "Q3 (Jul - Sep)":
                fromDatePicker.setValue(LocalDate.of(year, 7, 1));
                toDatePicker.setValue(LocalDate.of(year, 9, 30));
                break;
            case "Q4 (Oct - Dec)":
                fromDatePicker.setValue(LocalDate.of(year, 10, 1));
                toDatePicker.setValue(LocalDate.of(year, 12, 31));
                break;
            case "NO NEED":
                fromDatePicker.setValue(null);     // מנקה את התאריך ההתחלתי
                toDatePicker.setValue(null);
        }
    }
    private void showError (String title, String content){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
    private void showWarn(String title, String content) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}