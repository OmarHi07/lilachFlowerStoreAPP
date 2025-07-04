package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'primary.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
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
import javafx.scene.control.ToggleGroup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Insets;

public class PrimaryController{

	private int NumCol;
	private int NumRow;

	@FXML // fx:id="Blue"
	private RadioButton Blue; // Value injected by FXMLLoader

	@FXML // fx:id="CartBU"
	private Button CartBU; // Value injected by FXMLLoader

	@FXML // fx:id="RefreshBU"
	private Button RefreshBU; // Value injected by FXMLLoader

	@FXML // fx:id="ReportBU"
	private Button ReportBU; // Value injected by FXMLLoader

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
    private boolean RedSelected;
	private boolean YellowSelected;
	private boolean BlueSelected;
	private boolean PinkSelected;
	private boolean WhiteSelected;
	private ToggleGroup BranchGroup;
	private ToggleGroup colorGroup;
    private static String selectedBranch;
	private static List<String> selectedColor;
	private static List<Flower> filtered;
	@FXML
	void initialize(){
		EventBus.getDefault().register(this);
        BranchGroup = new ToggleGroup();
		//colorGroup = new ToggleGroup();
		Haifa.setToggleGroup(BranchGroup);
		TelAviv.setToggleGroup(BranchGroup);
		RedSelected = false;
		YellowSelected = false;
		BlueSelected = false;
		PinkSelected = false;
		WhiteSelected = false;
		//Pink.setToggleGroup(colorGroup);
		//Red.setToggleGroup(colorGroup);
		//Yellow.setToggleGroup(colorGroup);
		//White.setToggleGroup(colorGroup);
		//Blue.setToggleGroup(colorGroup);
		selectedColor = new ArrayList<String>();
		selectedBranch = null;
		NumRow = 0;
		NumCol = 0;
		Grid.setHgap(20);     // רווח אופקי בין טורים
		Grid.setVgap(20);     // רווח אנכי בין שורות
		Grid.setPadding(new Insets(20)); // רווח מהשוליים

		try{
			SimpleClient client = SimpleClient.getClient( "localhost", 3001);
			client.openConnection();
			SimpleClient.getClient().sendToServer("add client");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Subscribe
	public void init(List<Flower> flowers) {
			flowers.sort(Comparator.comparingInt(Flower::getId));
			Platform.runLater(()->{
				if (CurrentCustomer.getCurrentEmployee() != null) {
					System.out.println("Hi");
					Employee employee = (Employee) CurrentCustomer.getCurrentEmployee();
					System.out.println(employee.getUsername());
					if(employee.getPermission() == 5) {
						System.out.println("Hi");
						UpdateCatalogBU.setVisible(true);
					}
				}
				else {
					UpdateCatalogBU.setVisible(false);
				}
			   Grid.getChildren().clear();
               NumCol = 0;
			   NumRow = 1;
			   for (Flower flower : flowers) {
				   try {
					  AnchorPane FlowerNode;
					  if (FlowerCardCache.contains(flower.getId())) {
					    	FlowerNode = FlowerCardCache.getPane(flower.getId());
					  }
					  else {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("Item.fxml"));
						FlowerNode = loader.load();
						Item controller = loader.getController();
						controller.setData(flower);
						FlowerCardCache.put(flower.getId(), FlowerNode, controller);
					  }
					  FlowerNode.setPrefHeight(520);
					  Grid.add(FlowerNode, NumCol, NumRow);
					  NumCol++;
					  if (NumCol == 2) {
					    	NumCol = 0;
						    NumRow++;
					  }

				   }
				   catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}
	private void filterFlowers(){
		NumCol = 0;
		NumRow = 0;
		List<Flower> flowers1 = SimpleClient.getFlowers();
		List<Flower> filteredFlowers = flowers1;
		if(filteredFlowers==null){
			System.out.println("Hello from Java!!!!");

		}
		if (selectedColor != null && !selectedColor.isEmpty()) {
			filteredFlowers = filteredFlowers.stream().filter(f -> f.getColor() != null && selectedColor.contains(f.getColor())).collect(Collectors.toList());
		}
		if(selectedBranch != null){
			filteredFlowers = filteredFlowers.stream().filter(f -> selectedBranch.equals(f.getColor())).collect(Collectors.toList());

		}
		filtered = filteredFlowers;
		init(filteredFlowers);
  	}

	@FXML
	void BlueColor(ActionEvent event) {
		if (Blue.isSelected() && !BlueSelected){
			if(!selectedColor.contains("Blue")){
				selectedColor.add("Blue");
			}
			BlueSelected = true;
		}
		else {
			selectedColor.remove("Blue");
			BlueSelected = false;
		}
		filterFlowers();
	}

	@Subscribe
	public void onFlowerUpdated(Flower updatedFlower) {
		Platform.runLater(() -> {
			if (FlowerCardCache.contains(updatedFlower.getId())) {
				Item controller = FlowerCardCache.getController(updatedFlower.getId());
				controller.setData(updatedFlower);  // מעדכן את הפרח הקיים
			} else {
				System.out.println("כרטיס לא קיים ב־cache! צריך ליצור אותו קודם");
			}
		});
	}

	@FXML
	void ClearSelection(ActionEvent event) {
		BranchGroup.selectToggle(null);
		selectedColor.clear();
		selectedBranch = null;
		List<Flower> flowers = SimpleClient.getFlowers();
		filtered = flowers;
		EventBus.getDefault().post(flowers);
	}
	@FXML
	void DoneMaxMin(ActionEvent event) {
         String maxVal = Max.getText().trim();
		 String minVal = Min.getText().trim();

		 if (maxVal.isEmpty() || minVal.isEmpty()) return;
		 else{
			 try {
		     	double maxPrice = Double.parseDouble(maxVal);
				double minPrice = Double.parseDouble(minVal);
				List<Flower> filtered1 = SimpleClient.getFlowers();
				filtered1 = filtered1.stream().filter(flower -> flower.getPrice()>=minPrice && flower.getPrice()<=maxPrice).collect(Collectors.toList());
				if (selectedColor != null) {
					filtered1 = filtered1.stream().filter(flower -> flower.getColor().equals(selectedColor)).collect(Collectors.toList());
				}
				if (selectedBranch != null) {
					filtered1 = filtered1.stream().filter(flower -> flower.getBranch().equals(selectedBranch)).collect(Collectors.toList());
				}
				filtered = filtered1;
				EventBus.getDefault().post(filtered);
			 }

			 catch (NumberFormatException e) {return;}
		 }

	}

	@FXML
	void HaifaBranch(ActionEvent event) {
        selectedBranch = "Haifa";
		filterFlowers();
	}

	@FXML
	void PinkColor(ActionEvent event) {
		if (Pink.isSelected() && !PinkSelected){
			if(!selectedColor.contains("Pink")){
				selectedColor.add("Pink");
			}
			PinkSelected = true;
		}
		else {
			selectedColor.remove("Pink");
			PinkSelected = false;
		}
		filterFlowers();
	}

	@FXML
	void RedColor(ActionEvent event) {
		if (Red.isSelected() && !RedSelected){
			if(!selectedColor.contains("Red")){
				selectedColor.add("Red");
			}
			RedSelected = true;
		}
		else {
			selectedColor.remove("Red");
			RedSelected = false;
		}
		filterFlowers();
	}

	@FXML
	void TelAvivBranch(ActionEvent event) {
         selectedBranch = "TelAviv";
		 filterFlowers();
	}

	@FXML
	void WhiteColor(ActionEvent event) {
		if (White.isSelected() && !WhiteSelected){
			if(!selectedColor.contains("White")){
				selectedColor.add("White");
			}
			WhiteSelected = true;
		}
		else {
			selectedColor.remove("White");
			WhiteSelected = false;
		}
		filterFlowers();
	}

	@FXML
	void YellowColor(ActionEvent event) {
		if (Yellow.isSelected() && !YellowSelected){
			if(!selectedColor.contains("Yellow")){
				selectedColor.add("Yellow");
			}
			YellowSelected = true;
		}
		else {
			selectedColor.remove("Yellow");
			YellowSelected = false;
		}
		filterFlowers();
	}
	@FXML
	void Customizethebouquet(ActionEvent event) {

	}
	@FXML
	void LogOut(ActionEvent event) {
		 Object Current = CurrentCustomer.getCurrentUser();
		 String NameClass;
		 int id;
		 if(Current instanceof BranchManager){
			 id = ((BranchManager) Current).getId();
			 NameClass = "BranchManager";
		 }
		 else if(Current instanceof CostumerServiceEmployee){
			 id = ((CostumerServiceEmployee) Current).getId();
			 NameClass = "CostumerServiceEmployee";
		 }
		 else if(Current instanceof Customer){
			 NameClass = "Customer";
			 id = ((Customer) Current).getId();
		 }
		 else if(Current instanceof NetworkWorker){
			 NameClass = "NetworkWorker";
			 id = ((NetworkWorker) Current).getId();
		 }
		 else if(Current instanceof StoreChainManager){
			 NameClass = "StoreChainManager";
			 id = ((StoreChainManager) Current).getId();
		 }
		 else{
			 NameClass = "SystemAdmin";
			 id = ((SystemAdmin) Current).getId();
		 }
		 try {
			 SimpleClient.getClient().sendToServer("log out," + NameClass + "," + id);
		 }
		 catch (IOException e) {
			 e.printStackTrace();
		 }
	}
	@FXML
	void UpdateCatalog(ActionEvent event) {
		try {
			App.setRoot("InsertFlower", 900, 730);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	void GoToCart(ActionEvent event) {
		try {
			App.setRoot("OrderCart", 900, 730);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void Refresh(ActionEvent event) {
		List<Flower> refreshFlowers = new ArrayList<Flower>();
		List<Flower> flowers = SimpleClient.getFlowers();
		for (Flower flower : filtered){
             int NumID = flower.getId();
			 NumID = NumID - 1;
			 Flower flower1 = flowers.get(NumID);
			 refreshFlowers.add(flower1);
		}
		init(refreshFlowers);
	}

	@FXML
	void ViewingReports(ActionEvent event) {

	}



}
