package il.cshaifasweng.OCSFMediatorExample.server;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.AddClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import org.hibernate.Hibernate;

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
		if (msgString.startsWith("add client")) {
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);
			AddClient testClient = new AddClient();
			AddClient client1 = null;
			List<Flower> flowerList = null;
			List<Branch> branchList = null;
			try {
				flowerList = instance.getAllFlowers();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				branchList = instance.getAllBranches();
			} catch (Exception e) {
				e.printStackTrace();
			}
			client1 = new AddClient();
			client1.setBranchList(branchList);
			client1.setFlowerList(flowerList);
			if (client1 != null) {
				try {
					client.sendToClient(client1);
					System.out.println("AddClient message sent successfully.");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (msgString.startsWith("add client1")) {
			try {
				System.out.println("Hello from Java!");
				List<Flower> flowerList = instance.getAllFlowers();
				client.sendToClient(flowerList);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else if (msgString.startsWith("remove client")) {
			if (!SubscribersList.isEmpty()) {
				for (SubscribedClient subscribedClient : SubscribersList) {
					if (subscribedClient.getClient().equals(client)) {
						SubscribersList.remove(subscribedClient);
						break;
					}
				}
			}
		} else if (msgString.startsWith("dbpassword:")) {
			String[] parts = msgString.split(":");
			String dbPassword = parts[1];
		}
		else if(msgString.startsWith("log out")) {
			String[] parts = msgString.split(",");
			int id = Integer.parseInt(parts[2]);
			String Class = parts[1];
			if(Class.equals("Employee")) {
				instance.LogOutEmployee(id);
			}
			else if (Class.equals("Customer")) {
				instance.LogOutCustomer(id);
			}
		}


		//Added by arkan
		else if (msgString.startsWith("Delete")) {
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[1]);
			instance.deleteFlower(flowerId);
			sendToAllClients(msgString);
		} else if (msg instanceof LoginRequest) {
			LoginRequest request = (LoginRequest) msg;
			LoginResponse response;

			if ("employee".equals(request.getUserType())) {
				response = instance.loginEmployee(request.getUsername(), request.getPassword());
			} else {
				response = instance.loginCustomer(request.getUsername(), request.getPassword());
			}
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msgString.startsWith("Change Sale")) {
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[2]);
			int Sale = Integer.parseInt(parts[1]);
			instance.PutSale(flowerId, Sale);
			sendToAllClients(msgString);
		} else if (msgString.startsWith("Remove Sale")) {
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[1]);
			instance.PutSale(flowerId, 0);
			sendToAllClients(msgString);
		} else if (msg instanceof ChangeFlower) {
			ChangeFlower newFlower = (ChangeFlower) msg;
			Flower flower = instance.ChangeDetails(newFlower);
			sendToAllClients(flower);
		} else if (msg instanceof Flower) {
			Flower flower = (Flower) msg;
			Flower flower1 = instance.addFlower(flower);
			AddFlower newFlower = new AddFlower(flower1);
			sendToAllClients(newFlower);
		}
		//check if username already in use
		else if (msg instanceof UsernameCheckRequest) {
			UsernameCheckRequest request = (UsernameCheckRequest) msg;
			boolean isTaken = DataBaseManagement.isUsernameTaken(request.getUsername());

			UsernameCheckRequest response = new UsernameCheckRequest(request.getUsername(), isTaken); // boolean flag: true if taken
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		else if (msgString.startsWith("Give Orders")){
			String[] parts = msgString.split(",");
			int id = Integer.parseInt(parts[1]);
			List<Order> Orders1= instance.getUser(id);
			if (Orders1.isEmpty()||Orders1==null) {
				System.out.println("Orders1 is empty");
			}
			try{
				client.sendToClient(Orders1);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		else if (msgString.startsWith("delete Order")) {
			String[] parts = msgString.split(",");
			int id = Integer.parseInt(parts[1]);
			int orderId = Integer.parseInt(parts[2]);

			List<Order> ordersList = instance.getUser(id); // שליפת ההזמנות של המשתמש
			Order orderToDelete = null;

			if (ordersList != null) {
				for (Order order : ordersList) {
					if (order.getId() == orderId) {
						orderToDelete = order;
						break;
					}
				}
			}

			if (orderToDelete != null) {
				// מחיקה מהמסד נתונים
				instance.deleteorder(orderToDelete);
			}

			try {
				List<Order> ordersList1 = instance.getUser(id);
				client.sendToClient(ordersList1); // שלח חזרה את הרשימה לאחר עדכון
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



		//Add customer to the table
		//Add customer to the table
		else if (msg instanceof SignUpRequest) {
			SignUpRequest request = (SignUpRequest) msg;
			SignUpResponse response = instance.registerCustomer(request);
			try {
				if (response.isSuccess()) {
					// Send confirmation email
					EmailSender.sendEmail(request.getEmail(), "Welcome to Lelac Stores!","Dear " + request.getFirstName() + ",\n\nThank you for signing up at Lelac Stores! We're happy to have you on board.\n\nHappy shopping! 💐");
				}
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	    else if (msg instanceof GetEntitiesRequest) {
			GetEntitiesRequest request = (GetEntitiesRequest) msg;
			String type = request.getEntityType();
			try {
				List<?> result = instance.getAllEntities(type);  // ✅ Use the new helper
				client.sendToClient(new GetEntitiesResponse(result));
			} catch (Exception e) {
				e.printStackTrace();
				// Optionally send an error response to client
			}

		}
		else if (msg instanceof SendEmailRequest) {
			SendEmailRequest request = (SendEmailRequest) msg;

			String username = request.getUsername();
			String message = request.getMessage();

			// Example: search in customer table
			Object customerObj = instance.findUserByUsername("customer", request.getUsername()); // You implement this method
			Customer customer = (Customer) customerObj;
			if (customer != null) {
				String email = customer.getEmail();
				String subject = "Message from Lelac System Manager";
				EmailSender.sendEmail(email, subject, message);
				System.out.println("Email sent to " + email);
			} else {
				System.out.println("Username not found: " + username);
				// Optionally send an error response back
			}
		}
		else if (msg instanceof UpdateUserRequest) {
			UpdateUserRequest request = (UpdateUserRequest) msg;
			UpdateUserResponse response = instance.updateUser(request);
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		else if (msg instanceof BlockUserRequest) {
			BlockUserRequest request = (BlockUserRequest) msg;
			BlockUserResponse response = instance.handleBlockUser(request);
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		else if(msg instanceof Order){
			Order order = (Order) msg;
			instance.saveorder(order);

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
