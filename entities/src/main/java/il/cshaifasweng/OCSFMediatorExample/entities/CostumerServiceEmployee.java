package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CostumerServiceEmployee extends Employee implements Serializable {

    public CostumerServiceEmployee() {
        super();
    }

    public CostumerServiceEmployee(String name, String username, String password) {
        super(name, username, password);
        this.permission = 2;
    }

}
