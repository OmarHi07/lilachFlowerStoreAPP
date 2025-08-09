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
import java.util.*;
import java.util.stream.Collectors;

public class OrdersReportsController {

    private static final boolean DEBUG = false;
    private static boolean registered = false;

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private ComboBox<String> branchComboBox;

    // טבלת סיכום (שם + סה"כ קניות/יחידות)
    @FXML private TableView<FlowerSummary> flowerSummaryTable;
    @FXML private TableColumn<FlowerSummary, String>  fsNameCol;
    @FXML private TableColumn<FlowerSummary, Integer> fsQtyCol;

    // היסטוגרמה
    @FXML private BarChart<String, Number> ordersBarChart;

    @FXML
    public void initialize() {
        branchComboBox.getItems().setAll("All", "Haifa", "TelAviv");
        branchComboBox.setValue("All");

        if (!registered) {
            EventBus.getDefault().register(this);
            registered = true;
        }

        // אם זו מנהלת סניף – הגבל לבחירת הסניף שלה בלבד
        if (CurrentCustomer.getCurrentEmployee() != null &&
                CurrentCustomer.getCurrentEmployee().getPermission() == 3) {
            BranchManager man = (BranchManager) CurrentCustomer.getCurrentEmployee();
            String only = "Haifa".equals(man.getBranch().getAddress()) ? "Haifa" : "TelAviv";
            branchComboBox.getItems().setAll(only);
            branchComboBox.setValue(only);
        }

        if (flowerSummaryTable != null) {
            fsNameCol.setCellValueFactory(new PropertyValueFactory<>("flowerName"));
            fsQtyCol.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        }

        if (ordersBarChart != null) {
            ordersBarChart.setAnimated(false);
        }
    }

    private int parseBranchId() {
        String v = branchComboBox.getValue();
        if (v == null || v.equalsIgnoreCase("All")) return -1;
        if (v.equals("Haifa")) return 1;
        return 2; // TelAviv
    }

    @FXML
    private void loadOrdersHistogram() {
        if (ordersBarChart != null) ordersBarChart.getData().clear();
        if (flowerSummaryTable != null) flowerSummaryTable.getItems().clear();

        LocalDate from = fromDatePicker.getValue();
        LocalDate to   = toDatePicker.getValue();

        if (from == null || to == null)  {
            showWarn("Missing Date Range", "Please select both start and end dates before generating the report.");
            return;
        }
        if (from.isAfter(to)) {
            showWarn("Invalid Date Range", "Start date cannot be after the end date.");
            return;
        }

        int branchId = parseBranchId();
        try {
            SimpleClient.getClient().sendToServer(new HistogramReportRequest("orders", from, to, branchId));
        } catch (IOException e) {
            e.printStackTrace();
            showWarn("Network Error", "Failed to send request to server.");
        }
    }

