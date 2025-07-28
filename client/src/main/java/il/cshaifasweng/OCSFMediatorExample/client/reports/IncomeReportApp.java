
package il.cshaifasweng.OCSFMediatorExample.client.reports;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.persistence.*;
import java.util.List;

public class IncomeReportApp {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("your_persistence_unit");


    public void start(Stage stage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("דו\"ח הכנסות");
        xAxis.setLabel("תאריך הזמנה");
        yAxis.setLabel("סכום הכנסה");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("הכנסות");

        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT o.dateOrder, SUM(o.sum) FROM Order o WHERE o.status = 2 AND o.isCanceled = false GROUP BY o.dateOrder ORDER BY o.dateOrder";
        List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

        for (Object[] row : results) {
            String date = (String) row[0];
            Double income = (Double) row[1];
            series.getData().add(new XYChart.Data<>(date, income));
        }

        em.close();

        barChart.getData().add(series);

        BorderPane root = new BorderPane(barChart);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("דו\"ח הכנסות");
        stage.show();
    }


}
