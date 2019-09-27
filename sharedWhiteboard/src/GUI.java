

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	    JButton rectangleButton = new JButton("Rectangle");
	    rectangleButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.RECTANGLE);
	    	}
	    });

	    JButton lineButton = new JButton("Line");
	    lineButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.LINE);
	    	}
	    });
    
	    JButton circleButton = new JButton("Circle");
	    circleButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		canvas1.setShape(shape.CIRCLE);
	    	}
	    });
	    
	    JButton drawButton = new JButton("Draw");
	    drawButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    	    canvas1.setShape(shape.DRAW);
	    	}
	    });
	    
	    JButton clearButton = new JButton("Eraser");
	    clearButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    	    canvas1.setShape(shape.CLEAR);
	    	}
	    });
	    
	    JButton colorButton = new JButton("Color");
	    colorButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		Color initialcolor=Color.RED;    
	    		Color color=JColorChooser.showDialog(GUI.this,"Select a color",initialcolor);    
	    		canvas1.setPenColor(color); 
	    	}
	    });
	    
	    JButton textButton = new JButton("Text");
	    textButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){

	    	}
	    });
	    
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

