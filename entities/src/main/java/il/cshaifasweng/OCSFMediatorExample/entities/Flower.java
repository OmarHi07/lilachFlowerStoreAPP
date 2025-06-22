

package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.annotation.processing.Generated;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Flowers")
public class Flower implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(name = "image", columnDefinition="LONGBLOB")
    private byte[] image;

    private String flowerName;

    private double price;

    @Column(name = "type")
    private String type;

    private String color;

    @ManyToMany
    @JoinTable(name = "flower_branch", joinColumns = @JoinColumn(name = "flower_id"), inverseJoinColumns = @JoinColumn(name = "branch_id"))
    private List<Branch>  BranchesAvailable;

    private String PrimaryType;
    public Flower() {
    }

    public Flower(String flowerName, String Type, double price, String PrimaryType, byte[] image) {
        super();
        this.flowerName = flowerName;
        this.type = Type;
        this.price = price;
        this.PrimaryType = PrimaryType;
        this.image = image;
    }

    public List<Branch> getBranch() {return BranchesAvailable; }
    public void AddBranch(Branch branch) {
        BranchesAvailable.add(branch);
        branch.AddFlower(this);

    }
    public void RemoveBranch(Branch branch) {BranchesAvailable.remove(branch);}

    public int getId() {return id;}
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
    public String getPrimaryType() {
        return PrimaryType;
    }
    public void setPrimaryType(String primaryType) {
        this.PrimaryType = primaryType;
    }
    public byte[] getImage() {return image;}
    public void setImage(byte[] image) {this.image = image;}
}