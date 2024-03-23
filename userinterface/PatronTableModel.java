package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class PatronTableModel
{
	private final SimpleStringProperty name;
	private final SimpleStringProperty address;
	private final SimpleStringProperty city;
	private final SimpleStringProperty status;
    private final SimpleStringProperty code;
    private final SimpleStringProperty zip;
    private final SimpleStringProperty email;
    private final SimpleStringProperty dob;
    private final SimpleStringProperty patronId;

	//----------------------------------------------------------------------------
	public PatronTableModel(Vector<String> patronData)
	{
        patronId = new SimpleStringProperty(patronData.elementAt(0));
		name =  new SimpleStringProperty(patronData.elementAt(1));
		address =  new SimpleStringProperty(patronData.elementAt(2));
		city =  new SimpleStringProperty(patronData.elementAt(3));
		code =  new SimpleStringProperty(patronData.elementAt(4));
        zip =  new SimpleStringProperty(patronData.elementAt(5));
        email =  new SimpleStringProperty(patronData.elementAt(6));
        dob =  new SimpleStringProperty(patronData.elementAt(7));
        status =  new SimpleStringProperty(patronData.elementAt(8));
	}

    //----------------------------------------------------------------------------
    public String getPatronId() {
        return patronId.get();
    }

    //----------------------------------------------------------------------------
    public void setPatronId(String idString) {
        patronId.set(idString);
    }
	//----------------------------------------------------------------------------
	public String getName() {
        return name.get();
    }

	//----------------------------------------------------------------------------
    public void setName(String nameString) {
        name.set(nameString);
    }

    //----------------------------------------------------------------------------
    public String getAddress() {
        return address.get();
    }

    //----------------------------------------------------------------------------
    public void setAddress(String naddress) {
        address.set(naddress);
    }

    //----------------------------------------------------------------------------
    public String getCity() {
        return city.get();
    }

    //----------------------------------------------------------------------------
    public void setCity(String nCity) {
        city.set(nCity);
    }
    
    //----------------------------------------------------------------------------
    public String getCode() {
        return code.get();
    }

    //----------------------------------------------------------------------------
    public void setCode(String codeString)
    {
    	code.set(codeString);
    }

    //----------------------------------------------------------------------------
    public String getZip() {
        return zip.get();
    }

    //----------------------------------------------------------------------------
    public void setZip(String zipString)
    {
    	zip.set(zipString);
    }

    //----------------------------------------------------------------------------
    public String getEmail() {
        return email.get();
    }

    //----------------------------------------------------------------------------
    public void setEmail(String emailString)
    {
    	email.set(emailString);
    }

    //----------------------------------------------------------------------------
    public String getDob() {
        return dob.get();
    }

    //----------------------------------------------------------------------------
    public void setDob(String dobString)
    {
    	dob.set(dobString);
    }

    //----------------------------------------------------------------------------
    public String getStatus() {
        return status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String statusString)
    {
    	status.set(statusString);
    }
}

