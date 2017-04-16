package java_enc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.undo.UndoManager;

public class GUI extends JFrame implements MouseListener, KeyListener {

	private static JTextPane textPane = new JTextPane();
	private static UndoManager undoManager = new UndoManager();
	private static String password = "";
	private static String filename = "";
	public GUI(String title,int width, int height) throws WarningException, Exception {
		this.setTitle("Thing");
		this.setSize(width, height);
		filename = getFilename();
		password = getPassword();
		textPane.setText(FileEncryptor.decryptFile(filename,password));
		textPane.setSelectionStart(textPane.getText().length());
		textPane.getDocument().addUndoableEditListener(undoManager);
		textPane.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,40));
		textPane.addKeyListener(this);
		JScrollPane sp = new JScrollPane(textPane);
		textPane.setMinimumSize(new Dimension(width, height));
		sp.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		System.out.println(sp.getVerticalScrollBar().getValue());
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
		JMenuItem dateitem = new JMenuItem("Date");
		dateitem.setFont(new Font("arial",Font.LAYOUT_LEFT_TO_RIGHT,25));
		dateitem.addMouseListener(this);
		dateitem.setName("date");
		insert.add(dateitem);
		menuBar.add(menu);
		menuBar.add(insert);
		this.setJMenuBar(menuBar);
		//this.pack();
		this.setDefaultLookAndFeelDecorated(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
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
		String out = "";
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		try {
			return fc.getSelectedFile().getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}
	public static String getPassword() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter a password:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"OK"};
		pass.requestFocus();
		pass.requestFocusInWindow();
		int option = JOptionPane.showOptionDialog(null, panel, "Enter Password",
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         null, options, null);
		return new String(pass.getPassword());
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
				FileEncryptor.encryptAndWriteText(textPane.getText(),password,filename);
			} catch (Exception e) {}
		} else if (arg0.getSource() instanceof JMenuItem && ((JMenuItem)arg0.getSource()).getName().equals("date")) {
			DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
			textPane.setText(textPane.getText().substring(0, textPane.getSelectionStart()) + df.format(new Date()).toString() + "\n" + textPane.getText().substring(textPane.getSelectionStart()));
			
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar()-KeyEvent.VK_CONTROL == 9 && arg0.isControlDown() && undoManager.canUndo()){
			undoManager.undo();
		} else if (arg0.getKeyChar()-KeyEvent.VK_CONTROL == 8 && arg0.isControlDown() && undoManager.canRedo()){
			undoManager.redo();
		}
		
	}
	
	
}
