/**
 * Sample Skeleton for 'SignIn.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SignInController {

    @FXML // fx:id="CVV"
    private PasswordField CVV; // Value injected by FXMLLoader

    @FXML // fx:id="IDNumber"
    private TextField IDNumber; // Value injected by FXMLLoader

    @FXML // fx:id="UserName"
    private PasswordField UserName; // Value injected by FXMLLoader

    @FXML // fx:id="accountType"
    private ComboBox<?> accountType; // Value injected by FXMLLoader

    @FXML // fx:id="creditCard"
    private PasswordField creditCard; // Value injected by FXMLLoader

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

    @FXML
    void initialize() {

    }


    @FXML
    void signUpFunc(ActionEvent event) {

    }

}
