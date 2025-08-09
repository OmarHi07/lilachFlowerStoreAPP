package il.cshaifasweng.OCSFMediatorExample.client;

import com.mysql.cj.Session;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.CartProduct;
import il.cshaifasweng.OCSFMediatorExample.entities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import jdk.jfr.Event;

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
        private CheckBox someoneelse;

        @FXML
        private CheckBox forme;

        @FXML
        private TextField phoneapply;


        @FXML
        private TextField emailapply;


        @FXML
        private Spinner<String> timeapply;

        @FXML
        private Text payattention;

        @FXML
        private Button cancel;

        @FXML
        private TextField cardnum;

        @FXML
        private PasswordField cvv;

        @FXML
        private DatePicker dateapply;
        @FXML
        private Text deliverycost;

        @FXML
        private Text fourtyy;

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
        private ChoiceBox<String> creditchoice;

        @FXML
        private ChoiceBox<String> cardpaychoice;
        @FXML
        private Text membershipdicount;

        @FXML
        private Text membershipmessage;

        @FXML
        private TextField nameaplly;

        @FXML
        private Text name;

        @FXML
        private Text email;

        @FXML
        private Text phone;

        @FXML
        private DatePicker date;

        @FXML
        private TextField usecredit;

        @FXML
        private Text use;

        private double creditUsed = 0;
        private boolean creditApplied = false;
        private boolean deliveryApplied = false;

        @FXML
        void datecard(ActionEvent event) {
                LocalDate cardDate = date.getValue();
        }



        @FXML
        void nameaplly(ActionEvent event) {
                String name = nameaplly.getText();
        }

        @FXML
        void phoneapply(MouseEvent event) {
                String phone = phoneapply.getText();
        }
        @FXML
        void emailapply(ActionEvent event) {
                String email = emailapply.getText();
                if (email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                        System.out.println("Valid email: " + email);
                        // שמור או השתמש באימייל
                } else {
                        System.out.println("Invalid email format!");
                        // אפשר להציג Alert או Label עם שגיאה
                }

        }
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
                                App.setRoot("Ordercart", 1040, 780);
                                // סגירת החלון הנוכחי
