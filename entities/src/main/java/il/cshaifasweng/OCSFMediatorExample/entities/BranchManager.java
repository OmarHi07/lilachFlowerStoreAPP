package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class BranchManager extends Employee implements Serializable{
    @OneToOne(mappedBy = "manager")
    private Branch Branch;
    public BranchManager() {
        super();
        this.permission = 3;
    }
    public BranchManager(String name , String username, String password, Branch branch) {
        super(name, username, password);
        setBranch(branch);
        this.permission = 3;
    }
    public Branch getBranch(){
        return Branch;
    }
    public void setBranch(Branch branch){
        this.Branch = branch;
        Branch.setManager(this);
    }
}
