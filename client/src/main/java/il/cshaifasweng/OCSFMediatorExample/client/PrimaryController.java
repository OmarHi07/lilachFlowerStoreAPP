package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {
	private Image[] images;
	private List<Text> priceTexts;
	private List<Text> nameTexts;
	private List<Text> typeTexts;
	@FXML
	void sendWarning(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sample Skeleton for 'primary.fxml' Controller Class
	 */

	@FXML // fx:id="BackgroundIMAGE"
	private ImageView BackgroundIMAGE; // Value injected by FXMLLoader

	@FXML // fx:id="Change1"
	private Button Change1; // Value injected by FXMLLoader

	@FXML // fx:id="Change2"
	private Button Change2; // Value injected by FXMLLoader

	@FXML // fx:id="Change3"
	private Button Change3; // Value injected by FXMLLoader

	@FXML // fx:id="Change4"
	private Button Change4; // Value injected by FXMLLoader

	@FXML // fx:id="Change5"
	private Button Change5; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower1"
	private Text PriceFlower1; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower11"
	private TextField NewPrice1; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower2"
	private Text PriceFlower2; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower22"
	private TextField NewPrice2; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower3"
	private Text PriceFlower3; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower33"
	private TextField NewPrice3; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower4"
	private Text PriceFlower4; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower44"
	private TextField NewPrice4; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower5"
	private Text PriceFlower5; // Value injected by FXMLLoader

	@FXML // fx:id="PriceFlower55"
	private TextField NewPrice5; // Value injected by FXMLLoader

	@FXML // fx:id="PutName1"
	private Text PutName1; // Value injected by FXMLLoader

	@FXML // fx:id="PutName2"
	private Text PutName2; // Value injected by FXMLLoader

	@FXML // fx:id="PutName3"
	private Text PutName3; // Value injected by FXMLLoader

	@FXML // fx:id="PutName4"
	private Text PutName4; // Value injected by FXMLLoader

	@FXML // fx:id="PutName5"
	private Text PutName5; // Value injected by FXMLLoader

	@FXML // fx:id="PutType1"
	private Text PutType1; // Value injected by FXMLLoader

	@FXML // fx:id="PutType2"
	private Text PutType2; // Value injected by FXMLLoader

	@FXML // fx:id="PutType3"
	private Text PutType3; // Value injected by FXMLLoader

	@FXML // fx:id="PutType4"
	private Text PutType4; // Value injected by FXMLLoader

	@FXML // fx:id="PutType5"
	private Text PutType5; // Value injected by FXMLLoader

	@FXML // fx:id="image_1"
	private ImageView image_1; // Value injected by FXMLLoader

	@FXML // fx:id="image_2"
	private ImageView image_2; // Value injected by FXMLLoader

	@FXML // fx:id="image_3"
	private ImageView image_3; // Value injected by FXMLLoader

	@FXML // fx:id="image_4"
	private ImageView image_4; // Value injected by FXMLLoader

	@FXML // fx:id="image_5"
	private ImageView image_5; // Value injected by FXMLLoader

	@FXML
	void initialize(){
		EventBus.getDefault().register(this);
		priceTexts = new ArrayList<>(List.of(PriceFlower1, PriceFlower2, PriceFlower3, PriceFlower4, PriceFlower5));
		nameTexts = new ArrayList<>(List.of(PutName1,PutName2,PutName3,PutName4, PutName5));
		typeTexts = new ArrayList<>(List.of(PutType1,PutType2,PutType3,PutType4,PutType5));
		images = new Image[5];
		for(int i = 0; i < images.length; i++){
			images[i] = new Image(String.valueOf(PrimaryController.class.getResource("/images/" + i + ".png")));
		}
		Image image_background = new Image(String.valueOf(PrimaryController.class.getResource("/images/background.jpg")));
		BackgroundIMAGE.setImage(image_background);
		image_1.setImage(images[4]);
		image_2.setImage(images[2]);
		image_3.setImage(images[0]);
		image_4.setImage(images[1]);
		image_5.setImage(images[3]);
		try {
			SimpleClient.getClient().sendToServer("add client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Subscribe
	public void init(List<?> list){
		if (list.isEmpty() || !(list.get(0) instanceof Flower)) return;

		List<Flower> flowerList = list.stream().map(f -> (Flower) f).collect(Collectors.toList());
		flowerList.sort(Comparator.comparingInt(Flower::getId));

		Platform.runLater(() -> {
			for (int i = 0; i < flowerList.size(); i++) {
				Flower flower = flowerList.get(i);
				priceTexts.get(i).setText("Price:" + String.valueOf(flower.getPrice()) + "₪");
				typeTexts.get(i).setText("Type:" + flower.getPrimaryType());
				nameTexts.get(i).setText("Name:" + flower.getFlowerName());
			}
		});
	}
	@Subscribe
	public void ChangePrice1(ChangePrice changePrice){
		Platform.runLater(()->{
			int index = changePrice.getId() - 1 ;
			priceTexts.get(index).setText("Price:" + String.valueOf(changePrice.getPrice()) + "₪");
		});
	}

	@Subscribe
	public void SwitchToSecoundary(Flower flower){

	}

	@FXML
	void ChangeF1(ActionEvent event) {
		if (Integer.parseInt(NewPrice1.getText())>=0 && NewPrice1.getText()!=null ) {
			try {
				SimpleClient.getClient().sendToServer("change," + NewPrice1.getText() + ",1");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void ChangeF2(ActionEvent event) {
		if (Integer.parseInt(NewPrice2.getText())>=0 && NewPrice2.getText()!=null) {
			try {
				SimpleClient.getClient().sendToServer("change," + NewPrice2.getText() + ",2");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void ChangeF3(ActionEvent event) {
		if (Integer.parseInt(NewPrice3.getText())>=0 && NewPrice3.getText()!=null) {
			try {
				SimpleClient.getClient().sendToServer("change," + NewPrice3.getText() + ",3");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void ChangeF4(ActionEvent event) {
		if (Integer.parseInt(NewPrice4.getText())>=0 && NewPrice4.getText()!=null) {
			try {
				SimpleClient.getClient().sendToServer("change," + NewPrice4.getText() + ",4");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void ChangeF5(ActionEvent event) {
		if (Integer.parseInt(NewPrice5.getText())>=0 && NewPrice5.getText()!=null) {
			try {
				SimpleClient.getClient().sendToServer("change," + NewPrice5.getText() + ",5");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void image_1(MouseEvent event) {
		try {
			SimpleClient.getClient().sendToServer("image1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void image_2(MouseEvent event) {
		try {
			SimpleClient.getClient().sendToServer("image2");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void image_3(MouseEvent event) {
		try {
			SimpleClient.getClient().sendToServer("image3");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void image_4(MouseEvent event) {
		try {
			SimpleClient.getClient().sendToServer("image4");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void image_5(MouseEvent event) {
		try {
			SimpleClient.getClient().sendToServer("image5");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}