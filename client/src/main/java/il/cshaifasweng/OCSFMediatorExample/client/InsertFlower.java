/**
 * Sample Skeleton for 'InsertFlower.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InsertFlower {

    @FXML // fx:id="AlertBranch"
    private Text AlertBranch; // Value injected by FXMLLoader

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

    @FXML // fx:id="Branches"
    private TextField Branches; // Value injected by FXMLLoader

    @FXML // fx:id="Color"
    private TextField Color; // Value injected by FXMLLoader

    @FXML // fx:id="ImageBU"
    private Button ImageBU; // Value injected by FXMLLoader

    @FXML // fx:id="Name"
    private TextField Name; // Value injected by FXMLLoader

    @FXML // fx:id="Price"
    private TextField Price; // Value injected by FXMLLoader

    @FXML // fx:id="Sale"
    private TextField Sale; // Value injected by FXMLLoader

    @FXML // fx:id="Type"
    private TextField Type; // Value injected by FXMLLoader

    private byte[] selectedImageBytes;

    @FXML
    void initialize(){
       // EventBus.getDefault().register(this);
        AlertColor.setVisible(false);
        AlertImage.setVisible(false);
        AlertName.setVisible(false);
        AlertPrice.setVisible(false);
        AlertType.setVisible(false);
        AlertBranch.setVisible(false);
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

    public Branch getBranchByName(String nameBranch) {
        TypedQuery<Branch> query = entityManager.createQuery(
                "SELECT b FROM Branch b WHERE b.address = :name", Branch.class);
        query.setParameter("name", nameBranch);

        Branch branch = query.getSingleResult(); // זורק חריגה אם לא נמצא
        return branch;
    }

    void Clear(){
        Platform.runLater(()->{
            Color.clear();
            Name.clear();
            Price.clear();
            Sale.clear();
            Type.clear();
            Branches.clear();
        });
        selectedImageBytes = null;
    }

    @FXML
    void SaveFlower(ActionEvent event) {
        String color = Color.getText();
        String type = Type.getText();
        String name = Name.getText();
        String price = Price.getText();
        double priceValue = Double.parseDouble(price);
        String sale = Sale.getText();
        byte[] image = ImageBU.getText().getBytes();
        if(Color.getText().isEmpty()){
            AlertColor.setVisible(true);
        }
        else if(Type.getText().isEmpty()){
            AlertType.setVisible(true);
        }
        else if(Name.getText().isEmpty()){
            AlertName.setVisible(true);
        }
        else if(Price.getText().isEmpty()){
            AlertPrice.setVisible(true);
        }
        else if(selectedImageBytes == (null)){
            AlertImage.setVisible(true);
        }
        else{
            Flower flower = new Flower(name, type, priceValue, selectedImageBytes, color, 1);
            if(!(sale.isEmpty())){
                flower.setSale(Integer.parseInt(sale));
            }
            if(!Branches.getText().isEmpty()) {
                if (Branches.getText().equals("Haifa")) {
                    Branch branch = getBranchByName("Haifa");
                    flower.AddBranch(branch);
                }
                else if (Branches.getText().equals("TelAviv")){
                    Branch branch = getBranchByName("TelAviv");
                    flower.AddBranch(branch);
                }
                else if (Branches.getText().contains(",")){
                    String branchesTxt = Branches.getText();
                    branchesTxt = branchesTxt.replaceAll("\\s+", "");
                    String[] parts = Branches.getText().split(",");
                    if(parts.length == 2 && ((parts[0].equals("Haifa") && parts[1].equals("TelAviv")) || (parts[0].equals("TelAviv") && parts[1].equals("Haifa")))){
                        String branch1 = parts[0];
                        String branch2 = parts[1];
                        Branch branch11 = getBranchByName(branch1);
                        Branch branch12 = getBranchByName(branch2);
                        flower.AddBranch(branch11);
                        flower.AddBranch(branch12);
                        try {
                            SimpleClient.getClient().sendToServer(flower);
                            SimpleClient.getFlowers().add(flower);
                            Clear();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        AlertBranch.setVisible(true);
                    }
                }
                else{
                    String branchesTxt = Branches.getText();
                    String[] parts = Branches.getText().split(" ");
                    if(parts.length == 2 && ((parts[0].equals("Haifa") && parts[1].equals("TelAviv")) || (parts[0].equals("TelAviv") && parts[1].equals("Haifa")))){
                        String branch1 = parts[0];
                        String branch2 = parts[1];
                        Branch branch11 = getBranchByName(branch1);
                        Branch branch12 = getBranchByName(branch2);
                        flower.AddBranch(branch11);
                        flower.AddBranch(branch12);
                        try {
                            SimpleClient.getClient().sendToServer(flower);
                            SimpleClient.getFlowers().add(flower);
                            Clear();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        AlertBranch.setVisible(true);
                    }
                }

            }
            else {
                try {
                    SimpleClient.getClient().sendToServer(flower);
                   //תתקני את זה לכל ה Client
                    Clear();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }

        }

    }
    @FXML
    void LogOut(ActionEvent event) {

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
