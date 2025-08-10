package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class SignUpResponse implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private boolean success;
    private String message;

    public SignUpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
