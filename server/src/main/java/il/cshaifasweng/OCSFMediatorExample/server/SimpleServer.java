package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.AddClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static il.cshaifasweng.OCSFMediatorExample.server.App.instance;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if(msgString.startsWith("add client")){
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);
			AddClient testClient = new AddClient();
			System.out.println(testClient);
			NetworkWorker user = instance.getUser(1);
			AddClient client1 = null;
			List<Flower> flowerList=null;
			List<Branch> branchList=null;
			try {
				flowerList = instance.getAllFlowers();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				branchList = instance.getAllBranches();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			System.out.println("Sending AddClient with flowerList size: " + (flowerList == null ? "null" : flowerList.size()));
			System.out.println("Sending AddClient with branchList size: " + (branchList == null ? "null" : branchList.size()));
			client1 = new AddClient();
			client1.setBranchList(branchList);
			client1.setFlowerList(flowerList);
			client1.setEmployee(user);
			if (client1 != null) {
				try {
					client.sendToClient(client1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(msgString.startsWith("add client1")){
			try {
				System.out.println("Hello from Java!");
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

		else if (msgString.startsWith("dbpassword:")) {
			String[] parts = msgString.split(":");
			String dbPassword = parts[1];
		}


		//Added by arkan
		else if (msgString.startsWith("Delete")){
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[1]);
            instance.deleteFlower(flowerId);
			sendToAllClients(msgString);
		}


		else if (msgString.startsWith("Change Sale")){
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[2]);
			int Sale = Integer.parseInt(parts[1]);
			instance.PutSale(flowerId, Sale);
			sendToAllClients(msgString);
		}
		else if (msgString.startsWith("Remove Sale")){
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[1]);
			instance.PutSale(flowerId, 0);
			sendToAllClients(msgString);
		}
		else if(msg instanceof ChangeFlower){
			ChangeFlower newFlower = (ChangeFlower)msg;
			Flower flower = instance.ChangeDetails(newFlower);
			sendToAllClients(flower);
		}

		else if (msg instanceof Flower){
			Flower flower = (Flower)msg;
			Flower flower1 = instance.addFlower(flower);
			AddFlower newFlower = new AddFlower(flower1);
			sendToAllClients(newFlower);
		}
		//check if username already in use
		else if (msg instanceof UsernameCheckRequest ) {
			UsernameCheckRequest request = (UsernameCheckRequest) msg;
			boolean isTaken = DataBaseManagement.isUsernameTaken(request.getUsername());

			UsernameCheckRequest response=new UsernameCheckRequest(request.getUsername(), isTaken); // boolean flag: true if taken
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		//Add customer to the table
		else if (msg instanceof SignUpRequest) {
			SignUpRequest request = (SignUpRequest) msg;
			SignUpResponse response = DataBaseManagement.registerCustomer(request);
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
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
