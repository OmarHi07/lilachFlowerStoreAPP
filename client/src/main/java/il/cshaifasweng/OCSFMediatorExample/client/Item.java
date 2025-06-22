/**
 * Sample Skeleton for 'Item.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.scene.image.Image;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;

public class Item {

    @FXML // fx:id="Name"
    private Text Name; // Value injected by FXMLLoader

    @FXML // fx:id="Price"
    private Text Price; // Value injected by FXMLLoader

    @FXML // fx:id="Type"
    private Text Type; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    public void setData(Flower flower) {
        Name.setText("Name: " + flower.getFlowerName());
        Price.setText("Price: " + flower.getPrice());
        Type.setText("Type: " + flower.getPrimaryType());
        byte[] ImageByte = flower.getImage();
        Image imageObj = new Image(new ByteArrayInputStream(ImageByte));
        image.setImage(imageObj);
    }
}
