
package il.cshaifasweng.OCSFMediatorExample.client.reports;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.persistence.*;
import java.util.List;

public class ProductTypeReportApp  {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("your_persistence_unit");


    public void start(Stage stage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("דו\"ח הזמנות לפי סוג מוצר");
        xAxis.setLabel("סוג מוצר");
        yAxis.setLabel("מספר הזמנות");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("הזמנות");

        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT o.type, COUNT(o) FROM Order o WHERE o.status = 2 AND o.isCanceled = false GROUP BY o.type";
        List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

        for (Object[] row : results) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            series.getData().add(new XYChart.Data<>(type, count));
        }

        em.close();

        barChart.getData().add(series);

        BorderPane root = new BorderPane(barChart);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("דו\"ח הזמנות לפי סוג מוצר");
        stage.show();
    }


}
