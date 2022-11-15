package InteractiveSystem;

import java.io.PrintWriter;
import java.util.Scanner;

@SuppressWarnings("FieldMayBeFinal")
public class TicketOffice {

    final int NUMBER_OF_ALLOWED_REGISTRATION = 3;

    private SortedArrayList<Customer> customersSortedArrayList;

    private SortedArrayList<Activity> activitySortedArrayList;


    TicketOffice(SortedArrayList<Customer> customersSortedArrayList, SortedArrayList<Activity> activitySortedArrayList) {
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

        Customer existingCustomer = checkIfCustomerExist(customer);

        Activity activity = readActivityDetails(k);

        Activity existingActivity = checkActivityExist(activity);

        int noOfTicketToBuyOrCancel = readNoOfTicketToBuyOrCancel(k);


        // first check if customer exist or not
        if (existingCustomer == null) {
            System.out.println("Hi " + customer.getLastName() + ", Sorry, But we cannot find you as an registered customer.");
            clerk.println("Hi " + customer.getLastName() + ", Sorry, But we cannot find you as an registered customer.");
            return false;
        }


        // check if Activity is available
        if (existingActivity == null) {
            System.out.println("The provided activity : " + activity.getActivityName() + " doesn't exist, Please try entering the correct activity from the below list.");
            clerk.println("The provided activity : " + activity.getActivityName() + " doesn't exist, Please try entering the correct activity from the below list.");
            return false;
        }

        // If Customer is Purchasing the ticket isBuying will be true, or if cancelling the ticket it will be false
        if (isBuying) {
            // check if ticket is available for the activity
            if (!checkIfTicketAvailableForActivity(existingActivity, noOfTicketToBuyOrCancel)) {
                System.out.println("There is no more ticket available for the activity " + existingActivity.getActivityName() + ", Please try for another activity.");
                printLetter(letters, existingCustomer, existingActivity);
                return false;
            }

            // if customer is buying the another ticket for the registered activity again
            // then we need to allow the customer to buy the ticket, but we need to check if ticket is available or not,then allow the customer to buy the ticket
            if (checkIfCustomerGotTicketForTheActivity(existingCustomer, existingActivity)) {
                buyTicket(existingCustomer, existingActivity, noOfTicketToBuyOrCancel);
                System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase the another ticket, the total no.of ticket " + existingCustomer.getNoOfTicketForActivity().get(existingActivity) + " for the same activity : " + existingActivity.getActivityName() + ".");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase the another ticket, the total no.of ticket " + existingCustomer.getNoOfTicketForActivity().get(existingActivity) + " for the same activity : " + existingActivity.getActivityName() + ".");
                return true;
            }

            // check if customer has already registered for the number of allowed activity
            if (existingCustomer.getNoOfActivityRegistered() == NUMBER_OF_ALLOWED_REGISTRATION) {
                System.out.println("Hi " + existingCustomer.getLastName() + " you have been already registered for max no.of  activity.");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have been already registered for max no.of  activity.");
                return false;
            }

            if (buyTicket(existingCustomer, existingActivity, noOfTicketToBuyOrCancel)) {
                System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase " + noOfTicketToBuyOrCancel + ", ticket for the activity : " + existingActivity.getActivityName() + ".");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase " + noOfTicketToBuyOrCancel + ", ticket for the activity : " + existingActivity.getActivityName() + ".");
                return true;
            }
        } else {
            // check if Customer has purchased any ticket in the past for the provided activity
            if (!checkIfCustomerGotTicketForTheActivity(existingCustomer, existingActivity)) {
                System.out.println("There is no ticket found for the activity " + existingActivity.getActivityName() + ", Please try to cancel the ticket for another activity.");
                clerk.println("There is no ticket found for the activity " + existingActivity.getActivityName() + ", Please try to cancel the ticket for another activity.");
                return false;
            }

            if (cancelTicket(existingCustomer, existingActivity, noOfTicketToBuyOrCancel)) {
                System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully canceled  " + noOfTicketToBuyOrCancel + ", ticket for the activity " + existingActivity.getActivityName() + ".");
                clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully canceled  " + noOfTicketToBuyOrCancel + ", ticket for the activity " + existingActivity.getActivityName() + ".");
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
        System.out.println("Enter Activity name.");
        String activityName = k.next();
        k.nextLine();
        return new Activity(activityName);
    }

    /**
     * @return Customer object
     * Take the input from the user and return the customer object
     */
    int readNoOfTicketToBuyOrCancel(Scanner k) {
        System.out.println("Enter Number of ticket customer want to buy/cancel.");
        int noOfTicket = Integer.parseInt(k.next());
        k.nextLine();
        return noOfTicket;
    }

    /**
     * @param activity object as input from the user
     * @return activity object
     * Check if the activity existing or not ,return null if not in the list
     */
    public Activity checkActivityExist(Activity activity) {
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
    public boolean checkIfTicketAvailableForActivity(Activity activity, int noOfTicketToBuy) {
        for (Activity act : activitySortedArrayList) {
            if (act.getActivityName().equalsIgnoreCase(activity.getActivityName())) {
                return act.getNoOfTicketAvailableForActivity() >= noOfTicketToBuy;
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
    public boolean checkIfCustomerGotTicketForTheActivity(Customer customer, Activity activity) {
        if (customer.getNoOfTicketForActivity().get(activity) == null) {
            return false;
        }
        return customer.getNoOfTicketForActivity().get(activity) > 0;
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
     * Update the customer details with activity registered and number of ticket purchased
     * And, also update the activity with no of ticket left
     * @param activity          which activity user want to buy the ticket
     * @param numOfTicketBuying number of ticket buying
     * @return if successfully purchased the ticket
     */
    public boolean buyTicket(Customer customer, Activity activity, int numOfTicketBuying) {
        boolean flag = false;
        if (customer.getNoOfTicketForActivity().get(activity) == null) {
            customer.getNoOfTicketForActivity().put(activity, numOfTicketBuying); // add details about this activity and number of ticket
            customer.setNoOfActivityRegistered(customer.getNoOfActivityRegistered() + 1); // Increment number of activity registered
            flag = true;

        } else if (customer.getNoOfTicketForActivity().get(activity) != null) {
            int oldValue = customer.getNoOfTicketForActivity().get(activity); // get old value of number of ticket purchased
            customer.getNoOfTicketForActivity().put(activity, oldValue + numOfTicketBuying); // increment number ticket purchased
            flag = true;
        }
        activity.setNoOfTicketAvailableForActivity(activity.getNoOfTicketAvailableForActivity() - numOfTicketBuying); // update the activity object
        return flag;
    }

    /**
     * When user cancel the ticket for the provided activity,
     * First check if total no. ticket bought is greater than 1 ,in this case just decrement the value of numberOfTicketBoughtEachActivity
     * And, increment the value of number Of Ticket Available PerActivity
     * But, if number Of Ticket Bought for Activity was 1 , in this case update the value of totalNumberOfActivityRegistered
     * And also, increment the value of number Of Ticket Available For Activity
     *
     * @param activity              which activity user want to cancel the ticket
     * @param numOfTicketCancelling number of ticket cancelling
     * @return if successfully purchased the ticket
     */
    public boolean cancelTicket(Customer customer, Activity activity, int numOfTicketCancelling) {
        int oldValue = customer.getNoOfTicketForActivity().get(activity);

        if (oldValue < numOfTicketCancelling) {
            System.out.println("The number of ticket being cancelled is not matching the number of tickets being purchased by the customer!");
            return false;
        } else if (oldValue > numOfTicketCancelling) {
            customer.getNoOfTicketForActivity().put(activity, oldValue - numOfTicketCancelling);
        } else { // In this case customer is cancelling all the tickets he has purchased for the activity, he is being left with 1 ticket
            customer.getNoOfTicketForActivity().remove(activity); // remove the activity object from the map<Activity, Integer>
            customer.setNoOfActivityRegistered(customer.getNoOfActivityRegistered() - 1); // reduce the number of registered activity
        }
        //update the activity object , the number of tickets available
        activity.setNoOfTicketAvailableForActivity(activity.getNoOfTicketAvailableForActivity() + numOfTicketCancelling);
        return true;
    }
}
