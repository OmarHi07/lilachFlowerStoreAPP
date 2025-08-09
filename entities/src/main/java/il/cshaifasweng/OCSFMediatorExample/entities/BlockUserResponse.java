package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class BlockUserResponse implements Serializable {
    private boolean success;
    private String message;
    private String username;

    public BlockUserResponse(boolean success, String message,String username) {
        this.success = success;
        this.message = message;
        this.username = username;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}