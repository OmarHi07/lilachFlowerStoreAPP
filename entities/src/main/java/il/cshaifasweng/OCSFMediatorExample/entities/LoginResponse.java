package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {
    private boolean success;
    private String message;
    private Customer customer;
    private Employee employee;
    private List<Order> listOrders;

    public LoginResponse(boolean success,List<Order> listOrders ,String message) {
        this.success = success;
        this.message = message;
        this.listOrders = listOrders;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.employee = null;
    }
    public List<Order> getListOrders() {
        return listOrders;
    }
    public void setListOrders(List<Order> listOrders) {
        this.listOrders = listOrders;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.customer = null;
    }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
