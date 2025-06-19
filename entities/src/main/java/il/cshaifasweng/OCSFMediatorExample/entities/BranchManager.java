package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class BranchManager extends Employee implements Serializable{
    @OneToOne(mappedBy = "manager")
    private Branch Branch;
    public BranchManager() {super();}
    public BranchManager(String name , String username, String password, Branch branch) {
        super(name, username, password);
        setBranch(branch);
    }
    public Branch getBranch(){
        return Branch;
    }
    public void setBranch(Branch branch){
        this.Branch = branch;
        Branch.setManager(this);
    }
}
