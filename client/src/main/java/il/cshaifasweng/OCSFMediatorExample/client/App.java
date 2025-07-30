package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("1111111111111111111111");
        EventBus.getDefault().register(this);
        primaryStage = stage;
        System.out.println("222222222222222222222");
        // ✅ חיבור לשרת והגדרת לקוח סטטי
        SimpleClient client = new SimpleClient("localhost", 3001); // שנה פורט אם צריך
        client.openConnection();
        SimpleClient.setClient(client);
        System.out.println("333333333333333333333333333");
        scene = new Scene(loadFXML("Reports"), 996, 731);

        stage.setScene(scene);
        stage.setTitle("Connect to Server");
        stage.show();
        System.out.println("444444444444444444444444");
    }

        private static Parent loadFXML(String fxml) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        }

        static void setRoot(String fxml, double width, double height) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }


    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        if(SimpleClient.getClient().isConnected()) {
            SimpleClient  client = SimpleClient.getClient();
            client.sendToServer("remove client");
            client.closeConnection();
        }
        super.stop();
    }


    
    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});
    	
    }

	public static void main(String[] args) {
        launch();
    }

}