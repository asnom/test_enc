package java_enc;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.undo.UndoManager;

public class Page {
    public String content;
    public String econtent;
    public String date;
    public UndoManager undoManager;
    
    public Page (String icontent, String idate) {
    	date = idate;
    	content = icontent;
    	econtent = icontent;
    	undoManager = new UndoManager();
    }
    public Page (String icontent) {
    	//this(icontent,new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString());
    	content = icontent;
    	econtent = icontent;
    	undoManager = new UndoManager();
    	String idate = JOptionPane.showInputDialog("Input Date",new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString());
    	if (idate == null || idate.isEmpty()) {
    		date = new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString();
    	} else {
    		date = idate;
    	}
    }
    public String getOutput() {
    	return date + "\n" + content;
    }
    public boolean isModified() {
    	return !content.equals(econtent);
    }
}
