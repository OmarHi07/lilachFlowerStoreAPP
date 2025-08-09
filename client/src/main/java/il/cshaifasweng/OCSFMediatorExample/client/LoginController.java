package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginResponse;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

//import java.awt.event.MouseEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    @FXML // fx:id="CustomerLog"
    private Label CustomerLog; // Value injected by FXMLLoader

    @FXML // fx:id="EmployeeLog"
    private Label EmployeeLog; // Value injected by FXMLLoader

    @FXML // fx:id="LogInImages"
    private ImageView LogInImages; // Value injected by FXMLLoader

    @FXML // fx:id="customerLogInButton"
    private Button customerLogInButton; // Value injected by FXMLLoader

    @FXML // fx:id="employeeLogInButton"
    private Button employeeLogInButton; // Value injected by FXMLLoader

    @FXML // fx:id="goBackButton"
    private Button goBackButton; // Value injected by FXMLLoader

    @FXML // fx:id="goTosignUp"
    private Label goTosignUpLabel; // Value injected by FXMLLoader

    @FXML // fx:id="showPasswordButton"
    private Button showPasswordButton; // Value injected by FXMLLoader

    @FXML // fx:id="visiblePasswordField"
    private TextField visiblePasswordField; // Value injected by FXMLLoader

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;


    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);

        Image image = new Image(getClass().getResource("/Images/LogIn.jpeg").toExternalForm());
        LogInImages.setImage(image);
        visiblePasswordField.setVisible(false);

        if (Session.selectedRole.equals("customer")) {
            CustomerLog.setVisible(true);
            customerLogInButton.setVisible(true);
            EmployeeLog.setVisible(false);
            employeeLogInButton.setVisible(false);
        } else {
            CustomerLog.setVisible(false);
            customerLogInButton.setVisible(false);
            EmployeeLog.setVisible(true);
            employeeLogInButton.setVisible(true);
        }

    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        App.setRoot("Home",510,470);
    }

    @FXML
    void goToSignUp(MouseEvent event) {
        try {
            App.setRoot("SignIn",680,560);
        } catch (IOException e) {
            errorLabel.setText("❌ לא ניתן לעבור למסך הרשמה.");
        }
    }

    // התחברות כלקוח
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            LoginRequest request = new LoginRequest(username, password, "customer");
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            errorLabel.setText("❌ שגיאת תקשורת עם השרת.");
            e.printStackTrace();
        }
    }

    // התחברות כעובד
    @FXML
    public void handleEmployeeLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            LoginRequest request = new LoginRequest(username, password, "employee");
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            errorLabel.setText("❌ שגיאת תקשורת עם השרת.");
            e.printStackTrace();
        }
    }


    // טיפול בתגובה מהשרת לאחר התחברות
    @Subscribe
    public void handleLoginResponse(LoginResponse response) {
        System.out.println(">> קיבלתי LoginResponse: " + response.getMessage());
        javafx.application.Platform.runLater(() -> {
            if (response.isSuccess()) {
                if (CurrentCustomer.getCurrentUser() != null) {
                    if (CurrentCustomer.getCurrentUser().isBlocked()) {
                        errorLabel.setText("Your account has been blocked.");
                    } else {
                        errorLabel.setText("✅ " + response.getMessage());
                        try {
                            App.setRoot("primary", 1120, 760);
                        } catch (IOException e) {
                            errorLabel.setText("Failed to load main screen.");
                        }
                    }
                } else if (CurrentCustomer.getCurrentEmployee() != null) {
                    if (CurrentCustomer.getCurrentEmployee().isBlocked()) {
                        errorLabel.setText("Your account has been blocked.");
                    } else {
                        errorLabel.setText("✅ " + response.getMessage());
                        try {
                            App.setRoot("primary", 1120, 760);
                        } catch (IOException e) {
                            errorLabel.setText("Failed to load main screen.");
                        }
                    }
                }
            }
            else {
                errorLabel.setText("❌ " + response.getMessage());
            }
        });
    }


    @FXML
    void showPassword(ActionEvent event) {
        if (visiblePasswordField.isVisible()) {
            // Hide visible text, show password field
            passwordField.setText(visiblePasswordField.getText());
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            showPasswordButton.setText("👁");
        } else {
            // Show visible text field with password text
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            showPasswordButton.setText("🙈");
        }
    }

}
