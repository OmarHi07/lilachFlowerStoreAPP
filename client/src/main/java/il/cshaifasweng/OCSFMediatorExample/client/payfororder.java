package il.cshaifasweng.OCSFMediatorExample.client;

import com.mysql.cj.Session;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.CartProduct;
import il.cshaifasweng.OCSFMediatorExample.entities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.transaction.Transaction;

import static il.cshaifasweng.OCSFMediatorExample.client.CurrentCustomer.getCurrentCustomer;
import static il.cshaifasweng.OCSFMediatorExample.client.CurrentCustomer.getCurrentUser;
import static il.cshaifasweng.OCSFMediatorExample.client.Ordercart.cartItems;
import static il.cshaifasweng.OCSFMediatorExample.client.Ordercart.totalprice;
import static il.cshaifasweng.OCSFMediatorExample.client.PrimaryController.*;

public class payfororder {

        @FXML
        Label cardnumErrorLabel;

        @FXML
        Label cvvErrorLabel;

        @FXML
        Label dateErrorLabel;


        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;


        @FXML
        private Text payattention;

        @FXML
        private Button cancel;

        @FXML
        private TextField cardnum;

        @FXML
        private PasswordField cvv;

        @FXML
        private DatePicker date;

        @FXML
        private Button delivery;

        @FXML
        private Button goback;

        @FXML
        private Button greeting;

        @FXML
        private TextField greetingmessage;

        @FXML
        private TextField locationn;

        @FXML
        private Button payy;

        @FXML
        private Button pickup;

        @FXML
        private Label totaal;

        @FXML
        void cancel(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cancel Payment");
                alert.setHeaderText("Are you sure you want to exit?");
                alert.setContentText("All entered information will be lost.");

                ButtonType yesButton = new ButtonType("Yes, cancel", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();



                if (result.isPresent() && result.get() == yesButton) {
                        try {
                                // טוען את מסך ה‑Ordercart
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/Ordercart.fxml"));
                                Parent root = loader.load();

                                // יוצר סצנה חדשה
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setTitle("Ordercart"); // הכותרת
                                stage.setScene(scene);
                                stage.show();

                                // סוגר את החלון הנוכחי
                                ((Stage) goback.getScene().getWindow()).close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                // סגירת החלון הנוכחי
                Stage stage = (Stage) cancel.getScene().getWindow(); // מקבל את החלון מהכפתור
                stage.close();
                // If "No" is clicked, do nothing
        }


        @FXML
        void delivery(ActionEvent event) {
                locationn.setVisible(true);
                pickup.setVisible(false);

                Branch selectedBranch = CurrentCustomer.getSelectedBranch();
                if (selectedBranch != null) {
                        payattention.setText("Note: Delivery is only available within " + selectedBranch + ".");
                }
        }


        @FXML
        void greeting(ActionEvent event) {
                greetingmessage.setVisible(true);
                greetingmessage.setPromptText("Add here your greeting");
        }

        @FXML
        void handleLocationSelection(ActionEvent event) {

        }



        private void clearFormFields() {
                cardnum.clear();
                cvv.clear();
                date.setValue(null);
                locationn.clear();
                greetingmessage.clear();
                cartItems.clear();

                cardnumErrorLabel.setVisible(false);
                cvvErrorLabel.setVisible(false);
                dateErrorLabel.setVisible(false);
        }

        private void saveconfirm() {
        // יצירת אובייקט הזמנה
            Branch selectedBranch = CurrentCustomer.getSelectedBranch();


        }



        @FXML
        void payy(ActionEvent event) {
                boolean isValid = true;

                String cardNumber = cardnum.getText().trim();
                String cvvText = cvv.getText().trim();
                LocalDate expDate = date.getValue();
                String locationText = locationn.getText().trim();

                // בדיקת שדות ריקים
                if (cardNumber.isEmpty() || cvvText.isEmpty() || expDate == null ||
                        (delivery.equals(null) && locationText.isEmpty())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Missing Information");
                        alert.setHeaderText("Please complete all required fields.");
                        alert.setContentText("Make sure to enter card details, expiration date, and delivery location if needed.");
                        alert.showAndWait();
                        return;
                }

                // בדיקה: מספר כרטיס 12–19 ספרות
                if (!cardNumber.matches("\\d{16}")) {
                        cardnumErrorLabel.setText("Card number must be 12–19 digits.");
                        cardnumErrorLabel.setVisible(true);
                        isValid = false;
                } else {
                        cardnumErrorLabel.setVisible(false);
                }

                // בדיקה: CVV בדיוק 3 ספרות
                if (!cvvText.matches("\\d{3}")) {
                        cvvErrorLabel.setText("CVV must be exactly 3 digits.");
                        cvvErrorLabel.setVisible(true);
                        isValid = false;
                } else {
                        cvvErrorLabel.setVisible(false);
                }

                // בדיקה: תאריך תקף
                if (expDate.isBefore(LocalDate.now())) {
                        dateErrorLabel.setText("Expiration date must be today or in the future.");
                        dateErrorLabel.setVisible(true);
                        isValid = false;
                } else {
                        dateErrorLabel.setVisible(false);
                }



                // אם יש שגיאות – עצור
                if (!isValid) return;

                List<Branch> branches=SimpleClient.getAllBranches();
               //Branch branch = branches.stream().filter(branch1 -> branch1.getAddress().equals(selectedBranch)).findFirst().orElse(null);
                Branch branch = branches.stream().filter(branch1 -> branch1.getAddress().equals("Haifa")).findFirst().orElse(null);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                String currentTime = LocalTime.now().format(timeFormatter);

                //String phone=CurrentCustomer.getCurrentUser().getPhone();

                //Order order = new Order( CurrentCustomer.getCurrentUser(),branch, LocalDate.now().toString(),currentTime,totalprice,  greetingmessage.getText().trim(), "", phone, locationn.getText().trim(), false);
                //order.setProducts(cartItems);

                Order order = new Order( CurrentCustomer.getCurrentUser(),branch, LocalDate.now().toString(),currentTime,totalprice,  greetingmessage.getText().trim(), "", "", locationn.getText().trim(), false);
                order.setProducts(new ArrayList<>(cartItems));
                clearFormFields();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment");
                alert.setHeaderText("Payment Submitted");
                alert.setContentText("Thank you! Your payment is being processed.");
                alert.showAndWait();

                try {
                        SimpleClient.getClient().sendToServer(order);
                        cartItems.clear();

                } catch (IOException e) {
                        e.printStackTrace();
                }
                try {
                        App.setRoot("primary", 900, 730);
                }
                catch (IOException e) {
                        e.printStackTrace();
                }

        }


        @FXML
        void initialize() {
                assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cardnum != null : "fx:id=\"cardnum\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cvv != null : "fx:id=\"cvv\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert delivery != null : "fx:id=\"delivery\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert goback != null : "fx:id=\"goback\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert greeting != null : "fx:id=\"greeting\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert greetingmessage != null : "fx:id=\"greetingmessage\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert locationn != null : "fx:id=\"locationn\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert payy != null : "fx:id=\"payy\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert pickup != null : "fx:id=\"pickup\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert totaal != null : "fx:id=\"totaal\" was not injected: check your FXML file 'payfororder.fxml'.";
                totaal.setText(totalprice + " ₪");
                greetingmessage.setVisible(false);
                locationn.setVisible(false);
        }

        public void gobackk(MouseEvent mouseEvent) {

        }

        public void pickup(){
                delivery.setVisible(false);
                locationn.setVisible(false);
        }
}
