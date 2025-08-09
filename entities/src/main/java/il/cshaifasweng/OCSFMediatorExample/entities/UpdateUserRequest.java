package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

// add import
import java.time.LocalDate;

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

    // for payment info
    private int userId;
    private String cardNumber;
    private String expiryDate;
    private String cvv;

    private LocalDate membershipStartDate;

    private int accType;


    public UpdateUserRequest() {
        // Default constructor for manual object building
    }



    public UpdateUserRequest(String role, String originalUsername, String newUsername,
                             String firstName, String lastName, String email,
                             String phone, String password, String name, int permision, String branch , String workerPassword, String workerNewUsername,
                             String cardNumber, String expiryDate, String cvv, int accType ,int userId) {
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

        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;

        this.userId = userId;

        this.accType = accType;


    }

    public int getUserId(){
        return userId;
    }

    public void setAccType(int accType) {
        this.accType = accType;
    }
    public int getAccType() {
        return accType;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }
    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
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
    public String getCardNumber() { return cardNumber; }

    public String getExpiryDate() { return expiryDate; }
    public String getCvv() { return cvv; }

    public void setRole(String role) {
        this.role = role;
    }

    public void setOriginalUsername(String originalUsername) {
        this.originalUsername = originalUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermision(int permision) {
        this.permision = permision;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setWorkerPassword(String workerPassword) {
        this.workerPassword = workerPassword;
    }

    public void setWorkerNewUsername(String workerNewUsername) {
        this.workerNewUsername = workerNewUsername;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }



}