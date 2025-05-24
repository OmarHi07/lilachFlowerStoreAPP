

package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.annotation.processing.Generated;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Flowers")
public class Flower implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String flowerName;

    private double price;

    @Column(name = "type")
    private String type;

    public Flower() {
    }

    public Flower(String flowerName, String Type, double price) {
        super();
        this.flowerName = flowerName;
        this.type = Type;
        this.price = price;
    }

    public int getId() {
        return id;
    }
    public String getFlowerName() {
        return flowerName;
    }
    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}