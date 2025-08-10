package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;
    private static List<Flower> flowers;
    private static List<Flower> flowersSingles;
    private static List<Branch> AllBranches;
    private static List<Order> AllOrders;
    private static List<Branch> BranchCustomer;
    public static String IP = "127.0.0.1";
    public static int Port = 3001;
    protected SimpleClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {

        //1
        if (msg instanceof GetReportEvent){
            EventBus.getDefault().post((GetReportEvent) msg);
            return;
        }

        //2
        if (msg instanceof List<?>) {
            List<?> msgList = (List<?>) msg;
            boolean allAreorders = msgList.stream().allMatch(o -> o instanceof Order);
            if (allAreorders) {
                List<Order> orders = msgList.stream().map(o -> (Order) o).collect(Collectors.toList());
                EventBus.getDefault().post(orders);
            }
            // 2) New: handle complaint lists by yaman
            boolean allAreComplaints = msgList.stream().allMatch(o -> o instanceof Complain);
            if (allAreComplaints) {
                @SuppressWarnings("unchecked")
                List<Complain> complaints = (List<Complain>) msgList;
                EventBus.getDefault().post(complaints);
            }
        }

        //3
        else if (msg instanceof Flower) {
            Flower flower = (Flower) msg;
            int id = flower.getId();
            if (flower.getTypeOfFlower() == 1) {
                flowers.removeIf(f -> f.getId() == id);
                flowers.add(flower);
            }
            else if (flower.getTypeOfFlower() == 2) {
                flowersSingles.removeIf(f -> f.getId() == id);
                flowersSingles.add(flower);
            }

            EventBus.getDefault().post(flower);
        }
        //4
        else if (msg instanceof AddFlower){

            AddFlower newFlower = (AddFlower) msg;
            if(newFlower.isTrue()) {
                Flower flower = newFlower.getFlower();
                if (flower.getTypeOfFlower() == 1) {
                    SimpleClient.flowers.add(flower);
                } else if (flower.getTypeOfFlower() == 2) {
                    SimpleClient.flowersSingles.add(flower);
                }
            }
            EventBus.getDefault().post(newFlower);

        }

        //5
        else if(msg instanceof AddClient){
            AddClient addClient = (AddClient) msg;
            List<Flower> flowerList = addClient.getFlowerList();
            AllBranches = addClient.getBranchList();
            flowers = flowerList.stream().filter(f -> f.getTypeOfFlower() == 1).collect(Collectors.toList());
            flowersSingles = flowerList.stream().filter(f -> f.getTypeOfFlower() == 2).collect(Collectors.toList());
//            EventBus.getDefault().post(flowers);
        }

        //6
        //gets answer if username already in use
        else if (msg instanceof UsernameCheckRequest ) {
            UsernameCheckRequest response = (UsernameCheckRequest) msg;
            EventBus.getDefault().post(response);
        }

        //7
        //get response if adding customer was success
        else if (msg instanceof SignUpResponse) {
            SignUpResponse response = (SignUpResponse) msg;
            EventBus.getDefault().post(response);
        }


        //8
        else if (msg instanceof LoginResponse) {
            // ANDLOS ADD THIS: handle login response from server
            LoginResponse response = (LoginResponse) msg;
            if (response.getCustomer()!=null) {
                CurrentCustomer.setCurrentUser(response.getCustomer());
                CurrentCustomer.setCurrentCustomer("Customer");
                BranchCustomer = response.getListBranches();
                AllOrders = response.getListOrders();
                if(CurrentCustomer.getCurrentUser().getCustomerType()==1){
                    CurrentCustomer.setSelectedBranch(BranchCustomer.get(0));
                }
            }
            if (response.getEmployee()!=null) {
                CurrentCustomer.setCurrentEmployee(response.getEmployee());
                CurrentCustomer.setCurrentCustomer("Employee");
                AllOrders = response.getListOrders();
            }
            EventBus.getDefault().post(response);
        }

        String msgString = msg.toString();

       // if (msgString.startsWith("change")) {
       //     String[] msgParts = msgString.split(",");
       //     double newPrice = Double.parseDouble(msgParts[1]);
       //     int Id = Integer.parseInt(msgParts[2]);
       //     ChangePrice changePrice = new ChangePrice(Id, newPrice);
       //     EventBus.getDefault().post(changePrice);
       // }

        //9
        if(msgString.startsWith("Change Sale")) {
            String[] parts = msgString.split(",");
            int id = Integer.parseInt(parts[2]);
            Flower flower = flowers.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
            if (flower == null) {
                flower = flowersSingles.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
            }
            if(flower != null) {
                if(flower.getTypeOfFlower()==1) {
                    flowers.remove(flower);
                    flower.setSale(Integer.parseInt(parts[1]));
                    flowers.add(flower);
                }
                else{
                    flowersSingles.remove(flower);
                    flower.setSale(Integer.parseInt(parts[1]));
                    flowersSingles.add(flower);

                }
            }
            else {
                System.out.println("Flower found in cache. Updating...");
            }
            EventBus.getDefault().post(flower);
        }

        //10
        if(msgString.startsWith("Remove Sale")) {
            String[] parts = msgString.split(",");
            int id = Integer.parseInt(parts[1]);
            Flower flower = flowers.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
            if(flower == null) {
                flower = flowersSingles.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
            }
            if(flower != null) {
                if(flower.getTypeOfFlower()==1) {
                    flowers.remove(flower);
                    flower.setSale(0);
                    flowers.add(flower);
                }
                else{
                    flowersSingles.remove(flower);
                    flower.setSale(0);
                    flowersSingles.add(flower);
                }
            }
            else {
                System.out.println("Flower found in cache. Updating...");
            }
            EventBus.getDefault().post(flower);
        }

        //11
        //added by arkan
        if(msgString.startsWith("Delete")){
            String[] parts = msgString.split(",");
            int Id = Integer.parseInt(parts[1]);
            Flower flowerdelete = null;
            for (Flower flower : SimpleClient.flowers) {
                if (flower.getId() == Id) {
                    flowerdelete = flower;
                    SimpleClient.flowers.remove(flower);
                    break;
                }
            }
            if(flowerdelete == null) {
                for (Flower flower : SimpleClient.flowersSingles) {
                    if (flower.getId() == Id) {
                        flowerdelete = flower;
                        SimpleClient.flowersSingles.remove(flower);
                        break;
                    }
                }
            }
            DeletFlower deletflower = new DeletFlower(flowerdelete);
            EventBus.getDefault().post(deletflower);
        }

        //12
        else if (msg instanceof GetEntitiesResponse) {
            EventBus.getDefault().post(msg); // post directly to EventBus
        }

        //13
        else if (msg instanceof UpdateUserResponse) {
            UpdateUserResponse response = (UpdateUserResponse) msg;
            UpdateUserRequest request = response.getRequest();
            EventBus.getDefault().post(response);
            if(response.isSuccess()){
                Customer customer = CurrentCustomer.getCurrentUser();
                customer.setFirstName(request.getFirstName());
                customer.setLastName(request.getLastName());
                customer.setEmail(request.getEmail());
                customer.setPhone(request.getPhone());
                customer.setPassword(request.getPassword());
                customer.setUsername(request.getNewUsername());
                customer.setCreditCardCVV(request.getCvv());
                customer.setCreditCardExpiration(request.getExpiryDate());
                customer.setCreditCardNumber(request.getCardNumber());
            }
        }

        //14
        else if (msg instanceof BlockUserResponse) {
            BlockUserResponse response = (BlockUserResponse) msg;
            if(CurrentCustomer.getCurrentUser()!=null) {
                if(CurrentCustomer.getCurrentUser().getUsername().equals(response.getUsername())) {
                    CurrentCustomer.setSelectedBranch(null);
                    CurrentCustomer.setCurrentCustomer(null);
                    CurrentCustomer.setCurrentUser(null);
                    CurrentCustomer.setCurrentEmployee(null);
                    try{
                        App.setRoot("Home",510,470);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            EventBus.getDefault().post(msg);

        }

        //15
        if(msg instanceof ChangeFlower){
            ChangeFlower changeFlower = (ChangeFlower) msg;
            int id = changeFlower.getId();
            Flower flower = flowers.get(id);
            flowers.remove(flower);
            if(changeFlower.getNewPrice()!=null){
                double Price = Double.parseDouble(changeFlower.getNewPrice());
                flower.setPrice(Price);
            }
            if (changeFlower.getNewType()!=null){
                flower.setType(changeFlower.getNewType());
            }
            if(!changeFlower.getNewBranches().isEmpty()){
                for (int idBranch : changeFlower.getNewBranches()){
                    Branch branch = AllBranches.get(idBranch);
                    if(!(flower.getBranch().contains(branch))){
                        flower.getBranch().add(branch);
                    }
                }
            }
            if(changeFlower.getNewName()!=null){
                flower.setFlowerName(changeFlower.getNewName());
            }
            flowers.add(flower);
            flowers.sort(Comparator.comparingInt(Flower::getId));
            EventBus.getDefault().post(flower);
        }

        //16
        if(msg instanceof AddOrder){
            AddOrder orderClass = (AddOrder) msg;
            Order order = orderClass.getOrder();
            SimpleClient.getAllOrders().add(order);
        }

        //17
        if(msg instanceof Order){
            Order order = (Order) msg;
            for (Order order1: SimpleClient.getAllOrders()){
                if(order1.getId() == order.getId()){
                    SimpleClient.getAllOrders().remove(order1);
                }
            }
            EventBus.getDefault().post(SimpleClient.getAllOrders());
        }

        //17
        if(msg instanceof DeleteOrder){
            DeleteOrder orderClass = (DeleteOrder) msg;
            Order order = orderClass.getOrder();
            for (Order order1: SimpleClient.getAllOrders()){
                if(order1.getId() == order.getId()){
                    SimpleClient.getAllOrders().remove(order1);
                    break;
                }
            }
            if(orderClass.getUser() != null){
              CurrentCustomer.setCurrentUser(orderClass.getUser());
            }
            EventBus.getDefault().post(SimpleClient.getAllOrders());
        }

        //18
        if (msg instanceof AddSale) {
            AddSale addSale = (AddSale) msg;
            if(addSale.getNumBranch()==1){
                for (Branch branch: SimpleClient.getAllBranches()){
                    if(branch.getAddress().equals("Haifa")){
                        branch.setSale(addSale.getNumSale());
                    }
                }
            }
            else if(addSale.getNumBranch()==2){
                for (Branch branch: SimpleClient.getAllBranches()){
                    if(branch.getAddress().equals("TelAviv")){
                        branch.setSale(addSale.getNumSale());
                    }
                }
            }
            else {
                for (Branch branch: SimpleClient.getAllBranches()){
                    branch.setSale(addSale.getNumSale());
                }
            }
            EventBus.getDefault().post(addSale);
        }
        if (msg.getClass().equals(Warning.class)) {
            EventBus.getDefault().post(new WarningEvent((Warning) msg));
        } else {
            String message = msg.toString();
            System.out.println(message);
        }

    }
    public static List<Branch> getBranchCustomer(){return BranchCustomer;}
    public static List<Order> getAllOrders(){return AllOrders;}
    public static List<Branch> getAllBranches() {return AllBranches;}
    public static List<Flower> getFlowers() {return flowers;}
    public static List<Flower> getFlowersSingles(){return flowersSingles;}
    public static SimpleClient getClient () {
        if (client == null) {
            client = new SimpleClient(IP, Port);
        }
        return client;
    }

}


