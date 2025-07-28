package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;

import javax.persistence.Entity;


@SuppressWarnings("serial")
@Entity
public class StoreChainManager extends Employee implements Serializable {

    public StoreChainManager() {
        super();
    }
    public StoreChainManager(String name , String username, String password) {
        super(name, username, password);
        this.permission=4;
    }
}
