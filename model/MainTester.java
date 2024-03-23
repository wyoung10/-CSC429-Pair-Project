package model;

import model.Book;
import java.util.*;

public class MainTester {

    public static void main(String[] args) throws Exception{
        
        //Scanner for user input
        Scanner scanner = new Scanner(System.in);

        String choice = "";

        //Create option menu
        while (true) {
            System.out.println("\n--CSC429 Assignment 1 Menu--");
            System.out.println("(1) Search for a book by ID#");
            System.out.println("(2) Add New Book");
            System.out.println("(3) Search for multiple books");
            System.out.println("(4) Search for a patron by ID#");
            System.out.println("(5) Add New Patron");
            System.out.println("(6) Search for multiple patrons");
            System.out.println("(7) Close");
            System.out.println("Please enter the option number");
            choice = scanner.nextLine();            
            
            //System.out.println("input taken"); //Debug
            
            //Switch statement for user choice
            switch(choice){
                case "1":
                    System.out.println("\n--SEARCH--");
                    System.out.println("Please enter the book's ID#");
                    String bookId = scanner.nextLine();
                    searchBookId(bookId);
                    break;
                
                case "2":
                    insertBook();
                    break;
                
                case "3":
                    collTester();
                    break;
                
                case "4":
                    System.out.println("\n--SEARCH--");
                    System.out.println("Please enter the patron's ID#");
                    String patronId = scanner.nextLine();
                    searchPatronId(patronId);
                    break;
                
                case "5":
                    insertPatron();
                    break;
                
                case "6":
                    collPTester();
                    break;
                
                case "7":
                    System.out.println("Closing...");
                    System.exit(0);
                    scanner.close();
                    break;      

            }//End switch statement
        }//End While Menu-----------------------------------------------------
        
    }//End main----------------------------------------------------------------


    //==================METHODS=====================================================

    //===============================================================================
    /*searchBookId
     * Method takes a bookId as a string and creates Book object to be displayed
     */

    public static void searchBookId(String bookId) throws Exception{

        //Scanner for user input
        Scanner searchScanner = new Scanner(System.in);

        System.out.println("\nSearching...");

        //Book object to be created with entered id
        Book testBook = new Book(bookId);

        System.out.println("\nBook was found!");

        //Display book
        testBook.displayBook();

        //Give user option to change book
        System.out.println("\nWhat would you like to do with this book?");
        System.out.println("(1) Change data");
        System.out.println("(2) Cancel");
        String changeChoice = searchScanner.nextLine();

        switch (changeChoice){
            case "1":
                changeBook(testBook);
                break;

            case "2":
                System.out.println("\nReturning to main menu");
                break;
        }//End switch        

        //searchScanner.close();
    }//End searchBookId----------------------------------------------------------------

    //===============================================================================
    /*searchPatronId
     * Method takes a patronId as a string and creates Patron object to be displayed
     */

     public static void searchPatronId(String patronId) throws Exception{

        //Scanner for user input
        Scanner searchPScanner = new Scanner(System.in);

        System.out.println("\nSearching...");

        //Patron object to be created with entered id
        Patron testPatron = new Patron(patronId);

        System.out.println("\nPatron was found!");

        //Display patron
        testPatron.display();

        //Give user option to change patron
        System.out.println("\nWhat would you like to do with this book?");
        System.out.println("(1) Change data");
        System.out.println("(2) Cancel");
        String changeChoice = searchPScanner.nextLine();

        switch (changeChoice){
            case "1":
                changePatron(testPatron);
                break;

            case "2":
                System.out.println("\nReturning to main menu");
                break;
        }//End switch        

        //searchScanner.close();
    }//End searchBookId----------------------------------------------------------------

    //=====================================================================================================
     /*changePatron
      * Method takes previously searched for patron object and asks user for values to be changed. Method will
        call changeValue method in Patron.
      */

      public static void changePatron(Patron cPatron){

        Scanner changePScanner = new Scanner(System.in);
        String statusChoice = "";

        //Ask user for new info
        System.out.println("\nPlease enter the new Patron information");
        System.out.println("\nNew Name: ");
        String newName = changePScanner.nextLine();
        System.out.println("\nNew Address : ");
        String newAddress = changePScanner.nextLine();
        System.out.println("\nNew City: ");
        String newCity = changePScanner.nextLine();
        System.out.println("\nNew State Abbreviation: ");
        String newState = changePScanner.nextLine();
        System.out.println("\nNew Zip Code: ");
        String newZip = changePScanner.nextLine();
        System.out.println("\nNew Email: ");
        String newEmail = changePScanner.nextLine();
        System.out.println("\nNew DOB (YYYY-MM-DD): ");
        String newDOB = changePScanner.nextLine();
        System.out.println("\nPatron's current status is " + cPatron.getState("pstatus") + 
            "\nWould you like to change it's status? Enter the option number" + 
            "\n(1) Yes / (2) No");
        statusChoice = changePScanner.nextLine();

        //Call Book's changeState method to change correpsonding attributes
        cPatron.changeValue("name", newName);
        cPatron.changeValue("address", newAddress);
        cPatron.changeValue("city", newCity);
        cPatron.changeValue("stateCode", newState);
        cPatron.changeValue("zip", newZip);
        cPatron.changeValue("email", newEmail);
        cPatron.changeValue("dateOfBirth", newDOB);

        //If user wants to change status, pass patron to changeStatus method
        if (statusChoice.equals("1")){
            changePStatus(cPatron);
        }
        
        //save book in database1
        cPatron.save();

        System.out.println("\nPatron was changed!");

        //changeScanner.close();
  }//End changePatron-------------------------------------------------------------------------------------------

