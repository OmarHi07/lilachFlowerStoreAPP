package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChangeFlower implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private String NewName;
    private String NewPrice;
    private String NewType;
    private List<Integer> NewBranches;
    private List<Integer> RemoveBranches;
    private byte[] image;
    private int flowerId;
    public ChangeFlower(){
        NewName = null;
        NewPrice = null;
        NewType = null;
        NewBranches = new ArrayList<Integer>();
        RemoveBranches =  new ArrayList<>();
        image = null;
    }
    public ChangeFlower(String NewName, String NewPrice, String NewType, List<Integer> NewBranches, byte[] image) {
        this.NewName = NewName;
        this.NewPrice = NewPrice;
        this.NewType = NewType;
        this.NewBranches = NewBranches;
        this.image = image;
    }

    public void AddRemoveBranch(int id){
        RemoveBranches.add(id);
    }
    public List<Integer> getRemoveBranches(){
        return RemoveBranches;
    }
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getNewName() {
        return NewName;
    }
    public void setNewName(String NewName) {
        this.NewName = NewName;
    }
    public String getNewPrice() {
        return NewPrice;
    }
    public void setNewPrice(String NewPrice) {
        this.NewPrice = NewPrice;
    }
    public int getId(){
        return flowerId;
    }
    public void setId(int id){
        this.flowerId = id;
    }
    public String getNewType() {
        return NewType;
    }
    public void setNewType(String NewType) {
        this.NewType = NewType;
    }
    public List<Integer> getNewBranches() {
        return NewBranches;
    }
    public void setNewBranches(List<Integer> NewBranches) {
        this.NewBranches = NewBranches;
    }
    public void AddNewBranch(int NewBranch){
        NewBranches.add(NewBranch);
    }
}