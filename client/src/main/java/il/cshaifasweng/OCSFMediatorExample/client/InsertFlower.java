/**
 * Sample Skeleton for 'InsertFlower.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import il.cshaifasweng.OCSFMediatorExample.entities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.StoreChainManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class InsertFlower {

    @FXML // fx:id="AlertColor"
    private Text AlertColor; // Value injected by FXMLLoader

    @FXML // fx:id="AlertImage"
    private Text AlertImage; // Value injected by FXMLLoader

    @FXML // fx:id="AlertName"
    private Text AlertName; // Value injected by FXMLLoader

    @FXML // fx:id="AlertPrice"
    private Text AlertPrice; // Value injected by FXMLLoader

    @FXML // fx:id="AlertType"
    private Text AlertType; // Value injected by FXMLLoader

    @FXML // fx:id="SingleFlower"
    private RadioButton SingleFlower; // Value injected by FXMLLoader

    @FXML // fx:id="Bouquet"
    private RadioButton Bouquet; // Value injected by FXMLLoader

    @FXML // fx:id="AlertTT"
    private Text AlertTT; // Value injected by FXMLLoader

    @FXML // fx:id="ImageBU"
    private Button ImageBU; // Value injected by FXMLLoader

    private List<Branch> branchesAvaliable;

    @FXML // fx:id="Name"
    private TextField Name; // Value injected by FXMLLoader

    @FXML // fx:id="Price"
    private TextField Price; // Value injected by FXMLLoader

    @FXML // fx:id="Sale"
    private TextField Sale; // Value injected by FXMLLoader

    @FXML // fx:id="Type"
    private TextField Type; // Value injected by FXMLLoader

    @FXML // fx:id="Blue"
    private RadioButton Blue; // Value injected by FXMLLoader

    @FXML // fx:id="Green"
    private RadioButton Green; // Value injected by FXMLLoader

    @FXML // fx:id="Red"
    private RadioButton Red; // Value injected by FXMLLoader

    @FXML // fx:id="Pink"
    private RadioButton Pink; // Value injected by FXMLLoader

    @FXML // fx:id="White"
    private RadioButton White; // Value injected by FXMLLoader

    @FXML // fx:id="haifa"
    private RadioButton haifa; // Value injected by FXMLLoader

    @FXML // fx:id="telaviv"
    private RadioButton telaviv; // Value injected by FXMLLoader

    @FXML // fx:id="yellow"
    private RadioButton yellow; // Value injected by FXMLLoader

    private ToggleGroup TypeOfFlower;
    private String selectedColor;
    private int TypeOfFlowerSelected;
    private byte[] selectedImageBytes;
    private ToggleGroup colorGroup;

    @FXML
    void initialize(){
       // EventBus.getDefault().register(this);
        TypeOfFlowerSelected = 0;
        colorGroup = new ToggleGroup();
        TypeOfFlower = new ToggleGroup();
        branchesAvaliable = new ArrayList<>();
        SingleFlower.setToggleGroup(TypeOfFlower);
        Bouquet.setToggleGroup(TypeOfFlower);
        Blue.setToggleGroup(colorGroup);
        Green.setToggleGroup(colorGroup);
        Red.setToggleGroup(colorGroup);
        Pink.setToggleGroup(colorGroup);
        White.setToggleGroup(colorGroup);
        yellow.setToggleGroup(colorGroup);
        AlertColor.setVisible(false);
        AlertImage.setVisible(false);
        AlertName.setVisible(false);
        AlertPrice.setVisible(false);
        AlertType.setVisible(false);
        selectedImageBytes = null;
    }

    @FXML
    void ImageFlower(ActionEvent event) {
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

    private EntityManager entityManager;

    @FXML
    void BlueColor(ActionEvent event) {
        selectedColor = "Blue";
    }

    @FXML
    void GreenColor(ActionEvent event) {
        selectedColor = "Green";
    }

    @FXML
    void InHaifa(ActionEvent event) {
        List<Branch> branchesList = SimpleClient.getAllBranches();
        Branch branch = branchesList.stream().filter(branch1 -> branch1.getAddress().equals("Haifa")).findFirst().orElse(null);
        if(haifa.isSelected()){
            if(!branchesAvaliable.contains(branch)){
                branchesAvaliable.add(branch);
            }
        }
        else {
            if(branchesAvaliable.contains(branch)) {
                branchesAvaliable.remove(branch);
            }
        }
    }

    @FXML
    void InTelAviv(ActionEvent event) {
        List<Branch> branchesList = SimpleClient.getAllBranches();
        Branch branch = branchesList.stream().filter(branch1 -> branch1.getAddress().equals("TelAviv")).findFirst().orElse(null);
        if(telaviv.isSelected()){
            if(!branchesAvaliable.contains(branch)){
                branchesAvaliable.add(branch);
            }
        }
        else {
            if(branchesAvaliable.contains(branch)) {
                branchesAvaliable.remove(branch);
            }
        }

    }


    @FXML
    void SingleType(ActionEvent event) {
         TypeOfFlowerSelected = 2;
    }

    @FXML
    void BouquetType(ActionEvent event) {
         TypeOfFlowerSelected = 1;
    }


    @FXML
    void PinkColor(ActionEvent event) {
        selectedColor = "Pink";
    }

    @FXML
    void RedColor(ActionEvent event) {
        selectedColor = "Red";
    }

    @FXML
    void WhiteColor(ActionEvent event) {
        selectedColor = "White";
    }

    @FXML
    void yellowcolor(ActionEvent event) {
        selectedColor = "Yellow";
    }

    void Clear(){
        Platform.runLater(()->{
            AlertTT.setText(null);
            AlertName.setText(null);
            AlertPrice.setText(null);
            TypeOfFlowerSelected =0;
            AlertType.setText(null);
            AlertColor.setText(null);
            AlertImage.setText(null);
            haifa.setSelected(false);
            Blue.setSelected(false);
            Green.setSelected(false);
            Red.setSelected(false);
            Pink.setSelected(false);
            White.setSelected(false);
            yellow.setSelected(false);
            telaviv.setSelected(false);
            Name.clear();
            Price.clear();
            Sale.clear();
            Type.clear();
        });
        selectedImageBytes = null;
    }

    @FXML
    void SaveFlower(ActionEvent event) {
        String type = Type.getText();
        String name = Name.getText();
        String price = Price.getText();
        double priceValue = Double.parseDouble(price);
        String sale = Sale.getText();
        byte[] image = ImageBU.getText().getBytes();
        if(Type.getText().isEmpty()){
            AlertType.setVisible(true);
        }
        if(Name.getText().isEmpty()){
            AlertName.setVisible(true);
        }
        if(Price.getText().isEmpty()){
            AlertPrice.setVisible(true);
        }
        if(selectedImageBytes == (null)){
            AlertImage.setVisible(true);
        }
        if(TypeOfFlowerSelected==0){
           AlertTT.setVisible(true);
        }

        else{
            Flower flower = new Flower(name, type, priceValue, selectedImageBytes, selectedColor, TypeOfFlowerSelected);
            flower.setSaleBranchHaifaTelAviv(0);
            flower.setSaleBranchHaifa(0);
            flower.setSaleBranchTelAviv(0);
            if(!(sale.isEmpty())){
                flower.setSale(Integer.parseInt(sale));
            }
            else {
                flower.setSale(0);
            }
            if(!branchesAvaliable.isEmpty()){
                flower.setBranch(branchesAvaliable);
            }
            try {
                    SimpleClient.getClient().sendToServer(flower);
                    Clear();
            }
            catch(Exception e){
                    e.printStackTrace();
            }
        }

    }
    @FXML
    void LogOut(ActionEvent event) {
        Object Current = CurrentCustomer.getCurrentUser();
        String NameClass;
        FlowerCardCache.clear();
        int id;
        if(Current instanceof Customer){
            NameClass = "Customer";
            id = ((Customer) Current).getId();
        }
        else {
            NameClass = "Employee";
            id = ((StoreChainManager) Current).getId();
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
    void BackToCataloge(ActionEvent event) {
        try {
            App.setRoot("primary", 900, 730);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
