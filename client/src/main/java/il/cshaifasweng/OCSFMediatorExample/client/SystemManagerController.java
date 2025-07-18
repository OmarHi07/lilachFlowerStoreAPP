/**
 * Sample Skeleton for 'SystemManager.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class SystemManagerController {

    private ToggleGroup roleGroup;

    @FXML // fx:id="clearEmailButton"
    private Button clearEmailButton; // Value injected by FXMLLoader

    @FXML // fx:id="senEmailButton"
    private Button senEmailButton; // Value injected by FXMLLoader

    @FXML // fx:id="Customerphone"
    private TextField Customerphone; // Value injected by FXMLLoader

    @FXML // fx:id="blockButton"
    private Button blockButton; // Value injected by FXMLLoader

    @FXML // fx:id="branchManager"
    private Label branchManager; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader


    @FXML // fx:id="branchMangerButton"
    private RadioButton branchMangerButton; // Value injected by FXMLLoader

    @FXML // fx:id="customer"
    private Label customer; // Value injected by FXMLLoader

    @FXML // fx:id="customer1"
    private Label customer1; // Value injected by FXMLLoader

    @FXML // fx:id="customer2"
    private Label customer2; // Value injected by FXMLLoader

    @FXML // fx:id="customer3"
    private Label customer3; // Value injected by FXMLLoader

    @FXML // fx:id="customer4"
    private Label customer4; // Value injected by FXMLLoader

    @FXML // fx:id="customer5"
    private Label customer5; // Value injected by FXMLLoader

    @FXML // fx:id="customer6"
    private Label customer6; // Value injected by FXMLLoader

    @FXML // fx:id="customer7"
    private Label customer7; // Value injected by FXMLLoader

    @FXML // fx:id="customerButton"
    private RadioButton customerButton; // Value injected by FXMLLoader

    @FXML // fx:id="customerEmail"
    private TextField customerEmail; // Value injected by FXMLLoader

    @FXML // fx:id="customerFirstname"
    private TextField customerFirstname; // Value injected by FXMLLoader

    @FXML // fx:id="customerId"
    private TextField customerId; // Value injected by FXMLLoader

    @FXML // fx:id="customerLastName"
    private TextField customerLastName; // Value injected by FXMLLoader

    @FXML // fx:id="customerMessage"
    private TextArea customerMessage; // Value injected by FXMLLoader

    @FXML // fx:id="customerPassword"
    private TextField customerPassword; // Value injected by FXMLLoader

    @FXML // fx:id="customerSWButton"
    private RadioButton customerSWButton; // Value injected by FXMLLoader

    @FXML // fx:id="customerUsername"
    private TextField customerUsername; // Value injected by FXMLLoader

    @FXML // fx:id="logOutButton"
    private Button logOutButton; // Value injected by FXMLLoader

    @FXML // fx:id="networkMangerButton"
    private RadioButton networkMangerButton; // Value injected by FXMLLoader

    @FXML // fx:id="networkWorkerButton"
    private RadioButton networkWorkerButton; // Value injected by FXMLLoader

    @FXML // fx:id="newBranch"
    private TextField newBranch; // Value injected by FXMLLoader

    @FXML // fx:id="refreshButton"
    private Button refreshButton; // Value injected by FXMLLoader

    @FXML // fx:id="saveChangesButton"
    private Button saveChangesButton; // Value injected by FXMLLoader

    @FXML // fx:id="systemMnagerName"
    private Label systemMnagerName; // Value injected by FXMLLoader

    @FXML // fx:id="unblockButton"
    private Button unblockButton; // Value injected by FXMLLoader

    @FXML // fx:id="username"
    private TextField username; // Value injected by FXMLLoader

    @FXML // fx:id="usernames"
    private ScrollPane usernames; // Value injected by FXMLLoader

    @FXML // fx:id="usernamesLabel"
    private Label usernamesLabel; // Value injected by FXMLLoader

    @FXML // fx:id="worker1"
    private Label worker1; // Value injected by FXMLLoader

    @FXML // fx:id="worker2"
    private Label worker2; // Value injected by FXMLLoader

    @FXML // fx:id="worker3"
    private Label worker3; // Value injected by FXMLLoader

    @FXML // fx:id="worker4"
    private Label worker4; // Value injected by FXMLLoader

    @FXML // fx:id="workerName"
    private TextField workerName; // Value injected by FXMLLoader

    @FXML // fx:id="workerNewPassword"
    private TextField workerNewPassword; // Value injected by FXMLLoader

    @FXML // fx:id="workerNewusername"
    private TextField workerNewusername; // Value injected by FXMLLoader

    @FXML // fx:id="workerPermission"
    private TextField workerPermission; // Value injected by FXMLLoader

    @FXML
    private Button clearSelectionButton;



    // ========================= Initialization & Setup =========================


    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        turnoff();
        roleGroup = new ToggleGroup();

        customerButton.setToggleGroup(roleGroup);
        customerSWButton.setToggleGroup(roleGroup);
        branchMangerButton.setToggleGroup(roleGroup);
        networkMangerButton.setToggleGroup(roleGroup);
        networkWorkerButton.setToggleGroup(roleGroup);
    }

    // ========================= Event Handling =================================

    @Subscribe
    public void onBlockUserResponse(BlockUserResponse response) {
        Platform.runLater(() -> {
            showAlert(response.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    "Block Status", response.getMessage());
            refreshhelper();
        });
    }

    @Subscribe
    public void onGetEntitiesResponse(GetEntitiesResponse response) {
        Platform.runLater(() -> {
            System.out.println("Response received with " + response.getEntities().size() + " entities");
            List<?> entities = response.getEntities();

            // ✅ This was your silent failure:
            if (entities.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No entities found.");
                return;
            }


            Object first = entities.get(0);

            System.out.println(first.getClass());

            if (first instanceof Customer) {
                List<Customer> customers = (List<Customer>) (List<?>) entities;
                displayEntitiesInScrollPane(customers,
                        List.of("id", "firstName", "lastName", "username", "email", "phone", "identifyingNumber", "blocked"),
                        "ID\tFirst Name\tLast Name\tUsername\tEmail\tPhone\tID Number\tBlocked");

            }
//            else if (first instanceof Employee) {
//                List<Employee> employees = (List<Employee>) (List<?>) entities;
//                if(employees.get(0).getPermission() == 5) {
//                    displayEntitiesInScrollPane(employees,
//                       List.of("id", "name", "username", "password", "permission", "blocked"),
//                        "ID\tName\tUsername\tPassword\tPermission\tBlocked");
//                }
//
//            }
            else if (first instanceof NetworkWorker) {
                System.out.println("eee");
                List<NetworkWorker> workers = (List<NetworkWorker>) (List<?>) entities;
                displayEntitiesInScrollPane(workers,
                        List.of("id", "name", "username", "password", "permission", "blocked"),
                        "ID\tName\tUsername\tPassword\tPermission\tBlocked");

            } else if (first instanceof StoreChainManager) {
                List<StoreChainManager> managers = (List<StoreChainManager>) (List<?>) entities;
                displayEntitiesInScrollPane(managers,
                        List.of("id", "name", "username", "password", "permission", "blocked"),
                        "ID\tName\tUsername\tPassword\tPermission\tBlocked");

            } else if (first instanceof CostumerServiceEmployee) {
                List<CostumerServiceEmployee> cswList = (List<CostumerServiceEmployee>) (List<?>) entities;
                displayEntitiesInScrollPane(cswList,
                        List.of("id", "name", "username", "password", "permission", "blocked"),
                        "ID\tName\tUsername\tPassword\tPermission\tBlocked");

            } else if (first instanceof BranchManager) {
                List<BranchManager> branchManagers = (List<BranchManager>) (List<?>) entities;
                displayEntitiesInScrollPane(branchManagers,
                        List.of("id", "name", "username", "password", "permission", "blocked", "Branch"),
                        "ID\tName\tUsername\tPassword\tPermission\tBlocked\tBranch");

            }
        });

    }

    @Subscribe
    public void gotResponse2changes(UpdateUserResponse response) {
        Platform.runLater(() -> {showAlert(response.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, "Update User", response.getMessage());
            refreshhelper();
        });

    }

    // =========================== UI Action Handlers ===============================

    @FXML
    void clearSelection(ActionEvent event) {
        if (roleGroup != null) {
            roleGroup.selectToggle(null); // This clears the selection
            clearAllHelp();
            turnoff();
        }
    }

    @FXML
    void blockuser(ActionEvent event) {
        String selectedRole = getSelectedRole();
        String targetUsername = username.getText().trim();

        if (selectedRole == null || targetUsername.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Info", "Select role and enter username.");
            return;
        }

        try {
            BlockUserRequest request = new BlockUserRequest(selectedRole, targetUsername, true); // block = true
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send block request.");
            e.printStackTrace();
        }
    }


    @FXML
    void clearAll(ActionEvent event) {
        clearAllHelp();
    }

    @FXML
    void sendEmail(ActionEvent event) {
        String targetUsername = username.getText().trim();
        String messageText = customerMessage.getText();

        if (targetUsername.isEmpty() || messageText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please fill in both username and message.");
            return;
        }

        try {
            SendEmailRequest request = new SendEmailRequest(targetUsername, messageText);
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send email request to server.");
        }
    }


    @FXML
    void clearEmail(ActionEvent event) {
        customerMessage.clear();
    }

    @FXML
    void logOut(ActionEvent event) {

    }

    @FXML
    void refresh(ActionEvent event) {
        refreshhelper();
    }

    @FXML
    void saveButton(ActionEvent event) {
        String role = "";  // We detect from ToggleGroup
        if (roleGroup.getSelectedToggle() == customerButton) role = "customer";
        else if (roleGroup.getSelectedToggle() == customerSWButton) role = "costumerService";
        else if (roleGroup.getSelectedToggle() == networkWorkerButton) role = "networkWorker";
        else if (roleGroup.getSelectedToggle() == branchMangerButton) role = "branchManager";
        else if (roleGroup.getSelectedToggle() == networkMangerButton) role = "storeChainManager";
        else {
            showAlert(Alert.AlertType.ERROR, "No Role Selected", "Please select a role.");
            return;
        }

        String originalUsername = username.getText();

        //for customers
        String newUsername = customerUsername.getText();
        String firstName = customerFirstname.getText();
        String lastName = customerLastName.getText();
        String email = customerEmail.getText();
        String phone = Customerphone.getText();
        String password = customerPassword.getText();

        //for workers
        String newnwokername = workerName.getText();
        String newworkernewpassword = workerNewPassword.getText();
        String newworkernewusername = workerNewusername.getText();
        String newnewbranch = branchManager.getText();

        int newworkerpermission = -1 ;

        if (!(workerPermission.getText().isEmpty())) {
            newworkerpermission = Integer.parseInt(workerPermission.getText().trim());
        }

        UpdateUserRequest request = new UpdateUserRequest(
                role, originalUsername, newUsername, firstName, lastName, email,
                phone, password,newnwokername,newworkerpermission,newnewbranch,
                newworkernewpassword,newworkernewusername
        );

        try {
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Failed to send update request.");
        }
    }


    @FXML
    void switch2BM(ActionEvent event) {
        clearAllHelp();
        hideCustomerFields();
        showWorkerFields();
        branchManager.setVisible(true);
        newBranch.setVisible(true);

        saveChangesButton.setVisible(true);
        refreshButton.setVisible(true);
        clearButton.setVisible(true);

        usernamesLabel.setVisible(true);
        usernames.setVisible(true);

        // Clear the scroll pane before loading new data
        usernames.setContent(null);

        // Send request to get branch managers
        try {
            SimpleClient.getClient().sendToServer(new GetEntitiesRequest("branchManager"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switch2CSW(ActionEvent event) {
        clearAllHelp();

        hideCustomerFields();
        showWorkerFields();
        branchManager.setVisible(false);
        newBranch.setVisible(false);

        saveChangesButton.setVisible(true);
        refreshButton.setVisible(true);
        clearButton.setVisible(true);

        usernamesLabel.setVisible(true);
        usernames.setVisible(true);

        usernames.setContent(null);

        // Send request to get customer service workers
        try {
            SimpleClient.getClient().sendToServer(new GetEntitiesRequest("costumerService"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switch2NM(ActionEvent event) {
        clearAllHelp();

        hideCustomerFields();
        showWorkerFields();
        branchManager.setVisible(false);
        newBranch.setVisible(false);

        saveChangesButton.setVisible(true);
        refreshButton.setVisible(true);
        clearButton.setVisible(true);

        usernamesLabel.setVisible(true);
        usernames.setVisible(true);

        usernames.setContent(null);

        // Send request to get network managers (storeChainManagers)
        try {
            SimpleClient.getClient().sendToServer(new GetEntitiesRequest("storeChainManager"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switch2NW(ActionEvent event) {
        clearAllHelp();

        hideCustomerFields();
        showWorkerFields();
        branchManager.setVisible(false);
        newBranch.setVisible(false);

        saveChangesButton.setVisible(true);
        refreshButton.setVisible(true);
        clearButton.setVisible(true);

        usernamesLabel.setVisible(true);
        usernames.setVisible(true);

        usernames.setContent(null);

        // Send request to server for network workers
        try {
            SimpleClient.getClient().sendToServer(new GetEntitiesRequest("networkWorker"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToCustomer(ActionEvent event) {
        System.out.println("Customer button clicked!");  // ADD THIS
        clearAllHelp();
        // Show customer-related fields
        showCustomerFields();
        branchManager.setVisible(false);
        newBranch.setVisible(false);

        saveChangesButton.setVisible(true);
        refreshButton.setVisible(true);
        clearButton.setVisible(true);

        usernamesLabel.setVisible(true);
        usernames.setVisible(true);

        // Hide worker-related fields
        hideWorkerFields();

        // Clear the scroll pane content before loading customers
        usernames.setContent(null);

        // Call method to request customer list
        try {
            // Request the list of customers from the server
            SimpleClient.getClient().sendToServer(new GetEntitiesRequest("customer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void unBlockUser(ActionEvent event) {
        String selectedRole = getSelectedRole();
        String targetUsername = username.getText().trim();

        if (selectedRole == null || targetUsername.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Info", "Select role and enter username.");
            return;
        }

        try {
            BlockUserRequest request = new BlockUserRequest(selectedRole, targetUsername, false); // block = true
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send block request.");
            e.printStackTrace();
        }
    }


    // ================================= Helper Methods =================================

    private void turnoff() {
        clearAllHelp();
        hideCustomerFields();
        hideWorkerFields();
        usernamesLabel.setVisible(false);
        usernames.setVisible(false);
        branchManager.setVisible(false);
        newBranch.setVisible(false);
        customerMessage.setVisible(false);
        saveChangesButton.setVisible(false);
        refreshButton.setVisible(false);
        clearButton.setVisible(false);
        senEmailButton.setVisible(false);
        clearEmailButton.setVisible(false);
    }


    private String getSelectedRole() {
        if (roleGroup.getSelectedToggle() == customerButton) return "customer";
        if (roleGroup.getSelectedToggle() == customerSWButton) return "costumerService";
        if (roleGroup.getSelectedToggle() == networkWorkerButton) return "networkWorker";
        if (roleGroup.getSelectedToggle() == branchMangerButton) return "branchManager";
        if (roleGroup.getSelectedToggle() == networkMangerButton) return "storeChainManager";
        return null;
    }



    private void showAlert(Alert.AlertType type, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }



    private void clearAllHelp() {
        customerMessage.clear();
        customerFirstname.clear();
        customerId.clear();
        customerLastName.clear();
        customerEmail.clear();
        customerId.clear();
        customerPassword.clear();
        customerUsername.clear();
        Customerphone.clear();
        workerName.clear();
        workerNewPassword.clear();
        workerNewusername.clear();
        workerPermission.clear();
        newBranch.clear();
        username.clear();
    }




    private <T> void displayEntitiesInScrollPane(List<T> entities, List<String> fieldsToShow, String headerTitle) {
        GridPane grid = new GridPane();
        grid.setHgap(15); // horizontal spacing between columns
        grid.setVgap(5);  // vertical spacing between rows
        grid.setStyle("-fx-padding: 10;");

        // === Header Row ===
        for (int col = 0; col < fieldsToShow.size(); col++) {
            String title = fieldsToShow.get(col);
            Label headerLabel = new Label(title.toUpperCase());
            headerLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #c7bca6; -fx-padding: 4;");
            grid.add(headerLabel, col, 0);
        }

        // === Data Rows ===
        for (int row = 0; row < entities.size(); row++) {
            T entity = entities.get(row);
            for (int col = 0; col < fieldsToShow.size(); col++) {
                String fieldName = fieldsToShow.get(col);
                try {
                    Field f = getFieldFromClassHierarchy(entity.getClass(), fieldName);
                    f.setAccessible(true);

                    Object value = f.get(entity);

                    // Handle special types
                    if (value instanceof Branch ) {
                        Branch branch = (Branch) value;
                        value = branch.getAddress(); // or branch.getAddress() + " (ID: " + branch.getId() + ")";
                    } else if (value instanceof Boolean ) {
                        Boolean bool = (Boolean) value;
                        value = bool ? "Yes" : "No";
                    }

                    Label cell = new Label(value != null ? value.toString() : "null");
                    cell.setWrapText(true);
                    cell.setMaxWidth(150);
                    cell.setStyle("-fx-padding: 3; -fx-background-color: #c7bca6;");

                    grid.add(cell, col, row + 1); // +1 to account for header
                } catch (Exception e) {
                    Label cell = new Label("N/A");
                    grid.add(cell, col, row + 1);
                }
            }
        }

        usernames.setContent(grid);
    }

    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + fieldName + " not found in class hierarchy.");
    }



    private void refreshhelper() {
        if (roleGroup == null || roleGroup.getSelectedToggle() == null) {
            return; // No selection made
        }

        Object selected = roleGroup.getSelectedToggle();

        try {
            if (selected == customerButton) {
                SimpleClient.getClient().sendToServer(new GetEntitiesRequest("customer"));
            } else if (selected == customerSWButton) {
                SimpleClient.getClient().sendToServer(new GetEntitiesRequest("costumerService"));
            } else if (selected == branchMangerButton) {
                SimpleClient.getClient().sendToServer(new GetEntitiesRequest("branchManager"));
            } else if (selected == networkMangerButton) {
                SimpleClient.getClient().sendToServer(new GetEntitiesRequest("storeChainManager"));
            } else if (selected == networkWorkerButton) {
                SimpleClient.getClient().sendToServer(new GetEntitiesRequest("networkWorker"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void hideCustomerFields() {
        customer.setVisible(false);
        customer1.setVisible(false);
        customer2.setVisible(false);
        customer3.setVisible(false);
        customer4.setVisible(false);
        customer5.setVisible(false);
        customer6.setVisible(false);
        customer7.setVisible(false);

        customerFirstname.setVisible(false);
        customerLastName.setVisible(false);
        customerId.setVisible(false);
        customerUsername.setVisible(false);
        customerPassword.setVisible(false);
        customerEmail.setVisible(false);
        Customerphone.setVisible(false);

        senEmailButton.setVisible(false);
        clearEmailButton.setVisible(false);

        customerMessage.setVisible(false);
    }


    private void showCustomerFields() {
        customer.setVisible(true);
        customer1.setVisible(true);
        customer2.setVisible(true);
        customer3.setVisible(true);
        customer4.setVisible(true);
        customer5.setVisible(true);
        customer6.setVisible(true);
        customer7.setVisible(true);

        customerFirstname.setVisible(true);
        customerLastName.setVisible(true);
        customerId.setVisible(true);
        customerUsername.setVisible(true);
        customerPassword.setVisible(true);
        customerEmail.setVisible(true);
        Customerphone.setVisible(true);

        senEmailButton.setVisible(true);
        clearEmailButton.setVisible(true);

        customerMessage.setVisible(true);

    }


    private void showWorkerFields() {
        worker1.setVisible(true);
        worker2.setVisible(true);
        worker3.setVisible(true);
        worker4.setVisible(true);

        workerName.setVisible(true);
        workerNewusername.setVisible(true);
        workerNewPassword.setVisible(true);
        workerPermission.setVisible(true);

    }


    private void hideWorkerFields() {
        worker1.setVisible(false);
        worker2.setVisible(false);
        worker3.setVisible(false);
        worker4.setVisible(false);

        workerName.setVisible(false);
        workerNewusername.setVisible(false);
        workerNewPassword.setVisible(false);
        workerPermission.setVisible(false);
    }

}
