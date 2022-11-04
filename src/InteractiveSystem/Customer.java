package InteractiveSystem;


import java.io.PrintWriter;
import java.util.HashMap;

public class Customer implements Comparable<Customer>{

    private String firstName;

    private String lastName;

    private HashMap<Activity,Integer> numberOfTicketBoughtEachActivity = new HashMap<>();

    private int totalNumberOfActivityRegistered;

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

    public int getTotalNumberOfActivityRegistered() {
        return totalNumberOfActivityRegistered;
    }

    public void setTotalNumberOfActivityRegistered(int totalNumberOfActivityRegistered) {
        this.totalNumberOfActivityRegistered = totalNumberOfActivityRegistered;
    }

    public HashMap<Activity, Integer> getNumberOfTicketBoughtEachActivity() {
        return numberOfTicketBoughtEachActivity;
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

    /**
     * When user purchase the ticket for the first time or purchasing the another ticket for the same activity
     * @param activity which activity user want to buy the ticket
     * @param numOfTicketBuying number of ticket buying
     * @return if successfully purchased the ticket
     */
    public boolean buyTicket(Activity activity, int numOfTicketBuying){
        if(numberOfTicketBoughtEachActivity.get(activity) ==null){
            numberOfTicketBoughtEachActivity.put(activity,numOfTicketBuying);
            this.totalNumberOfActivityRegistered++;
            return true;

        }else if(numberOfTicketBoughtEachActivity.get(activity) !=null ){
            int oldValue= numberOfTicketBoughtEachActivity.get(activity);
            numberOfTicketBoughtEachActivity.put(activity,oldValue++);
            return true;
        }
        return false;
    }

    /**
     * When user cancel the ticket for the provided activity,
     * First check if total no. ticket bought is greater than 1 ,in this case just decrement the value of numberOfTicketBoughtEachActivity
     * But, if numberOfTicketBoughtEachActivity was 1 , in this case set the value to 0 and also
     * set the value for totalNumberOfActivityRegistered to 0.
     * @param activity which activity user want to cancel the ticket
     * @param numOfTicketBuying number of ticket cancelling
     * @return if successfully purchased the ticket
     */
    public boolean cancelTicket(Activity activity, int numOfTicketBuying){
        int oldValue= numberOfTicketBoughtEachActivity.get(activity);
        if(oldValue > 1){
            numberOfTicketBoughtEachActivity.put(activity,oldValue--);
            return true;
        } else if (oldValue ==1) {
            numberOfTicketBoughtEachActivity.put(activity,oldValue--);
            this.totalNumberOfActivityRegistered--;
        }
        return false;
    }
}
