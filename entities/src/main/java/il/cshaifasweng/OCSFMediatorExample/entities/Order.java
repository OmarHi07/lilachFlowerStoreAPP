package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name="orders")
public class Order implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Branch branch;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,   // ← חשוב: כך מחיקה של Order תגרור מחיקת CartProduct
            orphanRemoval = true
    )
    private List<CartProduct> products;

    // ב-Order

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Complain> complain;

    private LocalDate dateReceive;
    private LocalTime timeReceive;
    private LocalDate dateOrder;
    private LocalTime timeOrder;
    private double sum;
    private String greeting;
    private String nameReceives;
    private String phoneReceives;
    private String address;
    private int status; //1- cancel ,2-delivered, 3-pending
    private String type;
    private boolean isCanceled;

    public Order(Customer user, Branch store, LocalDate dateOrder, LocalTime timeOrder ,LocalDate dateReceive, LocalTime timeReceive, double sum, String greeting, String nameReceives, String phoneReceives, String address, boolean isCanceled) {
        this.dateOrder = dateOrder;
        this.timeOrder = timeOrder;
        this.customer = user;
        this.branch = store;
        this.dateReceive = dateReceive;
        this.timeReceive = timeReceive;
        this.sum = sum;
        this.greeting = greeting;
        this.nameReceives = nameReceives;
        this.phoneReceives = phoneReceives;
        this.address = address;
        this.status=3;
        this.isCanceled = isCanceled;
        this.products = new ArrayList<CartProduct>();
    }

    public Order() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;

    }
    public String getPhoneReceives() {
        return phoneReceives;
    }

    public void setPhoneReceives(String phoneReceives) {
        this.phoneReceives = phoneReceives;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    public void addProduct(CartProduct product){
        this.products.add(product);
        product.setOrder(this);
        this.sum+=product.getPrice();
    }

    public void removeProduct(CartProduct product){
        this.products.remove(product);
        product.setOrder(null);
        this.sum-=product.getPrice();
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public LocalDate getDateReceive() {
        return dateReceive;
    }

    public void setDateReceive(LocalDate dateReceive) {
        this.dateReceive = dateReceive;
    }

    public LocalTime getTimeReceive() {
        return timeReceive;
    }

    public void setTimeReceive(LocalTime timeReceive) {
        this.timeReceive = timeReceive;
    }

    public LocalDate getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder = dateOrder;
    }

    public LocalTime getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(LocalTime timeOrder) {
        this.timeOrder = timeOrder;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getNameReceives() {
        return nameReceives;
    }

    public void setNameReceives(String nameReceives) {
        this.nameReceives = nameReceives;
    }

    public String getPhoneRecives() {
        return phoneReceives;
    }

    public void setPhoneRecives(String phoneRecives) {
        this.phoneReceives = phoneRecives;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }


//    public List<Complain> getComplains() {
//        return complains;
//    }

//    public void setComplains(List<Complain> complains) {
//        this.complains = complains != null ? complains : new ArrayList<>();
//    }
//
//    public void addComplain(Complain c) {
//        if (c == null) return;
//        complains.add(c);
//        c.setOrder(this); // קבע את הצד ההפוך
//    }
//
//    public void removeComplain(Complain c) {
//        if (c == null) return;
//        complains.remove(c);
//        if (c.getOrder() == this) c.setOrder(null);
//    }


}
