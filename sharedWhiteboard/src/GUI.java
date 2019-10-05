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
import java.io.File;

import javax.swing.Box;
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
import java.awt.CardLayout;
import java.awt.Font;

class GUI extends JFrame {

	shape canvas1 = new shape();

	public GUI() {
		// drawing buttons
		JButton ovalButton = new JButton("Oval");
		ovalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.OVAL);
			}
		});
		ovalButton.setFocusable(false);

		JButton rectangleButton = new JButton("Rectangle");
		rectangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.RECTANGLE);
			}
		});
		rectangleButton.setFocusable(false);

		JButton lineButton = new JButton("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.LINE);
			}
		});
		lineButton.setFocusable(false);

		JButton circleButton = new JButton("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.CIRCLE);
			}
		});
		circleButton.setFocusable(false);

		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.DRAW);
			}
		});

		drawButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
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
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.CLEAR);
			}
		});

		/*
		 * TODO: Combine both event listeners to one (one mouseclick will be enough to
		 * set the canvas shape to CLEAR
		 */
		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
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
			public void actionPerformed(ActionEvent e) {
				Color initialcolor = Color.RED;
				Color color = JColorChooser.showDialog(GUI.this, "Select a color", initialcolor);
				canvas1.setPenColor(color);
			}
		});
		colorButton.setFocusable(false);

		JButton textButton = new JButton("Text");
		textButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.setShape(shape.TEXT);
			}
		});
		textButton.setFocusable(false);

		// file buttons
		JButton newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas1.refresh();
			}
		});
		newButton.setFocusable(false);

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
							canvas1.setFile(file);
							canvas1.open();
						}
					}
				}

				if (value == 1) {
					JFileChooser chooser = new JFileChooser();
					int cnt = chooser.showDialog(null, "open");
					if (cnt == 0) {
						File file = chooser.getSelectedFile();
						canvas1.setFile(file);
						canvas1.open();
					}
				}

			}
		});
		openButton.setFocusable(false);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				int cnt = chooser.showDialog(null, "save");
				if (cnt == 0) {
					File file = chooser.getSelectedFile();
					canvas1.setFile(file);
					canvas1.save();
					JOptionPane.showMessageDialog(null, "Saved successfully!");
				}

			}
		});
		saveButton.setFocusable(false);

		JButton saveasButton = new JButton("Save As");
		saveasButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				int cnt = chooser.showDialog(null, "save");
				if (cnt == 0) {
					File file = chooser.getSelectedFile();
					canvas1.setFile(file);
					canvas1.save();
					JOptionPane.showMessageDialog(null, "Saved successfully!");
				}

			}
		});
		saveasButton.setFocusable(false);

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		closeButton.setFocusable(false);

		JButton connectToButton = new JButton("Connect");
		connectToButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		connectToButton.setFocusable(false);

		// main panel layout
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
		filePanel.add(openButton);
		filePanel.add(saveButton);
		filePanel.add(saveasButton);
		filePanel.add(closeButton);

		// chatbox and user list layout
		JPanel chatPanel = new JPanel();
		JPanel userPanel = new JPanel();
		JPanel sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(300, 650));
		chatPanel.setPreferredSize(new Dimension(300, 650));
		userPanel.setPreferredSize(new Dimension(300, 200));
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
		JTextField hostField = new JTextField(15);
		JTextField usernameField = new JTextField(8);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Host:"));
		myPanel.add(hostField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Username:"));
		myPanel.add(usernameField);

		// overall layout
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

				int result = JOptionPane.showConfirmDialog(null, myPanel, "Enter your host and username",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					// TODO: ATTEMPT CONNECTION TO HOST, THEN OPEN CLIENT-SIDE INTERFACE

					System.out.println("x value: " + usernameField.getText());
					System.out.println("y value: " + hostField.getText());
				}

			}
		});
		connectButton.setFocusable(false);

		connectButton.setBounds(596, 223, 208, 76);
		entryPanel.add(connectButton);

		JButton hostButton = new JButton("Host a Whiteboard");
		hostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (getContentPane().getLayout());
				cl.show(content, "SERVERPANEL");
			}
		});
		hostButton.setFocusable(false);

		hostButton.setBounds(596, 311, 208, 76);
		entryPanel.add(hostButton);

		serverWhiteBoardInterface.add(main);
		serverWhiteBoardInterface.add(sidePanel);
		content.add(serverWhiteBoardInterface, "SERVERPANEL");

		// content.add(main, "MAINPANEL");
		// content.add(sidePanel, "SIDEPANEL");

		this.pack();
	}
}
