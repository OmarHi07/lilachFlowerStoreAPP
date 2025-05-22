package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import static il.cshaifasweng.OCSFMediatorExample.server.App.instance;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if (msgString.startsWith("image1")) {
			Flower flower1 = instance.getFlower(1);
			try {
				client.sendToClient(flower1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msgString.startsWith("image2")) {
			Flower flower2 = instance.getFlower(2);
			try {
				client.sendToClient(flower2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msgString.startsWith("image3")) {
			Flower flower3 = instance.getFlower(3);
			try {
				client.sendToClient(flower3);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msgString.startsWith("image4")) {
			Flower flower4 = instance.getFlower(4);
			try {
				client.sendToClient(flower4);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msgString.startsWith("image5")) {
			Flower flower5 = instance.getFlower(5);
			try {
				client.sendToClient(flower5);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(msgString.startsWith("add client")){
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);

			try {
				List<Flower> flowerList = instance.getAllFlowers();
				client.sendToClient(flowerList);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		else if(msgString.startsWith("remove client")){
			if(!SubscribersList.isEmpty()){
				for(SubscribedClient subscribedClient: SubscribersList){
					if(subscribedClient.getClient().equals(client)){
						SubscribersList.remove(subscribedClient);
						break;
					}
				}
			}
		}
		else if(msgString.startsWith("change")){
			String[] parts = msgString.split(",");

			int newPrice = Integer.parseInt(parts[1]);  // parts[1] = "150"
			int flowerId = Integer.parseInt(parts[2]);  // parts[2] = "1"

			instance.changePrice(flowerId, newPrice);

			try {
				client.sendToClient(msgString);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	public void sendToAllClients(String message) {
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
