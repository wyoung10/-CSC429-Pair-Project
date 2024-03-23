
// specify the package
package userinterface;

// system imports
import java.text.NumberFormat;
import java.util.Properties;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// project imports
import impresario.IModel;

/** The class containing the Teller View  for the ATM application */
//==============================================================
public class LibrarianView extends View
{

	// GUI stuff
	private TextField userid;
	private PasswordField password;
	private Button doneButton;
    private Button insertBookButton;
    private Button insertPatronButton;
    private Button searchBookButton;
    private Button searchPatronButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public LibrarianView( IModel librarian)
	{

		super(librarian, "LibrarianView");

		// create a container for showing the contents
		VBox container = new VBox(10);

		container.setPadding(new Insets(15, 5, 5, 5));

		// create a Node (Text) for showing the title
		container.getChildren().add(createTitle());

		// create a Node (GridPane) for showing data entry fields
		container.getChildren().add(createFormContents());

		// Error message area
		container.getChildren().add(createStatusLog("                          "));

		getChildren().add(container);

		//populateFields();

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("LoginError", this);
	}

	// Create the label (Text) for the title of the screen
	//-------------------------------------------------------------
	private Node createTitle()
	{
		
		Text titleText = new Text("       Library System          ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		
	
		return titleText;
	}

	// Create the main form contents
	//-------------------------------------------------------------
	private GridPane createFormContents()
	{
		GridPane grid = new GridPane();
        	grid.setAlignment(Pos.CENTER);
       		grid.setHgap(10);
        	grid.setVgap(10);
        	grid.setPadding(new Insets(25, 25, 25, 25));

		//Create Buttons
        
        //INSERT BOOK BUTTON-----------------------------------------------
        insertBookButton = new Button("Insert Book");
        insertBookButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("InsertBook", null);
            }
        });
		
        //INSERT PATRON BUTTON-----------------------------------------------
        insertPatronButton = new Button("Insert Patron");
        insertPatronButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("InsertPatron", null);
            }
        });

        //SEARCH BOOK BUTTON-------------------------------------------------
        searchBookButton = new Button("Search for a Book");
        searchBookButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("SearchBook", null);
            }
        });

        //SEARCH PATRON BUTTON------------------------------------------------
        searchPatronButton = new Button("Search for a Patron");
        searchPatronButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("SearchPatron", null);
            }
        });
        
        //Create button container for main function buttons and add to grid
        HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.TOP_CENTER);
		btnContainer.getChildren().add(insertBookButton);
        btnContainer.getChildren().add(insertPatronButton);
        btnContainer.getChildren().add(searchBookButton);
        btnContainer.getChildren().add(searchPatronButton);
		grid.add(btnContainer, 0, 1);

        //DONE BUTTON-----------------------------------------------------------
		doneButton = new Button("Done");
 		doneButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
					System.exit(0);    
            	     }
        	});
        
        //Create button container for done button and place it below main button container.
		HBox btnContainer2 = new HBox(10);
		btnContainer2.setAlignment(Pos.BOTTOM_CENTER);
		btnContainer2.getChildren().add(doneButton);
		grid.add(btnContainer2, 0, 2);

		return grid;
	}

	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	// //-------------------------------------------------------------
	// public void populateFields()
	// {
	// 	userid.setText("");
	// 	password.setText("");
	// }


	// This method processes events generated from our GUI components.
	// Make the ActionListeners delegate to this method
	//-------------------------------------------------------------
	public void processAction(Event evt)
	{
		// DEBUG: System.out.println("TellerView.actionPerformed()");

		clearErrorMessage();

		String useridEntered = userid.getText();

		if ((useridEntered == null) || (useridEntered.length() == 0))
		{
			displayErrorMessage("Please enter a user id!");
			userid.requestFocus();
		}
		else
		{
			String passwordEntered = password.getText();
			processUserIDAndPassword(useridEntered, passwordEntered);
		}

	}

	/**
	 * Process userid and pwd supplied when Submit button is hit.
	 * Action is to pass this info on to the teller object
	 */
	//----------------------------------------------------------
	private void processUserIDAndPassword(String useridString,
		String passwordString)
	{
		Properties props = new Properties();
		props.setProperty("ID", useridString);
		props.setProperty("Password", passwordString);

		// clear fields for next time around
		userid.setText("");
		password.setText("");

		myModel.stateChangeRequest("Login", props);
	}

	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// STEP 6: Be sure to finish the end of the 'perturbation'
		// by indicating how the view state gets updated.
		if (key.equals("LoginError") == true)
		{
			// display the passed text
			displayErrorMessage((String)value);
		}

	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}




//==========================================================================================================================
/*
 * Label userName = new Label("User ID:");
        	grid.add(userName, 0, 0);

		userid = new TextField();
		userid.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	processAction(e);    
            	     }
        	});
        	grid.add(userid, 1, 0);

		Label pw = new Label("Password:");
        	grid.add(pw, 0, 1);

		password = new PasswordField();
		password.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	processAction(e);    
            	     }
        	});
        	grid.add(password, 1, 1);
 */