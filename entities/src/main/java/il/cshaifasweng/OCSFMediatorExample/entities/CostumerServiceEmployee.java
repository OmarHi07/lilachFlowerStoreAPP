package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity

public class CostumerServiceEmployee extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;


    public CostumerServiceEmployee() {
        super();
    }

    public CostumerServiceEmployee(String name, String username, String password) {
        super(name, username, password);
        this.permission = 2;
    }

}
