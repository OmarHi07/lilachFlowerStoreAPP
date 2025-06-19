package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.SignUpResponse;
import il.cshaifasweng.OCSFMediatorExample.entities.UsernameCheckRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import org.greenrobot.eventbus.EventBus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;

    private SimpleClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof List<?>) {
            List<?> msgList = (List<?>) msg;
            boolean allAreFlowers = msgList.stream().allMatch(o -> o instanceof Flower);
            if (allAreFlowers) {
                List<Flower> flowerList = msgList.stream().map(o -> (Flower) o).collect(Collectors.toList());
                flowerList.sort(Comparator.comparingInt(Flower::getId));
                EventBus.getDefault().post(flowerList);
            }
        } else if (msg instanceof Flower) {
            Flower flower = (Flower) msg;
            FlowerHolder.CurrentFlower = flower;
            try {
                App.setRoot("secondary", 520, 421);
            } catch (Exception e) {
                e.printStackTrace();
            }

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


        String msgString = msg.toString();
        if (msgString.startsWith("change")) {
            String[] msgParts = msgString.split(",");
            double newPrice = Double.parseDouble(msgParts[1]);
            int Id = Integer.parseInt(msgParts[2]);
            ChangePrice changePrice = new ChangePrice(Id, newPrice);
            EventBus.getDefault().post(changePrice);
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

    public static SimpleClient getClient(String host, int port) {
        if (client == null) {
            client = new SimpleClient(host, port);
        }
        return client;
    }
}


