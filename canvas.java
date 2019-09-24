package sharedWhiteboard;

import java.io.Serializable;

import javax.swing.JFrame;

class canvas implements Serializable {

	public static void main(String[] args) {
		GUI window = new GUI();
		window.setTitle("Shared Whiteboard");
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
  

