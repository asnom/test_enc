package java_enc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
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
    	SpinnerDateModel sModel = new SpinnerDateModel(){
    		@Override
    		public Object getNextValue() {
	    		Calendar cal = Calendar.getInstance();
	    		cal.setTime(((Date) getValue()));
	    		cal.add(Calendar.DAY_OF_MONTH, 1);
	    		Date next = cal.getTime();
	    		return ((getEnd() == null) || (getEnd().compareTo(next) >= 0)) ? next : null;
			}
		};
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
    	JSpinner spinner = new JSpinner(sModel);
    	spinner.setEditor(new JSpinner.DateEditor(spinner,dateFormat.toPattern()));
    	JOptionPane.showMessageDialog(null, spinner);
    	String idate=dateFormat.format(spinner.getValue());
    	//String idate = JOptionPane.showInputDialog("Input Date",new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString());
    	if (idate == null || idate.isEmpty()) {
    		date = new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString();
    	} else {
    		date = idate;
    	}
    }
    public String getOutput() {
    	return date + "\n" + content.replaceAll("’", "'");
    }
    public boolean isModified() {
    	return !content.equals(econtent);
    }
}
