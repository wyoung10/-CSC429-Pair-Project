// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

import java.util.Properties;

// project imports
import impresario.IModel;
import model.Book;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class BookView extends View
{

	// GUI components
	protected TextField bookTitle;
	protected TextField author;
	protected TextField pubYear;
	protected TextField serviceCharge;
	protected ComboBox<String> activeComboBox;
	String activeString;

	protected Button cancelButton;
	protected Button subButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public BookView(IModel librarian)
	{
		super(librarian, "BookView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());
		
		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("bookMessage", this);
		myModel.subscribe("UpdateStatusMessage", this);
	}


	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" INSERT BOOK ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
		//Insert Book page title---------------------------------------
        Text prompt = new Text("ENTER NEW BOOK INFO");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		//Book Title Label and Text Field-----------------------------
		Text bookTitleLabel = new Text(" Book Title : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		bookTitleLabel.setFont(myFont);
		bookTitleLabel.setWrappingWidth(150);
		bookTitleLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(bookTitleLabel, 0, 1);

		bookTitle = new TextField();
		bookTitle.setEditable(true);
		grid.add(bookTitle, 1, 1);

		//Author Title Label and Text Field-----------------------------
		Text authorLabel = new Text(" Author : ");
		authorLabel.setFont(myFont);
		authorLabel.setWrappingWidth(150);
		authorLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(authorLabel, 0, 2);

		author = new TextField();
		author.setEditable(true);
		grid.add(author, 1, 2);

		//Publication Year Label and Text Field------------------------
		Text pubLabel = new Text(" Publication Year : ");
		pubLabel.setFont(myFont);
		pubLabel.setWrappingWidth(150);
		pubLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(pubLabel, 0, 3);

		pubYear = new TextField();
		pubYear.setEditable(true);
		grid.add(pubYear, 1, 3);

		//ComboBox for Active Status-----------------------------
		activeComboBox = new ComboBox<>();
		activeComboBox.getItems().addAll(
			"Active",
			"Inactive"
		);

		activeComboBox.getSelectionModel().select("Active");
		activeComboBox.setOnAction(e -> {
			//activeString = activeComboBox.getValue();
			//System.out.println("Active string: " + activeString);
		});
		grid.add(activeComboBox, 1, 4);
		
		//Setup separate hbox for submit and back buttons
		HBox subBack = new HBox(10);
		subBack.setAlignment(Pos.BOTTOM_CENTER);
		
		//Submit Button---------------------------------
		subButton = new Button("Submit");
		subButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		subButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {
				System.out.println("Debug: submit button registered");
				clearErrorMessage();
				processSubAction(e);				
			}
		});
		subBack.getChildren().add(subButton);
		
		//Back Button-----------------------------------
		cancelButton = new Button("Back");
		cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		    	clearErrorMessage();
       		    	myModel.stateChangeRequest("BackToLibrarian", null);   
            	  }
        	});
		subBack.getChildren().add(cancelButton);
		
		//Add form and buttons
		vbox.getChildren().add(grid);
		vbox.getChildren().add(subBack);

		return vbox;

		//service charge artifact----------------------------------------------------------------------
		// Text scLabel = new Text(" Service Charge : ");
		// scLabel.setFont(myFont);
		// scLabel.setWrappingWidth(150);
		// scLabel.setTextAlignment(TextAlignment.RIGHT);
		// grid.add(scLabel, 0, 4);

		// serviceCharge = new TextField();
		// serviceCharge.setEditable(true);
		// serviceCharge.setOnAction(new EventHandler<ActionEvent>() {

  		//      @Override
  		//      public void handle(ActionEvent e) {
  		//     	clearErrorMessage();
  		//     	myModel.stateChangeRequest("ServiceCharge", serviceCharge.getText());
       	//      }
        // });
		// grid.add(serviceCharge, 1, 4);
	}

	//--------------------------------------------------------------------------------------------
	/*processAction
	 * On submit button click, method will set up properties object with values taken from 
	 * form. 
	 * Passes the properties object to a newly created Book.
	 * Calls the Book's constructor and insert method.
	 */
	public void processSubAction(Event evt){
		//validate user input
		if (pubYear == null || author == null || bookTitle == null){
			clearErrorMessage();
			displayErrorMessage("Please completly fill in all fields");
		} else {

			//Convert properties to string
			String titleString = bookTitle.getText();
			String authorString = author.getText();
			String pubYearString = pubYear.getText();
			String statusString = activeComboBox.getValue();

			//Create properties and keys
			Properties insertProp = new Properties();
			insertProp.setProperty("bookTitle", titleString);
			insertProp.setProperty("author", authorString);
			insertProp.setProperty("pubYear", pubYearString);
			insertProp.setProperty("status", statusString);
			
			//Call Librarian method to create and save book
			myModel.stateChangeRequest("processBook", insertProp);
			
			//Print confirmation
			displayMessage("New Book was added!");
		}
	}

	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		bookTitle.setText((String)myModel.getState("BookTitle"));
		author.setText((String)myModel.getState("BookAuthor"));
		pubYear.setText((String)myModel.getState("PubYear"));
	 	//serviceCharge.setText((String)myModel.getState("ServiceCharge"));
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();
		String temp = ((String)value);

		if (key.equals("bookMessage") == true)
		{
			String val = (String)value;
			serviceCharge.setText(val);
			displayMessage(val);
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
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
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

//---------------------------------------------------------------
//	Revision History:
//



