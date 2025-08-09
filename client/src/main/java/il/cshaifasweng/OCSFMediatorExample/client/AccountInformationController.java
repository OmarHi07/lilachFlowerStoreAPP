/**
 * Sample Skeleton for 'AccountInformation.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AccountInformationController {

    @FXML
    private TextField CVV;

    @FXML
    private TextField IDNumber;

    @FXML
    private TextField accountType;

    @FXML
    private TextField cardnumber;

    @FXML
    private TextField email;

    @FXML
    private DatePicker expirtyDate;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField password;

    @FXML
    private TextField phonenumber;

    @FXML
    private TextField username;

    @FXML
    private Button renewSubButton;

    @FXML // fx:id="changePass"
    private Button changePass; // Value injected by FXMLLoader

    @FXML // fx:id="changePayment"
    private Button changePayment; // Value injected by FXMLLoader

    @FXML // fx:id="deleteAccount"
    private Button deleteAccount; // Value injected by FXMLLoader

    @FXML // fx:id="editButton"
    private Button editButton; // Value injected by FXMLLoader

    @FXML // fx:id="goBack"
    private Button goBack; // Value injected by FXMLLoader

    @FXML // fx:id="manageSub"
    private Button manageSub; // Value injected by FXMLLoader

    @FXML // fx:id="saveInfoChanges"
    private Button saveInfoChanges; // Value injected by FXMLLoader

    @FXML // fx:id="savePay"
    private Button savePay; // Value injected by FXMLLoader

    @FXML // fx:id="saveSub"
    private Button saveSub; // Value injected by FXMLLoader

    @FXML // fx:id="visaImage"
    private ImageView visaImage; // Value injected by FXMLLoader

    private int currentUserId;

    @FXML
    private ChoiceBox<String> accountTypeBox;

    private String originalusername ;

    private Customer currentCustomer;


    @FXML
    void initialize() {
        EventBus.getDefault().register(this);

        currentCustomer = CurrentCustomer.getCurrentUser();
        originalusername = currentCustomer.getUsername();

        disableAllEditText();

        accountTypeBox.getItems().addAll("Regular", "Network Account", "Membership");

        PutAllData();
    }


    private void PutAllData(){
        cardnumber.setText(currentCustomer.getCreditCardNumber());
        email.setText(currentCustomer.getEmail());
        CVV.setText(currentCustomer.getCreditCardCVV());
        firstName.setText(currentCustomer.getFirstName());
        lastName.setText(currentCustomer.getLastName());
        password.setText(currentCustomer.getPassword());
        phonenumber.setText(currentCustomer.getPhone());
        username.setText(currentCustomer.getUsername());
        originalusername = currentCustomer.getUsername();
        System.out.println(currentCustomer.getUsername());
        System.out.println(originalusername);
        IDNumber.setText(currentCustomer.getIdentifyingNumber());
        String expiryStr = currentCustomer.getCreditCardExpiration();

        switch (currentCustomer.getCustomerType()) {
            case 1 -> { accountType.setText("Regular");          accountTypeBox.setValue("Regular"); }
            case 2 -> { accountType.setText("Network Account");  accountTypeBox.setValue("Network Account"); }
            case 3 -> { accountType.setText("Membership");  accountTypeBox.setValue("Membership"); }
            default -> { accountType.setText("Membership");      accountTypeBox.setValue("Membership"); }
        }

        if (expiryStr != null && !expiryStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate expiryDate = LocalDate.parse(expiryStr, formatter);
                expirtyDate.setValue(expiryDate);
            } catch (Exception e) {
                e.printStackTrace();
                // אפשר להוסיף התראה למשתמש או טיפול בשגיאה
            }
        }


    }

    private void disableAllEditText() {
        cardnumber.setEditable(false);
        CVV.setEditable(false);
        expirtyDate.setDisable(true);

        password.setEditable(false);

        firstName.setEditable(false);
        lastName.setEditable(false);
        email.setEditable(false);
        phonenumber.setEditable(false);
        username.setEditable(false);
        IDNumber.setEditable(false);

        accountTypeBox.setDisable(true);
        accountType.setEditable(false);

        renewSubButton.setDisable(true);
    }

    private void  updateCurrentCustomer(){
        CurrentCustomer.getCurrentUser().setFirstName(firstName.getText());
        CurrentCustomer.getCurrentUser().setLastName(lastName.getText());
        CurrentCustomer.getCurrentUser().setEmail(email.getText());
        CurrentCustomer.getCurrentUser().setPhone(phonenumber.getText());
        CurrentCustomer.getCurrentUser().setUsername(username.getText());
        CurrentCustomer.getCurrentUser().setPassword(password.getText());
        CurrentCustomer.getCurrentUser().setCreditCardNumber(cardnumber.getText());
        CurrentCustomer.getCurrentUser().setCreditCardCVV(CVV.getText());

        if (expirtyDate.getValue() != null) {
            CurrentCustomer.getCurrentUser().setCreditCardExpiration(expirtyDate.getValue().toString());  // ✅ added
        }



        currentCustomer.setFirstName(firstName.getText());
        currentCustomer.setLastName(lastName.getText());
        currentCustomer.setEmail(email.getText());
        currentCustomer.setPhone(phonenumber.getText());
        currentCustomer.setUsername(username.getText());
        currentCustomer.setPassword(password.getText());
        currentCustomer.setCreditCardNumber(cardnumber.getText());
        currentCustomer.setCreditCardCVV(CVV.getText());

        if (expirtyDate.getValue() != null) {
            currentCustomer.setCreditCardExpiration(expirtyDate.getValue().toString());  // ✅ added
        }



    }


    @FXML
    void renewSub(ActionEvent event) {
        if (CurrentCustomer.getCurrentUser().getCustomerType() != 3) {
            showAlert(Alert.AlertType.INFORMATION, "Not a Member",
                    "You can only renew if your account type is Membership.");
            return;
        }



        LocalDate start = currentCustomer.getMembershipStartDate();
        if (start == null) {
            // No start date: treat as expired/missing -> allow setting now
            start = LocalDate.now().minusYears(1); // force 'expired' state
        }

        LocalDate expiry = start.plusYears(1);

        // Still active? block and tell user how long is left
        if (LocalDate.now().isBefore(expiry)) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
            showAlert(Alert.AlertType.INFORMATION, "Membership Active",
                    "Your membership is still active.\n" +
                            "You can renew on or after " + expiry + " (" + daysLeft + " day" + (daysLeft == 1 ? "" : "s") + " left).");
            return;
        }

        // Build the request to renew: keep accType=3 and reset membershipStartDate to today
        UpdateUserRequest req = new UpdateUserRequest(
                "customer",                    // role
                originalusername,              // originalUsername
                "", "", "", "", "",            // newUsername, firstName, lastName, email, phone
                "",                            // password
                "", 0, "",                     // worker fields
                "", "",                        // workerPassword, workerNewUsername
                "", "", "",                    // cardNumber, expiry, cvv (unchanged)
                3,                             // accType stays Membership
                currentCustomer.getId()        // userId
        );


        req.setMembershipStartDate(LocalDate.now());

        // Optimistic UI: prevent double-clicks while sending
        renewSubButton.setDisable(true);

        try {
            SimpleClient.getClient().sendToServer(req);
        } catch (Exception e) {
            renewSubButton.setDisable(false);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Network Error", "Could not send renewal request. Try again.");
        }
    }




    @Subscribe
    public void handleUpdateResponse(UpdateUserResponse response) {
        Platform.runLater(() -> {
            Alert alert = new Alert(response.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle("Update Status");
            alert.setHeaderText(null);
            alert.setContentText(response.getMessage());
            alert.showAndWait();


            if (response.isSuccess()) {
                originalusername = username.getText();  // Update the reference for future saves
                currentCustomer.setUsername(originalusername);

                int newType;
                if (accountTypeBox.getValue().equals("Regular")) {
                    newType = 1;
                }
                else if (accountTypeBox.getValue().equals("Network Account")) {
                    newType = 2;
                }
                else {
                    newType = 3;
                }
                if (currentCustomer.getCustomerType() != 3 && newType == 3) {
                    currentCustomer.setCustomerType(3);
                    CurrentCustomer.getCurrentUser().setCustomerType(3);
                    currentCustomer.setMembershipStartDate(LocalDate.now());
                    CurrentCustomer.getCurrentUser().setMembershipStartDate(LocalDate.now());
                }


                updateCurrentCustomer();
                currentCustomer = CurrentCustomer.getCurrentUser();
                int type = currentCustomer.getCustomerType();
                if (type == 1) {
                    accountTypeBox.setValue("Regular");
                    accountType.setText("Regular");
                }
                else if (type == 2) {
                    accountTypeBox.setValue("Network Account");
                    accountType.setText("Network Account");
                }
                else {
                    accountTypeBox.setValue("Membership");
                    accountType.setText("Membership");
                }

                PutAllData();

            }
            else {
                PutAllData();
            }
        });
    }

    @FXML
    void changePass(ActionEvent event) {

        originalusername = currentCustomer.getUsername();
        password.setEditable(true);
    }

    @FXML
    void changePayment(ActionEvent event) {
        originalusername = currentCustomer.getUsername();
        cardnumber.setEditable(true);
        CVV.setEditable(true);
        expirtyDate.setDisable(false);
    }

    @FXML
    void deleteMyAcc(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Account");
        confirm.setHeaderText("Are you sure you want to delete your account?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    SimpleClient.getClient().sendToServer(new DeleteUserRequest(currentCustomer.getId(), "Customer"));
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Account Deleted");
                    info.setHeaderText(null);
                    info.setContentText("Your account has been successfully deleted.");
                    info.showAndWait().ifPresent(infoResponse -> {
                        try {
                            CurrentCustomer.setCurrentUser(null);
                            CurrentCustomer.setCurrentCustomer(null);
                            App.setRoot("Home", 504,490);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @FXML
    void editInfo(ActionEvent event) {
        // Enable the editable fields
        originalusername = currentCustomer.getUsername();

        firstName.setEditable(true);
        lastName.setEditable(true);
        email.setEditable(true);
        phonenumber.setEditable(true);
        username.setEditable(true);
        IDNumber.setEditable(true);
    }


    @FXML
    void goBack(ActionEvent event) {
        try {
            App.setRoot("Primary", 1006, 750);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void manageSub(ActionEvent event) {
        if (CurrentCustomer.getCurrentUser().getCustomerType() == 3) {
            if (CurrentCustomer.getCurrentUser().getMembershipStartDate() != null) {
                LocalDate expiry = CurrentCustomer.getCurrentUser().getMembershipStartDate().plusYears(1);
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiry);
                boolean canRenew = !LocalDate.now().isBefore(expiry);
                if (canRenew) {
                    renewSubButton.setDisable(false);
                    originalusername = currentCustomer.getUsername();
                    accountTypeBox.setDisable(false);
                    accountType.setEditable(true);
                } else {
                    // Membership still active
                    showAlert(Alert.AlertType.INFORMATION,
                            "Membership Active",
                            "You already have a membership.\n" +
                                    "It will expire in " + daysLeft + " day" + (daysLeft != 1 ? "s" : "") + ".\n" +
                                    "You cannot change or renew until it ends.");
                    renewSubButton.setDisable(true);
                    return;
                }
            }
        } else {
            originalusername = CurrentCustomer.getCurrentUser().getUsername();
            accountTypeBox.setDisable(false);
            accountType.setEditable(true);
        }


    }


    @FXML
    void saveInfo(ActionEvent event) {
        // Collect values
        String role = "customer";
        String newUsernameStr = username.getText();
        String firstNameStr = firstName.getText();
        String lastNameStr = lastName.getText();
        String emailStr = email.getText();
        String passwordStr = password.getText();
        String phoneStr = phonenumber.getText();
        int userId ;
        if(!IDNumber.getText().isEmpty()) {
            userId = Integer.parseInt(IDNumber.getText());
        }
        else{
            userId = currentCustomer.getId();
        }

        // Build request using full constructor
        UpdateUserRequest request = new UpdateUserRequest(
                role,
                originalusername,
                newUsernameStr,
                firstNameStr,
                lastNameStr,
                emailStr,
                phoneStr,
                passwordStr,
                "",     // name (worker)
                0,      // permission
                "",     // branch
                "",     // workerPassword
                "",     // workerNewUsername
                "",     // cardNumber
                "",     // expiry
                "",     // cvv
                0,      // accType
                userId  // userId
        );
        disableAllEditText();

        try {
            SimpleClient.getClient().sendToServer(request);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void savePay(ActionEvent event) {
        String cardNum = cardnumber.getText();
        String expiry = expirtyDate.getValue().toString(); // LocalDate to String
        String cvv = CVV.getText();

        String role = "customer";
        UpdateUserRequest request = new UpdateUserRequest(
                "customer",              // role
                originalusername,        // originalUsername
                "", "", "", "", "",      // newUsername, firstName, lastName, email, phone
                "",                      // password
                "",                      // name (worker)
                0,                       // permission
                "",                      // branch
                "",                      // workerPassword
                "",                      // workerNewUsername
                cardNum,                 // credit card number
                expiry,                  // expiry date
                cvv,                     // CVV
                0,                       // accType (optional update)
                currentCustomer.getId()  // userId (required)
        );



        disableAllEditText();

        try {
            System.out.println("Sending update for: " + originalusername);

            SimpleClient.getClient().sendToServer(request);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void saveSub(ActionEvent event) {
        String selectedType = accountTypeBox.getValue();

        if (selectedType == null || selectedType.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Subscription Update", "Please select an account type.");
            return;
        }

        int accType = 0;
        switch (selectedType) {
            case "Membership":
                accType = 3;
                break;
            case "Network Account":
                accType = 2;
                break;
            default:
                accType = 1; // Default to Regular
        }

        int userId = currentCustomer.getId();

        UpdateUserRequest request = new UpdateUserRequest(
                "customer",            // role
                originalusername,      // originalUsername
                "", "", "", "", "",    // newUsername, firstName, lastName, email, phone
                "",                    // password
                "",                    // worker name
                0,                     // permission
                "",                    // branch
                "", "",                // workerPassword, workerNewUsername
                "", "", "",            // cardNumber, expiry, cvv
                accType,               // account type
                userId                 // user ID
        );

        if(accType == 3){
            request.setMembershipStartDate(LocalDate.now());
        }
        else{
            request.setMembershipStartDate(null);
        }
        disableAllEditText();


        try {
            SimpleClient.getClient().sendToServer(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}