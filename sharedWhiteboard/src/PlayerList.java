import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PlayerList extends JPanel{
	
	private int WIDTH = 300;
	private int HEIGHT = 200;
	private Color bg = Color.white;
	private JPanel main;
	private JList<String> userList;
	private JScrollPane scrollPane;
	private JButton submit;
	private DefaultListModel<String> model;
	private int index=-1;
	
	//information required for server
	String removedUser;
	boolean isKickOut = false;;

	
	public PlayerList() {
		//set up main panel
		main = new JPanel(new BorderLayout());
		main.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		main.setBackground(bg); 
		//set up title
	    JLabel user_title = new JLabel();
	    user_title.setText("Current players:");
	    main.add(user_title, BorderLayout.NORTH);
	    //set up JList
	    model = new DefaultListModel<String>();
	    userList = new JList<String>(model);
		scrollPane = new JScrollPane(userList);
		main.add(scrollPane, BorderLayout.CENTER);
		//set up submit button
		submit = new JButton("Remove!");
		main.add(submit, BorderLayout.SOUTH);
	    add(main);
	    
	    //click event for JList
		MouseListener mouseListener = new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
	            index = userList.locationToIndex(e.getPoint());
	            System.out.println("clicked on Item " + index);
		    }
		};
		userList.addMouseListener(mouseListener);
		
		//add event for submit button
		submit.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(index>=0) {
	    			//removedUser = model.elementAt(index);
	    			model.removeElementAt(index);
	    			isKickOut = true;
	    		}
	    	}
	    });
	    
	    
	}
	
	public void updatePlayerList(String username) {
		model.addElement(username);
	}
	
	public void deletePlayer(String username) {
		if (model.contains(username)) {
			model.removeElement(username);
		}
	}
	
	public void denyPopup(String s) {
		if(s.equals("kick out")) {
			JOptionPane.showMessageDialog(null,
			    "You are beinng kicked out by the whiteboard manager");
		}else if(s.equals("refuse connection")) {
			JOptionPane.showMessageDialog(null,
				    "Your connection request was rejected by manager");
		}else if(s.equals("username taken")) {
			JOptionPane.showMessageDialog(null,
				    "Username has been taken. Please change a new one");
		}
	}
	
	public boolean acceptPopup(String s) {
		int result = JOptionPane.showConfirmDialog(
			    null,
			    "Do you want new player "+s+" to join the game?",
			    "New Player Joining",
			    JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			return true;
		}else {
			return false;
		}
	}
}
