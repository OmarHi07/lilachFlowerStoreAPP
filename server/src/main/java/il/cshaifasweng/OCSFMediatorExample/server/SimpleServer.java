package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZoneId;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import il.cshaifasweng.OCSFMediatorExample.entities.AddClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import static il.cshaifasweng.OCSFMediatorExample.server.App.instance;

public class SimpleServer extends AbstractServer {
	private static final ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	// === Auto-Responder Thread ===
	private volatile boolean complaintsMonitorRunning = false;
	private Thread complaintsMonitorThread;

	public SimpleServer(int port) {
		super(port);
	}

	@Override
	protected void serverStarted() {
		super.serverStarted();
		startComplaintMonitor();
	}

	@Override
	protected void serverStopped() {
		super.serverStopped();
		stopComplaintMonitor();
	}


	// ===== Complaints monitor =====
	private void startComplaintMonitor() {
		if (complaintsMonitorRunning) return;
		complaintsMonitorRunning = true;

		complaintsMonitorThread = new Thread(this::complaintsMonitorLoop, "complaints-monitor");
		complaintsMonitorThread.setDaemon(true); // שלא יחסום יציאה מהתהליך
		complaintsMonitorThread.start();
	}

	private void stopComplaintMonitor() {
		complaintsMonitorRunning = false;
		if (complaintsMonitorThread != null) {
			complaintsMonitorThread.interrupt();
		}
	}