//                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                                stage.close();
                                // If "No" is clicked, do nothing
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                else{
                        alert.close();
                }

        }


        @FXML
        void delivery(ActionEvent event) {

                fourtyy.setVisible(true);
                deliverycost.setVisible(true);
                locationn.setVisible(true);

                String selectedBranch = CurrentCustomer.getSelectedBranch().getAddress();
                payattention.setVisible(true);
                if (selectedBranch != null) {
                        payattention.setText("Note: Delivery is only available within " + selectedBranch + ".");
                }
                // הדלקת pickup
                delivery.setStyle("-fx-background-color:  #613b23; -fx-text-fill: white;");

                // כיבוי delivery
                pickup.setStyle("-fx-background-color:#a18674; -fx-text-fill: white");

                if(!deliveryApplied){
                    totalprice=totalprice+40;
                    deliveryApplied=true;
                    totaal.setText(totalprice + " ₪");
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

        @FXML
        void forme(ActionEvent event) {
                emailapply.setVisible(false);
                phoneapply.setVisible(false);
                email.setVisible(false);
                phone.setVisible(false);
                someoneelse.setSelected(false);
        }
        @FXML
        void someoneelse(ActionEvent event) {
                emailapply.setVisible(true);
                phoneapply.setVisible(true);
                email.setVisible(true);
                phone.setVisible(true);
                forme.setSelected(false);
        }



        private void clearFormFields() {
                cardnum.clear();
                cvv.clear();
                dateapply.setValue(null);
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


        private boolean validateFormFields() {
                boolean isValid = true;
                LocalDate deliveryDate = dateapply.getValue();
                String selectedTime = timeapply.getValue();
                String nameText = nameaplly.getText().trim();

                String cardNumber ;
                String cvvText ;
                LocalDate expDate ;
                if(cardpaychoice.getValue()== null){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Missing Information");
                        alert.setHeaderText("Please complete all required fields.");
                        alert.setContentText("Make sure to enter card details.");
                        alert.showAndWait();
                        return false;
                }
                if ("Pay with new card".equals(cardpaychoice.getValue())) {
                        cardNumber = cardnum.getText().trim();
                        cvvText = cvv.getText().trim();
                        expDate = date.getValue();  // נבחר מה-DatePicker
                } else {
                        cardNumber = CurrentCustomer.getCurrentUser().getCreditCardNumber();
                        cvvText = CurrentCustomer.getCurrentUser().getCreditCardCVV();
                        String expiryRaw = CurrentCustomer.getCurrentUser().getCreditCardExpiration();

                        try {
                                // נסה כפורמט MMYY
                                int month = Integer.parseInt(expiryRaw.substring(0, 2));
                                int year = Integer.parseInt("20" + expiryRaw.substring(2));
                                expDate = LocalDate.of(year, month, 1);
                        } catch (NumberFormatException e) {
                                // אם נכשל – נסה כפורמט yyyy-MM-dd
                                try {
                                        expDate = LocalDate.parse(expiryRaw);
                                } catch (Exception ex) {
                                        dateErrorLabel.setText("Invalid expiration format.");
                                        dateErrorLabel.setVisible(true);
                                        return false; // עצור את האימות
                                }
                        }
                }

                // שדות חובה
                if ( (locationn.isVisible() && locationn.getText().trim().isEmpty())||cardNumber.isEmpty() || cvvText.isEmpty() || expDate == null || deliveryDate == null || selectedTime == null || nameText.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Missing Information");
                        alert.setHeaderText("Please complete all required fields.");
                        alert.setContentText("Make sure to enter card details, expiration date, and delivery location if needed.");
                        alert.showAndWait();
                        return false;
                }
                if (deliveryDate.equals(LocalDate.now()) && !selectedTime.equals("Now!")) {
                        LocalTime selectedLocalTime = LocalTime.parse(selectedTime);
                        if (selectedLocalTime.isBefore(LocalTime.now())) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Invalid Delivery Time");
                                alert.setHeaderText("Selected delivery time has already passed.");
                                alert.setContentText("Please choose a valid time later than now.");
                                alert.showAndWait();
                                return false;
                        }
                }



                if (!cardNumber.matches("\\d{16}")) {
                        cardnumErrorLabel.setText("Card number must be 12–19 digits.");
                        cardnumErrorLabel.setVisible(true);
                        isValid = false;
                        return false;
                } else {
                        cardnumErrorLabel.setVisible(false);
                }

                // בדיקה: CVV בדיוק 3 ספרות
                if (!cvvText.matches("\\d{3}")) {
                        cvvErrorLabel.setText("CVV must be exactly 3 digits.");
                        cvvErrorLabel.setVisible(true);
                        isValid = false;
                        return false;
                } else {
                        cvvErrorLabel.setVisible(false);
                }

                // בדיקה: תאריך תקף
                if (expDate.isBefore(LocalDate.now())) {
                        dateErrorLabel.setText("Expiration date must be today or in the future.");
                        dateErrorLabel.setVisible(true);
                        isValid = false;
                        return false;
                } else {
                        dateErrorLabel.setVisible(false);
                }

                if(dateapply.getValue().isBefore(LocalDate.now())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Please complete all required fields.");
                        alert.setContentText("Make sure to enter DATE RECIVE in the future.");
                        alert.showAndWait();
                        return false;
                }

                if (!someoneelse.isSelected() && !forme.isSelected()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Missing Information");
                        alert.setHeaderText("Please complete all required fields.");
                        alert.setContentText("Make sure to enter all the required fields.");
                        alert.showAndWait();
                        return false;
                }
                if(someoneelse.isSelected()) {
                        if (emailapply == null || phoneapply == null) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Missing Information");
                                alert.setHeaderText("Please complete all required fields.");
                                alert.setContentText("Make sure to enter email/phone details");
                                alert.showAndWait();
                                return false;
                        }

                        if (!emailapply.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Invalid Email");
                                alert.setHeaderText("The email format is incorrect.");
                                alert.setContentText("Please enter a valid email address.");
                                alert.showAndWait();
                                return false;
                        }
                        if (phoneapply.getText().matches("^[+]{1}[0-9]{10}$")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Invalid Phone Number");
                        }
                }
                return true;
        }


        @FXML
        void payy(ActionEvent event) throws IOException {

                boolean isValid = true;
                // אם יש שגיאות – עצור
                if (!isValid) return;
                if(!validateFormFields()) {
                        return;
                }

                List<Branch> branches = SimpleClient.getAllBranches();
                Branch branch = branches.stream().filter(branch1 -> branch1.getAddress().equals(CurrentCustomer.getSelectedBranch().getAddress())).findFirst().orElse(null);


                String phoneReceiver = "";
                String emailReceiver = "";

                if (someoneelse.isSelected()) {
                        phoneReceiver = phoneapply.getText().trim();
                        emailReceiver = emailapply.getText().trim();
                }
                else {
                        phoneReceiver = CurrentCustomer.getCurrentUser().getPhone();
                        emailReceiver = CurrentCustomer.getCurrentUser().getEmail();
                }


                LocalDate dateReceive ;
                LocalTime timeReceive ;
                if (timeapply.getValue().equals("Now!")) {
                        LocalTime now = LocalTime.now();
                        if (now.isAfter(LocalTime.of(14, 0))){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Unavailable Time");
                                alert.setHeaderText("Too late for same-day delivery");
                                alert.setContentText("Same-day delivery is only available before 14:00.");
                                alert.showAndWait();
                                return;
                        }
                        LocalDateTime nowPlus3 = LocalDateTime.now().plusHours(3);
                        dateReceive = nowPlus3.toLocalDate();
                        timeReceive = nowPlus3.toLocalTime();
                } else {
                        dateReceive = dateapply.getValue();
                        timeReceive = LocalTime.parse(timeapply.getValue()); // כמו "13:00"
                }

                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();
                LocalDate dateString = currentDate; // פורמט ISO: "2025-07-29"


                Order order = new Order(
                        CurrentCustomer.getCurrentUser(),
                        branch,
                        currentDate,
                        currentTime,
                        dateReceive,
                        timeReceive,
                        totalprice,
                        greetingmessage.getText().trim(),
                        nameaplly.getText().trim(),
                        phoneReceiver,
                        locationn.getText().trim(),
                        false
                );

                order.setProducts(new ArrayList<>(cartItems));
                clearFormFields();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment");
                alert.setHeaderText("Payment Submitted");
                alert.setContentText("Thank you! Your payment is being processed.");
                alert.showAndWait();
                CurrentCustomer.getCurrentUser().setCredit(  CurrentCustomer.getCurrentUser().getCredit() - creditUsed);
                try{
                        SimpleClient.getClient().sendToServer("credit" + "," + CurrentCustomer.getCurrentUser().getId() + "," + (CurrentCustomer.getCurrentUser().getCredit()));
                }
                catch(Exception e){
                        e.printStackTrace();
                }
                try {
                        SimpleClient.getClient().sendToServer(order);
                        cartItems.clear();

                } catch (IOException e) {
                        e.printStackTrace();
                }
                try {
                        App.setRoot("primary", 1040, 780);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        private void showError(String msg) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Credit Use");
                alert.setHeaderText("Credit Use Error");
                alert.setContentText(msg);
                alert.showAndWait();
                usecredit.clear();
        }


        @FXML
        void usecredit(ActionEvent event) {
                String input = usecredit.getText().trim();


                try {
                        double inputCredit = Double.parseDouble(input);
                        double availableCredit = CurrentCustomer.getCurrentUser().getCredit();
                        double priceToPay = (double) totalprice;

                        if (inputCredit <= 0) {
                                showError("Credit must be positive.");
                                return;
                        }

                        if (inputCredit > availableCredit) {
                                showError("You don't have enough credit.");
                                return;
                        }

                        if (inputCredit > priceToPay) {
                                showError("Credit can't exceed total price.");
                                return;
                        }

                        creditUsed = inputCredit;
                        double newtotal= totalprice -inputCredit;
                        totaal.setText( newtotal + "₪");

                } catch (NumberFormatException e) {
                        showError("Please enter a valid number.");
                }
        }

        @FXML
        void initialize () {
                assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cardnum != null : "fx:id=\"cardnum\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cardnumErrorLabel != null : "fx:id=\"cardnumErrorLabel\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cardpaychoice != null : "fx:id=\"cardpaychoice\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert creditchoice != null : "fx:id=\"creditchoice\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cvv != null : "fx:id=\"cvv\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert cvvErrorLabel != null : "fx:id=\"cvvErrorLabel\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert dateErrorLabel != null : "fx:id=\"dateErrorLabel\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert dateapply != null : "fx:id=\"dateapply\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert delivery != null : "fx:id=\"delivery\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert emailapply != null : "fx:id=\"emailapply\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert forme != null : "fx:id=\"forme\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert greeting != null : "fx:id=\"greeting\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert greetingmessage != null : "fx:id=\"greetingmessage\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert locationn != null : "fx:id=\"locationn\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert membershipdicount != null : "fx:id=\"membershipdicount\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert membershipmessage != null : "fx:id=\"membershipmessage\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert nameaplly != null : "fx:id=\"nameaplly\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert payattention != null : "fx:id=\"payattention\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert payy != null : "fx:id=\"payy\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert phone != null : "fx:id=\"phone\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert phoneapply != null : "fx:id=\"phoneapply\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert pickup != null : "fx:id=\"pickup\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert someoneelse != null : "fx:id=\"someoneelse\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert timeapply != null : "fx:id=\"timeapply\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert totaal != null : "fx:id=\"totaal\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert use != null : "fx:id=\"use\" was not injected: check your FXML file 'payfororder.fxml'.";
                assert usecredit != null : "fx:id=\"usecredit\" was not injected: check your FXML file 'payfororder.fxml'.";
                greetingmessage.setVisible(false);
                locationn.setVisible(false);
                membershipdicount.setVisible(false);
                membershipmessage.setVisible(false);
                emailapply.setVisible(false);
                phoneapply.setVisible(false);
                email.setVisible(false);
                phone.setVisible(false);
                use.setVisible(false);
                usecredit.setVisible(false);
                fourtyy.setVisible(false);
                deliverycost.setVisible(false);


                cardpaychoice.setItems(FXCollections.observableArrayList("Pay with new card", "Use saved card"));
                cardpaychoice.setValue("Pay with new card"); // ברירת מחדל

                cardpaychoice.setOnAction(e -> {
                        if (cardpaychoice.getValue().equals("Use saved card")) {
                                // קבלת פרטי הכרטיס מהמשתמש הנוכחי
                                cardnum.setText(CurrentCustomer.getCurrentUser().getCreditCardNumber());
                                cvv.setText(CurrentCustomer.getCurrentUser().getCreditCardCVV());

                                String expiryRaw = CurrentCustomer.getCurrentUser().getCreditCardExpiration(); // למשל "0727" או "2025-08-01"
                                try {
                                        if (expiryRaw.matches("\\d{4}")) {
                                                int month = Integer.parseInt(expiryRaw.substring(0, 2));
                                                int year = Integer.parseInt("20" + expiryRaw.substring(2));
                                                date.setValue(LocalDate.of(year, month, 1));
                                        } else {
                                                date.setValue(LocalDate.parse(expiryRaw));
                                        }
                                } catch (Exception ex) {
                                        date.setValue(null);
                                }

                                // הסתרת שדות עריכה אם את רוצה
                                cardnum.setEditable(false);
                                cvv.setEditable(false);
                                date.setDisable(true);
                        } else {
                                cardnum.clear();
                                cvv.clear();
                                date.setValue(null);

                                // פתיחת שדות להזנה ידנית
                                cardnum.setEditable(true);
                                cvv.setEditable(true);
                                date.setDisable(false);
                        }
                });


                List<String> allTimes = new ArrayList<>();
                allTimes.add("Now!");
                allTimes.add("09:00");
                allTimes.add("10:00");
                allTimes.add("11:00");
                allTimes.add("12:00");
                allTimes.add("13:00");
                allTimes.add("14:00");

                // קביעת ברירת מחדל
                ObservableList<String> initialTimes = FXCollections.observableArrayList(allTimes);
                SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(initialTimes);
                timeapply.setValueFactory(valueFactory);

                // מאזין לשינוי תאריך
                dateapply.setOnAction(e ->  {
                        LocalDate newDate = dateapply.getValue();
                        if (newDate != null) {
                                ObservableList<String> updatedTimes;

                                if (newDate.equals(LocalDate.now())) {
                                        updatedTimes = FXCollections.observableArrayList(allTimes); // כולל "Now!"
                                } else {
                                        updatedTimes = FXCollections.observableArrayList(allTimes.subList(1, allTimes.size())); // בלי "Now!"
                                }

                                SpinnerValueFactory<String> updatedFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(updatedTimes);
                                timeapply.setValueFactory(updatedFactory);
                        }
                });


                ObservableList<String> creditOptions = FXCollections.observableArrayList();
                creditOptions.add("Do not use credit");

                double creditAmount = CurrentCustomer.getCurrentUser().getCredit();
                if (creditAmount > 0) {
                        creditOptions.add("Use credit (" + creditAmount + "₪)");
                }

                creditchoice.setItems(creditOptions);
                creditchoice.setValue("Do not use credit");

                creditchoice.setOnAction(e -> {
                        String selected = creditchoice.getValue();

                        if (selected != null && selected.equals("Do not use credit")) {
                                // הסתר את שדה ההזנה
                                use.setVisible(false);
                                usecredit.setVisible(false);

                                // החזרת זיכוי אם השתמשו
                                if (creditApplied) {
                                        totalprice += creditUsed;
                                        CurrentCustomer.getCurrentUser().setCredit(
                                                CurrentCustomer.getCurrentUser().getCredit() + creditUsed
                                        );
                                        creditUsed = 0;
                                        creditApplied = false;
                                }
                                totaal.setText(totalprice + " ₪");
                        }
                        else{

                                use.setVisible(true);
                                usecredit.setVisible(true);

                                // איפוס במעבר מחדש
                                usecredit.clear();
                                creditUsed = 0;
                                creditApplied = false;


                        }



                });

                if(CurrentCustomer.getCurrentUser().getCustomerType()==3 && (totalprice>50)){
                        membershipdicount.setVisible(true);
                        membershipmessage.setVisible(true);
                        membershipdicount.setText( String.valueOf((totalprice*(-0.1)))+ " ₪");
                        totalprice = totalprice*0.9;
                }

                totaal.setText(totalprice + " ₪");
        }



        public void pickup (ActionEvent event) {

                fourtyy.setVisible(false);
                deliverycost.setVisible(false);

                locationn.setVisible(false);
                payattention.setVisible(false);
                // הדלקת pickup
                pickup.setStyle("-fx-background-color:  #613b23; -fx-text-fill: white;");

                // כיבוי delivery
                delivery.setStyle("-fx-background-color:#a18674; -fx-text-fill: white");

                if(deliveryApplied){
                        totalprice=totalprice-40;
                        deliveryApplied=false;
                        totaal.setText(totalprice + " ₪");
                }

        }

        public void gobackk(MouseEvent mouseEvent) {
        }


}