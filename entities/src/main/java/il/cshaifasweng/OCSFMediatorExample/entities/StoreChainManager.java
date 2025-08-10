package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;

import javax.persistence.Entity;


@SuppressWarnings("serial")
@Entity
public class StoreChainManager extends Employee implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;


    public StoreChainManager() {
        super();
    }
    public StoreChainManager(String name , String username, String password) {
        super(name, username, password);
        this.permission=4;
    }
}
