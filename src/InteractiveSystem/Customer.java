package InteractiveSystem;


import java.io.PrintWriter;

public class Customer implements Comparable<Customer>{

    private String firstName;

    private String lastName;

    private Activity chosenActivity;

    private int numberOfTicketBoughtEachActivity;

    public Customer() {
        this.firstName = "";
        this.lastName = "";
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName,String lastName, Activity chosenActivity, int numberOfTicketBoughtEachActivity){
        setFirstName(firstName);
        setLastName(lastName);
        setChosenActivity(chosenActivity);
        setNumberOfTicketBoughtEachActivity(numberOfTicketBoughtEachActivity);
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

    public Activity getChosenActivity() {
        return chosenActivity;
    }

    public void setChosenActivity(Activity chosenActivity) {
        this.chosenActivity = chosenActivity;
    }

    public int getNumberOfTicketBoughtEachActivity() {
        return numberOfTicketBoughtEachActivity;
    }

    public void setNumberOfTicketBoughtEachActivity(int numberOfTicketBoughtEachActivity) {
        this.numberOfTicketBoughtEachActivity = numberOfTicketBoughtEachActivity;
    }

    public void printName(PrintWriter f) {
        f.println(lastName + " -> " + firstName);
        System.out.println(lastName + " -> " + firstName);
    }

    public boolean equals(Customer otherCustomer) {
        return (firstName.equals(otherCustomer.getFirstName())
                && lastName.equals(otherCustomer.getLastName()));
    }

    @Override
    public int compareTo(Customer customer) {
        int lnCmp = lastName.compareTo(customer.lastName);
        if (lnCmp !=0) return lnCmp;
        int fnCmp = firstName.compareTo(customer.firstName);
        if (fnCmp != 0) return fnCmp;
        else return 1;
    }
}