    //================================================================================
    /*insertBook
     * Method asks user for property values and inserts new record into table.
     */

     public static void insertBook() throws Exception{

        //Call Scanner
        Scanner inscanner = new Scanner(System.in);

        //Take user input of properties of book
        System.out.println("\nPlease enter the title of the book: ");
        String title = inscanner.nextLine();

        System.out.println("\nPlease enter the author of the book");
        String author = inscanner.nextLine();

        System.out.println("\nPlease enter the publish year of the book");
        String year = inscanner.nextLine();

        //Create new properties object and set key and values accordingly. 
        Properties insertProp = new Properties();
        insertProp.setProperty("bookTitle", title);
        insertProp.setProperty("pubYear", year);
        insertProp.setProperty("author", author);
        insertProp.setProperty("bstatus", "Active");

        //Pass object to constructor
        Book insertBook = new Book(insertProp);
        
        //Insert book into database
        insertBook.save();
        
        //Display book to check
        insertBook.displayBook();

        System.out.println("\n--BOOK WAS ADDED INTO DATABASE--");

        //inscanner.close();
     }//End insertBook--------------------------------------------------------------------------------------

     //================================================================================
    /*insertPatron
     * Method asks user for property values and inserts new record into table.
     */

     public static void insertPatron() throws Exception{

        //Call Scanner
        Scanner inPscanner = new Scanner(System.in);

        //Take user input of properties of patron
        System.out.println("\nPlease enter the name of the patron: ");
        String name = inPscanner.nextLine();

        System.out.println("\nPlease enter the address of the patron");
        String address = inPscanner.nextLine();

        System.out.println("\nPlease enter the city of the patron");
        String city = inPscanner.nextLine();

        System.out.println("\nPlease enter the state abbreviation of the patron");
        String stateCode = inPscanner.nextLine();

        System.out.println("\nPlease enter the patron's zip code");
        String zip = inPscanner.nextLine();

        System.out.println("\nPlease enter the patron's email");
        String email = inPscanner.nextLine();

        System.out.println("\nPlease enter the patron's DOB (YYYY-MM-DD)");
        String dob = inPscanner.nextLine();

        //Create new properties object and set key and values accordingly. 
        Properties insertProp = new Properties();
        insertProp.setProperty("name", name);
        insertProp.setProperty("address", address);
        insertProp.setProperty("city", city);
        insertProp.setProperty("stateCode", stateCode);
        insertProp.setProperty("zip", zip);
        insertProp.setProperty("email", email);
        insertProp.setProperty("dateOfBirth", dob);
        insertProp.setProperty("pstatus", "Active");

        //Pass object to constructor
        Patron insertPatron = new Patron(insertProp);
        
        //Insert book into database
        insertPatron.save();
        
        //Display book to check
        insertPatron.display();

        System.out.println("\n--PATRON WAS ADDED INTO DATABASE--");

        //inscanner.close();
     }//End insertBook--------------------------------------------------------------------------------------


     //=====================================================================================================
     /*changeBook
      * Method takes previously searched for book object and asks user for values to be changed. Method will
        call changeState method in Book.
      */

      public static void changeBook(Book cBook){

            Scanner changeScanner = new Scanner(System.in);
            String statusChoice = "";

            //Ask user for new info
            System.out.println("\nPlease enter the new book information");
            System.out.println("\nNew title: ");
            String newTitle = changeScanner.nextLine();
            System.out.println("\nNew author: ");
            String newAuthor = changeScanner.nextLine();
            System.out.println("\nNew publish year: ");
            String newYear = changeScanner.nextLine();
            System.out.println("\nBook's current status is " + cBook.getState("bstatus") + 
                "\nWould you like to change it's status? Enter the option number" + 
                "\n(1) Yes / (2) No");
            statusChoice = changeScanner.nextLine();

            //Call Book's changeState method to change correpsonding attributes
            cBook.changeState("bookTitle", newTitle);
            cBook.changeState("author", newAuthor);
            cBook.changeState("pubYear", newYear);

            //If user wants to change status, pass book to changeStatus method
            if (statusChoice.equals("1")){
                changeStatus(cBook);
            }
            
            //save book in database1
            cBook.save();

            System.out.println("\nBook was changed!");

            //changeScanner.close();
      }//End changeBook-------------------------------------------------------------------------------------------

