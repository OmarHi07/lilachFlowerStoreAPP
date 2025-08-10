package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

//we use this checking if the user name already in use
public class UsernameCheckRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    boolean taken = false;
    public UsernameCheckRequest(String username) {
        this.username = username;
    }
    public UsernameCheckRequest(String username, boolean taken) {
        this.username = username;
        this.taken = taken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isTaken() {
        return taken;
    }
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
