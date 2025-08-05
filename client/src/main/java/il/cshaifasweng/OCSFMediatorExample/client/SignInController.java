/**
 * Sample Skeleton for 'SignIn.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SignUpResponse;
import il.cshaifasweng.OCSFMediatorExample.entities.UsernameCheckRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.SignUpRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;



public class SignInController {

    private final Map<String, String> accountDescriptions = new HashMap<>();

    @FXML // fx:id="CVV"
    private TextField CVV; // Value injected by FXMLLoader

    @FXML // fx:id="noteLabel"
    private Label noteLabel; // Value injected by FXMLLoader

    @FXML
    private TextField visiblePassword;

    @FXML
    private Button togglePasswordButton;

    @FXML // fx:id="IDNumber"
    private TextField IDNumber; // Value injected by FXMLLoader

    @FXML // fx:id="UserName"
    private TextField UserName; // Value injected by FXMLLoader

    @FXML // fx:id="accountType"
    private ComboBox<String> accountType; // Value injected by FXMLLoader

    @FXML // fx:id="branch"
    private ComboBox<String> branch; // Value injected by FXMLLoader

    @FXML // fx:id="creditCard"
    private TextField creditCard; // Value injected by FXMLLoader

    @FXML // fx:id="email"
    private TextField email; // Value injected by FXMLLoader

    @FXML // fx:id="epirtyDate"
    private DatePicker epirtyDate; // Value injected by FXMLLoader

    @FXML // fx:id="firstName"
    private TextField firstName; // Value injected by FXMLLoader

    @FXML // fx:id="lastName"
    private TextField lastName; // Value injected by FXMLLoader

    @FXML // fx:id="password"
    private PasswordField password; // Value injected by FXMLLoader

    @FXML // fx:id="phoneNumber"
    private TextField phoneNumber; // Value injected by FXMLLoader

    @FXML // fx:id="signInImages"
    private ImageView signInImages; // Value injected by FXMLLoader

    @FXML // fx:id="signUpButton"
    private Button signUpButton; // Value injected by FXMLLoader

    @FXML // fx:id="note"
    private TextArea note; // Value injected by FXMLLoader

    @FXML
    private Label loginRedirectLabel;

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);


        // Add account types to ComboBox
        accountType.getItems().addAll("Regular", "Network Account", "Membership");
        branch.getItems().addAll("Haifa", "TelAviv");

        // Descriptions for each type
        accountDescriptions.put("Regular", "Regular: you can only shop in one of our branches , if you are sure please choose a branch");
        accountDescriptions.put("Network Account", "Network Account: you would get to shop in all our branches.");
        accountDescriptions.put("Membership", "One year subscription : By choosing this type you pay 100 NIS and get 10% discount in every purchase over 50 NIS");

        // Keep both password fields in sync
        password.textProperty().addListener((obs, oldText, newText) -> {
            if (!visiblePassword.isVisible()) {
                visiblePassword.setText(newText);
            }
        });

        visiblePassword.textProperty().addListener((obs, oldText, newText) -> {
            if (visiblePassword.isVisible()) {
                password.setText(newText);
            }
        });

    }

    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        if (visiblePassword.isVisible()) {
            // Hide the text field, show password field
            password.setText(visiblePassword.getText());
            visiblePassword.setVisible(false);
            visiblePassword.setManaged(false);
            password.setVisible(true);
            password.setManaged(true);
            togglePasswordButton.setText("👁");
        } else {
            // Show the text field with password text
            visiblePassword.setText(password.getText());
            visiblePassword.setVisible(true);
            visiblePassword.setManaged(true);
            password.setVisible(false);
            password.setManaged(false);
            togglePasswordButton.setText("🙈");
        }
    }


    @FXML
    void chooseAccType(ActionEvent event) {
        Platform.runLater(() -> {
            String selected = (String) accountType.getSelectionModel().getSelectedItem();
            String desc = accountDescriptions.getOrDefault(selected, "");
            if (selected != null && desc != null) {
                note.setVisible(true);
                noteLabel.setVisible(true);
                note.setText(desc);
            }
            else {
                noteLabel.setVisible(false);
                note.setVisible(false);
                note.setText("");
            }

            if ("Regular".equals(selected)) {
                branch.setVisible(true);
            } else {
                branch.setVisible(false);
                branch.setPromptText("Branch");
            }
        });
    }

    @FXML
    void chooseBranch(ActionEvent event) {
        Platform.runLater(() -> {
            String selected = (String) branch.getSelectionModel().getSelectedItem();
            if (selected != null) {
                branch.setPromptText(selected);
            }
        });
    }


    @FXML
    void signUpFunc(ActionEvent event) {
        String fname = firstName.getText();
        String lname = lastName.getText();
        String id = IDNumber.getText();
        String emailText = email.getText();
        String phone = phoneNumber.getText();
        String username = UserName.getText();
        String pass = password.getText();  // synced with visiblePassword
        String credit = creditCard.getText().replaceAll("\\s+","");
        String cvv = CVV.getText();
        String accType = accountType.getValue();
        String selectedBranch = branch.getValue();
        LocalDate expDate = epirtyDate.getValue();

        // === Empty fields check ===
        if (fname.isEmpty() || lname.isEmpty() || id.isEmpty() || emailText.isEmpty() ||
                phone.isEmpty() || username.isEmpty() || pass.isEmpty() ||
                credit.isEmpty() || cvv.isEmpty() || accType == null || expDate == null ||
                (accType.equals("Regular") && selectedBranch == null)) {

            showAlert("Missing Information", "Please fill in all required fields.");
            return;
        }

        // === CVV must be 3 digits ===
        if (!cvv.matches("\\d{3}")) {
            showAlert("Invalid CVV", "CVV must be exactly 3 digits.");
            return;
        }

        // === Phone number must be 10 digits ===
        if (!phone.matches("\\d{10}")) {
            showAlert("Invalid Phone Number", "Phone number must be exactly 10 digits.");
            return;
        }

        // === Credit card must be 16 digits ===
        if (!credit.matches("\\d{16}")) {
            showAlert("Invalid Credit Card", "Credit card number must be exactly 16 digits.");
            return;
        }

        // === ID number must be 9 digits ===
        if (!id.matches("\\d{9}")) {
            showAlert("Invalid ID Number", "ID number must be exactly 9 digits.");
            return;
        }

        // === Expiry date must be after 2025-07-22 ===
        LocalDate minValidDate = LocalDate.of(2025, 7, 22);
        if (expDate.isBefore(minValidDate)) {
            showAlert("Invalid Expiry Date", "Expiry date must be after July 22, 2025.");
            return;
        }

        // === Username check (via server) ===
        // Send request to server to check if username is taken
        checkUsernameExists(username);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void checkUsernameExists(String username) {
        try {
            SimpleClient.getClient().sendToServer(new UsernameCheckRequest(username));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void gotresponse(UsernameCheckRequest usernameCheckRequest) {
        Platform.runLater(() -> {
            if (usernameCheckRequest.isTaken()) {
                showAlert("Username Taken", "The username is already in use. Please choose another.");
            } else {
                proceedWithSignUp(); // final step
            }
        });
    }

    @Subscribe
    public void gotresponse(SignUpResponse signUpResponse) {
        Platform.runLater(() -> {
            if (signUpResponse.isSuccess()) {
                showSuccessAlert("Success", "Account created successfully!");
                // Optionally switch scene to login
            } else {
                showAlert("Sign-Up Failed", signUpResponse.getMessage());
            }
        });
    }

    private void proceedWithSignUp() {
        // Gather data again (or cache it earlier)
        String fname = firstName.getText();
        String lname = lastName.getText();
        String id = IDNumber.getText();
        String emailText = email.getText();
        String phone = phoneNumber.getText();
        String username = UserName.getText();
        String pass = password.getText();
        String credit = creditCard.getText().replaceAll("\\s+","");
        String cvv = CVV.getText();
        String accType = accountType.getValue();
        String selectedBranch = branch.getValue();
        LocalDate expDate = epirtyDate.getValue();

        // Build and send sign-up request
        SignUpRequest request = new SignUpRequest(fname, lname, id, emailText, phone,
                username, pass, credit, cvv, accType,
                selectedBranch, expDate.toString());

        try {
            SimpleClient.getClient().sendToServer(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // need to implement it
    @FXML
    void goToLogin(MouseEvent event) {
        try {
            App.setRoot("Login", 600, 400);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}