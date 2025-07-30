package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.geometry.Insets;

public class PrimaryController{

	private int NumCol;
	private int NumRow;

	@FXML // fx:id="Blue"
	private RadioButton Blue; // Value injected by FXMLLoader

	@FXML // fx:id="ReturnBU"
	private Button ReturnBU; // Value injected by FXMLLoader

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



	@FXML
	private TableView<HistogramEntry> histogramTable;
	@FXML
	private TableColumn<HistogramEntry, String> branchColumn;
	@FXML
	private TableColumn<HistogramEntry, Long> countColumn;

	@FXML
	private TableView<Complain> complainTable;


	@FXML private TableColumn<Complain, Integer> idColumn;
	@FXML private TableColumn<Complain, String> complainTextColumn;
	@FXML private TableColumn<Complain, LocalDate> complainDateColumn;

	private boolean RedSelected;
	private boolean YellowSelected;
	private boolean BlueSelected;
	private boolean PinkSelected;
	private boolean WhiteSelected;
	private boolean isCustomize;
	private ToggleGroup BranchGroup;
	private ToggleGroup colorGroup;
    private static String selectedBranch;
	private static List<String> selectedColor;
	private static List<Flower> filtered;

	@FXML
	void initialize() {
		EventBus.getDefault().register(this);
		isCustomize = false;
	//	ReturnBU.setVisible(false);
        BranchGroup = new ToggleGroup();
		//colorGroup = new ToggleGroup();
		Haifa.setToggleGroup(BranchGroup);
		TelAviv.setToggleGroup(BranchGroup);
		RedSelected = false;
		YellowSelected = false;
		BlueSelected = false;
		PinkSelected = false;
		WhiteSelected = false;
		System.out.println("nnnnnnnnnnnnnnnooooooo");

		branchColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
		countColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
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
		init(SimpleClient.getFlowers());
		EventBus.getDefault().register(this);
		EventBus.getDefault().register(this);
		System.out.println("impotannnnnnnnnnnnnnnnnnnnnnnntttttttttttttt");

		branchColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
		countColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		complainTextColumn.setCellValueFactory(new PropertyValueFactory<>("complain_text"));
		complainDateColumn.setCellValueFactory(new PropertyValueFactory<>("complain_date"));
		System.out.println("impotannnnnnnnnnnnnnnnnnnnnnnntttttttttttttt");


	}


	@Subscribe
	public void onGetHistogramReportEvent(GetHistogramReportEvent event) {
		System.out.println("onGetHistogramReportEventpri");
		if (event.getType().equals("complaints")) {
			Platform.runLater(() -> {
				List<HistogramEntry> entries = new ArrayList<>();
				for (Map.Entry<String, Long> entry : event.getData().entrySet()) {
					entries.add(new HistogramEntry(entry.getKey(), entry.getValue()));
				}
				histogramTable.getItems().setAll(entries);
			});
		}
	}


