package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SecondaryController {
    @FXML // fx:id="AddToCart"
    private Button AddToCart; // Value injected by FXMLLoader

    @FXML // fx:id="TextName"
    private Text TextName; // Value injected by FXMLLoader

    @FXML // fx:id="TextPrice"
    private Text TextPrice; // Value injected by FXMLLoader

    @FXML // fx:id="TextType"
    private Text TextType; // Value injected by FXMLLoader

    @FXML // fx:id="Back"
    private Button Back; // Value injected by FXMLLoader

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        Flower flower = FlowerHolder.CurrentFlower;
        if(flower != null){
            TextName.setText(flower.getFlowerName());
            TextPrice.setText(String.valueOf(flower.getPrice()) + "₪");
            TextType.setText(flower.getType());
        }
    }
    @FXML
    void AddToCart(ActionEvent event) {

    }

    @FXML
    void BackToPrimary(ActionEvent event) {
        try {
            App.setRoot("primary", 900, 690);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void ChangePrice1(ChangePrice changePrice){
        Flower flower = FlowerHolder.CurrentFlower;
        int id = flower.getId();
        if(id == changePrice.getId()) {
            Platform.runLater(() -> {
                TextPrice.setText(String.valueOf(changePrice.getPrice()) + " ₪");
            });
        }
    }

}