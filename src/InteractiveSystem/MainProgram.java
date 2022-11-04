package InteractiveSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainProgram {

    public static int tempTotalNumberOfPerson = 0;
    public static int tempTotalNumberOfActivity = 0;
    public static Scanner k = new Scanner(System.in);
    public static ArrayList<Customer> customerArrayList = new ArrayList<>();
    public static ArrayList<Activity> activityArrayList = new ArrayList<>();
    public static HashMap<String, Activity> activityMap = new HashMap<>();
    public static SortedArrayList<Customer> sortedArrayList = new SortedArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter clerk = new PrintWriter("clerk.txt");
        PrintWriter letters = new PrintWriter("letters.txt");

        readFileInput();
        printUserMenu();

        char userInput = k.next().charAt(0);
        k.nextLine();
        while (userInput != 'f') {
            if (userInput == 'a') {
                printActivitySortedName(clerk);
            } else if (userInput == 'c') {
                printCustomerNameInSortedOrder(clerk);
            } else if (userInput == 't') {
                buyTicket(readCustomerNames(), readActivityDetails(), clerk);
            } else {
                System.out.println("Invalid entry, try again");
            }
            printUserMenu();
            userInput = k.next().charAt(0);
            k.nextLine();
        }
        clerk.close();
        letters.close();

    }

    private static void printUserMenu() {
        System.out.println("------------------------------");
        System.out.println("MENU");
        System.out.println("f - to finish running the program.");
        System.out.println("a - to display on the screen information about all the activities.");
        System.out.println("c - to display on the screen information about all the customers");
        System.out.println("t - to update the stored data when tickets are bought by one of the registered customers.");
        System.out.println("r - to update the stored data when a registered customer cancels tickets for a booking.");
        System.out.println("------------------------------");
        System.out.println("Type a letter and press Enter");
    }

    /**
     * Read input file is called whenever the main class is run
     * As the input file having the data always as @String, First of all we need to check if the entry is
     * : @integer or @string
     * We have considered that the first entry in the file, if data is empty we will break the loop else it will be considered as the
     * :@totalNumberOfActivity which will be always be the first entry.
     * We will store the information about the activities in a map with key,value pair,where the key -> Activity Name and value -> Number of tickets
     * We will store the information about the customer in the customer list .
     */
    private static void readFileInput() {

        try {
            File importedFile = new File("input.txt");
            Scanner fileInput = new Scanner(importedFile);
            int counter = 0;
            boolean isString = false;

            String tempKey = "";
            while (fileInput.hasNextLine()) {
                String data = fileInput.nextLine();

                try {
                    Integer.parseInt(data); // checking if the data is the String or int,if it's not int throws exception
                } catch (NumberFormatException numberFormatException) {
                    isString = true;        // when we get an exception it's means the entry was a String not int , it means it's either activity name or customer name, and then we will enable the flag to true
                    System.out.println(numberFormatException.getMessage());
                }

                if (data.isEmpty()) {
                    System.out.println("Data cannot be empty or blank, Please check your input file.");
                    break;
                }

                if (counter == 0) {
                    tempTotalNumberOfActivity = Integer.parseInt(data);      // The first entry will always be considered as the tempTotalNumberOfActivity
                } else if (activityMap.size() < tempTotalNumberOfActivity) { // We will check if our map size is less than the TotalNumberOfActivity
                    if (isString) {                                          // Check if it's String ,if String it mean it's a key i.e activity name
                        tempKey = data;                                      // store the key in tem variable
                        activityMap.put(data, null);                         // Initially keep the value of the key as null always
                        System.out.println("key -> " + data);
                    } else if (activityMap.get(tempKey) == null) {           // If isString is false and value is null for the previous key which we stored in the tmp variable
                        System.out.println("value for key -> " + tempKey + " is -> " + data);
                        activityMap.put(tempKey, new Activity(tempKey, Integer.parseInt(data)));    // We will add the value i.e total number of tickets for the activity in the map
                    }
                } else if (activityMap.size() == tempTotalNumberOfActivity && activityMap.get(tempKey) == null) { // This condition check is always for the last value i.e total number of tickets for the activity
                    System.out.println("value for the last key -> " + tempKey + " is -> " + data);
                    activityMap.put(tempKey, new Activity(tempKey, Integer.parseInt(data)));        // Add the value to the last activity
                } else if (!isString && activityMap.size() == tempTotalNumberOfActivity && activityMap.get(tempKey) != null) { // This condition check is always for setting the total number of person
                    tempTotalNumberOfPerson = Integer.parseInt(data);
                    System.out.println("tempTotalNumberOfPerson -> " + tempTotalNumberOfPerson);
                } else if (customerArrayList.size() <= tempTotalNumberOfPerson && tempTotalNumberOfPerson > 0) { // This conditions satisfies only when we have the details about total number of person
                    String[] firstNameLastName = data.split(" ");                                       // and the customer list size is less than total number of person
                    if (firstNameLastName.length > 1) {                                                        // Check if we have both first and last name else show message
                        customerArrayList.add(new Customer(firstNameLastName[0], firstNameLastName[1]));
                    } else {
                        System.out.println("Customer either don't have first name or last name , Please check your input file!" + data);
                    }
                }
                isString = false;

                counter++;
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("An error occurred.");
            fileNotFoundException.printStackTrace();
        }
    }

    private static void printActivitySortedName(PrintWriter clerk) {
        SortedArrayList<Activity> expectedArrayList = new SortedArrayList<>();

        activityMap.forEach((key, value) -> {
            activityArrayList.add(value);
        });

        activityArrayList.forEach(activity -> {
            sortedArrayList.insert(expectedArrayList, activity);
        });

        expectedArrayList.forEach(activity -> {
            activity.printName(clerk);
        });
    }

    private static void printCustomerNameInSortedOrder(PrintWriter clerk) {
        SortedArrayList<Customer> expectedArrayList = new SortedArrayList<>();
        customerArrayList.forEach(customer -> {
            sortedArrayList.insert(expectedArrayList, customer);
        });
        expectedArrayList.forEach(customer -> {
            customer.printName(clerk);
        });
    }

    public static boolean buyTicket(Customer customer, Activity activity, PrintWriter clerk) {

        Customer existingCustomer = checkIfCustomerExist(customer);
        Activity existingActivity = checkIfActivityExist(activity);

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
            printActivitySortedName(clerk);
            return false;
        }

        // check if ticket is available for the activity
        if (!checkIfTicketIsAvailableForTheProvidedActivity(existingActivity)) {
            System.out.println("There is no more ticket available for the activity " + existingActivity.getActivityName() + ", Please try for another activity.");
            clerk.println("There is no more ticket available for the activity " + existingActivity.getActivityName() + ", Please try for another activity.");
            return false;
        }

        // if customer is already registered for 3 activities
        if (existingCustomer.getTotalNumberOfActivityRegistered() == 3) {
            System.out.println("Hi " + existingCustomer.getLastName() + " you have been already registered for this activity " + existingActivity.getActivityName() + ".");
            clerk.println("Hi " + existingCustomer.getLastName() + " you have been already registered for this activity " + existingActivity.getActivityName() + ".");
            return false;
        }

        if (existingCustomer.buyTicket(existingActivity, 1)) {
            existingActivity.setTotalNumberOfTicketAvailablePerActivity(existingActivity.getTotalNumberOfTicketAvailablePerActivity() - 1);
            System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase the ticket the no.of ticket "+1+" for the activity " + existingActivity.getActivityName() + ".");
            clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully purchase the ticket the no.of ticket "+1+" for the activity " + existingActivity.getActivityName() + ".");
        }

        return false;
    }

    public static boolean cancelTicket(Customer customer, Activity activity, PrintWriter clerk) {

        Customer existingCustomer = checkIfCustomerExist(customer);
        Activity existingActivity = checkIfActivityExist(activity);

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
            printActivitySortedName(clerk);
            return false;
        }

        // check if Customer has purchased any ticket in the past for the provided activity
        if (!checkIfCustomerHaveAssignedTicketForTheProvidedActivity(existingCustomer,existingActivity)) {
            System.out.println("There is no ticket found for the activity " + existingActivity.getActivityName() + ", Please try to cancel the ticket for another activity.");
            clerk.println("There is no ticket found for the activity " + existingActivity.getActivityName() + ", Please try to cancel the ticket for another activity.");
            return false;
        }

        if (existingCustomer.cancelTicket(existingActivity, 1)) {
            existingActivity.setTotalNumberOfTicketAvailablePerActivity(existingActivity.getTotalNumberOfTicketAvailablePerActivity() + 1);
            System.out.println("Hi " + existingCustomer.getLastName() + " you have Successfully canceled the ticket the no.of ticket "+1+" for the activity " + existingActivity.getActivityName() + ".");
            clerk.println("Hi " + existingCustomer.getLastName() + " you have Successfully canceled the ticket the no.of ticket "+1+" for the activity " + existingActivity.getActivityName() + ".");
        }

        return false;
    }

    private static Customer readCustomerNames() {
        System.out.println("Enter person’s first name and last name,"
                + " and press Enter");
        String firstName = k.next();
        String lastName = k.next();
        k.nextLine();
        return new Customer(firstName, lastName);
    }


    private static Activity readActivityDetails() {
        System.out.println("Enter Activity name you want to get registered.");
        String activityName = k.next();
        k.nextLine();
        return new Activity(activityName);
    }

    private static Activity checkIfActivityExist(Activity activity) {

        for (String key : activityMap.keySet())
            if (key.equalsIgnoreCase(activity.getActivityName())) return activityMap.get(key);

        return null;
    }

    private static Customer checkIfCustomerExist(Customer customer) {

        for (Customer cust : customerArrayList) {
            if (cust.getFirstName().equalsIgnoreCase(customer.getFirstName()) &&
                    cust.getLastName().equalsIgnoreCase(customer.getLastName())) {
                return cust;
            }
        }
        return null;
    }

    public static boolean checkIfTicketIsAvailableForTheProvidedActivity(Activity activity) {
        int numberOfTicketAvailable = activityMap.get(activity.getActivityName()).getTotalNumberOfTicketAvailablePerActivity();
        return numberOfTicketAvailable > 0;
    }

    public static boolean checkIfCustomerHaveAssignedTicketForTheProvidedActivity(Customer customer, Activity activity){
            return customer.getNumberOfTicketBoughtEachActivity().get(activity) > 0 ;
    }
}
