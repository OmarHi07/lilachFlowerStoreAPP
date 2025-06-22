package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class FlowerCardCache {
    private static final Map<Integer, AnchorPane> cache = new HashMap<>();
    public static void put(int id, AnchorPane pane) {
        cache.put(id, pane);
    }
    public static AnchorPane get(int id) {
        return cache.get(id);
    }
    public static boolean contains(int id) {
        return cache.containsKey(id);
    }
    public static void clear() {
        cache.clear();
    }
}
