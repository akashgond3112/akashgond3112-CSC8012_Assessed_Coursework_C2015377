package InteractiveSystem;

import java.io.PrintWriter;
import java.util.Scanner;

@SuppressWarnings("FieldMayBeFinal")
public class TicketOffice {

    final  int NUMBER_OF_ALLOWED_REGISTRATION = 3;

    private SortedArrayList<Customer> customersSortedArrayList;

    private SortedArrayList<Activity> activitySortedArrayList;


    TicketOffice(SortedArrayList<Customer> customersSortedArrayList, SortedArrayList<Activity> activitySortedArrayList){
        this.customersSortedArrayList = customersSortedArrayList;
        this.activitySortedArrayList = activitySortedArrayList;
    }

    /**
     * @param clerk to print the customer input/output details in the clerk.txt file
     */
     void printActivityDetails(PrintWriter clerk) {
        activitySortedArrayList.forEach(activity -> {
            activity.printDetails(clerk);
        });
    }

    /**
     * @param clerk to print the customer input/output details in the clerk.txt file
     */
    void printCustomerDetails(PrintWriter clerk) {
        customersSortedArrayList.forEach(customer -> {
            customer.printDetails(clerk);
        });
    }

    /**
     * @param clerk    to print the customer input/output details in the clerk.txt file
     * @param letters  to print the customer letter in the letters.txt file
     * @param isBuying boolean flag indicating whether the customer is buying the  ticket or canceling the ticket
     * @return return the customer if its successfully purchased or canceled the ticket
     * There are several checks in this method in order to either buy And Cancel the Ticket
     * 1. If the customer is already  registered or not ?
     * 2. If the activity is in the  list of activities or not ?
     * 3. If ticket is available for the provided activity or not ?
     * 4. Customer can only register for the total number of activities provided
     * 5. If the customer is canceling the ticket check if the customer is already purchase the ticket or not
     * 6. Also update the customer and activity details accordingly whenever customer is buying or cancel the ticket .
     * 7. If the customer is already registered for one of the activities and buying the ticket again , will allow to purchase the ticket again.
     * e.g. The no. of ticket left out of the no. of tickets available.
     * The total number of tickets purchased but the ticket for each activity.
     */
    boolean buyAndCancelTicket(Scanner k, PrintWriter clerk, PrintWriter letters, boolean isBuying) {

        Customer customer = readCustomerNames(k);

        Activity activity = readActivityDetails(k);

        Customer existingCustomer = checkIfCustomerExist(customer);

        // first check if customer exist or not
        if (existingCustomer == null) {
            System.out.println("Hi " + customer.getLastName() + ", Sorry, But we cannot find you as an registered customer.");
            clerk.println("Hi " + customer.getLastName() + ", Sorry, But we cannot find you as an registered customer.");
            return false;
        }

        Activity existingActivity = checkIfActivityExist(activity);

        // check if Activity is available
        if (existingActivity == null) {
            System.out.println("The provided activity : " + activity.getActivityName() + " doesn't exist, Please try entering the correct activity from the below list.");
            clerk.println("The provided activity : " + activity.getActivityName() + " doesn't exist, Please try entering the correct activity from the below list.");
            return false;
        }

        // If Customer is Purchasing the ticket isBuying will be true, or if cancelling the ticket it will be false
        if (isBuying) {
            // check if ticket is available for the activity
            if (!checkIfTicketIsAvailableForTheProvidedActivity(existingActivity)) {
                System.out.println("There is no more ticket available for the activity " + existingActivity.getActivityName() + ", Please try for another activity.");
                printLetter(letters, existingCustomer, existingActivity);
                return false;
            }

            // if customer is buying the another ticket for the registered activity again
            // then we need to allow the customer to buy the ticket
            if (checkIfCustomerHaveAssignedTicketForTheProvidedActivity(existingCustomer, existingActivity)) {
                buyTicket(existingCustomer, existingActivity, 1);
                System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase the another ticket, the total no.of ticket " + existingCustomer.getNumberOfTicketBoughtEachActivity().get(existingActivity) + " for the same activity : " + existingActivity.getActivityName() + ".");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase the another ticket, the total no.of ticket " + existingCustomer.getNumberOfTicketBoughtEachActivity().get(existingActivity) + " for the same activity : " + existingActivity.getActivityName() + ".");
                return true;
            }

            // check if customer has already registered for the number of allowed activity
            if (existingCustomer.getTotalNumberOfActivityRegistered() == NUMBER_OF_ALLOWED_REGISTRATION) {
                System.out.println("Hi " + existingCustomer.getLastName() + " you have been already registered for max no.of  activity.");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have been already registered for max no.of  activity.");
                return false;
            }

            if (buyTicket(existingCustomer, existingActivity, 1)) {
                System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase 1 ticket for the activity : " + existingActivity.getActivityName() + ".");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase 1 ticket for the activity : " + existingActivity.getActivityName() + ".");
                return true;
            }
        } else {
            // check if Customer has purchased any ticket in the past for the provided activity
            if (!checkIfCustomerHaveAssignedTicketForTheProvidedActivity(existingCustomer, existingActivity)) {
                System.out.println("There is no ticket found for the activity " + existingActivity.getActivityName() + ", Please try to cancel the ticket for another activity.");
                clerk.println("There is no ticket found for the activity " + existingActivity.getActivityName() + ", Please try to cancel the ticket for another activity.");
                return false;
            }

            if (cancelTicket(existingCustomer, existingActivity, 1)) {
                System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully canceled the ticket the no.of ticket " + 1 + " for the activity " + existingActivity.getActivityName() + ".");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully canceled the ticket the no.of ticket " + 1 + " for the activity " + existingActivity.getActivityName() + ".");
                return true;
            }
        }
        return isBuying;
    }

    /**
     * @return Customer object
     * Take the input from the user and return the customer object
     */
    Customer readCustomerNames(Scanner k) {
        System.out.println("Enter person’s first name and last name,"
                + " and press Enter");
        String firstName = k.next();
        String lastName = k.next();
        k.nextLine();
        return new Customer(firstName, lastName);
    }

    /**
     * @return Activity object
     * Take the input from the user and return the activity object
     */
    public Activity readActivityDetails(Scanner k) {
        System.out.println("Enter Activity name you want to get registered.");
        String activityName = k.next();
        k.nextLine();
        return new Activity(activityName);
    }

    /**
     * @param activity object as input from the user
     * @return activity object
     * Check if the activity existing or not ,return null if not in the list
     */
    public Activity checkIfActivityExist(Activity activity) {
        for (Activity act : activitySortedArrayList) {
            if (act.getActivityName().equalsIgnoreCase(activity.getActivityName())) return act;
        }
        return null;
    }

    /**
     * @param customer object as input from the user
     * @return customer object
     * Check if the customer exists or not ,return null if not in the list
     */
    public Customer checkIfCustomerExist(Customer customer) {

        for (Customer cust : customersSortedArrayList) {
            if (cust.getFirstName().equalsIgnoreCase(customer.getFirstName()) &&
                    cust.getLastName().equalsIgnoreCase(customer.getLastName())) {
                return cust;
            }
        }
        return null;
    }

    /**
     * @param activity object as input
     * @return boolean
     * Check if the ticket is available or not ,return false if not available
     */
    public boolean checkIfTicketIsAvailableForTheProvidedActivity(Activity activity) {
        for (Activity act : activitySortedArrayList) {
            if (act.getActivityName().equalsIgnoreCase(activity.getActivityName())) {
                return act.getTotalNumberOfTicketAvailablePerActivity() > 0;
            }
        }
        return false;
    }

    /**
     * @param customer object as input
     * @param activity object as input
     * @return boolean
     * Check if the customer has already purchased the ticket for the provided activity
     */
    public boolean checkIfCustomerHaveAssignedTicketForTheProvidedActivity(Customer customer, Activity activity) {
        if (customer.getNumberOfTicketBoughtEachActivity().get(activity) == null) {
            return false;
        }
        return customer.getNumberOfTicketBoughtEachActivity().get(activity) > 0;
    }

    /**
     * @param letter   to print the customer letter in the  letter.txt file
     * @param customer object as input
     * @param activity object as input
     *                 This method will print the customer a letter which is being called when there is no ticket is available
     */
    public void printLetter(PrintWriter letter, Customer customer, Activity activity) {
        letter.print("Dear ");
        letter.print(customer.getFirstName());
        letter.println(",");
        letter.println("There is no more ticket available for the activity " + activity.getActivityName() + ", Please try for another activity.");
        letter.println("Thank you ");
    }

    /**
     * When user purchase the ticket for the first time or purchasing the another ticket for the same activity
     *
     * @param activity          which activity user want to buy the ticket
     * @param numOfTicketBuying number of ticket buying
     * @return if successfully purchased the ticket
     */
    public boolean buyTicket(Customer customer, Activity activity, int numOfTicketBuying) {
        if (customer.getNumberOfTicketBoughtEachActivity().get(activity) == null) {
            customer.getNumberOfTicketBoughtEachActivity().put(activity, numOfTicketBuying);
            customer.setTotalNumberOfActivityRegistered(customer.getTotalNumberOfActivityRegistered() + 1);
            activity.setTotalNumberOfTicketAvailablePerActivity(activity.getTotalNumberOfTicketAvailablePerActivity() - 1);
            return true;

        } else if (customer.getNumberOfTicketBoughtEachActivity().get(activity) != null) {
            int oldValue = customer.getNumberOfTicketBoughtEachActivity().get(activity);
            customer.getNumberOfTicketBoughtEachActivity().put(activity, oldValue + 1);
            activity.setTotalNumberOfTicketAvailablePerActivity(activity.getTotalNumberOfTicketAvailablePerActivity() - 1);
            return true;
        }
        return false;
    }

    /**
     * When user cancel the ticket for the provided activity,
     * First check if total no. ticket bought is greater than 1 ,in this case just decrement the value of numberOfTicketBoughtEachActivity
     * But, if numberOfTicketBoughtEachActivity was 1 , in this case set the value to 0 and also
     * set the value for totalNumberOfActivityRegistered to 0.
     *
     * @param activity              which activity user want to cancel the ticket
     * @param numOfTicketCancelling number of ticket cancelling
     * @return if successfully purchased the ticket
     */
    public boolean cancelTicket(Customer customer, Activity activity, int numOfTicketCancelling) {
        int oldValue = customer.getNumberOfTicketBoughtEachActivity().get(activity);
        if (oldValue > 1) {
            customer.getNumberOfTicketBoughtEachActivity().put(activity, oldValue - numOfTicketCancelling);
            activity.setTotalNumberOfTicketAvailablePerActivity(activity.getTotalNumberOfTicketAvailablePerActivity() + numOfTicketCancelling);
            return true;
        } else if (oldValue == 1) {
            customer.getNumberOfTicketBoughtEachActivity().put(activity, 0);
            customer.setTotalNumberOfActivityRegistered(customer.getTotalNumberOfActivityRegistered() + numOfTicketCancelling);
            return true;
        }
        return false;
    }
}
