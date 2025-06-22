package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'primary.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PrimaryController{

	private int NumCol;
	private int NumRow;

	@FXML // fx:id="Blue"
	private RadioButton Blue; // Value injected by FXMLLoader

	@FXML // fx:id="Haifa"
	private RadioButton Haifa; // Value injected by FXMLLoader

	@FXML // fx:id="Max"
	private TextField Max; // Value injected by FXMLLoader

	@FXML // fx:id="Min"
	private TextField Min; // Value injected by FXMLLoader

	@FXML // fx:id="Pink"
	private RadioButton Pink; // Value injected by FXMLLoader

	@FXML // fx:id="Red"
	private RadioButton Red; // Value injected by FXMLLoader

	@FXML // fx:id="TelAviv"
	private RadioButton TelAviv; // Value injected by FXMLLoader

	@FXML // fx:id="White"
	private RadioButton White; // Value injected by FXMLLoader

	@FXML // fx:id="Yellow"
	private RadioButton Yellow; // Value injected by FXMLLoader

	@FXML // fx:id="UpdateCatalogBU"
	private Button UpdateCatalogBU; // Value injected by FXMLLoader

	@FXML // fx:id="Grid"
	private GridPane Grid; // Value injected by FXMLLoader

	@FXML
	void initialize(){
		EventBus.getDefault().register(this);
		NumRow = 0;
		NumCol = 0;
		try{
			SimpleClient.getClient().sendToServer("add client");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Subscribe
	public void init(List<?> listFlower){
		if (listFlower.isEmpty() || !(listFlower.get(0) instanceof Flower)) return;
		List<Flower> flowers = listFlower.stream().map(f -> (Flower) f).collect(Collectors.toList());
		flowers.sort(Comparator.comparingInt(Flower::getId));
		Grid.getChildren().clear();

		for (Flower flower : flowers) {
			try {
				AnchorPane FlowerNode;
				if(FlowerCardCache.contains(flower.getId())) {
					FlowerNode = FlowerCardCache.get(flower.getId());
				}
				else {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Item.fxml"));
					FlowerNode = loader.load();
					Item controller = loader.getController();
					controller.setData(flower);
					FlowerCardCache.put(flower.getId(), FlowerNode);
				}
				Grid.add(FlowerNode, NumCol, NumRow);
				NumCol++;
				if(NumCol == 2){
					NumCol = 0 ;
					NumRow++;
				}

			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	@FXML
	void BlueColor(ActionEvent event) {

	}

	@FXML
	void DoneMaxMin(ActionEvent event) {

	}

	@FXML
	void HaifaBranch(ActionEvent event) {

	}

	@FXML
	void PinkColor(ActionEvent event) {

	}

	@FXML
	void RedColor(ActionEvent event) {

	}

	@FXML
	void TelAvivBranch(ActionEvent event) {

	}

	@FXML
	void WhiteColor(ActionEvent event) {

	}

	@FXML
	void YellowColor(ActionEvent event) {

	}
	@FXML
	void Customizethebouquet(ActionEvent event) {

	}
	@FXML
	void LogOut(ActionEvent event) {

	}
	@FXML
	void UpdateCatalog(ActionEvent event) {

	}
	@FXML
	void GoToCart(ActionEvent event) {

	}

}
