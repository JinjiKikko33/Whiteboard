import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.google.gson.Gson;

public class ServerRunnable implements Runnable {

	Socket conn; 
	shape canvas1; 
	
	public ServerRunnable(Socket conn, shape canvas) {
		this.canvas1 = canvas;
		this.conn = conn;
	
	}
	
	@Override
	public void run() {
		DataInputStream din = null;
		DataOutputStream dout = null;
		
		
		try {
		 din = new DataInputStream(conn.getInputStream());
		 dout = new DataOutputStream(conn.getOutputStream());
		} catch (IOException e) {
			System.err.println("ERROR: Could not initialize streams. Removing user");
			
			ActiveConnections.endPoints.remove(conn);
			ActiveConnections.SocketUsernameMap.remove(conn);
			
			// remove from chat list? 
			return;
		}
		
		while (true) {
			// readin JSON
			String request = null;
			try {
				request = din.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("ERROR: Lost connection with client");
				e.printStackTrace();
				try {
					ActiveConnections.endPoints.remove(conn);
					ActiveConnections.SocketUsernameMap.remove(conn);

					conn.close();
					return;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			}
			
			Message m = Message.makeMessageFromJson(request);
			
			// check username
			if (m.getRequestType() == 10) {
				Message reply = new Message();
				String desiredName = m.getUsername();
				
				
				for (String s : ActiveConnections.SocketUsernameMap.values()) {
					if (s.equals(desiredName)){
						reply.setConnectionDenied(true);
						reply.setDeniedMessage("username taken");
						String strReply = Message.toJson(reply);
						try { 
							dout.writeUTF(strReply);
							System.out.println(strReply);
							return;
						} catch (IOException e) {
							ActiveConnections.endPoints.remove(conn);
							try {
								conn.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
				}
				// username free
				ActiveConnections.SocketUsernameMap.put(conn, desiredName);
				reply.setConnectionDenied(false);
				String strReply = Message.toJson(reply);	
				try { 
					dout.writeUTF(strReply);
				} catch (IOException e) {
					ActiveConnections.endPoints.remove(conn);
					ActiveConnections.SocketUsernameMap.remove(conn);
					e.printStackTrace();
				}
				
				continue;
				
			}
			
			//System.out.println("IN LOOP: " + m);
			// draw locally
			canvas1.drawServerShape(m);
			
			// pass instruction to all non-originating clients
			InetAddress addr = conn.getInetAddress();
			for (Socket activeConnection : ActiveConnections.endPoints) {
				if (activeConnection.getInetAddress() != addr) {
					try {
						DataOutputStream activeOut = new DataOutputStream(activeConnection.getOutputStream());
						activeOut.writeUTF(request);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					
					
				}
			}
			
			
			
			
			
			
		}
		
		
	}

}
