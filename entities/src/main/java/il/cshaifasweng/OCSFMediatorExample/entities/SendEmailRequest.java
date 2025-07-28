package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class SendEmailRequest implements Serializable {
    private String username;
    private String message;

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
}
