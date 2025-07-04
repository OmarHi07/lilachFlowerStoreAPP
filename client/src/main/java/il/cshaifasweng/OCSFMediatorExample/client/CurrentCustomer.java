package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.Employee;

public class CurrentCustomer {
    private static String currentCustomer;
    private static Customer currentuser;
    private static Employee currentEmployee;

    public CurrentCustomer() {}

    public static Customer getCurrentUser() {
        return currentuser;
    }
    public static void setCurrentUser(Customer Customer) {
        currentuser = Customer;
        currentEmployee = null;
    }
    public static Employee getCurrentEmployee() {
        return currentEmployee;
    }
    public static void setCurrentEmployee(Employee Employee) {
        currentEmployee = Employee;
        currentuser = null;
    }
    public static String getCurrentCustomer() {
        return currentCustomer;
    }
    public static void setCurrentCustomer(String currentCustomer) {
        CurrentCustomer.currentCustomer = currentCustomer;
    }
}
