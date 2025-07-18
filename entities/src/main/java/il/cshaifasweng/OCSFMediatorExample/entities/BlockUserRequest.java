package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class BlockUserRequest implements Serializable {
    private String role;
    private String username;
    private boolean block; // true = block, false = unblock

    public BlockUserRequest(String role, String username, boolean block) {
        this.role = role;
        this.username = username;
        this.block = block;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public boolean isBlock() {
        return block;
    }
}
