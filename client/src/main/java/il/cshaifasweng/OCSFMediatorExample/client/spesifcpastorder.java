package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.CartProduct;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class spesifcpastorder {


        @FXML
        private ResourceBundle resources;

        @FXML
        private AnchorPane cart;

        @FXML
        private Button goback;

        @FXML
        private GridPane pastorder;

        private Order order;

       public void setOrder(Order order) {
           this.order = order;
           renderOrder();  // תקראי לפונקציה שתמלא את ה־GridPane//}
       }


    private void renderOrder() {
        pastorder.getChildren().clear();

        int row = 0;

        for (CartProduct item : order.getProducts()) {
            // תמונה
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(item.getFlower().getImage())));
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            imageView.setPreserveRatio(true);


            // שם פרח
            Label nameLabel = new Label(item.getFlower().getFlowerName());
            nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4e3620;");


            // כמות
            Label quantityLabel = new Label("x" + item.getQuantity());
            quantityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4e3620;");


            // מחיר
            double total = item.getQuantity() * item.getPrice();
            Label priceLabel = new Label(String.format("%.2f ₪", total));
            priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4e3620;");


            pastorder.add(imageView, 0, row);
            GridPane.setHalignment(imageView, HPos.CENTER);
            pastorder.add(nameLabel, 1, row);
            GridPane.setHalignment(nameLabel, HPos.CENTER);
            pastorder.add(quantityLabel, 2, row);
            GridPane.setHalignment(quantityLabel, HPos.CENTER);
            pastorder.add(priceLabel, 3, row);
            GridPane.setHalignment(priceLabel, HPos.CENTER);
            row++;
        }
    }

    @FXML
    void gobackk(ActionEvent event) {
        Button source = (Button) event.getSource(); // הכפתור שהפעיל את האירוע
        Stage stage = (Stage) source.getScene().getWindow(); // מקבל את החלון מהכפתור
        stage.close();

    }



    @FXML
        void initialize() {
            assert cart != null : "fx:id=\"cart\" was not injected: check your FXML file 'spesifcpastorder.fxml'.";
            assert goback != null : "fx:id=\"goback\" was not injected: check your FXML file 'spesifcpastorder.fxml'.";
            assert pastorder != null : "fx:id=\"myorder\" was not injected: check your FXML file 'spesifcpastorder.fxml'.";

        }

    }

