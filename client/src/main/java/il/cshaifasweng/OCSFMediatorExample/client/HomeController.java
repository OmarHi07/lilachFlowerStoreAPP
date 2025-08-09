/**
 * Sample Skeleton for 'Home.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;


public class HomeController  {

    @FXML
    private Label createAcount;

    @FXML // fx:id="ExitButton"
    private Button ExitButton; // Value injected by FXMLLoader

    @FXML // fx:id="loginAsCustomerButton"
    private Button loginAsCustomerButton; // Value injected by FXMLLoader

    @FXML // fx:id="loginAsEmployeeButton"
    private Button loginAsEmployeeButton; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        try {
            SimpleClient.getClient().sendToServer("add client");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
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

    @FXML
    void createAcount(MouseEvent event) throws IOException {
         App.setRoot("SignIn",680,560);
    }


    @FXML
    void loginAsAguest(ActionEvent event) throws IOException {
        CurrentCustomer.setCurrentCustomer("Guest");
        CurrentCustomer.setCurrentEmployee(null);
        CurrentCustomer.setCurrentUser(null);
        App.setRoot("primary",1120,760);
    }

}
