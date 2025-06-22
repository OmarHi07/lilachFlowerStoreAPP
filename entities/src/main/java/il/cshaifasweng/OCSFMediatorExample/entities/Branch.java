package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Branch implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private int id;

       @OneToOne
       @JoinColumn(name = "Branch_manager")
       private BranchManager manager;

       private String address;

       @ManyToMany(mappedBy = "BranchesAvailable")
       private List<Flower> flowers;

       @ManyToMany(mappedBy = "ListBranch")
       private List<Customer> ListUsers;

       @OneToMany(mappedBy = "branch")
       private List<Order> ListOrders;

       @OneToMany(mappedBy = "Branch")
       private List<Complain> ListComplains;

       public Branch(){}

       public Branch(String address){
              this.address = address;
              this.ListUsers = new ArrayList<Customer>();
              this.ListOrders = new ArrayList<Order>();
              this.ListComplains = new ArrayList<Complain>();
              this.flowers = new ArrayList<Flower>();
       }

       public List<Flower> getFlowers() { return flowers; };
       public void AddFlower (Flower flower) {
              flowers.add(flower);
       }
       public String getAddress() {return address;}
       public void setAddress(String address) {this.address = address;}
       public void setManager(BranchManager manager){this.manager = manager;}
       public  BranchManager getManager(){return this.manager;}


       public void AddComplain(Complain complain){
              this.ListComplains.add(complain);
              complain.setBranch(this);
       }

       public void AddOrder(Order order){
              this.ListOrders.add(order);
              order.setBranch(this);
       }

       public void AddUser(Customer NewUser){
              if (!this.ListUsers.contains(NewUser)) {
                     this.ListUsers.add(NewUser);        // ONLY update my own list
                     // DO NOT call user.addStore(this); again — it already did the work
              }
       }
       public void addCustomer2(Customer NewUser){this.ListUsers.add(NewUser);}

       public void RemoveUser(Customer user){
              this.ListUsers.remove(user);
       }


       public void setListComplains(List<Complain> listComplains){ListComplains = listComplains; }
       public List<Complain> getListComplains() {return ListComplains;}

       public void setListOrders(List<Order> listOrders) {ListOrders = listOrders;}
       public List<Order> getListOrders() {return ListOrders;}

       public List<Customer> getListUsers() {return ListUsers;}
       public void setListUsers(List<Customer> listUsers) {ListUsers = listUsers;}

}
