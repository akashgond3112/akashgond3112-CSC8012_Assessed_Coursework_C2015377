package InteractiveSystem;

import Exceptions.ActivityException;
import Exceptions.CustomerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainProgram {

    static int tempTotalNumberOfPerson = 0;
    static int tempTotalNumberOfActivity = 0;
    static Scanner k = new Scanner(System.in);
    static SortedArrayList<Customer> customersSortedArrayList = new SortedArrayList<>(); // Initialize sorted  customer arrayList initially empty.
    static SortedArrayList<Activity> activitySortedArrayList = new SortedArrayList<>(); // Initialize sorted activity arrayList initially empty.
    static TicketOffice ticketOffice = new TicketOffice(customersSortedArrayList, activitySortedArrayList);


    public static void main(String[] args) {
        PrintWriter clerk = null;
        PrintWriter letters = null;

        try {
            clerk = new PrintWriter("clerk.txt");
            letters = new PrintWriter("letters.txt");

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        readFileInput(); // read input file when the main program starts
        printUserMenu(); // Initialize user menu with user menu

        char userInput = k.next().charAt(0);
        k.nextLine();
        while (userInput != 'f') {
            if (userInput == 'a') {
                ticketOffice.printActivityDetails(clerk);
            } else if (userInput == 'c') {
                ticketOffice.printCustomerDetails(clerk);
            } else if (userInput == 't') {
                ticketOffice.buyAndCancelTicket(k, clerk, letters, true);
            } else if (userInput == 'r') {
                ticketOffice.buyAndCancelTicket(k, clerk, letters, false);
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

    /**
     * Method that prints the user menu
     */
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
     * We will store the information about the activities in a sorted activity list
     * We will store the information about the customer in a sorted customer list .
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
                } else if (activitySortedArrayList.size() < tempTotalNumberOfActivity) { // We will check if our map size is less than the TotalNumberOfActivity
                    if (isString) {                                          // Check if it's String ,if String it mean it's a key i.e activity name
                        tempKey = data;                                      // store the key in tem variable
                        System.out.println("key -> " + data);
                    } else {           // If isString is false and value is null for the previous key which we stored in the tmp variable
                        System.out.println("value for key -> " + tempKey + " is -> " + data);
                        //check if activity already exists
                        if (ticketOffice.checkIfActivityExist(new Activity(tempKey, Integer.parseInt(data))) == null) {
                            activitySortedArrayList.add(new Activity(tempKey, Integer.parseInt(data)));
                        } else {
                            throw new ActivityException("Activity already exists, Please check the input data. Duplicate activity entry are not allowed!");
                        }
                    }
                } else if (!isString && activitySortedArrayList.size() == tempTotalNumberOfActivity) { // This condition check is always for setting the total number of person
                    tempTotalNumberOfPerson = Integer.parseInt(data);
                    System.out.println("tempTotalNumberOfPerson -> " + tempTotalNumberOfPerson);
                } else if (customersSortedArrayList.size() <= tempTotalNumberOfPerson && tempTotalNumberOfPerson > 0) { // This conditions satisfies only when we have the details about total number of person
                    String[] firstNameLastName = data.split(" ");                                       // and the customer list size is less than total number of person
                    if (firstNameLastName.length > 1) {
                        if (ticketOffice.checkIfCustomerExist(new Customer(firstNameLastName[0], firstNameLastName[1])) == null) {
                            customersSortedArrayList.add(new Customer(firstNameLastName[0], firstNameLastName[1]));// Check if we have both first and last name else show message
                        } else {
                            throw new CustomerException("Customer already exists, Please check, first name and last name in input data!");
                        }
                    } else {
                        System.out.println("Customer either don't have first name or last name , Please check your input file!" + data);
                    }
                }
                isString = false;

                counter++;
            }
        } catch (FileNotFoundException | CustomerException | ActivityException exception) {
            System.out.println(exception.getMessage());
            System.exit(0);
        }
    }


}
