package InteractiveSystem;


import java.io.PrintWriter;
import java.util.HashMap;

public class Customer implements Comparable<Customer> {

    private String firstName;

    private String lastName;

    private HashMap<Activity, Integer> numberOfTicketBoughtEachActivity = new HashMap<>();

    private int totalNoOfActivityRegistered;

    public Customer() {
        this.firstName = "";
        this.lastName = "";
    }


    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNoOfActivityRegistered() {
        return totalNoOfActivityRegistered;
    }

    public void setNoOfActivityRegistered(int totalNumberOfActivityRegistered) {
        this.totalNoOfActivityRegistered = totalNumberOfActivityRegistered;
    }

    public HashMap<Activity, Integer> getNoOfTicketForActivity() {
        return numberOfTicketBoughtEachActivity;
    }

    public void printCustomerDetails() {
        StringBuilder sb = new StringBuilder(firstName +" "+ lastName);

        if (totalNoOfActivityRegistered == 0) {
            sb.append(", You have been registered for ").append(totalNoOfActivityRegistered).append(", activity.");
        } else {
            sb.append(", You have been registered for ").append(totalNoOfActivityRegistered).append(", activities.").append(" \n");
            getNoOfTicketForActivity().forEach((key, value) -> {
                sb.append("Ticket bought for activity, ").append(key.getActivityName()).append(" is ").append(value).append(".\n");
            });
        }
        System.out.println(sb.toString());
    }

    public boolean equals(Customer otherCustomer) {
        return (firstName.equals(otherCustomer.getFirstName())
                && lastName.equals(otherCustomer.getLastName()));
    }

    @Override
    public int compareTo(Customer customer) {
        int lnCmp = lastName.compareTo(customer.lastName);
        if (lnCmp != 0) return lnCmp;
        int fnCmp = firstName.compareTo(customer.firstName);
        if (fnCmp != 0) return fnCmp;
        else return 1;
    }

}