    @Subscribe
    public void onReportReceived(GetReportEvent event) {
        if (event == null || !"orders".equals(event.getReportType())) return;

        Object data = event.getReportData();
        if (!(data instanceof List<?>)) {
            Platform.runLater(() -> showError("No Data", "The report is empty or invalid. Try a different date range."));
            return;
        }

        @SuppressWarnings("unchecked")
        List<Order> rawList = (List<Order>) data;
        if (rawList.isEmpty()) {
            Platform.runLater(() -> showWarn("No Data", "The report is empty."));
            return;
        }

        try {
            // 1) סינון בסיסי
            List<Order> orders = rawList.stream()
                    .filter(Objects::nonNull)
                    .filter(o -> o.getSum() != 0)
                    .filter(o -> o.getStatus() != 0 && o.getStatus() != 1) // אל תכלול מבוטלות
                    .filter(o -> o.getCustomer() != null && o.getBranch() != null)
                    .collect(Collectors.toList());

            // 2) דידופ הזמנות לפי id
            Map<Integer, Order> byId = new LinkedHashMap<>();
            for (Order o : orders) byId.putIfAbsent(o.getId(), o);
            orders = new ArrayList<>(byId.values());

            // 3) מילון פרחים
            List<Flower> flowers = Optional.ofNullable(SimpleClient.getFlowers()).orElseGet(Collections::emptyList);
            List<Flower> singles = Optional.ofNullable(SimpleClient.getFlowersSingles()).orElseGet(Collections::emptyList);

            Set<Integer> idsAll = new HashSet<>();
            idsAll.addAll(flowers.stream().map(Flower::getId).collect(Collectors.toList()));
            idsAll.addAll(singles.stream().map(Flower::getId).collect(Collectors.toList()));

            Map<Integer, String> idToName = new HashMap<>();
            flowers.forEach(f -> idToName.putIfAbsent(f.getId(), safeName(f)));
            singles.forEach(f -> idToName.putIfAbsent(f.getId(), safeName(f)));

            // 4) צבירה: סה"כ יחידות (quantity) לכל פרח
            Map<Integer, Integer> qtyById = new HashMap<>();
            Set<String> seenCpGlobal = new HashSet<>(); // דידופ CP לפי orderId#cartProductId

            for (Order o : orders) {
                if (o.getProducts() == null) continue;
                int oid = o.getId();

                for (CartProduct p : o.getProducts()) {
                    if (p == null || p.getFlower() == null) continue;

                    int fid = p.getFlower().getId();
                    if (!idsAll.contains(fid)) continue;

                    // דילוג כפילויות CP (במקרה של JOIN שחוזר פעמיים)
                    int cpId = p.getId();
                    String uniq = oid + "#" + cpId;
                    if (cpId > 0 && !seenCpGlobal.add(uniq)) {
                        if (DEBUG) System.out.println("Skip DUP CP: " + uniq);
                        continue;
                    }

                    int qty = p.getQuantity(); // ספירת יחידות כפי שנקנה
                    if (qty <= 0) continue;

                    qtyById.merge(fid, qty, Integer::sum);
                }
            }

            // ודא שכל הפרחים קיימים גם אם לא נמכרו
            List<Flower> allKnownFlowers = new ArrayList<>();
            allKnownFlowers.addAll(flowers);
            allKnownFlowers.addAll(singles);
            for (Flower f0 : allKnownFlowers) {
                if (f0 == null) continue;
                int fid0 = f0.getId();
                qtyById.putIfAbsent(fid0, 0);
            }

            // בניית שורות טבלה: שם + Purchases (סה"כ יחידות)
            List<Integer> idsSorted = qtyById.keySet().stream().sorted().collect(Collectors.toList());
            List<FlowerSummary> summaryRows = new ArrayList<>();
            for (Integer fid : idsSorted) {
                String name = Optional.ofNullable(idToName.get(fid)).orElse("ID " + fid);
                int totalQty = qtyById.getOrDefault(fid, 0);
                summaryRows.add(new FlowerSummary(name, 0, totalQty, 0.0)); // ordersCount=0, income=0
            }

            final List<FlowerSummary> summaryRowsF = new ArrayList<>(summaryRows);

            Platform.runLater(() -> {
                // טבלה: שם + Purchases
                if (flowerSummaryTable != null) {
                    flowerSummaryTable.setItems(FXCollections.observableArrayList(summaryRowsF));
                }

                // גרף: Y = Purchases (סה"כ יחידות), X = שם
                if (ordersBarChart != null) {
                    ordersBarChart.setAnimated(false);
                    ordersBarChart.getData().clear();

                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Purchases by Flower");
                    for (FlowerSummary row : summaryRowsF) {
                        series.getData().add(new XYChart.Data<>(row.getFlowerName(), row.getTotalQuantity()));
                    }
                    ordersBarChart.getData().setAll(series);
                    ordersBarChart.requestLayout();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> showError("Processing Error", "Failed to process the purchases report data."));
        }
    }

    // שם נקי/ברירת מחדל
    private static String safeName(Flower f) {
        if (f == null) return "Unknown";
        String n = f.getFlowerName();
        if (n == null) n = "";
        n = n.trim();
        if (n.isEmpty()) n = "ID " + f.getId();
        return n;
    }

    private void showWarn(String title, String content) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }

    private void showError(String title, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}
