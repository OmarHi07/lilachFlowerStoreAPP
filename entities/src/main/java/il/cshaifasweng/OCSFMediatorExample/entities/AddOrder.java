package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class AddOrder implements Serializable {
    private Order order;
    public AddOrder(Order order) {
        this.order = order;
    }
    public AddOrder() {}
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

}
