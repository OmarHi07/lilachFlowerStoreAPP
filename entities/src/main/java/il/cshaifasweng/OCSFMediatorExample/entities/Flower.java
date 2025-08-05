package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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

    private int Sale;

    private int SaleBranchHaifa;
    private int SaleBranchTelAviv;
    private int SaleBranchHaifaTelAviv;

    private int TypeOfFlower; // =1 Bouquet , =2 Single

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "flower_branch", joinColumns = @JoinColumn(name = "flower_id"), inverseJoinColumns = @JoinColumn(name = "branch_id"))
    private List<Branch>  BranchesAvailable;
//    private int SaleBranchNUM; // 1-Haifa || 2-TelAviv || 3-All

    public Flower() {}

    public Flower(String flowerName, String Type, double price, byte[] image, String color, int TypeOfFlower) {
        super();
        this.flowerName = flowerName;
        this.TypeOfFlower = TypeOfFlower;
        this.type = Type;
        this.price = price;
        this.image = image;
        this.Sale = 0;
        this.color = color;
        BranchesAvailable = new ArrayList<Branch>();
    }
    public List<Branch> getBranch() {return BranchesAvailable; }
    public void setBranch(List<Branch> branch) {
        BranchesAvailable = branch;
    }
    public void AddBranch(Branch branch) {
        BranchesAvailable.add(branch);
        branch.AddFlower(this);
    }
    public int getSaleBranchHaifa(){
        return SaleBranchHaifa;
    }
    public void setSaleBranchHaifa(int SaleBranchHaifa){
        this.SaleBranchHaifa = SaleBranchHaifa;
    }
    public void setSaleBranchTelAviv(int SaleBranch) {
        this.SaleBranchTelAviv = SaleBranch;
    }
    public void setSaleBranchHaifaTelAviv(int SaleBranch) {
        this.SaleBranchHaifaTelAviv = SaleBranch;
    }
    public int getSaleBranchTelAviv(){
        return SaleBranchTelAviv;
    }
    public int getSaleBranchHaifaTelAviv(){
        return SaleBranchHaifaTelAviv;
    }

    public void setTypeOfFlower(int TypeOfFlower) {this.TypeOfFlower = TypeOfFlower;}
    public int getTypeOfFlower() {return TypeOfFlower;}
    public void RemoveBranch(Branch branch) {BranchesAvailable.remove(branch);}
    public int getSale() {return Sale;}
    public void setSale(int Sale) {this.Sale = Sale;}
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
    public byte[] getImage() {return image;}
    public void setImage(byte[] image) {this.image = image;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
}