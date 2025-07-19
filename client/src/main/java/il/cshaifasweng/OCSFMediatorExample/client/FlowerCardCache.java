package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class FlowerCardCache {
    private static final Map<Integer, AnchorPane> cache = new HashMap<>();
    private static final Map<Integer, Item> controllerCache = new HashMap<>();
    public static void put(int id, AnchorPane pane, Item controller) {
        cache.put(id, pane);
        controllerCache.put(id, controller);
    }
    public static AnchorPane getPane(int id) {
        return cache.get(id);
    }
    public static Item getController(int id) {
        return controllerCache.get(id);
    }
    public static boolean contains(int id) {
        return cache.containsKey(id);
    }
    public static void clear() {
        cache.clear();
        controllerCache.clear();
    }
    public static void updateFlower(Flower updatedFlower) {
        if (contains(updatedFlower.getId())) {
            Item controller = getController(updatedFlower.getId());
            controller.setData(updatedFlower);
        }
    }

}