	@Subscribe
	public void init(List<Flower> flowers) {
			flowers.sort(Comparator.comparingInt(Flower::getId));
		    filtered = SimpleClient.getFlowers();
			Platform.runLater(()->{
				System.out.println("Entered");
				if (CurrentCustomer.getCurrentEmployee() != null) {
					Employee employee = (Employee) CurrentCustomer.getCurrentEmployee();
					System.out.println(employee.getUsername());
					if(employee.getPermission() == 5) {
						UpdateCatalogBU.setVisible(true);
					}
				}
				else {
					if (CurrentCustomer.getCurrentUser() != null) {
						Customer customer = (Customer) CurrentCustomer.getCurrentUser();
						if(customer.getCustomerType() == 1){
							Haifa.setDisable(false);
							TelAviv.setDisable(false);
						}
					}
					UpdateCatalogBU.setVisible(false);
					ReportBU.setDisable(false);

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
		List<Flower> flowers1;
		if(isCustomize){
			flowers1 = SimpleClient.getFlowersSingles();
		}
		else {
			flowers1 = SimpleClient.getFlowers();
		}
		List<Flower> filteredFlowers = flowers1;
		if(filteredFlowers==null){
			System.out.println("Hello from Java!!!!");

		}
		if (selectedColor != null && !selectedColor.isEmpty()) {
            assert filteredFlowers != null;
            filteredFlowers = filteredFlowers.stream().filter(f -> f.getColor() != null && selectedColor.contains(f.getColor())).collect(Collectors.toList());
		}

		if (selectedBranch != null) {
			List<Branch> branchList = SimpleClient.getAllBranches();

			final Branch matchedBranch = branchList.stream().filter(branch -> branch.getAddress().equals(selectedBranch)).findFirst().orElse(null);  // עכשיו זה final

			if (matchedBranch != null) {
                assert filteredFlowers != null;
                filteredFlowers =filteredFlowers.stream()
						.filter(flower -> flower.getBranch().contains(matchedBranch))
						.collect(Collectors.toList());
			}
		}
		filtered = filteredFlowers;
        assert filteredFlowers != null;
        init(filteredFlowers);
  	}

	@FXML
	private void loadComplaintsHistogram() {
		System.out.println("loadComplaintsHistogram");

		LocalDate from = LocalDate.now().minusMonths(3);
		LocalDate to = LocalDate.now();
        try {
            SimpleClient.getClient().sendToServer(new HistogramReportRequest(from, to));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
		Yellow.setSelected(false);
		YellowSelected = false;
		Blue.setSelected(false);
		BlueSelected = false;
		Pink.setSelected(false);
		PinkSelected = false;
		Red.setSelected(false);
		RedSelected = false;
		White.setSelected(false);
		WhiteSelected = false;
		selectedColor.clear();
		selectedBranch = null;
		Max.setText(null);
		Min.setText(null);
		List<Flower> flowers;
		if(isCustomize) {
		  flowers = SimpleClient.getFlowersSingles();
		}
		else {
			flowers = SimpleClient.getFlowers();
		}
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
				List<Flower> filtered1;
				if(isCustomize){
					filtered1 = SimpleClient.getFlowersSingles();
				}
				else {
					filtered1 = SimpleClient.getFlowers();
				}
				filtered1 = filtered1.stream().filter(flower -> flower.getPrice()>=minPrice && flower.getPrice()<=maxPrice).collect(Collectors.toList());
				if (selectedColor != null && !selectedColor.isEmpty()) {
					filtered1 = filtered1.stream().filter(f -> f.getColor() != null && selectedColor.contains(f.getColor())).collect(Collectors.toList());

				}
				if (selectedBranch != null) {
					 List<Branch> branchList = SimpleClient.getAllBranches();

					 final Branch matchedBranch = branchList.stream()
							 .filter(branch -> branch.getAddress().equals(selectedBranch))
							 .findFirst()
							 .orElse(null);  // עכשיו זה final

					 if (matchedBranch != null) {
						 filtered1 = filtered1.stream()
								 .filter(flower -> flower.getBranch().contains(matchedBranch))
								 .collect(Collectors.toList());
					 }
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
		List<Branch> branchList = SimpleClient.getAllBranches();
		Branch branch = branchList.stream().filter(branch1 -> branch1.getAddress().equals(selectedBranch)).findFirst().orElse(null);
		CurrentCustomer.setSelectedBranch(branch);
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
		List<Branch> branchList = SimpleClient.getAllBranches();
		Branch branch = branchList.stream().filter(branch1 -> branch1.getAddress().equals(selectedBranch)).findFirst().orElse(null);
		CurrentCustomer.setSelectedBranch(branch);
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
		isCustomize = true;
		filtered = SimpleClient.getFlowersSingles();
		ReturnBU.setVisible(true);
		List<Flower> listFlowers = SimpleClient.getFlowersSingles();
		init(listFlowers);
	}

	@FXML
	void LogOut(ActionEvent event) {
	     int id;
		 String NameClass;
		 if(CurrentCustomer.getCurrentCustomer() == "Customer") {
			 Customer Current = CurrentCustomer.getCurrentUser();
			 NameClass = "Customer";
			 id = ((Customer) Current).getId();
		 }
		 else {
			 Employee employee = CurrentCustomer.getCurrentEmployee();
			 NameClass = "Employee";
			 id = ((Employee) employee).getId();
		 }
		 try {
			 SimpleClient.getClient().sendToServer("log out," + NameClass + "," + id);
			 CurrentCustomer.setCurrentUser(null);
			 CurrentCustomer.setCurrentEmployee(null);
			 CurrentCustomer.setCurrentCustomer(null);
			 CurrentCustomer.setSelectedBranch(null);
			 App.setRoot("SignIn", 900, 760);
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
		EventBus.getDefault().unregister(this);

		try {
			App.setRoot("Ordercart", 1016, 760);
		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}
	@FXML
	void ReturnToCA(ActionEvent event) {
		isCustomize = false;
	//.......	ReturnBU.setVisible(false);
		filtered = SimpleClient.getFlowers();
		List<Flower> listFlowers = SimpleClient.getFlowers();
		init(listFlowers);

	}
	@FXML
	void Refresh(ActionEvent event) {
		List<Flower> refreshFlowers = new ArrayList<Flower>();
		List<Flower> flowers;
		if(isCustomize) {
			flowers = SimpleClient.getFlowersSingles();
		}
		else {
			flowers = SimpleClient.getFlowers();
		}
		for (Flower flower : filtered){
             int NumID = flower.getId();
			 NumID = NumID - 1;
			 Flower flower1 = flowers.get(NumID);
			 refreshFlowers.add(flower1);
		}
		init(refreshFlowers);
	}

	@FXML
	void ViewingReports(ActionEvent event) {}
}
