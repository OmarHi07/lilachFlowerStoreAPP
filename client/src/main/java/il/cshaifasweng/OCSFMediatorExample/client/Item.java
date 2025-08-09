/**
 * Sample Skeleton for 'Item.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Permission;
import java.util.List;
import javafx.stage.FileChooser;
import org.greenrobot.eventbus.EventBus;


public class Item {

    @FXML // fx:id="Name"
    private Text Name; // Value injected by FXMLLoader

    @FXML // fx:id="ID"
    private Text ID; // Value injected by FXMLLoader

    @FXML // fx:id="Delete"
    private Button Delete; // Value injected by FXMLLoader

    @FXML // fx:id="Price"
    private Text Price; // Value injected by FXMLLoader

    @FXML // fx:id="Done4"
    private CheckBox Done4; // Value injected by FXMLLoader

    @FXML // fx:id="Sale"
    private Text Sale; // Value injected by FXMLLoader

    @FXML // fx:id="SaleText"
    private TextField SaleText; // Value injected by FXMLLoader

    @FXML // fx:id="EditInformationBU"
    private Button EditInformationBU; // Value injected by FXMLLoader

    @FXML // fx:id="Type"
    private Text Type; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    @FXML // fx:id="Avalible"
    private Text Avalible; // Value injected by FXMLLoader

    @FXML // fx:id="RemoveSaleBU"
    private Button RemoveSaleBU; // Value injected by FXMLLoader

    @FXML // fx:id="Edit1"
    private Text Edit1; // Value injected by FXMLLoader

    @FXML // fx:id="Edit2"
    private Text Edit2; // Value injected by FXMLLoader

    @FXML // fx:id="ChangesBU"
    private Button ChangesBU; // Value injected by FXMLLoader

    @FXML // fx:id="Edit3"
    private Text Edit3; // Value injected by FXMLLoader

    @FXML // fx:id="Edit4"
    private Text Edit4; // Value injected by FXMLLoader

    @FXML // fx:id="SoldOut"
    private Text SoldOut; // Value injected by FXMLLoader

    @FXML // fx:id="HaifaText"
    private Text HaifaText; // Value injected by FXMLLoader

    @FXML // fx:id="TelAviv"
    private Text TelAviv; // Value injected by FXMLLoader

    @FXML // fx:id="AddHaifa"
    private RadioButton AddHaifa; // Value injected by FXMLLoader

    @FXML // fx:id="AddTelAviv"
    private RadioButton AddTelAviv; // Value injected by FXMLLoader

    @FXML // fx:id="PutNewName"
    private TextField PutNewName; // Value injected by FXMLLoader

    @FXML // fx:id="PutNewPrice"
    private TextField PutNewPrice; // Value injected by FXMLLoader

    @FXML // fx:id="PutNewType"
    private TextField PutNewType; // Value injected by FXMLLoader

    @FXML // fx:id="AddCart"
    private Button AddCart; // Value injected by FXMLLoader

    @FXML // fx:id="ChangeImageBU"
    private Button ChangeImageBU; // Value injected by FXMLLoader

    private byte[] selectedImageBytes;
    private boolean selectedHaifa;
    private boolean selectedTelAviv;

    public void setData(Flower flower) {
        selectedImageBytes = null;
        selectedHaifa = false;
        selectedTelAviv = false;
        Name.setText("Name: " + flower.getFlowerName());
        if(flower.getSale() != 0) {
            double sale = flower.getSale() / 100.0;
            double price = flower.getPrice();
            double total = price - (sale * price);
            Price.setText("Price: " + flower.getPrice() + "₪"  + "Sale Price:" + total + "₪");
        }
        else{
            Price.setText("Price: " + flower.getPrice());
        }
        Type.setText("Type: " + flower.getType());
        ID.setText(String.valueOf(flower.getId()));
        byte[] ImageByte = flower.getImage();
        Image imageObj = new Image(new ByteArrayInputStream(ImageByte));
        image.setImage(imageObj);

        if((CurrentCustomer.getCurrentEmployee()!=null) || CurrentCustomer.getCurrentCustomer().equals("Customer")) {
            boolean Permission = false;
            if(CurrentCustomer.getCurrentEmployee()!=null) {
                Employee emp = (Employee) CurrentCustomer.getCurrentEmployee();
                if (emp.getPermission() != 5) {
                    Permission = false;
                }
                else{
                    Permission = true;
                }
            }
            else if(CurrentCustomer.getCurrentUser()!=null) {
                Permission = false;
            }
            if (!Permission) {
                RemoveSaleBU.setVisible(false);
                ChangesBU.setVisible(false);
                ChangeImageBU.setVisible(false);
                HaifaText.setVisible(false);
                Done4.setVisible(false);
                SaleText.setVisible(false);
                Sale.setVisible(false);
                AddHaifa.setVisible(false);
                AddTelAviv.setVisible(false);
                TelAviv.setVisible(false);
                PutNewName.setVisible(false);
                PutNewPrice.setVisible(false);
                PutNewType.setVisible(false);
                Edit1.setVisible(false);
                Edit2.setVisible(false);
                Edit3.setVisible(false);
                Delete.setVisible(false);
                Edit4.setVisible(false);
                SoldOut.setVisible(false);
            } else {
                AddCart.setVisible(false);
                ChangesBU.setVisible(true);
                ChangeImageBU.setVisible(true);
                HaifaText.setVisible(true);
                Done4.setVisible(true);
                SaleText.setVisible(true);
                Sale.setVisible(true);
                AddHaifa.setVisible(true);
                AddTelAviv.setVisible(true);
                TelAviv.setVisible(true);
                PutNewName.setVisible(true);
                PutNewPrice.setVisible(true);
                PutNewType.setVisible(true);
                Edit1.setVisible(true);
                Edit2.setVisible(true);
                Edit3.setVisible(true);
                Delete.setVisible(true);
                Edit4.setVisible(true);
                SoldOut.setVisible(false);
            }
        }
        if (CurrentCustomer.getCurrentCustomer().equals("Employee")){
            AddCart.setVisible(false);
            SoldOut.setVisible(false);
        }
        if(flower.getBranch() == null){
            SoldOut.setVisible(true);
            AddCart.setDisable(false);
        }
        if(flower.getBranch().isEmpty()) {
            Avalible.setText("Not Available");
            AddCart.setVisible(false);
            SoldOut.setVisible(true);
        }
        else {
            Avalible.setText("Available:");
            int Num = flower.getBranch().size();
            if(Num == 2){
                Avalible.setText(Avalible.getText() + "Haifa, TelAviv");
                AddHaifa.setSelected(true);
                selectedTelAviv = true;
                selectedHaifa = true;
                AddTelAviv.setSelected(true);
                if(CurrentCustomer.getCurrentUser()!=null) {
                    AddCart.setVisible(true);
                }
            }
            else {
                Branch branch = flower.getBranch().get(0);
                if(branch.getAddress().equals("Haifa")) {
                    AddHaifa.setSelected(true);
                    selectedHaifa = true;
                    Avalible.setText(Avalible.getText() + "Haifa");
                }
                else {
                    AddHaifa.setSelected(false);
                    AddTelAviv.setSelected(true);
                    selectedTelAviv = true;
                    Avalible.setText(Avalible.getText() + "TelAviv");
                }
            }

        }
        if(CurrentCustomer.getCurrentUser()!=null) {
            Customer customer = (Customer) CurrentCustomer.getCurrentUser();
            if(customer.getCustomerType() == 1){
                Branch branch = SimpleClient.getAllBranches().get(0);
                boolean notAvailable = flower.getBranch()
                        .stream()
                        .noneMatch(b -> b.getAddress().equals(branch.getAddress()));
                if(notAvailable){
                    AddCart.setDisable(true);
                    SoldOut.setVisible(true);
                }
                else {
                    SoldOut.setVisible(false);
                }
            }
        }

        if(CurrentCustomer.getCurrentCustomer().equals("Guest")){
            RemoveSaleBU.setVisible(false);
            HaifaText.setVisible(false);
            TelAviv.setVisible(false);
            Delete.setVisible(false);
            ChangeImageBU.setVisible(false);
            SoldOut.setVisible(false);
            Edit1.setVisible(false);
            Edit2.setVisible(false);
            Edit3.setVisible(false);
            Edit4.setVisible(false);
            PutNewName.setVisible(false);
            PutNewPrice.setVisible(false);
            PutNewType.setVisible(false);
            AddHaifa.setVisible(false);
            AddTelAviv.setVisible(false);
            Done4.setVisible(false);
            Sale.setVisible(false);
            SaleText.setVisible(false);
            ChangesBU.setVisible(false);
            AddCart.setDisable(true);
        }
        ID.setVisible(false);
    }
    public void PutSale(Flower flower){
        setData(flower);
        double price = flower.getPrice();
        double discount;
        double finalPrice = flower.getPrice();
        if(flower.getSale()!=0){
                double discount1 = flower.getSale() / 100.0;
                finalPrice = price - (price * discount1);
        }
//        if(flower.getSaleBranchHaifaTelAviv()!=0){
//                    double discount1 = flower.getSaleBranchHaifaTelAviv() / 100.0;
//                    finalPrice = finalPrice - (finalPrice * discount1);
//        } else if (flower.getSaleBranchTelAviv()!=0 && CurrentCustomer.getSelectedBranch().equals("TelAviv")) {
//                    double discount2 = flower.getSaleBranchTelAviv() / 100.0;
//                    finalPrice = finalPrice - (finalPrice * discount2);
//        }
//        else if(flower.getSaleBranchHaifa()!=0 && CurrentCustomer.getSelectedBranch().equals("Haifa")) {
//                    double discount3 = flower.getSaleBranchHaifa() / 100.0;
//                    finalPrice = finalPrice - (finalPrice * discount3);
//        }
        discount = CurrentCustomer.getSelectedBranch().getSale() / 100.0;
        finalPrice = finalPrice * discount;

        Price.setText("Original Price:" + price +"₪ " +" Sale Price:" + finalPrice + "₪");
    }


    public void setName(String name){
        Name.setText(name);
    }
    public void setPrice(String price){
        Price.setText(String.valueOf(price));
    }
    public void setType(String type){
        Type.setText(type);
    }
    public void setAvalible(String avalible){
        if (avalible == null){
            AddCart.setDisable(false);
            SoldOut.setVisible(true);
        }
        else {
            Avalible.setText(avalible);
        }

    }
    @FXML
    void AddToTheCart(ActionEvent event) {
        if(CurrentCustomer.getCurrentUser() != null) {
            Customer customer = (Customer) CurrentCustomer.getCurrentUser();
            if ((customer.getCustomerType() == 2 || customer.getCustomerType()==3) && CurrentCustomer.getSelectedBranch() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Select Branch");
                    alert.setHeaderText("Select a Branch");
                    alert.setContentText("Please select a branch before adding item to the cart");
                    alert.showAndWait();
            }
            else {
                int idValue = Integer.parseInt(ID.getText());
                List<Flower> flowers = SimpleClient.getFlowers();
                List<Flower> flowerList = SimpleClient.getFlowersSingles();
                Flower flower = flowers.stream().filter(f -> f.getId() == idValue).findFirst().orElse(null);
                if(flower == null){
                    flower = flowerList.stream().filter(f -> f.getId() == idValue).findFirst().orElse(null);
                }
                if(flower != null) {
                    double price = flower.getPrice();
                    CartProduct cart = new CartProduct(1, price, flower);
                    Ordercart.cartItems.add(cart);
                    AddCart.setDisable(false);
                }
            }
        }
        else if (CurrentCustomer.getCurrentCustomer().equals("Guest")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Required");
            alert.setHeaderText("Create an Account");
            alert.setContentText("You must create an account before you can buy items.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteFlower(ActionEvent event) {
         int idValue = Integer.parseInt(ID.getText());
         try {
             SimpleClient.getClient().sendToServer("Delete," + idValue);
         }
         catch (Exception e) {
             e.printStackTrace();
         }
    }

    @FXML
    void NewSale(ActionEvent event) {
        int idValue = Integer.parseInt(ID.getText().trim());
        String newSale = SaleText.getText().trim();
        if(!(newSale.isEmpty())){
            try {
                SimpleClient.getClient().sendToServer("Change Sale," + newSale + "," + idValue);
                SaleText.setText(null);
                Done4.setSelected(false);

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    @FXML
    void RemoveSale(ActionEvent event) {
        int idValue = Integer.parseInt(ID.getText().trim());
        try {
            SimpleClient.getClient().sendToServer("Remove Sale," + idValue);
        }
        catch(Exception e){
                e.printStackTrace();
        }
    }

    @FXML
    void SaveChanges(ActionEvent event) {
        ChangeFlower flower = new ChangeFlower();
        int idValue = Integer.parseInt(ID.getText().trim());
        flower.setId(idValue);
        if(PutNewName.getText()!=null && !PutNewName.getText().isEmpty()){
            flower.setNewName(PutNewName.getText());
        }
        if(PutNewPrice.getText()!=null && !(PutNewPrice.getText().isEmpty())){
            flower.setNewPrice(PutNewPrice.getText());
        }
        if(PutNewType.getText()!=null && !(PutNewType.getText().isEmpty())){
            flower.setNewType(PutNewType.getText());
        }
        List<Branch> Branches = SimpleClient.getAllBranches();
        int idHaifa = 0;
        int idTelAviv = 0;
        for(Branch branch : Branches){
            if(branch.getAddress().equals("Haifa")){
                idHaifa = branch.getId();
            }
            else {
                idTelAviv = branch.getId();
            }
        }
        if(AddHaifa.isSelected()){
            flower.AddNewBranch(idHaifa);
        }
        else {
            flower.AddRemoveBranch(idHaifa);
        }
        if(AddTelAviv.isSelected()){
            flower.AddNewBranch(idTelAviv);
        }
        else {
            flower.AddRemoveBranch(idTelAviv);
        }
        if(selectedImageBytes!=null){
            flower.setImage(selectedImageBytes);
        }
        try {
            SimpleClient.getClient().sendToServer(flower);
            PutNewName.setText(null);
            PutNewPrice.setText(null);
            PutNewType.setText(null);
            selectedImageBytes = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void ClickHaifa(ActionEvent event) {
        selectedHaifa = !selectedHaifa;
        AddHaifa.setSelected(selectedHaifa);
    }

    @FXML
    void ClickTelAviv(ActionEvent event) {
        selectedTelAviv = !selectedTelAviv;
        AddTelAviv.setSelected(selectedTelAviv);
    }

    @FXML
    void ChangeImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try{
                selectedImageBytes = Files.readAllBytes(selectedFile.toPath());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }


    @FXML
    void EditInformationDone(ActionEvent event) {
        EventBus.getDefault().unregister(this);
        try {
            App.setRoot("SystemManager", 485, 410);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
