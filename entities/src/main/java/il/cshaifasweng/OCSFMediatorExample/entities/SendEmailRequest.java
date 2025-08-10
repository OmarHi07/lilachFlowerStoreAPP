package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class SendEmailRequest implements Serializable {
    private static final long serialVersionUID = -8224097662914849956L;

    private String username;
    private String message;
    private int massageType;

    public SendEmailRequest(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public int getMassageType() {
        return massageType;
    }
}
