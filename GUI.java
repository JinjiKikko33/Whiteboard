package sharedWhiteboard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class GUI extends JFrame { 


    shape canvas1 = new shape();
    
    public GUI() {
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
    


    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 3));
    buttonPanel.add(ovalButton);
    buttonPanel.add(rectangleButton);
    buttonPanel.add(lineButton);
    buttonPanel.add(circleButton);

    Container content = this.getContentPane();
    content.setLayout(new BorderLayout());
    content.add(buttonPanel, BorderLayout.NORTH);
    content.add(canvas1, BorderLayout.CENTER);

    this.pack();
    }

}

