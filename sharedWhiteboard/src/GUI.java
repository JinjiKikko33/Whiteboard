

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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class GUI extends JFrame { 


    shape canvas1 = new shape();
    

    
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
	    textButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	   
	    	}
	    });
	    
	    JButton saveasButton = new JButton("Save As");
	    textButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		

	    	}
	    });
	    
	    JButton newButton = new JButton("New");
	    textButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

	    	}
	    });
	    
	    JButton closeButton = new JButton("Close");
	    textButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

	    	}
	    });
	    
	    JButton connectToButton = new JButton("Connect");
	    connectToButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

	    	}
	    });
	    
	    
	    

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
	    
	    
	    //overall layout
	    JPanel main = new JPanel();
	    main.setLayout(new BorderLayout());
	    
	    
	    
	    
	    main.add(canvas1, BorderLayout.CENTER);
	    main.add(buttonPanel, BorderLayout.SOUTH);
	    main.add(filePanel, BorderLayout.NORTH);
	    
	    Container content = this.getContentPane();
	    content.setLayout(new FlowLayout());
	    
	    
	    content.add(main);
	    content.add(sidePanel);
	    

	
	    this.pack();
    }

}

