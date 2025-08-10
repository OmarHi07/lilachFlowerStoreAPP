package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class SignUpRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName, lastName, id, email, phone, username, password;
    private String creditCard, cvv, expiryDate, accountType;
    private String branch;
    private int customerType ;

    public SignUpRequest(String firstName, String lastName, String id, String email, String phone,
                         String username, String password, String creditCard, String cvv,
                         String accountType, String branch, String expiryDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.creditCard = creditCard;
        this.cvv = cvv;
        this.accountType = accountType;
        this.branch = branch;
        this.expiryDate = expiryDate;
        if (this.accountType.equals("Regular")) {
            this.customerType = 1;
        } else if (this.accountType.equals("Network Account")) {
            this.customerType = 2;
        }
        else {
            this.customerType = 3;
        }
    }

    // Getters (add setters if needed)
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCreditCard() { return creditCard; }
    public String getCvv() { return cvv; }
    public String getAccountType() { return accountType; }
    public String getBranch() { return branch; }
    public String getExpiryDate() { return expiryDate; }
}
