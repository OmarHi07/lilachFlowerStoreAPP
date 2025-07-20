package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private boolean success;
    private String message;
    private Customer customer;
    private Employee employee;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.employee = null;
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
