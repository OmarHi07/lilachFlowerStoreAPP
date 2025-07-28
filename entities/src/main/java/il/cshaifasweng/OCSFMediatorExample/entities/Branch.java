package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Branch implements Serializable {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private int id;

       @OneToOne
       @JoinColumn(name = "Branch_manager")
       private BranchManager manager;

       private String address;

       @ManyToMany(mappedBy = "BranchesAvailable", fetch = FetchType.EAGER)
       private transient Set<Flower> flowers;

       @ManyToMany(mappedBy = "ListBranch" , fetch = FetchType.EAGER)
       private transient Set<Customer> ListUsers;

       @OneToMany(mappedBy = "branch", fetch = FetchType.EAGER)
       private transient Set<Order> ListOrders;

       @OneToMany(mappedBy = "branch", fetch = FetchType.EAGER)
       private transient Set<Complain> ListComplains;

       public Branch(){
              this.ListUsers = new HashSet<>();
              this.ListOrders = new HashSet<>();
              this.ListComplains = new HashSet<>();
              this.flowers = new HashSet<>();
       }

       public Branch(String address){
              this.address = address;
              this.ListUsers = new HashSet<Customer>();
              this.ListOrders = new HashSet<Order>();
              this.ListComplains = new HashSet<Complain>();
              this.flowers = new HashSet<Flower>();
       }

       public List<Flower> getFlowers() { return new ArrayList<>(flowers); };
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

       public int getId(){return this.id;}
       public void setListComplains(List<Complain> listComplains){ this.ListComplains = new HashSet<>(listComplains);}
       public List<Complain> getListComplains() {return new ArrayList<>(ListComplains);}

       public void setListOrders(List<Order> listOrders) {ListOrders = new HashSet<>(listOrders);}
       public List<Order> getListOrders() {return new ArrayList<>(ListOrders);}

       public List<Customer> getListUsers() {return new ArrayList<>(ListUsers);}
       public void setListUsers(List<Customer> listUsers) {ListUsers = new HashSet<>(listUsers);}

}
