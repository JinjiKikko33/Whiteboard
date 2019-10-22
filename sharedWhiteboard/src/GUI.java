
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Button;

class GUI extends JFrame {


    shape canvas1 = new shape();
	private File openedFile = null;



    public GUI() {
    	//drawing buttons
	    JButton ovalButton = new JButton("Oval");
	    ovalButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.OVAL);
	    	}
	    });
	    ovalButton.setFocusable(false);


	    JButton rectangleButton = new JButton("Rectangle");
	    rectangleButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.RECTANGLE);
	    	}
	    });
	    rectangleButton.setFocusable(false);


	    JButton lineButton = new JButton("Line");
	    lineButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.LINE);
	    	}
	    });
	    lineButton.setFocusable(false);


	    JButton circleButton = new JButton("Circle");
	    circleButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.CIRCLE);
	    	}
	    });
	    circleButton.setFocusable(false);


	    JButton drawButton = new JButton("Draw");
	    drawButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    	    canvas1.setShape(shape.DRAW);
	    	}
	    });

	    drawButton.addMouseListener(new MouseAdapter(){
	        @Override
	        public void mouseClicked(MouseEvent e){
	            if(e.getClickCount()==2){
	            	JFrame parent = new JFrame();
		    	    JOptionPane sliderPane = new JOptionPane();

		    	    JSlider slider = SliderMaker.getSlider(sliderPane, canvas1.getPenThickness());
		    	    sliderPane.setMessage(new Object[] { "", slider });
		    	    sliderPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);

		    	    JDialog dialog = sliderPane.createDialog(parent, "Pen Thickness");
		    	    dialog.setVisible(true);

		    	    if (sliderPane.getInputValue().equals("uninitializedValue")) {
			    	    canvas1.setPenThickness(-1);
		    	    } else {
			    	    canvas1.setPenThickness((int) sliderPane.getInputValue());
		    	    }
	            }
	        }
	    });
	    drawButton.setFocusable(false);



	    JButton clearButton = new JButton("Eraser");
	    clearButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.CLEAR);
	    	}
	    });


	    /* TODO: Combine both event listeners to one (one mouseclick will be enough
	    * to set the canvas shape to CLEAR
	    */
	    clearButton.addMouseListener(new MouseAdapter(){
	        @Override
	        public void mouseClicked(MouseEvent e){
	            if(e.getClickCount()==2){
	            	JFrame parent = new JFrame();
		    	    JOptionPane sliderPane = new JOptionPane();
		    	    JSlider slider = SliderMaker.getSlider(sliderPane, canvas1.getEraserThickness());
		    	    sliderPane.setMessage(new Object[] { "", slider });
		    	    sliderPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		    	    JDialog dialog = sliderPane.createDialog(parent, "Eraser Thickness");
		    	    dialog.setVisible(true);
		    	    if (sliderPane.getInputValue().equals("uninitializedValue")) {
			    	    canvas1.setEraserThickness(-1);
		    	    } else {
			    	    canvas1.setEraserThickness((int) sliderPane.getInputValue());
		    	    }
	            }
	        }
	    });
	    clearButton.setFocusable(false);


	    JButton colorButton = new JButton("Color");
	    colorButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		Color initialcolor=Color.RED;
	    		Color color=JColorChooser.showDialog(GUI.this,"Select a color",initialcolor);
	    		canvas1.setPenColor(color);
	    	}
	    });
	    colorButton.setFocusable(false);

	    JButton textButton = new JButton("Text");
	    textButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.TEXT);
	    	}
	    });
	    textButton.setFocusable(false);

	    //file buttons
	    JButton saveButton = new JButton("Save");
	    saveButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		if (openedFile != null) {
					canvas1.setFile(openedFile);
					canvas1.save();
				} else {
					JFileChooser chooser = new JFileChooser();
					int cnt = chooser.showDialog(null, "save");
					if (cnt == 0) {
						File file = chooser.getSelectedFile();
						if (file != null) {
							canvas1.setFile(file);
							canvas1.save();
						}
						JOptionPane.showMessageDialog(null, "Saved successfully!");
					}
				}

	    	}
	    });
	    saveButton.setFocusable(false);


		JButton openButton = new JButton("Open");
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int value = JOptionPane.showConfirmDialog(null, "Do you want to save changes?", "Message", 0);
				if (value == 0) {
					JFileChooser newChooser = new JFileChooser();
					int newcnt = newChooser.showDialog(null, "save");
					if (newcnt == 0) {
						File newFile = newChooser.getSelectedFile();
						canvas1.setFile(newFile);
						canvas1.save();
						JOptionPane.showMessageDialog(null, "Saved successfully!");

						JFileChooser chooser = new JFileChooser();
						int cnt = chooser.showDialog(null, "open");
						if (cnt == 0) {
							File file = chooser.getSelectedFile();
							if (file != null) {
								openedFile = file;
								canvas1.setFile(file);
								canvas1.open();
							}
						}
					}
				}

				if (value == 1) {
					JFileChooser chooser = new JFileChooser();
					int cnt = chooser.showDialog(null, "open");
					if (cnt == 0) {
						File file = chooser.getSelectedFile();
						if (file != null) {
							openedFile = file;
							canvas1.setFile(file);
							canvas1.open();
						}
					}
				}

			}
		});
		openButton.setFocusable(false);


	    JButton saveasButton = new JButton("Save As");
	    saveasButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				int cnt = chooser.showDialog(null, "save");
				if (cnt == 0) {
					File file = chooser.getSelectedFile();
					if (file != null) {
						canvas1.setFile(file);
						canvas1.save();
					}
					JOptionPane.showMessageDialog(null, "Saved successfully!");
				}

	    	}
	    });
	    saveasButton.setFocusable(false);


	    JButton newButton = new JButton("New");
	    newButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

				openedFile = null;
	    		canvas1.refresh();

	    		Message m = new Message();
	    		m.setRequestType(shape.CLEARCANVAS);

	    		canvas1.setMessage(m);
	    		canvas1.sendMessage();
	    	}
	    });
	    newButton.setFocusable(false);


	    JButton closeButton = new JButton("Close");
	    closeButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

	    	}
	    });
	    closeButton.setFocusable(false);


	    JButton connectToButton = new JButton("Connect");
	    connectToButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

	    	}
	    });
	    connectToButton.setFocusable(false);





	    //main panel layout
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new GridLayout(1, 8));
	    buttonPanel.add(ovalButton);
	    buttonPanel.add(rectangleButton);
	    buttonPanel.add(lineButton);
	    buttonPanel.add(circleButton);
	    buttonPanel.add(textButton);
	    buttonPanel.add(drawButton);
	    buttonPanel.add(clearButton);
	    buttonPanel.add(colorButton);

	    JPanel filePanel = new JPanel();
	    filePanel.setLayout(new GridLayout(1, 4));
	    filePanel.add(newButton);
	    filePanel.add(saveButton);
	    filePanel.add(saveasButton);
	    filePanel.add(closeButton);
		filePanel.add(openButton);



	    //chatbox and user list layout
	    JPanel chatPanel = new JPanel();
	    JPanel userPanel = new JPanel();
	    JPanel sidePanel = new JPanel();
	    sidePanel.setPreferredSize(new Dimension(300,650));
	    chatPanel.setPreferredSize(new Dimension(300,650));
	    userPanel.setPreferredSize(new Dimension(300,200));
	    chatPanel.setBackground(Color.white);
	    userPanel.setBackground(Color.white);
	    sidePanel.setLayout(new FlowLayout());
	    sidePanel.add(userPanel);
	    sidePanel.add(chatPanel);

	    JLabel chat_title = new JLabel();
	    chat_title.setText("Chat Box");
	    chatPanel.add(chat_title);

	    JLabel user_title = new JLabel();
	    user_title.setText("Current Players");
	    userPanel.add(user_title);

	    // Panel and items for dialog
	    JTextField serverHostField = new JTextField(15);
	    JTextField serverUsernameField = new JTextField(8);
	    JTextField serverPortField = new JTextField(5);
	    
	    JTextField hostField = new JTextField(15);
	    JTextField usernameField = new JTextField(8);
	    JTextField portField = new JTextField(5);

	    JPanel clientConnectionPanel = new JPanel();
	    clientConnectionPanel.add(new JLabel("Host:"));
	    clientConnectionPanel.add(hostField);
	    clientConnectionPanel.add(Box.createHorizontalStrut(15)); // a spacer
	    clientConnectionPanel.add(new JLabel("Username:"));
	    clientConnectionPanel.add(usernameField);
	    clientConnectionPanel.add(Box.createVerticalStrut(15)); // a spacer
	    clientConnectionPanel.add(new JLabel("Port:"));
	    clientConnectionPanel.add(portField);
	    
	    JPanel serverConnectionPanel = new JPanel();
	    serverConnectionPanel.add(Box.createHorizontalStrut(15)); // a spacer
	    serverConnectionPanel.add(new JLabel("Username:"));
	    serverConnectionPanel.add(serverUsernameField);
	    serverConnectionPanel.add(Box.createVerticalStrut(15)); // a spacer
	    serverConnectionPanel.add(new JLabel("Port:"));
	    serverConnectionPanel.add(serverPortField);

	    	    //overall layout
	    JPanel main = new JPanel();
	    main.setLayout(new BorderLayout());



	    main.add(canvas1, BorderLayout.CENTER);
	    main.add(buttonPanel, BorderLayout.SOUTH);
	    main.add(filePanel, BorderLayout.NORTH);

	    JPanel serverWhiteBoardInterface = new JPanel();

	    Container content = this.getContentPane();
	    getContentPane().setLayout(new CardLayout(0, 0));

	    JPanel entryPanel = new JPanel();
	    entryPanel.setLayout(null);

	    getContentPane().add(entryPanel, "ENTRYPANEL");



	    JLabel lblWelcomeToThe = new JLabel("Welcome to the Shared Whiteboard");
	    lblWelcomeToThe.setBounds(445, 26, 462, 33);
	    lblWelcomeToThe.setFont(new Font("Lucida Grande", Font.PLAIN, 27));
	    entryPanel.add(lblWelcomeToThe);

	    JButton connectButton = new JButton("Connect to a Whiteboard");
	    connectButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {

	    	    int result = JOptionPane.showConfirmDialog(null, clientConnectionPanel,
	    	            "Enter your host and username", JOptionPane.OK_CANCEL_OPTION);

	    	          System.out.println("username: " + usernameField.getText());
	    	          System.out.println("host: " + hostField.getText());
	    	          System.out.println("port: " + portField.getText());

	    	        if (result == JOptionPane.OK_OPTION) {

	    	          try {
	    	        	  String host = hostField.getText();
	    	        	  int port = Integer.parseInt(portField.getText());
	    	        	  Socket conn = new Socket(host, port);
	    	        	  DataInputStream din = new DataInputStream(conn.getInputStream());
	    	        	  DataOutputStream dout = new DataOutputStream(conn.getOutputStream());

	    	        	  canvas1.addOutputStream(dout);

	    	        	  ClientRunnable clir;
	    	        	  // handle exception here (no contact with server or username taken
	    	        	  try {
	    	        		clir = new ClientRunnable(conn, canvas1, usernameField.getText());
	    	        	  } catch (IllegalArgumentException ilex) {
	    	        		  System.err.println("Error: Taken");
	    	        		  conn.close();
	    	        		  JOptionPane.showMessageDialog(null, "Error: Username already taken");
	    	        		  return;
	    	        	  }

	    	        	  new Thread(clir).start();
	    	          } catch (IOException ex) {
	    	        	  System.err.println("Error: Could not connect to server. Check the host and port number");

	    	          }
	    	        CardLayout cl = (CardLayout)(getContentPane().getLayout());
 	    	        filePanel.setVisible(false);
	  	    		  cl.show(content, "SERVERPANEL");

	    	        }
	    	     }
	    });
	    connectButton.setFocusable(false);

	    connectButton.setBounds(596, 223, 208, 76);
	    entryPanel.add(connectButton);

	    JButton hostButton = new JButton("Host a Whiteboard");
	    hostButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    int result = JOptionPane.showConfirmDialog(null, serverConnectionPanel,
	    	            "Enter your username and port number", JOptionPane.OK_CANCEL_OPTION);
	    	   
	    	    if (result == JOptionPane.OK_OPTION) {
	    	    	//select port number
					System.out.println("username: " + serverUsernameField.getText());
					System.out.println("port: " + serverPortField.getText());
					String username = serverUsernameField.getText();
					int port = Integer.parseInt(serverPortField.getText());
					Server s = new Server(canvas1, port);
					new Thread(s).start();

					// TODO: handle port number in use exception

		    		CardLayout cl = (CardLayout)(getContentPane().getLayout());
		    		cl.show(content, "SERVERPANEL");
	    	    }
	    	}
	    });

	    hostButton.setFocusable(false);

	    hostButton.setBounds(596, 311, 208, 76);
	    entryPanel.add(hostButton);


	    serverWhiteBoardInterface.add(main);
	    serverWhiteBoardInterface.add(sidePanel);
	    content.add(serverWhiteBoardInterface, "SERVERPANEL");

	    //content.add(main, "MAINPANEL");
	    //content.add(sidePanel, "SIDEPANEL");

	    this.pack();
    }
}
