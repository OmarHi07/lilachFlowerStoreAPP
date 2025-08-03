
package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import java.io.IOException;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.LocalTime;
import org.greenrobot.eventbus.EventBus;

import org.greenrobot.eventbus.Subscribe;
import java.util.List;

import java.util.stream.Collectors;



public class ComplaintHandlingController
{
    @FXML
    private TableView<Complain> complaintsTable;

    @FXML
    private TableColumn<Complain, Integer> idColumn;

    @FXML
    private TableColumn<Complain, String> textColumn;

    @FXML
    private TableColumn<Complain, LocalDate> dateColumn;

    @FXML
    private TableColumn<Complain, LocalTime> timeColumn;

    @FXML
    private TableColumn<Complain, Boolean> statusColumn;

    @FXML
    private TextArea answerArea;

    @FXML
    private TextField refundField;

    @FXML
    private Button sendAnswerButton;

    @FXML
    private Button backButton;

    @FXML
    private Label confirmationLabel;

    //intalizing the table
    @FXML
    public void initialize() {

        EventBus.getDefault().register(this);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("complain_text"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // initialize with empty list for now
        complaintsTable.setItems(FXCollections.observableArrayList());

        //to clear the message
        answerArea.textProperty().addListener((obs, oldText, newText) -> {
            confirmationLabel.setText("");
        });

        //here we telling the server  please send me the list of all complaints
        try {
            SimpleClient.getClient().sendToServer("get complaints");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // fills the table with the unprocessed complaint list.
    @Subscribe
    public void onComplaintsReceived(List<Complain> complaints) {
        Platform.runLater(() -> {
            List<Complain> unhandled = complaints.stream()
                    .filter(c -> !c.getStatus())
                    .collect(Collectors.toList());//here we do filtring to fill the table with only the unhandled complaints
            complaintsTable.setItems(FXCollections.observableArrayList(unhandled));
        });
        System.out.println("Received unhandled complaints: " + complaintsTable.getItems().size());
    }

    //handeler for the sendanswer button
    //it work when you click on the complaint and write the answer in the textarea
    @FXML
    void sendAnswerToServer() {
        boolean Yes = true;
        Complain selected = complaintsTable.getSelectionModel().getSelectedItem();
        String answer = answerArea.getText();
        String refundText = refundField.getText();
        if (selected == null) {
            confirmationLabel.setText("no complaint selected");
            confirmationLabel.setStyle("-fx-text-fill: #FF0000;");
            Yes = false;
        }
        if (answer.isEmpty()) {
            confirmationLabel.setText("no answer typed");
            confirmationLabel.setStyle("-fx-text-fill: #FF0000;");
            Yes = false;

        }
        if (refundText.isEmpty()) {
            confirmationLabel.setText("no refund selected");
            confirmationLabel.setStyle("-fx-text-fill: #FF0000;");
            Yes = false;
        }
        if(Yes) {
            try {
                double refund = Double.parseDouble(refundText);
                selected.setAnswer_text(answer);
                selected.setRefund(refund);
                selected.setStatus(true);

                // send the update to the server
                SimpleClient.getClient().sendToServer(selected);
                answerArea.setText(null);
                refundField.setText(null);
                System.out.println("Answer sent for complaint id: " + selected.getId());

                // **remove it from the UI** so it disappears immediately:
                complaintsTable.getItems().remove(selected);
                confirmationLabel.setText("✅ Answer submitted successfully!");//show successfull message if the worker send mwssage
                confirmationLabel.setStyle("-fx-text-fill: #6b8e23;");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    @FXML
    void backTo(ActionEvent event) {//to return to the previous screen
        try {
            App.setRoot("primary", 400, 600);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



}