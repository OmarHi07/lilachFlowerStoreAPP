/**
 * Sample Skeleton for 'Home.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController  {

    @FXML // fx:id="ExitButton"
    private Button ExitButton; // Value injected by FXMLLoader

    @FXML // fx:id="loginAsCustomerButton"
    private Button loginAsCustomerButton; // Value injected by FXMLLoader

    @FXML // fx:id="loginAsEmployeeButton"
    private Button loginAsEmployeeButton; // Value injected by FXMLLoader

    @FXML
    void Exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void loginAsCustomer(ActionEvent event) throws IOException {
        Session.selectedRole = "customer";
        App.setRoot("Login", 600, 400);
    }

    @FXML
    void loginAsEmployee(ActionEvent event) throws IOException {
        Session.selectedRole = "employee";
        App.setRoot("Login", 600, 400);
    }


}
