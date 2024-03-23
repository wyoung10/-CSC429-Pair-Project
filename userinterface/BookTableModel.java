package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class BookTableModel
{
	private final SimpleStringProperty bookTitle;
	private final SimpleStringProperty author;
	private final SimpleStringProperty pubYear;
	private final SimpleStringProperty bstatus;
    private final SimpleStringProperty bookId;

	//----------------------------------------------------------------------------
	public BookTableModel(Vector<String> bookData)
	{
        bookId = new SimpleStringProperty(bookData.elementAt(0));
		bookTitle =  new SimpleStringProperty(bookData.elementAt(1));
		author =  new SimpleStringProperty(bookData.elementAt(2));
		pubYear =  new SimpleStringProperty(bookData.elementAt(3));
		bstatus =  new SimpleStringProperty(bookData.elementAt(4));
	}

    //----------------------------------------------------------------------------
    public String getBookId() {
        return bookId.get();
    }

    //----------------------------------------------------------------------------
    public void setBookId(String idString) {
        bookId.set(idString);
    }
	//----------------------------------------------------------------------------
	public String getBookTitle() {
        return bookTitle.get();
    }

	//----------------------------------------------------------------------------
    public void setBookTitle(String title) {
        bookTitle.set(title);
    }

    //----------------------------------------------------------------------------
    public String getAuthor() {
        return author.get();
    }

    //----------------------------------------------------------------------------
    public void setAuthor(String nauthor) {
        author.set(nauthor);
    }

    //----------------------------------------------------------------------------
    public String getPubYear() {
        return pubYear.get();
    }

    //----------------------------------------------------------------------------
    public void setPubYear(String nPubYear) {
        pubYear.set(nPubYear);
    }
    
    //----------------------------------------------------------------------------
    public String getStatus() {
        return bstatus.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String statusString)
    {
    	bstatus.set(statusString);
    }
}
