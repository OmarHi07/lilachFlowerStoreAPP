package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class NetworkWorker extends Employee implements Serializable{

    public NetworkWorker() {
        super();
    }

    public NetworkWorker(String name, String username, String pass) {
        super(name, username, pass);
        this.permission = 5;
    }

}
