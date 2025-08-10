package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UpdateUserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private UpdateUserRequest request;

    public UpdateUserResponse(boolean success, UpdateUserRequest request1,String message) {
        this.success = success;
        this.message = message;
        this.request = request1;
    }

    public UpdateUserRequest getRequest() {
        return request;
    }
    public void setRequest(UpdateUserRequest request) {
        this.request = request;
    }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
