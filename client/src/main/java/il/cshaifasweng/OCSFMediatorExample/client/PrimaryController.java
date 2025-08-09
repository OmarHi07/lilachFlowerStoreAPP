package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

public class PrimaryController{

	private int NumCol;
	private int NumRow;

	@FXML
	private Button customize;

	@FXML // fx:id="ProfileBU"
	private Button ProfileBU; // Value injected by FXMLLoader

	@FXML // fx:id="ComplaintBU"
	private Button ComplaintBU; // Value injected by FXMLLoader

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

	@FXML // fx:id="EditInformationBU"
	private Button EditInformationBU; // Value injected by FXMLLoader

	@FXML // fx:id="Yellow"
	private RadioButton Yellow; // Value injected by FXMLLoader

	@FXML // fx:id="UpdateCatalogBU"
	private Button UpdateCatalogBU; // Value injected by FXMLLoader

	@FXML // fx:id="Grid"
	private GridPane Grid; // Value injected by FXMLLoader

	@FXML // fx:id="AddSaleBU"
	private Button AddSaleBU; // Value injected by FXMLLoader

	@FXML // fx:id="SaleSentence"
	private Text SaleSentence; // Value injected by FXMLLoader

	@FXML // fx:id="AddSaleEm"
	private Text AddSaleEm; // Value injected by FXMLLoader

	@FXML // fx:id="BranchBoxEm"
	private ChoiceBox<String> BranchBoxEm; // Value injected by FXMLLoader

	@FXML // fx:id="DiscountEm"
	private Text DiscountEm; // Value injected by FXMLLoader

	@FXML // fx:id="DiscountSS"
	private Text DiscountSS; // Value injected by FXMLLoader
	@FXML // fx:id="PutDiscountEm"
	private TextField PutDiscountEm; // Value injected by FXMLLoader

	@FXML // fx:id="SelectBranchEm"
	private Text SelectBranchEm; // Value injected by FXMLLoader
	@FXML
	private TableView<HistogramEntry> histogramTable;
	@FXML
	private TableColumn<HistogramEntry, String> branchColumn;
	@FXML
	private TableColumn<HistogramEntry, Long> countColumn;

	@FXML
	private TableView<Complain> complainTable;

