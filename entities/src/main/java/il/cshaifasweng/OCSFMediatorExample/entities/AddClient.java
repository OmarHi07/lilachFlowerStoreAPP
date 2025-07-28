package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class AddClient implements Serializable {
    public List<Flower> flowerList;
    public List<Branch> branchList;
    public AddClient(){}
    public List<Branch> getBranchList() {
        return branchList;
    }
    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }
    public List<Flower> getFlowerList() {
        return flowerList;
    }
    public void setFlowerList(List<Flower> flowerList) {
        this.flowerList = flowerList;
    }
}
