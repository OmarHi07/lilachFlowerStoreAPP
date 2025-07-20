package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import il.cshaifasweng.OCSFMediatorExample.entities.CartProduct;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.hibernate.Hibernate;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import static il.cshaifasweng.OCSFMediatorExample.client.Ordercart.cartItems;
import static il.cshaifasweng.OCSFMediatorExample.client.Ordercart.totalprice;

public class pastOrders  {

    @FXML
    private ResourceBundle resources;


    @FXML
    private Button Exit;

    @FXML
    private AnchorPane previes;

    @FXML
    private GridPane ordersbefore;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Order> orderList = CurrentCustomer.getCurrentUser().getListOrders();

    @FXML
    void exit(ActionEvent event) {
        Button source = (Button) event.getSource(); // הכפתור שהפעיל את האירוע
        Stage stage = (Stage) source.getScene().getWindow(); // מקבל את החלון מהכפתור
        stage.close();
    }


    private void renderPastOrders(List<Order> pastOrders) {
        ordersbefore.getChildren().clear(); // נקה גריד קודם

        int row = 0;

        for (Order order : pastOrders) {
            if (order.getProducts().isEmpty()) continue;

            // --- תמונה של הפרח הראשון ---
            byte[] imgBytes = order.getProducts().get(0).getFlower().getImage();
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imgBytes)));
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            imageView.setPreserveRatio(true);

            imageView.setOnMouseClicked(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("pastSpecificOrder.fxml"));
                    Parent root = loader.load();

                    // העברת האובייקט Order שכרגע בלולאה
                    spesifcpastorder controller = loader.getController();
                    controller.setOrder(order); // זה האובייקט מהלולאה שלך

                    Stage stage = new Stage();
                    stage.setTitle("Order Details");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            // --- מחיר סופי של ההזמנה ---
            Label priceLabel = new Label(String.format("%.2f ₪", order.getSum()));
            priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4e3620;");
            priceLabel.setAlignment(Pos.CENTER_LEFT);
            GridPane.setHalignment(priceLabel, HPos.CENTER);

            // --- כפתור הגשת תלונה ---
            Button complainButton = new Button("Submit Complaint");
            complainButton.setStyle("-fx-background-color: #e0d2c0; -fx-text-fill: #5b3a29; -fx-font-size: 13px;");
            complainButton.setOnAction(e -> {
                // כאן אפשר לפתוח חלון חדש להגשת תלונה
                System.out.println("Complaint clicked for order ID: " + order.getId());
            });

            // --- הוספה לגריד ---
            ordersbefore.add(imageView, 0, row);
            ordersbefore.add(priceLabel, 1, row);
            ordersbefore.add(complainButton, 2, row);
            row++;
        }

        ordersbefore.setVgap(15);
        ordersbefore.setHgap(30);
        ordersbefore.setStyle("-fx-padding: 20;");
    }





    @FXML
    void initialize() {
        assert Exit != null : "fx:id=\"Exit\" was not injected: check your FXML file 'pastOrders.fxml'.";
        assert previes != null : "fx:id=\"cart\" was not injected: check your FXML file 'pastOrders.fxml'.";
        assert ordersbefore != null : "fx:id=\"ordersbefore\" was not injected: check your FXML file 'pastOrders.fxml'.";
        renderPastOrders(CurrentCustomer.getCurrentUser().getListOrders());
    }

}
