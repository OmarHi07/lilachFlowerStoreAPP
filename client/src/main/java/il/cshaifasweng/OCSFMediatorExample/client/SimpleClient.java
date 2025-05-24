package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if(msg instanceof List<?>){
            List<Flower> msgList = (List<Flower>) msg;
			EventBus.getDefault().post(msgList);
		}
		else if(msg instanceof Flower){
             Flower flower = (Flower) msg;
			 FlowerHolder.CurrentFlower = flower;
			 try {
				 App.setRoot("secondary");
			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		}

		String msgString = msg.toString();
		if(msgString.startsWith("change:")){
			String[] msgParts = msgString.split(",");
			int newPrice = Integer.parseInt(msgParts[1]);
			int Id = Integer.parseInt(msgParts[2]);
			ChangePrice changePrice = new ChangePrice(Id, newPrice);
			EventBus.getDefault().post(changePrice);
		}
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else{
			String message = msg.toString();
			System.out.println(message);
		}

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
