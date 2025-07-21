package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.CartProduct;
import il.cshaifasweng.OCSFMediatorExample.entities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class Ordercart {

        @FXML
        AnchorPane gobackk;
        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Label pastOrders;

        @FXML
        private AnchorPane cart;

        @FXML
        private Button conttinue;

        @FXML
        private Button goback;

        @FXML
        private GridPane myorder;

        @FXML
        private Label total;

        @FXML
        private Text shipmember;

        public static ObservableList<CartProduct> cartItems = FXCollections.observableArrayList();
        public static double totalprice;


        private void renderItems() {
                myorder.getChildren().clear();

                // שורת כותרת
                myorder.add(new Label("Image"), 0, 0);
                myorder.add(new Label("Name"), 1, 0);
                myorder.add(new Label("Quantity"), 2, 0);
                myorder.add(new Label("Total"), 3, 0);
                myorder.add(new Label("X"), 4, 0);

                myorder.setVgap(15);
                myorder.setHgap(25);
                myorder.setStyle("-fx-padding: 20;");

                int row = 1; // שורה 0 שמורה לכותרות

                for (CartProduct item : cartItems) {
                        // תמונה
                        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(item.getFlower().getImage())));
                        imageView.setFitWidth(60);
                        imageView.setFitHeight(60);
                        imageView.setPreserveRatio(true);
                        myorder.add(imageView, 0, row);

                        // שם הפרח
                        Label nameLabel = new Label(item.getFlower().getFlowerName());
                        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4e3620;");
                        nameLabel.setAlignment(Pos.CENTER_LEFT);
                        myorder.add(nameLabel, 1, row);

                        // כמות עם כפתורים
                        Button minusButton = new Button("-");
                        Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
                        Button plusButton = new Button("+");

                        quantityLabel.setMinWidth(30);
                        quantityLabel.setAlignment(Pos.CENTER);
                        quantityLabel.setStyle("-fx-font-size: 14px;-fx-text-fill: #4e3620;");

                        minusButton.setStyle("-fx-font-size: 14px;-fx-text-fill: #4e3620;");
                        plusButton.setStyle("-fx-font-size: 14px;-fx-text-fill: #4e3620;");

                        minusButton.setOnAction(e -> {
                                item.setQuantity(item.getQuantity() - 1);
                                if (item.getQuantity() <= 0) {
                                        removeItem(item);
                                } else {
                                        renderItems();
                                }
                        });

                        plusButton.setOnAction(e -> {
                                item.setQuantity(item.getQuantity() + 1);
                                renderItems();
                        });

                        HBox quantityBox = new HBox(5, minusButton, quantityLabel, plusButton);
                        quantityBox.setAlignment(Pos.CENTER_LEFT);
                        myorder.add(quantityBox, 2, row);

                        // מחיר כולל
                        Label totalItemLabel = new Label(String.format("%.2f ₪", item.getTotal()));
                        totalItemLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4e3620;");
                        totalItemLabel.setAlignment(Pos.CENTER); // ממרכז את הטקסט עצמו
                        GridPane.setHalignment(totalItemLabel, HPos.CENTER); // ממרכז את הלייבל בתוך התא
                        myorder.add(totalItemLabel, 3, row);

                        // כפתור מחיקה
                        Button removeButton = new Button("X");
                        removeButton.setStyle("-fx-background-color: #c44a4a; -fx-text-fill:  #4e3620;");
                        removeButton.setOnAction(event -> removeItem(item));
                        myorder.add(removeButton, 4, row);

                        row++;
                }

                updateTotalLabel();
        }



        public void setCartItems(ObservableList<CartProduct> cartItems) {
                cartItems.setAll(cartItems);
        }


                public void addItem(CartProduct item) {
                cartItems.add(item);
                renderItems();
        }


        public void removeItem(CartProduct item) {
                cartItems.remove(item);
                renderItems();
        }


        @FXML
        void conttinue(ActionEvent event) {
                if (cartItems.isEmpty()) {
                        return;
                }
                try {
                        App.setRoot("payfororder", 640, 640);

                } catch (IOException e) {
                        e.printStackTrace();
                        // אפשר להוסיף כאן התראה למשתמש במקרה של שגיאה
                }

        }

        @FXML
        void goback(ActionEvent event) {
                try {
                        App.setRoot("primary", 1040, 780);
                }
                catch (IOException e) {
                        e.printStackTrace();
                }

        }

        @FXML
        void total(MouseEvent event) {
                cartItems.addListener((ListChangeListener<CartProduct>) change -> {
                        renderItems();
                });

        }

        @FXML
        void initialize() {


                assert cart != null : "fx:id=\"cart\" was not injected.";
                assert conttinue != null : "fx:id=\"conttinue\" was not injected.";
                assert goback != null : "fx:id=\"goback\" was not injected.";
                assert myorder != null : "fx:id=\"myorder\" was not injected.";
                assert total != null : "fx:id=\"total\" was not injected.";

                // הכפתור יכבה אם אין פריטים
                conttinue.setDisable(cartItems.isEmpty());

                // כאן "נרשמים" לשינויים
                cartItems.addListener((ListChangeListener<CartProduct>) change -> {
                        renderItems();
                });
                renderItems();

                Object user = CurrentCustomer.getCurrentUser();
                if (user instanceof Customer) {
                        Customer customer = (Customer) user;
                        if (customer.getCustomerType() == 3) {
                                shipmember.setText("As a valued Membership customer, you automatically receive 10% off every order!");
                                shipmember.setVisible(true);
                        } else {
                                shipmember.setVisible(false);
                        }
                }


        }


        private void updateTotalLabel() {
                if (cartItems.isEmpty()) {
                        total.setText("No items");
                        return;
                }

                double totalSum = 0.0;
                for (CartProduct item : cartItems) {
                        totalSum += item.getPrice() * item.getQuantity();
                }

                double discount = 0.0;
                Object user = CurrentCustomer.getCurrentUser();

                if (user instanceof Customer) {
                        Customer customer = (Customer) user;
                        if (customer.getCustomerType() == 3) {
                                discount = totalSum * 0.10;
                                totalSum -= discount;
                        }
                }

                totalprice = totalSum;
                total.setText(String.format("Total: %.2f ₪", totalSum));

        }


        @FXML
        void pastOrders(ActionEvent actionEvent) {
                try {
                App.setRoot("pastorders", 1006, 750);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}


