package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.boot.cfgxml.spi.MappingReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String username;
    @Column(name = "user_password" )
    private String password;
    private String creditCardNumber;
    private String creditCardExpiration;
    private String creditCardCVV;
    private String identifyingNumber; // ID number
    private boolean loggedIn;
    private int customerType ; // 3 membership , 2 networkCustomer , 1 regular Customer

    @OneToMany (mappedBy = "customer")
    private List<Order> listOrders;

    @OneToMany(mappedBy = "customer")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Complain> listComplains;

    @ManyToMany
    @JoinTable(name = "user_branch",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private List<Branch> listBranch;

    public Customer() {}

    public Customer(String firstName, String lastName, String email, String phone, String username, String password, String creditCardNumber, String creditCardExpiration, String creditCardCVV, String identifyingNumber, int customerType  ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiration = creditCardExpiration;
        this.creditCardCVV = creditCardCVV;
        this.identifyingNumber = identifyingNumber;
        this.customerType = customerType;

        this.listOrders = new ArrayList<Order>();
        this.listComplains = new ArrayList<Complain>();
        this.listBranch = new ArrayList<Branch>();

    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;

    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getUsername() {
        return username;

    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCreditCardNumber() {
        return creditCardNumber;
    }
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
    public String getCreditCardExpiration() {
        return creditCardExpiration;
    }
    public void setCreditCardExpiration(String creditCardExpiration) {
          this.creditCardExpiration = creditCardExpiration;
    }
    public String getCreditCardCVV() {
        return creditCardCVV;
    }
    public void setCreditCardCVV(String creditCardCVV) {
        this.creditCardCVV = creditCardCVV;
    }
    public String getIdentifyingNumber() {
        return identifyingNumber;
    }
    public void setIdentifyingNumber(String identifyingNumber) {
        this.identifyingNumber = identifyingNumber;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    public int getCustomerType() {
        return customerType;

    }
    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }


    public List<Order> getListOrders() {
        return listOrders;
    }

    public void setListOrders(List<Order> listOrders) {
        this.listOrders = listOrders;
    }

    public void addStore(Branch store){
        if (!this.listBranch.contains(store)) {
            this.listBranch.add(store);      // Update my own list
            store.AddUser(this);             // Update the other side once
        }
    }
    public void removeStore(Branch store){
        this.listBranch.remove(store);
        store.RemoveUser(this);
    }

    public void addStore2(List<Branch> stores) {
        for (Branch store : stores) {
            this.listBranch.add(store);
            store.addCustomer2(this);
        }
    }

    public List<Complain> getListComplains() {return listComplains;}

    public void addComplain(Complain complain) {
        listComplains.add(complain);
        complain.setCustomer(this);
    }

    public void addOrder(Order order) {
        listOrders.add(order);
        order.setCustomer(this);
    }

    public void removeOrder(Order order){
        listOrders.remove(order);
    }

    public void removeOrderByID(int orderID){
        listOrders.removeIf(order -> order.getId() == orderID);

    }
    public List<Branch> getListBranch() {
        return this.listBranch;
    }


}
