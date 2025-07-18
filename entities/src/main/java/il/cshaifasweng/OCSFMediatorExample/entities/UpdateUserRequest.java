package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UpdateUserRequest implements Serializable {
    private String role;  // "customer", "networkWorker", etc.

    //for customer
    private String originalUsername;
    private String newUsername;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    //for workers
    private String name ;
    private int permision;
    private String branch ;
    private String workerPassword;
    private String workerNewUsername;

    public UpdateUserRequest(String role, String originalUsername, String newUsername,
                             String firstName, String lastName, String email,
                             String phone, String password, String name, int permision, String branch , String workerPassword, String workerNewUsername) {
        this.role = role;
        this.originalUsername = originalUsername;
        this.newUsername = newUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.permision = permision;
        this.branch = branch;
        this.workerPassword = workerPassword;
        this.workerNewUsername = workerNewUsername;
    }

    public String getRole() { return role; }
    public String getOriginalUsername() { return originalUsername; }
    public String getNewUsername() { return newUsername; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public int getPermision() { return permision; }
    public String getBranch() { return branch; }
    public String getWorkerPassword() { return workerPassword; }
    public String getWorkerNewUsername() { return workerNewUsername; }
}
