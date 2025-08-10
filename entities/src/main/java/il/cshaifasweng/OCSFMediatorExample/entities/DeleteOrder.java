package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class DeleteOrder implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;


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
