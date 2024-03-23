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


/** The class containing the PatronCollection for the pair programming1: Database access from java */
//==============================================================
public class PatronCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Patron";

    private Vector<Patron> patronList;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public PatronCollection()
    {
        super(myTableName);

        patronList = new Vector<Patron>();



    }

    //----------------------------------------------------------------------------------
    public void findPatronsOlderThan(String date) throws Exception
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (dateOfBirth < '" + date + "')";
        retrieveQueryData(query);
    }

    //----------------------------------------------------------------------------------
    public void findPatronsYoungerThan(String date) throws Exception
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (dateOfBirth > '" + date + "')";
        retrieveQueryData(query);

    }

    //----------------------------------------------------------------------------------
    public void findPatronsAtZipCode(String zip) throws Exception
    {

        //Exception if title is not inputted
        if (zip == null){
            new Event(Event.getLeafLevelClassName(this), "<init>",
				"No input detected", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: BookCollection.<init>: data for author is missing");
		}

        String query = "SELECT * FROM " + myTableName + " WHERE (zip = " + zip + ")";
        retrieveQueryData(query);
    }

    //----------------------------------------------------------------------------------
    public void findPatronsWithNameLike(String name) throws Exception
    {
        String query = "SELECT * FROM " + myTableName + " WHERE (name LIKE '%" + name + "%')";
        retrieveQueryData(query);
    }

    //----------------------------------------------------------------------------------
    private void addPatron(Patron p)
    {
        //patronList.add(p);
        int index = findIndexToAdd(p);
        patronList.insertElementAt(p,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Patron p)
    {
        //users.add(u);
        int low=0;
        int high = patronList.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Patron midSession = patronList.elementAt(middle);

            int result = Patron.compare(p,midSession);

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
        if (key.equals("Patrons"))
            return patronList;
        else
        if (key.equals("PatronList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("PatronCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("PatronCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("PatronCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    private void retrieveQueryData(String query) throws Exception
    {
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            patronList = new Vector<Patron>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);

                Patron patron = new Patron(nextPatronData);

                if (patron != null)
                {
                    addPatron(patron);
                }
            }
            displayPatronList();
        }
        else
        {
            throw new InvalidPrimaryKeyException("No Patrons found regarding this parameter");
        }
    }

    public void displayPatronList()
    {
        Iterator<Patron> iterator = patronList.iterator();
        while (iterator.hasNext())
        {
            Patron patronDisplay = iterator.next();
            System.out.println(patronDisplay.toString());
        }
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
