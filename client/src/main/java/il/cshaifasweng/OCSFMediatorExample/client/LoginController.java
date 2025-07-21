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

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView backgroundImage;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);


    }

    // התחברות כלקוח
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            LoginRequest request = new LoginRequest(username, password, "customer");
            SimpleClient.getClient().sendToServer("add client");
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

    // מעבר למסך הרשמה
    @FXML
    public void goToSignUp(ActionEvent event) {
        try {
            App.setRoot("SignIn", 600, 400);
        } catch (IOException e) {
            errorLabel.setText("❌ לא ניתן לעבור למסך הרשמה.");
        }
    }

    // טיפול בתגובה מהשרת לאחר התחברות
    @Subscribe
    public void handleLoginResponse(LoginResponse response) {
        System.out.println(">> קיבלתי LoginResponse: " + response.getMessage());

        javafx.application.Platform.runLater(() -> {
            if (response.isSuccess()) {
                errorLabel.setText("✅ " + response.getMessage());
                try {
                    SimpleClient.getClient().sendToServer("add client");
                } catch (IOException e) {
                    errorLabel.setText("failed in sendToServer.");
                }
                try {
                    App.setRoot("Primary", 1006, 750);
                }
                catch (IOException e) {
                    errorLabel.setText("Failed to load main screen.");
                }
            } else {
                errorLabel.setText("❌ " + response.getMessage());
            }
        });
    }
}
