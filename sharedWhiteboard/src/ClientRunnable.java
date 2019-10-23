import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;

import javax.swing.JOptionPane;

// We listen for updates from the host owner in a separate thread

public class ClientRunnable implements Runnable {
	Socket conn;
	shape canvas1;
	PlayerList userPanel;

	CardLayout cl;
	Container container; 

	DataInputStream din;

	public ClientRunnable(Socket conn, shape canvas1, String username, PlayerList userPanel,
			Container c, CardLayout cl) {
		this.conn = conn;
		this.canvas1 = canvas1;
		this.userPanel = userPanel;
		this.container = c;
		this.cl = cl;
		


		try {
			din = new DataInputStream(conn.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

			//TODO: Implement server error
			handleServerDisconnect();

		}
		if (!freeUsername(username)) {
			throw new IllegalArgumentException();
		}


	}

	private boolean freeUsername(String username) {
		Message m = new Message();
		m.setRequestType(10);
		m.setUsername(username);
		Message in = new Message();

		try {
			DataOutputStream dout = new DataOutputStream(conn.getOutputStream());
			dout.writeUTF(Message.toJson(m));
		} catch (IOException e) {
			return false;
		}

		try {
			in = Message.makeMessageFromJson(din.readUTF());
		} catch (IOException e) {
			return false;
		}

		if (in.isConnectionDenied() && in.getDeniedMessage().equals("username taken")){
			System.out.println("username taken!!");
			userPanel.denyPopup(in.getDeniedMessage());
			return false;
		} else if(in.isConnectionDenied() && in.getDeniedMessage().equals("refuse connection")) {
			System.out.println("refuse connection!!");
			userPanel.denyPopup(in.getDeniedMessage());
			return false;
		} else {
			canvas1.drawServerShape(in);
			
			return true;
		}


	}


	@Override
	public void run() {
		while (true) {
			String request = null;
			try {
				request = din.readUTF();
			} catch (IOException e) {
				System.err.println("ERROR: Could not get request from server");
				//e.printStackTrace();
				handleServerDisconnect();
			}


			Message m = Message.makeMessageFromJson(request);
			//check whether server refuse connection
			if (m.isConnectionDenied()) {
				try {
				String denyMsg = m.getDeniedMessage();
				System.out.println(denyMsg);
				conn.close();
				userPanel.denyPopup(denyMsg); //pop up window before closing the program
				canvas1.refresh();
    	        CardLayout cl = (CardLayout)(container.getLayout());
    	        cl.show(container, "ENTRYPANEL");				
				break;
				}
				catch (IOException e) {
					//e.printStackTrace();
				}
			}

			// draw locally
			//System.out.println("REQ:" + m.getRequestType());
			canvas1.drawServerShape(m);

		}
	}

	private void handleServerDisconnect() {
		JOptionPane.showMessageDialog(null, "Error: Could not make contact with the server");
		closeAllConnections();
		canvas1.refresh();
		CardLayout cl = (CardLayout)(container.getLayout());
		cl.show(container, "ENTRYPANEL");
	}
	
	private void closeAllConnections() {
		try {
			din.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
