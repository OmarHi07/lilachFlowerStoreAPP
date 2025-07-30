package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
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

public class OrdersReportsController {
    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<String> branchComboBox;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> idColumn;
    @FXML private TableColumn<Order, String> addressColumn;
    @FXML private TableColumn<Order, LocalDate> dateOrderColumn;
    @FXML private TableColumn<Order, LocalDate> dateReceiveColumn;
    @FXML private TableColumn<Order, String> greetingColumn;
    @FXML private TableColumn<Order, String> nameReceivesColumn;
    @FXML private TableColumn<Order, String> phoneReceivesColumn;
    @FXML private TableColumn<Order, Integer> statusColumn;
    @FXML private TableColumn<Order, Double> sumColumn;
    @FXML private TableColumn<Order, String> typeColumn;
    @FXML private TableColumn<Order, Integer> branchIdColumn;
    @FXML private TableColumn<Order, Integer> customerIdColumn;
    @FXML private TableColumn<Order, LocalTime> timeOrderColumn;
    @FXML private TableColumn<Order, LocalTime> timeReceiveColumn;
    @FXML private BarChart<String, Number> ordersBarChart;
    @FXML private Label totalIncomeLabel;

    @FXML
    public void initialize() {
        branchComboBox.getItems().addAll("All", "1", "2");
        branchComboBox.setValue(("All")); // ברירת מחדל
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
        branchIdColumn.setCellValueFactory(new PropertyValueFactory<>("branch.id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customer.id"));
        timeOrderColumn.setCellValueFactory(new PropertyValueFactory<>("timeOrder"));
        timeReceiveColumn.setCellValueFactory(new PropertyValueFactory<>("timeReceive"));
    }

    @FXML
    private void loadOrdersHistogram() throws IOException {
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();

        // טיפול במקרה שלא נבחרו תאריכים
        if (from == null || to == null) {
            System.out.println("⚠️ יש לבחור טווח תאריכים לפני שליחה לשרת");
            return;
        }

        int branchId = 1;
        SimpleClient.getClient().sendToServer(new HistogramReportRequest("orders", from, to, branchId));
    }


    @Subscribe
    public void onReportReceived(GetReportEvent event) {
        if (!"orders".equals(event.getReportType())) {
            System.out.println("Skipped event, not an orders report.");
            return;
        }
        Object data = event.getReportData();
        if (data == null || !(data instanceof List<?> rawList) || rawList.isEmpty()) {
            System.out.println("⚠️ Invalid or empty report data");
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
}
