package il.cshaifasweng.OCSFMediatorExample.entities;


import java.io.Serializable;

public class GetSingleUserRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;

    public GetSingleUserRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}