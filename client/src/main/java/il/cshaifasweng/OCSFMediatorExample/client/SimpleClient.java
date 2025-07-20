package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;
    private static List<Flower> flowers;
    private static List<Flower> flowersSingles;
    private static List<Branch> AllBranches;

    private SimpleClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof List<?>) {
            List<?> msgList = (List<?>) msg;
            boolean allAreFlowers = msgList.stream().allMatch(o -> o instanceof Flower);
            if (allAreFlowers) {
                System.out.println("Hello from Java!");
                flowers = msgList.stream().map(o -> (Flower) o).collect(Collectors.toList());
                flowers.sort(Comparator.comparingInt(Flower::getId));
                EventBus.getDefault().post(flowers);
            }
        } else if (msg instanceof Flower) {
            Flower flower = (Flower) msg;
            int id = flower.getId();
            id = id -1;
            flowers.remove(id);
            flowers.add(flower);
            flowers.sort(Comparator.comparingInt(Flower::getId));
            EventBus.getDefault().post(flower);
        }
        else if (msg instanceof AddFlower){
            SimpleClient.flowers.add((Flower) msg);
        }
        else if(msg instanceof AddClient){
            AddClient addClient = (AddClient) msg;
            List<Flower> flowerList = addClient.getFlowerList();
            AllBranches = addClient.getBranchList();
            flowers = flowerList.stream().filter(f -> f.getTypeOfFlower() == 1).collect(Collectors.toList());
            flowersSingles = flowerList.stream().filter(f -> f.getTypeOfFlower() == 2).collect(Collectors.toList());
//            EventBus.getDefault().post(flowers);
        }

        //gets answer if username already in use
        else if (msg instanceof UsernameCheckRequest ) {
            UsernameCheckRequest response = (UsernameCheckRequest) msg;
            EventBus.getDefault().post(response);
        }
        //get response if adding customer was success
        else if (msg instanceof SignUpResponse) {
            SignUpResponse response = (SignUpResponse) msg;
            EventBus.getDefault().post(response);
        }



        else if (msg instanceof LoginResponse) {
            // ANDLOS ADD THIS: handle login response from server
            LoginResponse response = (LoginResponse) msg;
            if (response.getCustomer()!=null) {
                CurrentCustomer.setCurrentUser(response.getCustomer());
                CurrentCustomer.setCurrentCustomer("Customer");
            }
            if (response.getEmployee()!=null) {
                CurrentCustomer.setCurrentEmployee(response.getEmployee());
                CurrentCustomer.setCurrentCustomer("Employee");
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

        if(msgString.startsWith("Change Sale")) {
            String[] parts = msgString.split(",");
            int id = Integer.parseInt(parts[2]);
            Flower flower = flowers.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
            if(flower != null) {
                flowers.remove(flower);
                flower.setSale(Integer.parseInt(parts[1]));
                flowers.add(flower);
            }
            else {
                System.out.println("Flower found in cache. Updating...");
            }
            EventBus.getDefault().post(flower);
        }
        if(msgString.startsWith("Remove Sale")) {
            String[] parts = msgString.split(",");
            int id = Integer.parseInt(parts[1]);
            Flower flower = flowers.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
            if(flower != null) {
                flowers.remove(flower);
                flower.setSale(0);
                flowers.add(flower);
            }
            else {
                System.out.println("Flower found in cache. Updating...");
            }
            EventBus.getDefault().post(flower);
        }

        //added by arkan
        if(msgString.startsWith("Delete")){
            String[] parts = msgString.split(",");
            int Id = Integer.parseInt(parts[1]);
            SimpleClient.getFlowers().remove(Id);
        }
        else if (msg instanceof GetEntitiesResponse) {
            EventBus.getDefault().post(msg); // post directly to EventBus
        }
        else if (msg instanceof UpdateUserResponse) {
            EventBus.getDefault().post(msg);
        }
        else if (msg instanceof BlockUserResponse) {
            EventBus.getDefault().post(msg);

        }
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

        if (msg.getClass().equals(Warning.class)) {
            EventBus.getDefault().post(new WarningEvent((Warning) msg));
        } else {
            String message = msg.toString();
            System.out.println(message);
        }

    }

    public static SimpleClient getClient() {
        return client;
    }
    public static List<Branch> getAllBranches() {return AllBranches;}
    public static List<Flower> getFlowers() {return flowers;}
    public static List<Flower> getFlowersSingles(){return flowersSingles;}
    public static SimpleClient getClient(String host, int port) {
        if (client == null) {
            client = new SimpleClient(host, port);
        }
        return client;
    }
}


