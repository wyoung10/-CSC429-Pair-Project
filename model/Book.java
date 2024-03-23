/*William Young
CSC429 Assignment 1
Book Class
*/

package model;

//import packages
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/**Book Class Instance variables 
 */
//==============================================================
public class Book extends EntityBase implements IView
{
    //Declare string of table name to reference. 
    private static final String myTableName = "Book";

    //Properties object declared
    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";    

    //======================================================================================================
    // Constructor used to pull existing record from table
    // Takes id value
    //----------------------------------------------------------
    public Book(String bookId)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (BookId = " + bookId + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        //If value from table isn't null...
        if (allDataRetrieved != null) //Should also have "&& (allDataRef...size() > 0))"
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one record. Throw error otherwise
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple accounts matching id : "
                        + bookId + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state.
                // This properties object holds data that is being passed to be used 
                // within this class locally.
                Properties retrievedBookData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedBookData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedBookData.getProperty(nextKey);
                    // accountNumber = Integer.parseInt(retrievedAccountData.getProperty("accountNumber"));

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
            System.out.println("--NOTICE: BOOK OBJECT CREATED FROM TABLE--"); //Debug
        }//Endif taking existing entry

        // If no book exists, throw error
        else
        {
            throw new InvalidPrimaryKeyException("No account matching id : "
                    + bookId + " found.");
        }
    }//End constructor1-------------------------------------------------------------------------------------------

    //=============================================================================================================
    // Constructor creates new book when passed properties object
    //----------------------------------------------------------
    public Book(Properties props)
    {
        super(myTableName);

        setDependencies();

        //persistentState object used to hold properties to be used locally
        //This method has not interacted with table at all yet.
        persistentState = new Properties(); 
        
        //Enumeration will set the names of each column to allkeys.
        Enumeration allKeys = props.propertyNames();

        //While allkeys has elements, set the property of persistentState to
        //the corresponding all keys element.
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
        //System.out.println("--NOTICE: BOOK OBJECT CREATED FROM PASSED PROPERTIES--"); //Debug
    }//End Constructor2---------------------------------------------------------------------------------

    //====================METHODS==================================================================
    //===============================================================================================
    /*Method takes key and value of a property and changes current object */
    public void changeState(String key, String value){

        persistentState.setProperty(key, value);
        System.out.println("--CURRENT BOOK OBJECT'S " + key + " attribute was changed to " + value + "--");
    }//End changeState---------------------------------------------------------------------------------

    //================================================================================================
    /*getValue
     * Method returns the value of the corresponding key
     */
    public String getValue(String key){
        String value = persistentState.getProperty(key);
        return value;
    }//End getValue-------------------------------------------------------------------------------------

    //===================================================================================================
    public void save() // save()
    {
        updateStateInDatabase();
    }//End update-----------------------------------------------------------------------------------------

    //=================================================================================================
    /*Method either updates an existing Book record or inserts a new one
     * Retrieves the bookId of the persistentState Properties object, and if it is not null, then it must have been
     * extracted from the table, therefore must be existing, thus update.
     * If the bookId is null, then there is no corresponding value in the table, and we insert.
     */
    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        System.out.println("Update State Called");
        try
        {
            //This if else block checks if we need to do an insert or an update
            if (persistentState.getProperty("bookId") != null)
            {
                //update existing record with values from 
                Properties whereClause = new Properties();
                whereClause.setProperty("bookId",
                persistentState.getProperty("bookId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "NOTICE: Book Data for book : " +
                        persistentState.getProperty("bookId") +
                        " updated successfully in database!";
            }
            else
            {
                // insert
                Integer bookVal =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("bookId", "" + bookVal.intValue());
                updateStatusMessage = "NOTICE: Book data for new account : " +
                        persistentState.getProperty("bookId") +
                        "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing book data in database!";
            ex.printStackTrace();
        }
        System.out.println("updateStateInDatabase " + updateStatusMessage);
        
    }

    //================================================================================================
    /*Unsure of what this method does. Seems to create Properties object  
     */
    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //===============================================================================================
    /* Based on passed key value, returns the corresponding key value of a Properties object 
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //================================================================================================
    /*Unsure if this method is required. 
     */
    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //===============================================================================================
    /*Method takes key and value and changes the corresponding property in current object */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    /**
     * creates a vector of values from the Book 
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("bookId"));
        v.addElement(persistentState.getProperty("bookTitle"));
        v.addElement(persistentState.getProperty("author"));
        v.addElement(persistentState.getProperty("pubYear"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }

    //====================================================================
    /*toString method returns a string listing the values from the Book*/
    public String toString(){
        return "\nTitle: " + persistentState.getProperty("bookTitle") + 
        "\nAuthor: " + persistentState.getProperty("author") + 
        "\nYear: " + persistentState.getProperty("pubYear") + "\nStatus: " + persistentState.getProperty(("status"));
    }//End toString----------------------------------------------------------

    //=====================================================================
    /*displayBook calls toSTring*/
    public void displayBook(){
        //System.out.println("\n--DISPLAYBOOK METHOD CALLED--"); //Debug
        System.out.println(toString());
    }
    //-----------------------------------------------------------------------------------


    //=====================================================================================
    /*
    This class provides the schema information of a table
    */
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    //-----------------------------------------------------------------------------------
    public static int compare(Book a, Book b)
    {
        String aNum = (String)a.getState("bookId");
        String bNum = (String)b.getState("bookId");

        return aNum.compareTo(bNum);
    }

}//End Class==============================================================================================