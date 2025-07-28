package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.scene.image.Image;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;


public class ComplaintController {


    @FXML
    private TextArea complaintText;

    public static Order order;

    @FXML
    private Button submitButton;

    @FXML
    private Button backButton;





    @FXML
    void handleSubmit(ActionEvent event) {//this function save the complaint in complaint
        String complaint = complaintText.getText();
        if (complaint == null || complaint.trim().isEmpty()) {//if the complaint is empty or spaces 
            System.out.println("Complaint is empty!");
            return;
        }


        System.out.println("Complaint submitted: " + complaint);//printing success message
        // Later: send complaint to server


        // Create a basic complaint object (without customer/order/branch for now)
        Complain c = new Complain(LocalDate.now(), LocalTime.now(), order, complaint);
        c.setStatus(false);

        try {
            SimpleClient.getClient().sendToServer(c); // Send to server
            System.out.println("Complaint sent to server.");
            //checking if sending the complaint to the server sucesseded
        } catch (IOException e) {
            System.out.println("Failed to send complaint: " + e.getMessage());
        }

        complaintText.setText(null);
    }



    @FXML
    void Back(ActionEvent event) {//to return to the previous screen
        try {
            App.setRoot("connect", 400, 600);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
