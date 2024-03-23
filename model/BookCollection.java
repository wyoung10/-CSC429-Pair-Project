// specify the package
package model;

// system imports
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/* The class contains the BookCollection*/
//==============================================================
public class BookCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Book";

    private Vector<Book> bookList;

    // constructor for this class
    //----------------------------------------------------------
    public BookCollection()
    {
        super(myTableName);
        
        //Instance Vector for collections based on methods called. 
        this.bookList = new Vector<>();

        
    }//End Constructor-----------------------------------------------------

    //====================================================================
    /*buildBookCollection
     * Method takes String query and fills class Vector booklist with Book objects
     * based on query.
     */

     private void buildBookCollection(String query) throws Exception{
        
        System.out.println("\nQuery received");//Debug

        //Vector for the data of all records retrieved from respective
        //query
        Vector allDataRetrieved = getSelectQueryResult(query);

        System.out.println("\nBuilding book collection...");//Debug

        /*If records were retrieved fill "books" Vector 
         */
        if (allDataRetrieved != null){
            this.bookList = new Vector<Book>();
            
            /*For the elements retrieved... */
            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);
                
                Book book = new Book(nextBookData);
                
                //Checks if there is data in record. Add if so.
                if (book != null)
                {
                    addBook(book);
                    //System.out.println("Book object added");//Debug
                }//Endif
            }//Endfor

            //Display the vector of books collected
            displayBookList();
        }//Endif
        
        else
        {
            throw new InvalidPrimaryKeyException("No books found");
        }
        
     }
    //==================================================================
    /*findBooksOlderthanDate
     * Method takes String input of year and retrieves all records in table
     * with same year with Like query. 
     */
    public void findBooksOlderThanDate(String year) throws
    Exception{
        
        //Exception if year is not inputted
        if (year == null){
            new Event(Event.getLeafLevelClassName(this), "<init>",
				"No input detected", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: BookCollection.<init>: data for year is incorrect");
		}
        
        //Query should compare date of records to inputted date
        String query = "SELECT * FROM " +
        myTableName + " WHERE (pubYear < " + year + ")";

        System.out.println("Passing query...");//Debug

        //Pass query to buildBooksCollection
        buildBookCollection(query);

        System.out.println("\n--OLDER THAN DATE METHOD DONE"); //Debug

        
    }//End method---------------------------------------------------------

    //=====================================================================
    /*findBooksNewerthanDate
     * Method takes String input of year and retrieves all records in table
     * with same year with specific query. 
     */
    public void findBooksNewerThanDate(String year) throws Exception{
        //Exception if year is not inputted
        if (year == null){
            new Event(Event.getLeafLevelClassName(this), "<init>",
				"No input detected", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: BookCollection.<init>: data for year is incorrect");
		}
        
        //Query should compare date of records to inputted date
        String query = "SELECT * FROM " +
        myTableName + " WHERE (pubYear > " + year + ")";

        System.out.println("Query created and passed to build");//Debug

        buildBookCollection(query);

        System.out.println("\n--NEWER THAN DATE METHOD DONE--"); //Debug


    }//End method---------------------------------------------------------


    //=======================================================================
    /*findBooksWithAuthorLike
     * Method takes String input of author and retrieves all records in table
     * with similar author with Like query. 
     */
    public void findBooksWithAuthorLike(String author) throws Exception{
        //Exception if title is not inputted
        if (author == null){
            new Event(Event.getLeafLevelClassName(this), "<init>",
				"No input detected", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: BookCollection.<init>: data for author is missing");
		}
        
        //Query should compare date of records to inputted title
        String query = "SELECT * FROM " +
        myTableName + " WHERE author LIKE '%" + author + "%'";

        System.out.println("Query created and passed to build");//Debug
        
        buildBookCollection(query);

        System.out.println("\n--AUTHOR METHOD DONE--"); //Debug


    }

    //========================================================================
    /*findBooksWithTitleLike
     * Method takes String input of title and retrieves all records in table
     * with similar title with Like query. 
     */
    public void findBooksWithTitleLike(String title) throws Exception{
        //Exception if title is not inputted
        if (title == null){
            new Event(Event.getLeafLevelClassName(this), "<init>",
				"No input detected", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: BookCollection.<init>: data for author is missing");
		}
        
        //Query should compare date of records to inputted title
        String query = "SELECT * FROM " +
        myTableName + " WHERE bookTitle LIKE '%" + title + "%'";

        System.out.println("Query created and passed to build");//Debug
        
        buildBookCollection(query);

        System.out.println("\n--TITLE METHOD DONE--");//Debug

    }


    //=====================================================================
    /*displayBookList displays all book entries in the vector.
     */
    private void displayBookList(){
        System.out.println("--BOOKS FOUND--");

        //Loop through vector to print out each book record
        Iterator<Book> iterator = this.bookList.iterator();
        while (iterator.hasNext()){
            //Book bookdisplay = iterator.next();
            //System.out.println(bookdisplay.toString());
            System.out.println("-----------------------------");
            System.out.println(iterator.next().toString());
        }
    }//End displayBookList------------------------------------------------



    //----------------------------------------------------------------------------------
    private void addBook(Book a)
    {
        //accounts.add(a);
        int index = findIndexToAdd(a);
        this.bookList.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Book a)
    {
        //users.add(u);
        int low=0;
        int high = this.bookList.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Book midSession = this.bookList.elementAt(middle);

            int result = Book.compare(a,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }

    
    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Books"))
            return this.bookList;
        else
        if (key.equals("BookList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Book retrieve(String bookNumber)
    {
        Book retValue = null;
        for (int cnt = 0; cnt < this.bookList.size(); cnt++)
        {
            Book nextBook = this.bookList.elementAt(cnt);
            String nextBookNum = (String)nextBook.getState("bookId");
            if (nextBookNum.equals(bookNumber) == true)
            {
                retValue = nextBook;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("BookCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("BookCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("BookCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    
}
