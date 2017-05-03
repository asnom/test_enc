package java_enc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

@SuppressWarnings("serial")
public class GUI extends JFrame implements MouseListener, KeyListener, WindowListener {

	private static JTextPane textPane = new JTextPane();
	private static String password = "";
	private static String filename = "";
	private static int currentPage = 0;
	private static ArrayList<Page> pages = new ArrayList<Page>();
	public GUI(String title,int width, int height) throws WarningException, Exception {
		this.setTitle("Thing");
		this.setSize(width, height);
		filename = getFilename();
		password = getPassword();
		getPages(FileEncryptor.decryptFile(filename,password));
		textPane.setText(pages.get(currentPage).content);
		SpellChecker.setUserDictionaryProvider( new FileUserDictionary() );
        
        SpellChecker.registerDictionaries( null, null );

        SpellChecker.register( textPane );
		this.setTitle(pages.get(currentPage).date);
		textPane.setSelectionStart(textPane.getText().length());
		textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
		textPane.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,40));
		textPane.addKeyListener(this);
		JScrollPane sp = new JScrollPane(textPane);
		textPane.setMinimumSize(new Dimension(width, height));
		sp.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		sp.setMinimumSize(new Dimension(width, height));
		this.add(sp);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		JMenuItem save = new JMenuItem("Save");
		save.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,25));
		save.setName("save");
		save.addMouseListener(this);
		menu.add(save);
		JMenu insert = new JMenu("Insert");
		insert.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		/*JMenuItem dateitem = new JMenuItem("Date");
		dateitem.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,25));
		dateitem.addMouseListener(this);
		dateitem.setName("date");
		insert.add(dateitem); */
		JMenuItem pageItem = new JMenuItem("Page");
		pageItem.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,25));
		pageItem.addMouseListener(this);
		pageItem.setName("page");
		insert.add(pageItem);
		JMenu nav = new JMenu("Navigate");
		nav.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		JMenuItem nextItem = new JMenuItem("Next");
		nextItem.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		nextItem.addMouseListener(this);
		nextItem.setName("next");
		JMenuItem prev = new JMenuItem("Previous");
		prev.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		prev.addMouseListener(this);
		prev.setName("prev");
		JMenuItem last = new JMenuItem("Last");
		last.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		last.addMouseListener(this);
		last.setName("last");
		JMenuItem search = new JMenuItem("Search");
		search.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,30));
		search.addMouseListener(this);
		search.setName("search");
		nav.add(nextItem);
		nav.add(prev);
		nav.add(last);
		nav.add(search);
		menuBar.add(menu);
		menuBar.add(insert);
		menuBar.add(nav);
		this.setJMenuBar(menuBar);
		//this.pack();
		setDefaultLookAndFeelDecorated(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		this.addWindowListener(this);
		setVisible(true);
	}
	private static void getPages(String textin) {
		for (String page : textin.split("===PG===")) {
			String content = new String("");
			String date = new String("");
			if (page.contains("===DATE===")) {
				if (page.split("===DATE===").length > 1)
					content = page.split("===DATE===")[1];
				date = page.split("===DATE===")[0];
			} else if (!page.isEmpty()){
				date = page.split("\n")[0];
				content = page.substring(page.indexOf('\n'));
			} else {
				date = new SimpleDateFormat("E, MMM dd yyyy").format(new Date()).toString();
				content = "";
			}
			pages.add(new Page(content,date));
		}
	}
	private static String getPages() {
		String output = "";
		for (Page page : pages){
			output = output + page.date + "===DATE===" + page.econtent + "===PG===";
			page.content = page.econtent;
		}
		return output;
	}
	public static void setText(String text) {
		textPane.setText(text);
	}
	public static void setPassword(String pw) {
		password = pw;
	}
	public static void setFilename(String fn) {
		filename = fn;
	}
	public static String getFilename() {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		try {
			return fc.getSelectedFile().getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		};
		return null;
	}
	public static String getPassword() {
		JPanel panel = new JPanel();
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		JLabel label = new JLabel("Enter a password:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"OK"};
		int option = JOptionPane.showOptionDialog(null, panel, "Enter Password",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, null);
		if (option < 0) { 
			System.exit(-1);
		}
		return new String(pass.getPassword());
	}
	public static String doSearch() {
		JPanel panel = new JPanel();
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		JLabel label = new JLabel("Select the date:");
		ArrayList<String> listEntries = new ArrayList<String>();
		for (Page page : pages) {
			listEntries.add(page.date);
		}
		JList list = new JList(listEntries.toArray()); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		for (Page page : pages) {

		}
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		panel.add(label);
		panel.add(listScroller);
		String[] options = new String[]{"OK"};
		int option = JOptionPane.showOptionDialog(null, panel, "Select Date",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, null);
		if (option < 0) { 
			return "";
		}
		if (list.getSelectedValue() != null) {
			return new String((String) list.getSelectedValue());
		} else {
			return "";
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("save")) {
			try {
				pages.get(currentPage).econtent = textPane.getText();
				FileEncryptor.encryptAndWriteText(getPages(),password,filename);
				JOptionPane.showMessageDialog(null, "Save Successful!", "Save Confirmation", JOptionPane.INFORMATION_MESSAGE);
				this.setTitle(this.getTitle().replace('*', ' ').trim());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Saving Failed.", "Error: Save Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("date")) {
			DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
			textPane.setText(textPane.getText().substring(0, textPane.getSelectionStart()) + 
					df.format(new Date()).toString() + "\n" + textPane.getText().substring(textPane.getSelectionStart()));
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("page")) {
			pages.get(currentPage).econtent = textPane.getText();
			textPane.getDocument().removeUndoableEditListener(pages.get(currentPage).undoManager);
			String mod = "";
			if (this.getTitle().contains("*")) {
				mod = "*";
			}
			Page newPage = new Page("");
			boolean found = false;
			for (int i = 0; i < pages.size(); i++) {
				if (pages.get(i).date.equals(newPage.date)) {
					currentPage = i;
					this.setTitle(pages.get(currentPage).date + mod);
					textPane.setText(pages.get(currentPage).econtent);
					textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
					found = true;
					break;
				}
			}
			if (!found) {
				pages.add(newPage);
				currentPage = pages.size()-1;
				this.setTitle(pages.get(currentPage).date + mod);
				textPane.setText("");
				textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
			}
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("next")) {
			pages.get(currentPage).econtent = textPane.getText();
			textPane.getDocument().removeUndoableEditListener(pages.get(currentPage).undoManager);
			String mod = "";
			if (this.getTitle().contains("*")) {
				mod = "*";
			}
			if (currentPage+1 < pages.size()) {
				currentPage++;
			}
			this.setTitle(pages.get(currentPage).date + mod);
			textPane.setText(pages.get(currentPage).econtent);
			textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("prev")) {
			pages.get(currentPage).econtent = textPane.getText();
			textPane.getDocument().removeUndoableEditListener(pages.get(currentPage).undoManager);
			String mod = "";
			if (this.getTitle().contains("*")) {
				mod = "*";
			}
			if (currentPage-1 >= 0) {
				currentPage--;
			}
			this.setTitle(pages.get(currentPage).date + mod);
			textPane.setText(pages.get(currentPage).econtent);
			textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("last")) {
			pages.get(currentPage).econtent = textPane.getText();
			textPane.getDocument().removeUndoableEditListener(pages.get(currentPage).undoManager);
			String mod = "";
			if (this.getTitle().contains("*")) {
				mod = "*";
			}
			currentPage=pages.size()-1;
			this.setTitle(pages.get(currentPage).date + mod);
			textPane.setText(pages.get(currentPage).econtent);
			textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("search")) {
			pages.get(currentPage).econtent = textPane.getText();
			textPane.getDocument().removeUndoableEditListener(pages.get(currentPage).undoManager);
			String mod = "";
			if (this.getTitle().contains("*")) {
				mod = "*";
			}
			String date = doSearch();
			System.out.println(date);
			for (int i = 0; i < pages.size(); i++) {
				if (pages.get(i).date.equals(date)) {
					currentPage = i;
					break;
				}
			}
			this.setTitle(pages.get(currentPage).date + mod);
			textPane.setText(pages.get(currentPage).econtent);
			textPane.getDocument().addUndoableEditListener(pages.get(currentPage).undoManager);
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {
	//	System.out.println(arg0.getKeyChar()-KeyEvent.VK_CONTROL);
		String newkey = ""+arg0.getKeyChar();

		if (!newkey.matches("[!-~ ]")) {
			newkey="";
		}
		if ((textPane.getText()+newkey).equals(pages.get(currentPage).content) && !isEdited()) {
			this.setTitle(this.getTitle().replace('*', ' ').trim());
		} else if (!this.getTitle().contains("*")) {
			this.setTitle(this.getTitle().replace('*', ' ').trim()+"*");
		}
		if (arg0.getKeyChar()-KeyEvent.VK_CONTROL == 9 && arg0.isControlDown() && pages.get(currentPage).undoManager.canUndo()){
			pages.get(currentPage).undoManager.undo();
			if ((textPane.getText()).equals(pages.get(currentPage).content) && !isEdited()) {
				this.setTitle(this.getTitle().replace('*', ' ').trim());
			} else if (!this.getTitle().contains("*")) {
				this.setTitle(this.getTitle().replace('*', ' ').trim()+"*");
			}
		} else if (arg0.getKeyChar()-KeyEvent.VK_CONTROL == 8 && arg0.isControlDown() && pages.get(currentPage).undoManager.canRedo()){
			pages.get(currentPage).undoManager.redo();
			if ((textPane.getText()).equals(pages.get(currentPage).content) && !isEdited()) {
				this.setTitle(this.getTitle().replace('*', ' ').trim());
			} else if (!this.getTitle().contains("*")) {
				this.setTitle(this.getTitle().replace('*', ' ').trim()+"*");
			}
		} else if (arg0.getKeyChar()-KeyEvent.VK_CONTROL == 2 && arg0.isControlDown()){
			try {
				pages.get(currentPage).econtent = textPane.getText();
				FileEncryptor.encryptAndWriteText(getPages(),password,filename);
				JOptionPane.showMessageDialog(null, "Save Successful!", "Save Confirmation", JOptionPane.INFORMATION_MESSAGE);
				this.setTitle(this.getTitle().replace('*', ' ').trim());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Saving Failed.", "Error: Save Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {
       System.exit(0);
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		if (this.getTitle().contains("*")) {
			if (JOptionPane.showConfirmDialog(null, "Unsaved Changes. Are you sure you want to exit?", 
					"Unsaved Changes!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0) 
				this.dispose();
		} else {
			this.dispose();
		}
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}

    public boolean isEdited() {
    	for (int i = 0; i < pages.size(); i++) {
    		if (i == currentPage) {
    			continue;
    		} else if (pages.get(i).isModified()) {
    			return true;
    		}
    	}
    	return false;
    }
}
