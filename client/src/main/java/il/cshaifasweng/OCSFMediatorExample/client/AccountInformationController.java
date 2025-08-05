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
        PutAllData();
        accountTypeBox.getItems().addAll("Regular", "Network Account", "Membership");

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
        IDNumber.setText(currentCustomer.getIdentifyingNumber());
        String expiryStr = currentCustomer.getCreditCardExpiration();
        if(currentCustomer.getCustomerType() == 1 ){
            accountType.setText("Regular");
        }
        else if(currentCustomer.getCustomerType() == 2 ){
            accountType.setText("Network Account");
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
        expirtyDate.setEditable(false);

        password.setEditable(false);

        firstName.setEditable(false);
        lastName.setEditable(false);
        email.setEditable(false);
        phonenumber.setEditable(false);
        username.setEditable(false);
        IDNumber.setEditable(false);

        accountTypeBox.setDisable(true);
        accountType.setEditable(false);
    }


    @Subscribe
    public void handleUpdateResponse(UpdateUserResponse response) {
        Platform.runLater(() -> {
            Alert alert = new Alert(response.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle("Update Status");
            alert.setHeaderText(null);
            alert.setContentText(response.getMessage());
            alert.showAndWait();
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
        expirtyDate.setEditable(true);
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
                            App.setRoot("Home", 230, 500);
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
        originalusername = currentCustomer.getUsername();
        accountTypeBox.setDisable(false);
        accountType.setEditable(true);
    }


    @FXML
    void saveInfo(ActionEvent event) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setRole("customer");
        request.setOriginalUsername(originalusername);
        request.setNewUsername(username.getText());
        request.setFirstName(firstName.getText());
        request.setLastName(lastName.getText());
        request.setEmail(email.getText());
        request.setPassword(password.getText());
        request.setUserId(Integer.parseInt(IDNumber.getText()));
        request.setPhone(phonenumber.getText());

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

        UpdateUserRequest request = new UpdateUserRequest();
        request.setRole("customer");
        request.setOriginalUsername(originalusername);
        request.setCardNumber(cardNum);
        request.setExpiryDate(expiry);
        request.setCvv(cvv);

        disableAllEditText();

        try {
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

        UpdateUserRequest request = new UpdateUserRequest();
        request.setRole("customer");
        request.setOriginalUsername(originalusername);
       // request.setAccountType(selectedType);

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