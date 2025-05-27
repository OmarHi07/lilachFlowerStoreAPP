package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * Sample Skeleton for 'connect.fxml' Controller Class
 */


public class connectController {

    @FXML // fx:id="backgroundImage"
    private ImageView backgroundImage; // Value injected by FXMLLoader


    @FXML // fx:id="connect"
    private Button connect; // Value injected by FXMLLoader

    @FXML // fx:id="connect2"
    private Button connect2; // Value injected by FXMLLoader

    @FXML // fx:id="ipText"
    private TextField ipText; // Value injected by FXMLLoader


    @FXML // fx:id="portText"
    private TextField portText; // Value injected by FXMLLoader

    @FXML
    void initialize(){
        Image image_background = new Image(String.valueOf(connectController.class.getResource("/images/connect1.png")));
        backgroundImage.setImage(image_background);
    }

    @FXML
    void connect2toserver(ActionEvent event) {
        System.out.println("Connect button clicked!");
        String host = ipText.getText();
        int port = Integer.parseInt(portText.getText());

        try {
            // Connect to the server
            SimpleClient client = SimpleClient.getClient(host, port);
            client.openConnection();

            // Navigate to the next screen
            App.setRoot("primary",900,690);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