    //=============================================================================================
    /*changePStatus
     * Method changes active status of patron object
     */

    public static void changePStatus(Patron sPatron){

        String status = sPatron.getValue("pstatus");
        //System.out.println(status); //Debug

        //If Patron object is active change to inactive, otherwise change to active
        if (status.equals("Active")){
            sPatron.changeValue("pstatus", "Inactive");
            //System.out.println("\nBook is now deactivated!"); //Debug
        } else {
            sPatron.changeValue("pstatus", "Active");
            //System.out.println("\nBook is now active!"); //Debug
        }//End if

    }//End changeStatus----------------------------------------------------------------------------------------------


    //=========================================================================================================
    /*collTester
     * Method asks user for type of collection to be built and called appropriate method from BookCollection
     */

     public static void collTester() throws Exception{

        Scanner collScanner = new Scanner(System.in);
        String collchoice = "";
        String collDate = "";

        BookCollection testBookCollection = new BookCollection();

        System.out.println("\n--SEARCH COLLECTION MENU--");
        System.out.println("(1) Search for books published before a date");
        System.out.println("(2) Search for books published after a date");
        System.out.println("(3) Search for books by author");
        System.out.println("(4) Search for books by title");
        System.out.println("(5) Cancel");
        System.out.println("Please enter the option number");
        collchoice = collScanner.nextLine();

        switch (collchoice){
            case "1":
                System.out.println("\nPlease enter a year");
                collDate = collScanner.nextLine();
                testBookCollection.findBooksOlderThanDate(collDate);
                break;
            
            case "2":
                System.out.println("\nPlease enter a year");
                collDate = collScanner.nextLine();
                testBookCollection.findBooksNewerThanDate(collDate);
                break;
            
            case "3":
                System.out.println("\nPlease enter an author");
                String collAuthor = collScanner.nextLine();
                testBookCollection.findBooksWithAuthorLike(collAuthor);
                break;
            
            case "4":
                System.out.println("\nPlease enter a title");
                String collTitle = collScanner.nextLine();
                testBookCollection.findBooksWithTitleLike(collTitle);
                break;

            case "5":
                System.out.println("\nReturning to main menu");
                break;
        }//End switch of collection choice

        //collScanner.close();
     }//End collection method----------------------------------------------------------------------

    //=============================================================================================
    /*changeStatus
     * Method changes active status of book object
     */

    public static void changeStatus(Book sBook){

        String status = sBook.getValue("bstatus");
        //System.out.println(status); //Debug

        //If Book object is active change to inactive, otherwise change to active
        if (status.equals("Active")){
            sBook.changeState("bstatus", "Inactive");
            //System.out.println("\nBook is now deactivated!"); //Debug
        } else {
            sBook.changeState("bstatus", "Active");
            //System.out.println("\nBook is now active!"); //Debug
        }//End if

    }//End changeStatus----------------------------------------------------------------------------------------------

    //=====================================================================
    /*collPTester
     * Method creates menu of collection options and 
     */

     public static void collPTester() throws Exception{

        Scanner collPScanner = new Scanner(System.in);
        String collchoice = "";
        String collDate = "";

        PatronCollection testPatronCollection = new PatronCollection();

        System.out.println("\n--SEARCH COLLECTION MENU--");
        System.out.println("(1) Search for patrons older than a date");
        System.out.println("(2) Search for patrons younger than a date");
        System.out.println("(3) Search for patrons by zip code");
        System.out.println("(4) Search for patrons by name");
        System.out.println("(5) Cancel");
        System.out.println("Please enter the option number");
        collchoice = collPScanner.nextLine();

        switch (collchoice){
            case "1":
                System.out.println("\nPlease enter a date (YYYY-MM-DD)");
                collDate = collPScanner.nextLine();
                testPatronCollection.findPatronsOlderThan(collDate);
                break;
            
            case "2":
                System.out.println("\nPlease enter a date (YYYY-MM-DD)");
                collDate = collPScanner.nextLine();
                testPatronCollection.findPatronsYoungerThan(collDate);
                break;
            
            case "3":
                System.out.println("\nPlease enter a zip code");
                String collZip = collPScanner.nextLine();
                testPatronCollection.findPatronsAtZipCode(collZip);
                break;
            
            case "4":
                System.out.println("\nPlease enter a name");
                String collName = collPScanner.nextLine();
                testPatronCollection.findPatronsWithNameLike(collName);;
                break;

            case "5":
                System.out.println("\nReturning to main menu");
                break;
        }//End switch of collection choice

        //collScanner.close();
     }//End collection method----------------------------------------------------------------------

}//END CLASS
