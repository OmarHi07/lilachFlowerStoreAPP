package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UserDataEvent implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;
    private Object user;  // can be Customer or Employee

    public UserDataEvent(Object user) {
        this.user = user;
    }

    public Object getUser() {
        return user;
    }
}