package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CartProduct implements Serializable{
    private static final long serialVersionUID = 1L;


    @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private int id;
          int quantity;
          private double price;

          @ManyToOne(fetch = FetchType.LAZY)
          @JoinColumn(name = "flower_id")
          private Flower flower;

          @ManyToOne
          @JoinColumn(name = "order_id")
          private Order order;

          public CartProduct() {}
          public CartProduct(int quantity, double price, Flower flower) {
              this.quantity = quantity;
              this.price = price;
              this.flower = flower;
          }
          public int getId() {
              return id;
          }
          public int getQuantity(){
              return quantity;
          }
          public double getPrice(){
              return price;
          }
          public Flower getFlower(){
              return flower;
          }
          public Order getOrder(){
              return order;
          }
          public void setQuantity(int quantity){
              this.quantity = quantity;
          }
          public void setPrice(double price){
              this.price = price;
          }
          public void setFlower(Flower flower){
              this.flower = flower;
          }
          public void setOrder(Order order){
              this.order = order;
          }
          public double getTotal(){
              return price*quantity;
          }

}
