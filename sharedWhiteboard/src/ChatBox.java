
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatBox extends JPanel {

	private JPanel chatPanel;
	private JPanel sendPanel;
	private JPanel main;
	private JButton sendButton;
	private JTextArea ta;
	private JTextField tx;
	static DataOutputStream dout;
	String serverUsername;
	String username;
	
	boolean clientMode = false;
	

	private String message;
	// boolean isSendChat = false;

	public ChatBox() {

		main = new JPanel(new BorderLayout());
		chatPanel = new JPanel();
		sendPanel = new JPanel();
		chatPanel.setBackground(Color.white);
		sendPanel.setBackground(Color.white);
		sendPanel.setPreferredSize(new Dimension(300, 50));

		chatPanel.setPreferredSize(new Dimension(300, 400));
		

		tx = new JTextField(15);
		sendPanel.add(tx, BorderLayout.WEST);

		sendButton = new JButton("Send");
		sendPanel.add(sendButton, BorderLayout.EAST);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("sendButton working");
				sendChat();
				tx.setText("");
			}
		});

		JLabel chat_title = new JLabel();
		chat_title.setText("Chat Box");
		chatPanel.add(chat_title, BorderLayout.NORTH);

		ta = new JTextArea(20, 20);
		chatPanel.add(ta, BorderLayout.SOUTH);
		ta.setRows(20);
		JScrollPane scroll = new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatPanel.add(scroll);

		main.add(chatPanel, BorderLayout.NORTH);
		main.add(sendPanel, BorderLayout.SOUTH);
		add(main);
	}
	
	
	public void setClientGUI() {
		chatPanel.setPreferredSize(new Dimension(300, 600));
		ta.setRows(32);
	}
	
	public void setServerUsername(String name) {
		serverUsername = name;
	}
	
	public void updateChat(String s) {
		ta.append(s + "\n");
	}

	public void sendChat() {
		// System.out.println(111111);
		message = tx.getText();
		Message chat = new Message();
		chat.setRequestType(7);
		if(clientMode) {
			chat.setUsername(username);
		}else {
			chat.setUsername(serverUsername);
		}
		chat.setChatMessage(message);
		if(clientMode) {
			updateChat(username + " : " + message);
		}else {
			updateChat(serverUsername + " : " + message);
		}
		String strChat = Message.toJson(chat);
		// System.out.println(strChat);
		
		if(clientMode) {
			try {
				System.out.println(strChat);
				dout.writeUTF(strChat);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		}else {
			for (Socket s : ActiveConnections.endPoints) {
				DataOutputStream toClient = null;
				try {
					toClient = new DataOutputStream(s.getOutputStream());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					toClient.writeUTF(strChat);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ActiveConnections.endPoints.remove(s);
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					continue;
				}
			}
		}

		

	}

	public void addOutputStream(DataOutputStream dout) {
		System.out.println("Added outputstream");
		this.dout = dout;

	}

}