	@FXML // fx:id="logoutBU"
	private Button logoutBU; // Value injected by FXMLLoader


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
		BranchBoxEm.getItems().addAll("Haifa", "TelAviv", "All");
		BranchBoxEm.setValue(("All"));
		isCustomize = false;
		ReturnBU.setVisible(false);
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
		Grid.setPadding(new Insets(20));
		if(CurrentCustomer.getSelectedBranch()!=null){
			if(CurrentCustomer.getSelectedBranch().getAddress().equals("Haifa")){
				Haifa.setSelected(true);
			}
			else if(CurrentCustomer.getSelectedBranch().getAddress().equals("TelAviv")){
				TelAviv.setSelected(true);
			}
		}
		if(CurrentCustomer.getCurrentCustomer().equals("Guest")) {
			logoutBU.setText("Sign up");
			CartBU.setVisible(false);
			EditInformationBU.setVisible(false);
			AddSaleBU.setVisible(false);
			SelectBranchEm.setVisible(false);
			BranchBoxEm.setVisible(false);
			DiscountEm.setVisible(false);
			DiscountSS.setVisible(false);
			PutDiscountEm.setVisible(false);
			ProfileBU.setVisible(false);
			AddSaleEm.setVisible(false);
			CartBU.setDisable(false);
			ReturnBU.setDisable(false);
			ComplaintBU.setVisible(false);
			ReportBU.setVisible(false);
			UpdateCatalogBU.setVisible(false);
			ProfileBU.setVisible(false);
		}
		else if (CurrentCustomer.getCurrentEmployee() != null) {
			Employee employee = (Employee) CurrentCustomer.getCurrentEmployee();
			System.out.println(employee.getUsername());
			if (employee.getPermission() == 2) {
				customize.setVisible(true);
				ProfileBU.setVisible(false);
				ComplaintBU.setVisible(true);
				EditInformationBU.setVisible(false);
				ReportBU.setVisible(false);
				UpdateCatalogBU.setVisible(false);
				CartBU.setVisible(false);
				AddSaleBU.setVisible(false);
				SelectBranchEm.setVisible(false);
				BranchBoxEm.setVisible(false);
				DiscountEm.setVisible(false);
				DiscountSS.setVisible(false);
				PutDiscountEm.setVisible(false);
				AddSaleEm.setVisible(false);
			} else if (employee.getPermission() == 3) {
				customize.setVisible(true);
				ProfileBU.setVisible(false);
				ComplaintBU.setVisible(false);
				EditInformationBU.setVisible(false);
				ReportBU.setVisible(true);
				UpdateCatalogBU.setVisible(false);
				CartBU.setVisible(false);
				AddSaleBU.setVisible(false);
				SelectBranchEm.setVisible(false);
				BranchBoxEm.setVisible(false);
				DiscountEm.setVisible(false);
				DiscountSS.setVisible(false);
				PutDiscountEm.setVisible(false);
				AddSaleEm.setVisible(false);
			}
			else if (employee.getPermission() == 4) {
				customize.setVisible(true);
				ProfileBU.setVisible(false);
				ComplaintBU.setVisible(false);
				EditInformationBU.setVisible(false);
				ReportBU.setVisible(true);
				UpdateCatalogBU.setVisible(false);
				CartBU.setVisible(false);
				AddSaleBU.setVisible(true);
				SelectBranchEm.setVisible(true);
				BranchBoxEm.setVisible(true);
				DiscountEm.setVisible(true);
				DiscountSS.setVisible(true);
				PutDiscountEm.setVisible(true);
				AddSaleEm.setVisible(true);
			} else if (employee.getPermission() == 5) {
				customize.setVisible(true);
				ProfileBU.setVisible(false);
				ComplaintBU.setVisible(false);
				EditInformationBU.setVisible(false);
				ReportBU.setVisible(false);
				UpdateCatalogBU.setVisible(true);
				CartBU.setVisible(false);
				AddSaleBU.setVisible(false);
				SelectBranchEm.setVisible(false);
				BranchBoxEm.setVisible(false);
				DiscountEm.setVisible(false);
				DiscountSS.setVisible(false);
				PutDiscountEm.setVisible(false);
				AddSaleEm.setVisible(false);
			} else if (employee.getPermission() == 6) {
				customize.setVisible(true);
				ProfileBU.setVisible(false);
				ComplaintBU.setVisible(false);
				EditInformationBU.setVisible(true);
				ReportBU.setVisible(false);
				UpdateCatalogBU.setVisible(false);
				CartBU.setVisible(false);
				AddSaleBU.setVisible(false);
				SelectBranchEm.setVisible(false);
				BranchBoxEm.setVisible(false);
				DiscountEm.setVisible(false);
				DiscountSS.setVisible(false);
				PutDiscountEm.setVisible(false);
				AddSaleEm.setVisible(false);
			}
		}
		else {
			if (CurrentCustomer.getCurrentUser() != null) {
				AddSaleBU.setVisible(false);
				SelectBranchEm.setVisible(false);
				BranchBoxEm.setVisible(false);
				DiscountEm.setVisible(false);
				DiscountSS.setVisible(false);
				PutDiscountEm.setVisible(false);
				AddSaleEm.setVisible(false);
				Customer customer = (Customer) CurrentCustomer.getCurrentUser();
				if (customer.getCustomerType() == 1) {
					filtered = SimpleClient.getFlowers();
					Haifa.setDisable(true);
					TelAviv.setDisable(true);
//					filterFlowers();
				}
			}
			UpdateCatalogBU.setVisible(false);
			ReportBU.setVisible(false);
			ComplaintBU.setVisible(false);
			EditInformationBU.setVisible(false);
		}
		init(SimpleClient.getFlowers());
	}



	@Subscribe
	public void init(List<Flower> flowers) {
			flowers.sort(Comparator.comparingInt(Flower::getId));
		    filtered = flowers;
			Platform.runLater(()->{
					Grid.getChildren().clear();
					NumCol = 0;
					NumRow = 1;
				flowers.sort((f1, f2) -> Double.compare(f2.getSale(), f1.getSale()));
				for (Flower flower : flowers) {
						try {
							AnchorPane FlowerNode;
							if (FlowerCardCache.contains(flower.getId())) {
								Item controller = FlowerCardCache.getController(flower.getId());
								FlowerNode = FlowerCardCache.getPane(flower.getId());
								if (CurrentCustomer.getSelectedBranch() != null) {
									if (CurrentCustomer.getSelectedBranch().getSale()!=0) {
										controller.PutSale(flower);
									} else {
										controller.setData(flower);
									}
								} else {
									controller.setData(flower);
								}
							} else {
								FXMLLoader loader = new FXMLLoader(getClass().getResource("Item.fxml"));
								FlowerNode = loader.load();
								Item controller = loader.getController();
								if (CurrentCustomer.getSelectedBranch() != null) {
									if (CurrentCustomer.getSelectedBranch().getSale()!=0) {
										controller.PutSale(flower);
									} else {
										controller.setData(flower);
									}
								} else {
									controller.setData(flower);
								}
								FlowerCardCache.put(flower.getId(), FlowerNode, controller);
							}
							FlowerNode.setPrefHeight(520);
							Grid.add(FlowerNode, NumCol, NumRow);
							NumCol++;
							if (NumCol == 2) {
								NumCol = 0;
								NumRow++;
							}

						} catch (IOException e) {
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
		if (selectedColor != null && !selectedColor.isEmpty()) {
            filteredFlowers = filteredFlowers.stream().filter(f -> f.getColor() != null && selectedColor.contains(f.getColor())).collect(Collectors.toList());
		}
		if (selectedBranch != null) {
			List<Branch> branchList = SimpleClient.getAllBranches();
			final Branch matchedBranch = branchList.stream()
					.filter(branch -> branch.getAddress().equals(selectedBranch))
					.findFirst()
					.orElse(null);

			if (matchedBranch != null) {
				int branchId = matchedBranch.getId();
				filteredFlowers = filteredFlowers.stream()
						.filter(flower -> flower.getBranch().stream()
								.anyMatch(b -> b.getId() == branchId))
						.collect(Collectors.toList());
			}
		}
		filtered = filteredFlowers;
        init(filteredFlowers);
  	}

	@Subscribe
	public void UpdateAfterSale(AddSale event) {
//		List<Flower> flowers = SimpleClient.getFlowers();
//		List<Flower> flowers1 = SimpleClient.getFlowersSingles();
//		List<Flower> filteredFlowers = new ArrayList<>();
//		for (Flower flower: filtered){
//			int index = flower.getId();
//			for (Flower flower1: flowers){
//				if(flower1.getId() == index){
//					filteredFlowers.add(flower1);
//					break;
//				}
//			}
//			for (Flower flower1: flowers1){
//				if(flower1.getId() == index){
//					filteredFlowers.add(flower1);
//					break;
//				}
//			}
//			filtered = filteredFlowers;
//			init(filteredFlowers);
//		}
		init(filtered);

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
			if(CurrentCustomer.getCurrentEmployee() != null){
				if(CurrentCustomer.getCurrentEmployee() instanceof  NetworkWorker){
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Flower Update");
					alert.setHeaderText(null);
					alert.setContentText("The flower has been successfully changed!");
					alert.showAndWait();
				}
			}
			if (FlowerCardCache.contains(updatedFlower.getId())) {
				Item controller = FlowerCardCache.getController(updatedFlower.getId());
				controller.setData(updatedFlower);  // מעדכן את הפרח הקיים
			} else {
				System.out.println("כרטיס לא קיים ב־cache! צריך ליצור אותו קודם");
			}
			List<Flower> newList = new ArrayList<>(filtered.size());
			boolean found = false;
			for (Flower f : filtered) {
				if (f.getId() == updatedFlower.getId()) {
					newList.add(updatedFlower);
					found = true;
				} else {
					newList.add(f);
				}
			}
//			if (!found) newList.add(updatedFlower);

			filtered = newList;           // החלפה אטומית
			init(new ArrayList<>(filtered));
		});
	}

	@Subscribe
	public void AddingFlower(AddFlower newflower){
		if(newflower.isTrue()) {
			Flower flower = newflower.getFlower();
			boolean isTrue = true;
			if (!selectedColor.isEmpty()) {
				String color = newflower.getFlower().getColor();
				if (selectedColor.contains(color)) {
					isTrue = true;
				} else {
					isTrue = false;
				}
			}
			if (selectedBranch != null) {
				List<Branch> branchList = SimpleClient.getAllBranches();
				final Branch matchedBranch = branchList.stream()
						.filter(branch -> branch.getAddress().equals(selectedBranch))
						.findFirst()
						.orElse(null);  // עכשיו זה final
				int id = matchedBranch.getId();
				if (flower.getBranch().stream().anyMatch(branch -> branch.getId() == id)) {
					isTrue = true;
				} else {
					isTrue = false;
				}
			}
			if (isCustomize) {
				if (flower.getTypeOfFlower() == 2) {
					isTrue = true;
				} else {
					isTrue = false;
				}
			}
			if(!isCustomize){
				if(flower.getTypeOfFlower() == 1) {
					isTrue = true;
				}
				else {
					isTrue = false;
				}
			}
			if (isTrue) {
				filtered.add(flower);
				init(filtered);
			}
		}
	}


//	@Subscribe
//	public void DeleteFlower(DeletFlower deleteItem) {
//		List<Flower> flowersList = new ArrayList<>();
//		for(Flower flower: filtered){
//			if(flower.getId()!=deleteItem.getFlower().getId()){
//					flowersList.add(flower);
//				}
//		}
//		filtered = flowersList;
//		init(flowersList);
//	}

	@Subscribe
	public void DeleteFlower(DeletFlower deleteItem) {
		if (deleteItem == null || deleteItem.getFlower() == null) return; // הגנה מ-NPE
		final int id = deleteItem.getFlower().getId();

		javafx.application.Platform.runLater(() -> {
			if(CurrentCustomer.getCurrentEmployee()!=null) {
				if (CurrentCustomer.getCurrentEmployee() instanceof NetworkWorker) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Flower Deleted");
					alert.setHeaderText(null);
					alert.setContentText("The flower has been successfully deleted!");
					alert.showAndWait();
				}
			}
			if (filtered != null) {
				filtered.removeIf(f -> f.getId() == id); // מחיקה בטוחה
				init(new ArrayList<>(filtered));         // אם init מצפה לרשימה חדשה
				// אם יש לך TableView: table.refresh();
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
		CurrentCustomer.setSelectedBranch(null);
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
		Ordercart.cartItems.clear();
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
		Ordercart.cartItems.clear();
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
		 FlowerCardCache.clear();

		 if(CurrentCustomer.getCurrentCustomer().equals("Guest")){
			 try {
				 EventBus.getDefault().unregister(this);
				 App.setRoot("SignIn",680,560);
			 }
			 catch (IOException e) {
				 e.printStackTrace();
			 }
		 }else {
		     if (CurrentCustomer.getCurrentCustomer() == "Customer") {
				 Customer Current = CurrentCustomer.getCurrentUser();
				 NameClass = "Customer";
				 id = ((Customer) Current).getId();
			 } else {
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
				 EventBus.getDefault().unregister(this);
				 App.setRoot("Home",510,470);
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
	}
	@FXML
	void UpdateCatalog(ActionEvent event) {
		EventBus.getDefault().unregister(this);
		try {
			App.setRoot("InsertFlower", 900, 750);
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@FXML
	void ReturnToCA(ActionEvent event) {
		isCustomize = false;
	    ReturnBU.setVisible(false);
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
			if(flowers.contains(flower)) {
				refreshFlowers.add(flower);
			}
		}
		init(refreshFlowers);
	}

	@FXML
	void ViewingReports(ActionEvent event) {
		EventBus.getDefault().unregister(this);
		try {
			App.setRoot("MainReportsMenu", 720, 610);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void EditInformationDone(ActionEvent event) {
		EventBus.getDefault().unregister(this);
		try {
			App.setRoot("SystemManager", 1011, 700);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	@FXML
	void HandleComplaint(ActionEvent event) {
		EventBus.getDefault().unregister(this);
		try {
			App.setRoot("HandleComplaints", 1016, 760);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}


	@FXML
	void AddSale(ActionEvent event) {
		if(!PutDiscountEm.getText().isEmpty() && !BranchBoxEm.getValue().isEmpty()){
			AddSale newsale = new AddSale();
			if(BranchBoxEm.getValue().equals("Haifa")) {
				newsale.setNumBranch(1);
				newsale.setNumSale(Integer.parseInt(PutDiscountEm.getText()));
			}
			else if(BranchBoxEm.getValue().equals("TelAviv")){
				newsale.setNumBranch(2);
				newsale.setNumSale(Integer.parseInt(PutDiscountEm.getText()));
			}
			else {
				newsale.setNumBranch(3);
				newsale.setNumSale(Integer.parseInt(PutDiscountEm.getText()));
			}
			try {
				PutDiscountEm.setText(null);
				SimpleClient.getClient().sendToServer(newsale);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void GoToMyprofile(ActionEvent event) {
		EventBus.getDefault().unregister(this);
		try{
			App.setRoot("AccountInformation", 1016, 760);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}