package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import il.cshaifasweng.OCSFMediatorExample.entities.DeleteOrder;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import il.cshaifasweng.OCSFMediatorExample.entities.CartProduct;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.Hibernate;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import static il.cshaifasweng.OCSFMediatorExample.client.Ordercart.cartItems;
import static il.cshaifasweng.OCSFMediatorExample.client.Ordercart.totalprice;

public class pastOrders {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button Exit;

    @FXML
    private AnchorPane previes;

    @FXML
    private GridPane ordersbefore;


    @FXML
    void exit(ActionEvent event) {
        EventBus.getDefault().unregister(this);
        try {
            App.setRoot("Ordercart", 1040, 780);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCancelMessage(LocalDateTime deliveryTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(now, deliveryTime);
        long minutesLeft = diff.toMinutes();

        if (minutesLeft < 0) {
            return "Delivery time has already passed – cancellation is not allowed.";
        } else if (minutesLeft < 60) {
            return "The order will be canceled without a refund. Do you want to proceed?";
        } else if (minutesLeft < 180) {
            return "The order will be canceled with a 50% refund. Do you want to proceed?";
        } else {
            return "The order will be canceled with a full refund. Do you want to proceed?";
        }
    }

    private boolean confirmCancel(Order order) {
        LocalDate date = order.getDateReceive();
        LocalTime time = order.getTimeReceive();
        LocalDateTime deliveryTime = LocalDateTime.of(date, time);

        String refundMessage = getCancelMessage(deliveryTime);

        if (Duration.between(LocalDateTime.now(), deliveryTime).toMinutes() < 0) {
            // הזמן כבר עבר – תצוגת הודעה בלבד
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancellation Not Allowed");
            alert.setHeaderText("Too Late to Cancel");
            alert.setContentText(refundMessage);
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            return false; // אין ביטול
        }

        // מותר לבטל – הצג אישור
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Are you sure you want to cancel this order?");
        alert.setContentText(refundMessage);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

     public void gotodelete(Order order){
//       try {
//             SimpleClient.getClient().sendToServer("delete Order,"+ CurrentCustomer.getCurrentUser().getId()+","+order.getId());
//       } catch (IOException e) {
//             e.printStackTrace();
//       }
         LocalDate date = order.getDateReceive();
         LocalTime time = order.getTimeReceive();
         LocalDateTime deliveryTime = LocalDateTime.of(date, time);
         long minutesLeft = Duration.between(LocalDateTime.now(), deliveryTime).toMinutes();
         double refund = 0;
         if (minutesLeft >= 180) {
                 refund = order.getSum(); // החזר מלא
         } else if (minutesLeft >= 60) {
                 refund = order.getSum() * 0.5; // החזר 50%
         } else {
                 refund = 0; // אין החזר
         }
         try {
             DeleteOrder deleteOrder = new DeleteOrder(order);
             SimpleClient.getClient().sendToServer(deleteOrder);
         }
         catch (IOException e){
             e.printStackTrace();
         }

     }


    @Subscribe
    public void renderPastOrders(List<?> pastOrders) {
        Platform.runLater(() -> {
        boolean allAreOrders = pastOrders.stream().allMatch(o -> o instanceof Order);
        if (!allAreOrders) return;

        List<Order> orders = pastOrders.stream().map(o -> (Order) o).collect(Collectors.toList());

            ordersbefore.getChildren().clear(); // נקה גריד קודם
            ordersbefore.setVgap(15);
            ordersbefore.setHgap(30);
            ordersbefore.setStyle("-fx-padding: 20;");

            int row = 0;

            for (Order order : orders) {
                if (order.getProducts().isEmpty()) continue;

                // --- תמונה ---
                byte[] imgBytes = order.getProducts().get(0).getFlower().getImage();
                ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imgBytes)));
                imageView.setFitWidth(70);
                imageView.setFitHeight(70);
                imageView.setPreserveRatio(true);
                imageView.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("spesifcpastorder.fxml"));
                        Parent root = loader.load();
                        spesifcpastorder controller = loader.getController();
                        controller.setOrder(order);
                        Stage stage = new Stage();
                        stage.setTitle("Order Details");
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                GridPane.setHalignment(imageView, HPos.CENTER);

                // --- מחיר ---
                Label priceLabel = new Label(String.format("%.2f ₪", order.getSum()));
                priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4e3620;");
                GridPane.setHalignment(priceLabel, HPos.CENTER);

                // --- כפתור תלונה ---
                Button complainButton = new Button("Submit Complaint");
                complainButton.setStyle("-fx-background-color: #613b23; -fx-text-fill: white; -fx-font-size: 13px;");
                complainButton.setOnAction(e -> {
                    try{
                        ComplaintController.order = order;
//                        App.setRoot("ComplaintScreen", 1040, 780);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ComplaintScreen.fxml"));
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Complaint Screen");
                        stage.setScene(new Scene(root, 800, 780));
                        stage.initModality(Modality.APPLICATION_MODAL); // חוסם את החלון שמתחת עד שסוגרים את זה
                        stage.show();
                    }
                    catch (IOException e1){
                        e1.printStackTrace();
                    }
                    System.out.println("Complaint clicked for order ID: " + order.getId());
                });
                GridPane.setHalignment(complainButton, HPos.CENTER);

                // --- תאריך הזמנה ---
                LocalDate date = (order.getDateOrder() != null ? order.getDateOrder() : null);
                String dateText = date == null ? "unknown" : date.toString();
                Label dateLabel = new Label("Ordered on: " + dateText);
                dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6b4c3b;");
                GridPane.setHalignment(dateLabel, HPos.CENTER);

                // --- כפתור ביטול ---
                Button cancelButton = new Button("Cancel Order");
                cancelButton.setStyle("-fx-background-color: #613b23; -fx-text-fill: white; -fx-font-size: 13px;");

                cancelButton.setOnAction(e -> {
                    if (confirmCancel(order)) {
                        System.out.println("Canceled order ID: " + order.getId());
                        gotodelete(order);
                    }
                });




//                boolean canCancel = false;
//                try {
//                    if (order.getDateOrder() != null ) {
//                        LocalDate today = LocalDate.now();
//                        LocalDate receiveDate = order.getDateOrder();
//                        long daysLeft = ChronoUnit.DAYS.between(today, receiveDate);
//                        daysLeft = Math.max(daysLeft, 0); // לא יהיה שלילי
//                        System.out.println("Order " + order.getId() + " daysLeft = " + daysLeft);
//                        canCancel = daysLeft <= 3;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                // --- הוספה לגריד ---
                ordersbefore.add(imageView, 0, row);
                ordersbefore.add(priceLabel, 1, row);
                ordersbefore.add(complainButton, 2, row);
                ordersbefore.add(dateLabel, 3, row);

//                if (canCancel) {
                    ordersbefore.add(cancelButton, 4, row);
                    GridPane.setHalignment(cancelButton, HPos.CENTER);
//               }

                row++;
            }
        });
    }


    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        assert Exit != null : "fx:id=\"Exit\" was not injected: check your FXML file 'pastOrders.fxml'.";
        assert previes != null : "fx:id=\"cart\" was not injected: check your FXML file 'pastOrders.fxml'.";
        assert ordersbefore != null : "fx:id=\"ordersbefore\" was not injected: check your FXML file 'pastOrders.fxml'.";
//        try{
//            SimpleClient.getClient().sendToServer("Give Orders ," + CurrentCustomer.getCurrentUser().getId());
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
        renderPastOrders(SimpleClient.getAllOrders());
    }

}
