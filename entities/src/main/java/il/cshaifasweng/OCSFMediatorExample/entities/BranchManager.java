package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class BranchManager extends Employee implements Serializable{
    private static final long serialVersionUID = -8224097662914849956L;

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
