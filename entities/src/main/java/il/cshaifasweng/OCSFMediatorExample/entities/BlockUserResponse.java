package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class BlockUserResponse implements Serializable {
    private boolean success;
    private String message;

    public BlockUserResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
