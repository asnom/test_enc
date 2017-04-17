package java_enc;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Page {
    public String content;
    public String date;
    
    public Page (String icontent, String idate) {
    	date = idate;
    	content = icontent;
    }
    public Page (String icontent) {
    	this(icontent,new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString());    	
    }
    public String getOutput() {
    	return date + "\n" + content;
    }
}
