package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class DeleteOrder implements Serializable {
    private Order order;
    private Customer user;
    public DeleteOrder(Order order) {
        this.order = order;
    }
    public DeleteOrder() {}
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Customer getUser() {
        return user;
    }
    public void setUser(Customer user) {
        this.user = user;
    }
}
