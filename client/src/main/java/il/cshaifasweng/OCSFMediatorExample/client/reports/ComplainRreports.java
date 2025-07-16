
package il.cshaifasweng.OCSFMediatorExample.client.reports;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class ComplainRreports {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("your_persistence_unit");


    public void start(Stage stage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("דו\"ח הכנסות");
        xAxis.setLabel("תאריך תלונה");
        yAxis.setLabel("מספר תלונות");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("תלונות");

        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT c.Date, COUNT(c) FROM Complain c GROUP BY c.Date ORDER BY c.Date";
        List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

        for (Object[] row : results) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            series.getData().add(new XYChart.Data<>(date.toString(), count));
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
