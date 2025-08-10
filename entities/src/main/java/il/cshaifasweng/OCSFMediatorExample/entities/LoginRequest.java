package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String userType;

    public LoginRequest(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }


    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserType() {return userType;}

}
