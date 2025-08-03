package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UserDataEvent implements Serializable {
    private Object user;  // can be Customer or Employee

    public UserDataEvent(Object user) {
        this.user = user;
    }

    public Object getUser() {
        return user;
    }
}