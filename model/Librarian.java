// specify the package
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.application.Preloader.PreloaderNotification;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/** The class containing the Teller  for the ATM application */
//==============================================================
public class Librarian implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
	// For Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;

	private AccountHolder myAccountHolder;

	// GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage	  	myStage;

	private String loginErrorMessage = "";
	private String transactionErrorMessage = "";
	private String bookMessage = "";
	private String searchBookMessage = "";
	private String patronMessage = "";
	private String searchPatronMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Librarian()
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("Librarian");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "Librarian",
				"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		createAndShowLibrarianView();
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		//Dependencies
		dependencies.setProperty("processBook", "bookMessage");
		dependencies.setProperty("searchBook","searchBookMessage");
		dependencies.setProperty("processPatron", "patronMessage");
		dependencies.setProperty("searchPatron", "searchPatronMessage");
		dependencies.setProperty("Login", "LoginError");
		dependencies.setProperty("Deposit", "TransactionError");
		dependencies.setProperty("Withdraw", "TransactionError");
		dependencies.setProperty("Transfer", "TransactionError");
		dependencies.setProperty("BalanceInquiry", "TransactionError");
		dependencies.setProperty("ImposeServiceCharge", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field
	 * held by the objects encapsulated by this object.
	 *
	 * @param	key	Name of database column (field) for which the client wants the value
	 *
	 * @return	Value associated with the field
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("bookMessage") == true)
		{
			return bookMessage;
		}
		else
		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}
		else
		if (key.equals("Name") == true)
		{
			if (myAccountHolder != null)
			{
				return myAccountHolder.getState("Name");
			}
			else
				return "Undefined";
		}
		else
		if (key.equals("searchBookMessage") == true){
			return searchBookMessage;
		}
		else
			return "";
	}

	//----------------------------------------------------------------
	/*stateChangeRequest
	 * Method takes a key value which determines what actions it should take. 
	 * All view buttons and actions will eventually return here.
	 */
	public void stateChangeRequest(String key, Object value)
	{
		// STEP 4: Write the sCR method component for the key you
		// just set up dependencies for
		// DEBUG System.out.println("Teller.sCR: key = " + key);

		if (key.equals("InsertBook") == true){
			createAndShowIBView();
		} 
		
		if (key.equals("InsertPatron") == true){
			createAndShowIPView();
		}

		if (key.equals("SearchBook") == true) {
			createAndShowSBView();
		}

		if (key.equals("SearchPatron") == true) {
			createAndShowSPView();
		}
		
		//processBook takes prop value and creates and inserts new book
		if (key.equals("processBook") == true){
			Book newBook = null;

			try {
				newBook = new Book((Properties)value);
				newBook.save();
				newBook.displayBook();
				bookMessage = "Book was added";
			} catch (Exception ex){
				System.out.println(ex.toString());
				ex.printStackTrace();
				bookMessage = "Book was not added";
			}
		}

		//searchBook takes title string and creates collection
		if (key.equals("searchBook") == true){
			BookCollection searchBook = new BookCollection();

			try {
				String searchString = value.toString();
				searchBook.subscribe("BackToLibrarian", this);
				searchBook.findBooksWithTitleLike(searchString);
				searchBook.createAndShowView();
				
			} catch (Exception ex){
				System.out.println(ex.toString());
				ex.printStackTrace();
				searchBookMessage = "Error in search";
			}
		}

		//searchPatron takes zip and creates collection
		if (key.equals("searchPatron") == true){
			
			PatronCollection searchPatron = new PatronCollection();

			try {
				String zipString = value.toString();
				searchPatron.subscribe("BackToLibrarian", this);
				searchPatron.findPatronsAtZipCode(zipString);
				searchPatron.createAndShowView();
			} catch (Exception ex){
				System.out.println(ex.toString());
				ex.printStackTrace();
				searchPatronMessage = "Error in search";
			}
		}

		if (key.equals("DoPatronInsert") == true){
			try {
				Patron newPatron = new Patron((Properties)value);
				newPatron.save();
				newPatron.display();
				patronMessage = "Book was added";
			} catch (Exception ex){
				System.out.println(ex.toString());
				ex.printStackTrace();
				bookMessage = "Book was not added";
			}
		}
		
		//BookSelected takes bookId and presents it
		if (key.equals("BookSelected") == true){		

			try {
				String bookIdString = value.toString();
				int bookId = Integer.parseInt(bookIdString);
			} catch (Exception ex){

			}
		}


		//Swaps view back to Librarian view
		if (key.equals("BackToLibrarian") == true){
			System.out.println("returning to main menu");
			createAndShowLibrarianView();
		}


		// if (key.equals("Login") == true)
		// {
		// 	if (value != null)
		// 	{
		// 		loginErrorMessage = "";

		// 		boolean flag = loginAccountHolder((Properties)value);
		// 		if (flag == true)
		// 		{
		// 			createAndShowTransactionChoiceView();
		// 		}
		// 	}
		// }
		// else
		// if (key.equals("CancelTransaction") == true)
		// {
		// 	createAndShowTransactionChoiceView();
		// }
		// else
		// if ((key.equals("Deposit") == true) || (key.equals("Withdraw") == true) ||
		// 	(key.equals("Transfer") == true) || (key.equals("BalanceInquiry") == true) ||
		// 	(key.equals("ImposeServiceCharge") == true))
		// {
		// 	String transType = key;

		// 	if (myAccountHolder != null)
		// 	{
		// 		doTransaction(transType);
		// 	}
		// 	else
		// 	{
		// 		transactionErrorMessage = "Transaction impossible: Customer not identified";
		// 	}

		// }
		// else
		// if (key.equals("Logout") == true)
		// {
		// 	myAccountHolder = null;
		// 	myViews.remove("TransactionChoiceView");

		// 	createAndShowLibrarianView();
		// }

		// myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("Teller.updateState: key: " + key);

		stateChangeRequest(key, value);
	}

	// /**
	//  * Login AccountHolder corresponding to user name and password.
	//  */
	// //----------------------------------------------------------
	// public boolean loginAccountHolder(Properties props)
	// {
	// 	try
	// 	{
	// 		myAccountHolder = new AccountHolder(props);
	// 		// DEBUG System.out.println("Account Holder: " + myAccountHolder.getState("Name") + " successfully logged in");
	// 		return true;
	// 	}
	// 	catch (InvalidPrimaryKeyException ex)
	// 	{
	// 			loginErrorMessage = "ERROR: " + ex.getMessage();
	// 			return false;
	// 	}
	// 	catch (PasswordMismatchException exec)
	// 	{

	// 			loginErrorMessage = "ERROR: " + exec.getMessage();
	// 			return false;
	// 	}
	// }


	// /**
	//  * Create a Transaction depending on the Transaction type (deposit,
	//  * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
	//  * create.
	//  */
	// //----------------------------------------------------------
	// public void doTransaction(String transactionType)
	// {
	// 	try
	// 	{
	// 		Transaction trans = TransactionFactory.createTransaction(
	// 			transactionType, myAccountHolder);

	// 		trans.subscribe("CancelTransaction", this);
	// 		trans.stateChangeRequest("DoYourJob", "");
	// 	}
	// 	catch (Exception ex)
	// 	{
	// 		transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
	// 		new Event(Event.getLeafLevelClassName(this), "createTransaction",
	// 				"Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
	// 				Event.ERROR);
	// 	}
	// }
	
	//==========================================================================================================
	//=======================CREATE AND SHOW METHODS===============================================================
	//=============================================================================================================

	//------------------------------------------------------------
	/*createAndShowLibrarianView
	 * Method creates Librarian view from view factory
	 */
	private void createAndShowLibrarianView()
	{
		Scene currentScene = (Scene)myViews.get("LibrarianView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("LibrarianView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("LibrarianView", currentScene);
		}
				
		swapToView(currentScene);
		
	}

	//--------------------------------------------------------------
	/*createAndShowIBView
	 * Method calls viewfactory to create insertbookview
	 */
	private void createAndShowIBView(){
		Scene currentScene = (Scene)myViews.get("BookView");

		if (currentScene == null){
			View newView = ViewFactory.createView("BookView", this);
			currentScene = new Scene(newView);
			myViews.put("BookView", currentScene);
		}

		swapToView(currentScene);
	}//End createAndShowIBView

	//--------------------------------------------------------------
	/*createAndShowIPView
	 * Method calls viewfactory to create insertpatronview
	 */
	private void createAndShowIPView(){
		Scene currentScene = (Scene)myViews.get("PatronView");

		if (currentScene == null){
			View newView = ViewFactory.createView("PatronView", this);
			currentScene = new Scene(newView);
			myViews.put("PatronView", currentScene);
		}

		swapToView(currentScene);
	}//End createAndShowIBView

	//--------------------------------------------------------------
	/*createAndShowSBView
	 * Method calls viewfactory to create searchbookview
	 */
	private void createAndShowSBView(){
		Scene currentScene = (Scene)myViews.get("BookSearchView");

		if (currentScene == null){
			View newView = ViewFactory.createView("BookSearchView", this);
			currentScene = new Scene(newView);
			myViews.put("BookSearchView", currentScene);
		}

		swapToView(currentScene);
	}//End createAndShowIBView

	//--------------------------------------------------------------
	/*createAndShowSPView
	 * Method calls viewfactory to create search patron view
	 */
	private void createAndShowSPView(){
		Scene currentScene = (Scene)myViews.get("PatronSearchView");

		if (currentScene == null){
			View newView = ViewFactory.createView("PatronSearchView", this);
			currentScene = new Scene(newView);
			myViews.put("PatronSearchView", currentScene);
		}

		swapToView(currentScene);
	}//End createAndShowIBView

	private void createAndShowBSView(){
		Scene currentScene = (Scene)myViews.get("BookSearchView");

		if (currentScene == null){
			View newView = ViewFactory.createView("BookSearchView", this);
			currentScene = new Scene(newView);
			myViews.put("BookSearchView", currentScene);
		}

		swapToView(currentScene); 
	}

	// //----------------------------------------------------------
	// private void createAndShowTransactionChoiceView()
	// {
	// 	Scene currentScene = (Scene)myViews.get("TransactionChoiceView");
		
	// 	if (currentScene == null)
	// 	{
	// 		// create our initial view
	// 		View newView = ViewFactory.createView("TransactionChoiceView", this); // USE VIEW FACTORY
	// 		currentScene = new Scene(newView);
	// 		myViews.put("TransactionChoiceView", currentScene);
	// 	}
				

	// 	// make the view visible by installing it into the frame
	// 	swapToView(currentScene);
		
	// }

	//==================================END CREATE AND SHOW METHODS=================================================================

	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}



	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{

		
		if (newScene == null)
		{
			System.out.println("Teller.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();
		
			
		//Place in center
		WindowPosition.placeCenter(myStage);

	}

}