	private void complaintsMonitorLoop() {
		final ZoneId tz = ZoneId.of("Asia/Jerusalem");
		final String AUTO_MARK = "[AUTO] ";

		while (complaintsMonitorRunning) {
			try {
				LocalDateTime now = LocalDateTime.now(tz);
				LocalDateTime threshold = now.minusHours(24);

				// מביאים מה-DB את כל התלונות (פשוט) — אפשר אח"כ לייעל ל-HQL מסונן
				List<Complain> all = instance.getAllComplaints();

				for (Complain c : all) {
					try {
						// דילוג על תלונות שכבר קיבלו תשובה ידנית/אוטומטית
						String ans = c.getAnswer_text();
						if (ans != null && !ans.isBlank()) continue;

						// איחוד תאריך+שעה כדי לבדוק 24 שעות מלאות
						LocalDate d = c.getDate();
						LocalTime t = c.getTime();
						if (d == null || t == null) continue;

						LocalDateTime at = LocalDateTime.of(d, t);
						if (at.isAfter(threshold)) continue; // עדיין לא עברו 24 שעות

						// בונים תשובה אוטומטית עם סימון, כדי לא לשלוח שוב
						String autoAnswer = AUTO_MARK + "We apologize for the delay. This is your automatic response.";

						Complain db = instance.getComplaintById(c.getId()); // הוסיפי מתודה אם אין
						if (db == null) continue;

						String already = db.getAnswer_text();
						if (already != null && !already.isBlank()) continue;


						db.setAnswer_text(autoAnswer);
						db.setStatus(true);
						db.setRefund(20.0);
						instance.updateComplaint(db);

						// שליחת מייל ללקוח (אחרי שהעדכון נשמר)
						Customer cust = db.getCustomer();
						if (cust != null && cust.getEmail() != null && !cust.getEmail().isBlank()) {
							String email   = cust.getEmail();
							String name    = (cust.getFirstName() != null) ? cust.getFirstName() : "Customer";
							String subject = "Automatic Response to Your Complaint";
							String body    = "Dear " + name + ",\n\n" + autoAnswer + "\n\nAs compensation, we have refunded 20₪ to your account."
									+ "We’re sorry for the inconvenience,\n— The Lilach Team";
							try {
								EmailSender.sendEmail(email, subject, body);
							} catch (Exception mailEx) {
								mailEx.printStackTrace();
							}
						}
					} catch (Exception perItem) {
						perItem.printStackTrace();
					}
				}

				// המתנה 30 דקות בין סריקות
				Thread.sleep(30 * 60 * 1000L);

			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt(); // יציאה אלגנטית כשמכבים
			} catch (Exception e) {
				e.printStackTrace();
				try { Thread.sleep(5_000L); } catch (InterruptedException ignored) {}
			}
		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		//1
		if (msg instanceof HistogramReportRequest) {
			HistogramReportRequest request = (HistogramReportRequest) msg;
			LocalDate fromDate = request.getFromDate();
			LocalDate toDate = request.getToDate();
			String reportType = request.getType(); // "complain" or "orders"
			int branchId = request.getBranchId();
			if (reportType.equals("complain")) {
				System.out.println("Fetching complaints from database...");
				List<Complain> complaintsList = (List<Complain>) instance.getReportData(
						fromDate, toDate, branchId, "complain"
				);
				System.out.println("Complaints retrieved: " + complaintsList.size());
				try {
					client.sendToClient(new GetReportEvent("complain", complaintsList));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reportType.equals("orders")) {
				System.out.println("Fetching orders histogram from database...");
				List<Order> ordersCount = (List<Order>) instance.getReportData(fromDate, toDate, branchId, "orders");
				System.out.println("Orders count retrieved: " + ordersCount.size());
				try {
					client.sendToClient(new GetReportEvent("orders", ordersCount));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		//2
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
					System.out.println("All the data send successfully.");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		//3
		else if (msg instanceof String && msg.equals("get complaints")) {
			// Fetch *all* complaints from the database
			List<Complain> all = instance.getAllComplaints();

			// Keep only those still unanswered (status == false)
			List<Complain> open = all.stream()
					.filter(c -> !c.getStatus())
					.collect(Collectors.toList());

			// Send the filtered list back, catching IOException
			try {
				client.sendToClient(open);
				System.out.println("All the complains send successfully.");
			} catch (IOException e) {
				e.printStackTrace();
				// Optionally log an error or notify admin
			}
		}

		//4
		else if (msg instanceof Complain) {
			Complain complaint = (Complain) msg;

			complaint.getCustomer().getEmail();

			if (!complaint.getStatus()) {
				// New complaint (not saved yet, no ID)
				System.out.println("Server received new complaint: " + complaint.getComplain_text());
				instance.saveComplaint(complaint);
				String email = complaint.getCustomer().getEmail();
				String name = complaint.getCustomer().getFirstName();
				String subject = "We've Received Your Complaint";
				String body = "Dear " + name + ",\n\n"
						+ "Thank you for reaching out to us. We have received your complaint:\n\n"
						+ "\"" + complaint.getComplain_text() + "\"\n\n"
						+ "Our team is reviewing the issue and will respond within the next 2 business days.\n"
						+ "If you have any further questions, feel free to reply to this email.\n\n"
						+ "Best regards,\n"
						+ "— The Lilach Team";

				EmailSender.sendEmail(email, subject, body);
				System.out.println("Complaint confirmation sent to: " + email);

			} else {
				// Existing complaint (has ID, update it)
				System.out.println("Server received update for complaint ID: " + complaint.getId());
				instance.updateComplaint(complaint);

				// sending email after setting the response
				String answer = complaint.getAnswer_text();
				if (answer != null && !answer.isEmpty()) {
					String email   = complaint.getCustomer().getEmail();
					String name    = complaint.getCustomer().getFirstName();
					String subject = "Response to Your Complaint";
					String body    = "Dear " + name + ",\n\n"
							+ "We have responded to your complaint:\n\n"
							+ answer + "\n\n"
							+ "Thank you for your patience,\n"
							+ "— The Lilach Team";

					EmailSender.sendEmail(email, subject, body); //here we send an email after the response of the complaint is completed
					System.out.println("Email sent to: " + email);
				}
			}
		}

//		//5
//		if (msgString.startsWith("add client1")) {
//			try {
//				System.out.println("Hello from Java!");
//				List<Flower> flowerList = instance.getAllFlowers();
//				client.sendToClient(flowerList);
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new RuntimeException(e);
//			}
//		}
		//6
		else if (msgString.startsWith("remove")) {
			if (!SubscribersList.isEmpty()) {
				for (SubscribedClient subscribedClient : SubscribersList) {
					if (subscribedClient.getClient().equals(client)) {
						SubscribersList.remove(subscribedClient);
						break;
					}
				}
			}
			if(msgString.startsWith("remove customer")) {
				String[] parts = msgString.split(",");
				int id = Integer.parseInt(parts[1]);
				instance.LogOutCustomer(id);
			}
			else if(msgString.startsWith("remove employee")) {
				String[] parts = msgString.split(",");
				int id = Integer.parseInt(parts[1]);
				instance.LogOutEmployee(id);
			}
		}

		//6
		else if (msgString.startsWith("dbpassword:")) {
			String[] parts = msgString.split(":");
			String dbPassword = parts[1];
		}

		//7
		else if (msgString.startsWith("log out")) {
			String[] parts = msgString.split(",");
			int id = Integer.parseInt(parts[2]);
			String Class = parts[1];
			if (Class.equals("Employee")) {
				instance.LogOutEmployee(id);
				System.out.println("log out employee with ID: " + id);
			} else if (Class.equals("Customer")) {
				instance.LogOutCustomer(id);
				System.out.println("log out Customer with ID: " + id);
			}
		}

		//8
		else if (msgString.startsWith("Delete")) {
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[1]);
			instance.deleteFlower(flowerId);
			System.out.println("delete flower with ID: " + flowerId);
			sendToAllClients(msgString);
		}

		//9
		else if (msg instanceof LoginRequest) {
			LoginRequest request = (LoginRequest) msg;
			LoginResponse response;

			if ("employee".equals(request.getUserType())) {
				response = instance.loginEmployee(request.getUsername(), request.getPassword());
				if(response.isSuccess()){
					System.out.println("Employee with Username" + request.getUsername() + " successfully logged in.");
				}
			} else {
				response = instance.loginCustomer(request.getUsername(), request.getPassword());
				if(response.isSuccess()){
					System.out.println("Customer with Username" + request.getUsername() + " successfully logged in.");
				}
			}
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//10
		else if (msgString.startsWith("Change Sale")) {
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[2]);
			int Sale = Integer.parseInt(parts[1]);
			Flower flower = instance.PutSale(flowerId, Sale);
			System.out.println("Change Sale Flower with ID: " + flowerId + " to " + Sale);
			sendToAllClients(msgString);
		}

		//11
		else if (msgString.startsWith("Remove Sale")) {
			String[] parts = msgString.split(",");
			int flowerId = Integer.parseInt(parts[1]);
			instance.PutSale(flowerId, 0);
			System.out.println("Remove Sale Flower with ID: " + flowerId);
			sendToAllClients(msgString);
		}

		//12
		else if (msg instanceof ChangeFlower) {
			ChangeFlower newFlower = (ChangeFlower) msg;
			Flower flower = instance.ChangeDetails(newFlower);
			System.out.println("The details of the flower with id" + flower.getId()+ "change successfully.");
			sendToAllClients(flower);
		}

		//13
		else if (msg instanceof Flower) {
			Flower flower = (Flower) msg;
			AddFlower flower1 = instance.addFlower(flower);
			System.out.println("Adding flower successfully.");
			sendToAllClients(flower1);
		}

		//14
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

		//15
		else if (msgString.startsWith("Give Orders")) {
			String[] parts = msgString.split(",");
			int id = Integer.parseInt(parts[1]);
			List<Order> Orders1 = instance.getUser(id);
			if (Orders1.isEmpty() || Orders1 == null) {
				System.out.println("Orders1 is empty");
			}
			try {
				client.sendToClient(Orders1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//16
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
				instance.deleter(orderToDelete);
				System.out.println("Deleting order successfully.");
			}

			try {
				client.sendToClient(orderToDelete); // שלח חזרה את הרשימה לאחר עדכון
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		//17
		//Add customer to the table
		//Add customer to the table
		else if (msg instanceof SignUpRequest) {
			SignUpRequest request = (SignUpRequest) msg;
			SignUpResponse response = instance.registerCustomer(request);
			try {
				if (response.isSuccess()) {
					System.out.println("Register Customer successfully.");
					// Send confirmation email
					EmailSender.sendEmail(request.getEmail(), "Welcome to Lelac Stores!","Dear " + request.getFirstName() + ",\n\nThank you for signing up at Lelac Stores! We're happy to have you on board.\n\nHappy shopping! 💐");
				}
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


		//18
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

		//19
		else if (msg instanceof SendEmailRequest) {
			SendEmailRequest request = (SendEmailRequest) msg;

			String username = request.getUsername();
			int massagetype = request.getMassageType();
			String message = request.getMessage();

			// Example: search in customer table
			Object customerObj = instance.findUserByUsername("customer", request.getUsername()); // You implement this method

			Customer customer = (Customer) customerObj;
			if (customer != null) {
				if (massagetype == 1) {
					String email = customer.getEmail();
					String subject = "Message from Lelac System Manager";
					EmailSender.sendEmail(email, subject, message);
					System.out.println("Email sent to " + email);
				}
			} else {
				System.out.println("Username not found: " + username);
				// Optionally send an error response back
			}
		}

		//20
		else if (msg instanceof UpdateUserRequest) {
			UpdateUserRequest request = (UpdateUserRequest) msg;
			UpdateUserResponse response = instance.updateUser(request);
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		//21
		else if(msg instanceof AddSale){
			AddSale addSale = (AddSale) msg;
			try {
				instance.PutSaleBranch(addSale);
				System.out.println("Adding sale successfully.");
				sendToAllClients(addSale);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}

		//23
		else if (msg instanceof BlockUserRequest) {
			BlockUserRequest request = (BlockUserRequest) msg;
			BlockUserResponse response = instance.handleBlockUser(request);
			System.out.println("Blocking user: " + request.getUsername());
			try {
				client.sendToClient(response);
				if (response.isSuccess()) {
					sendToAllClients(response);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


		//22
		else if(msg instanceof Order){
			Order order = (Order) msg;
			Order Order1 = instance.saveorder(order);
			System.out.println("Saving order successfully.");
			AddOrder newOrder = new AddOrder();
			newOrder.setOrder(Order1);
			try{
				client.sendToClient(newOrder);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			/* 2️⃣  Send confirmation e‑mail */
			Customer customer = order.getCustomer();
			if (customer != null) {
				String email = customer.getEmail();
				String name  = customer.getFirstName();

				if (email != null && !email.isBlank()) {

					String subject = "Your Lilach Order #" + order.getId() + " is Confirmed!";
					String body    = "Dear " + name + ",\n\n"
							+ "Thank you for shopping with Lilach.\n"
							+ "We’ve received your order and started preparing it.\n\n"
							+ "With gratitude,\n"
							+ "— The Lilach Team";

					try {
						EmailSender.sendEmail(email, subject, body);
						System.out.println("Order confirmation e‑mail sent to: " + email);
					} catch (Exception e) {
						System.err.println("Failed to send order confirmation to " + email);
						e.printStackTrace();
					}
				}
			}

		}

		//23
		else if (msg instanceof DeleteOrder){
			DeleteOrder deleteOrder = (DeleteOrder) msg;
			Order order = deleteOrder.getOrder();
			Customer user = instance.deleter(order);
			System.out.println("Deleting order successfully.");
			deleteOrder.setUser(user);
			try {
				client.sendToClient(deleteOrder);
			}
			catch (IOException e){
				e.printStackTrace();
			}

		}
		//24
		else if(msg instanceof DeleteUserRequest){
			DeleteUserRequest request = (DeleteUserRequest) msg;
			instance.DeleteUser(request);
			System.out.println("Deleting user successfully.");
		}

		//25
		else if(msgString.startsWith("credit")){
			String[] parts = msgString.split(",");
			int id = Integer.parseInt(parts[1]);
			double credit = Double.parseDouble(parts[2]);
			instance.setcredit(id,credit);
		}

		//26
		else if (msg instanceof GetSingleUserRequest) {
			GetSingleUserRequest request = (GetSingleUserRequest) msg;
			String username = request.getUsername();

			// Since you're dealing with logged-in users, we assume customers for now
			Object user = DataBaseManagement.findUserByUsername("customer", username);

			if (user != null) {
				try {
					client.sendToClient(new UserDataEvent(user));  // already handled in your controller
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("User not found: " + username);
				// You can optionally send back a failure object if needed
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
