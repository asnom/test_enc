package java_enc;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		try {
		    GUI gui = new GUI("Title",1200,800);
		} catch (WarningException e) {
			
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "The password that was input was incorrect." ,"Incorrect Password", JOptionPane.ERROR_MESSAGE);
		}
		//System.out.println(FileEncryptor.decryptFile("stringdata.out","redgreenblue"));
	//	System.out.println(new String(encValue));

	}
}
