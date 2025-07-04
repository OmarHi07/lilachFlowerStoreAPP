package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class AddClient implements Serializable {
    public Employee employee;
    public Customer customer;
    public List<Flower> flowerList;
    public List<Branch> branchList;
    public AddClient(){}
    public List<Branch> getBranchList() {
        return branchList;
    }
    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
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
    public List<Flower> getFlowerList() {
        return flowerList;
    }
    public void setFlowerList(List<Flower> flowerList) {
        this.flowerList = flowerList;
    }
}
