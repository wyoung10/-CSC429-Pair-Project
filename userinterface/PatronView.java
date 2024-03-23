// specify the package
package userinterface;

// system imports
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class PatronView extends View
{

    // GUI components
    protected TextField name;
    protected TextField address;
    protected TextField city;
    protected TextField stateCode;

    protected TextField zip;

    protected TextField email;

    protected TextField dob;

    protected ComboBox<String> status;

    protected Button doneButton;

    protected Button submitButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public PatronView(IModel librarian)
    {
        super(librarian, "PatronView");

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

        myModel.subscribe("patronMessage", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }


    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Insert Patron ");
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

        Text prompt = new Text("Patron INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text nameLabel = new Text(" Name : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        nameLabel.setFont(myFont);
        nameLabel.setWrappingWidth(150);
        nameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(nameLabel, 0, 1);

        name = new TextField();
        name.setEditable(true);
        grid.add(name, 1, 1);

        Text addressLabel = new Text(" Address : ");
        addressLabel.setFont(myFont);
        addressLabel.setWrappingWidth(150);
        addressLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(addressLabel, 0, 2);

        address = new TextField();
        address.setEditable(true);
        grid.add(address, 1, 2);

        Text cityLabel = new Text(" City : ");
        cityLabel.setFont(myFont);
        cityLabel.setWrappingWidth(150);
        cityLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(cityLabel, 0, 3);

        city = new TextField();
        city.setEditable(true);
        grid.add(city, 1, 3);

        Text stateCodeLabel = new Text(" StateCode : ");
        stateCodeLabel.setFont(myFont);
        stateCodeLabel.setWrappingWidth(150);
        stateCodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(stateCodeLabel, 0, 4);

        stateCode = new TextField();
        stateCode.setEditable(true);
        grid.add(stateCode, 1, 4);

        Text zipLabel = new Text(" Zipcode : ");
        zipLabel.setFont(myFont);
        zipLabel.setWrappingWidth(150);
        zipLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(zipLabel, 0, 5);

        zip = new TextField();
        zip.setEditable(true);
        grid.add(zip, 1, 5);

        Text emailLabel = new Text(" Email : ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(emailLabel, 0, 6);

        email = new TextField();
        email.setEditable(true);
        grid.add(email, 1, 6);

        Text dobLabel = new Text(" DOB : ");
        dobLabel.setFont(myFont);
        dobLabel.setWrappingWidth(150);
        dobLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dobLabel, 0, 7);

        dob = new TextField();
        dob.setEditable(true);
        grid.add(dob, 1, 7);

        Text statusLabel = new Text(" Status : ");
        statusLabel.setFont(myFont);
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(statusLabel, 0, 8);

        status = new ComboBox<String>();
        status.setItems(FXCollections.observableArrayList("Active", "Inactive"));
        status.getSelectionModel().selectFirst();
        grid.add(status, 1, 8);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the insert
                processInsertPatron(e);
            }
        });

        doneButton = new Button("Back");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("BackToLibrarian", null);
            }
        });
        doneCont.getChildren().add(submitButton);
        doneCont.getChildren().add(doneButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
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
        name.setText("");
        address.setText((String)myModel.getState("Address"));
        city.setText((String)myModel.getState("City"));
        stateCode.setText((String)myModel.getState("StateCode"));
        zip.setText((String)myModel.getState("Zipcode"));
        email.setText((String)myModel.getState("Email"));
        dob.setText((String)myModel.getState("DateOfBirth"));
    }

    public void processInsertPatron(Event evt)
    {
        // DEBUG: System.out.println("WithdrawTransactionView.processAction()");

        clearErrorMessage();

        // do the insert patron

        String nameEntered = name.getText();
        String addressEntered = address.getText();
        String cityEntered = city.getText();
        String stateCodeEntered = stateCode.getText();
        String zipcodeEntered = zip.getText();
        String emailEntered = email.getText();
        String dateOfBirthEntered = dob.getText();
        String statusSelected = status.getValue();

        if ((nameEntered == null)        || (nameEntered.isEmpty()) ||
            (addressEntered == null)     || (addressEntered.isEmpty()) ||
            (cityEntered == null)        || (cityEntered.isEmpty()) ||
            (stateCodeEntered == null)   || (stateCodeEntered.isEmpty()) ||
            (zipcodeEntered == null)     || (zipcodeEntered.isEmpty()) ||
            (emailEntered == null)       || (emailEntered.isEmpty()) ||
            (dateOfBirthEntered == null) || (dateOfBirthEntered.isEmpty()))
        {
            displayErrorMessage("All fields must be filled out");
        }
        else
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date minDate, maxDate;

            try {
                Date date = dateFormat.parse(dateOfBirthEntered);

                minDate = dateFormat.parse("1920-01-01");
                maxDate = dateFormat.parse("2006-01-01");

                if (date.compareTo(minDate) >= 0 && date.compareTo(maxDate) <= 0){
                    processPatronData(nameEntered, addressEntered, cityEntered,
                                      stateCodeEntered, zipcodeEntered, emailEntered,
                                      dateOfBirthEntered, statusSelected);
                    displayMessage("Patron successfully added");
                } else {
                    displayMessage("Patron must be 18 or older");
                }
            }
            catch (ParseException e)
            {
                displayErrorMessage("Invalid date format: " + e.getMessage());
            }

        }
    }

    /**
     * Process data for patron insert.
     * Action is to pass this info on to the transaction object
     */
    //----------------------------------------------------------
    private void processPatronData(String name, String address, String city,
                                   String stateCode, String zipcode, String email,
                                   String dateOfBirth, String status)
    {
        Properties props = new Properties();
        props.setProperty("name", name);
        props.setProperty("address", address);
        props.setProperty("city", city);
        props.setProperty("stateCode", stateCode);
        props.setProperty("zip", zipcode);
        props.setProperty("email", email);
        props.setProperty("dateOfBirth", dateOfBirth);
        props.setProperty("pstatus", status);
        myModel.stateChangeRequest("DoPatronInsert", props);
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();

        if (key.equals("ServiceCharge") == true)
        {
            String val = (String)value;
            //serviceCharge.setText(val);
            displayMessage("Service Charge Imposed: $ " + val);
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
