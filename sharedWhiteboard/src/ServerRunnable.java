import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.google.gson.Gson;

public class ServerRunnable implements Runnable {

	Socket conn;
	shape canvas1;
	PlayerList userPanel;
	String username;
	String managerUsername;

	public ServerRunnable(Socket conn, shape canvas, PlayerList userPanel, String managerUsername) {
		this.canvas1 = canvas;
		this.conn = conn;
		this.userPanel = userPanel;
		this.managerUsername = managerUsername;
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
				if (conn.isClosed()) {
					return;
				}
			
				String request = null;
				try {
					request = din.readUTF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("ERROR: Lost connection with client");
					//e.printStackTrace();
					try {
						ActiveConnections.endPoints.remove(conn);
						if (ActiveConnections.SocketUsernameMap.containsKey(username)) {
							ActiveConnections.SocketUsernameMap.remove(username);
							userPanel.deletePlayer(username);
						}
						conn.close();
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} 

				Message m = Message.makeMessageFromJson(request);
			

				// check username
				if (m.getRequestType() == 10) {
					String desiredName = m.getUsername();


					if (desiredName.equals(managerUsername)) {
						sendTakenUsernameMessage(dout);
						return;
					}
					
					for (String s : ActiveConnections.SocketUsernameMap.keySet()) {

						if (s.equals(desiredName))	{	
							sendTakenUsernameMessage(dout);
							return;
						}
					}
					
					//manager refuse connection
					boolean isAccept = userPanel.acceptPopup(desiredName);
					if (!isAccept) {
						Message reply = new Message();
						reply.setConnectionDenied(true);
						reply.setDeniedMessage("refuse connection");
						String strReply = Message.toJson(reply);
						try {
							dout.writeUTF(strReply);
							conn.close();
							ActiveConnections.endPoints.remove(conn);
							return;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					// username free
					Message reply = new Message();
					ActiveConnections.SocketUsernameMap.put(desiredName, conn);
					reply.setConnectionDenied(false);
					
					//update local username
					username = desiredName;
					
					//update playerlist locally
					userPanel.updatePlayerList(desiredName);
					
					// get bufImage and send
					BufferedImage img = canvas1.getBufImage();
					reply.setImg(canvas1.encodeString(img));
					reply.setRequestType(shape.OPEN);

					String strReply = Message.toJson(reply);
					try {
						dout.writeUTF(strReply);
					} catch (IOException e) {
						ActiveConnections.endPoints.remove(conn);
						ActiveConnections.SocketUsernameMap.remove(username);
						e.printStackTrace();
					}

					continue;

				}

				//System.out.println("IN LOOP: " + m);
				
				//check whether manager kick this client out
				if(userPanel.isKickOut && username.equals(userPanel.removedUser)) {
					Message deny = new Message();
					deny.setConnectionDenied(true);
					deny.setDeniedMessage("kick out");
					String strDeny = Message.toJson(deny);
					System.out.println(strDeny);
					try {
						dout.writeUTF(strDeny);
						conn.close();
						ActiveConnections.endPoints.remove(conn);
						ActiveConnections.SocketUsernameMap.remove(username);
						userPanel.isKickOut = false;

						return;
					}
					catch (IOException e2) {
						e2.printStackTrace();
					}
				}
				
				// draw locally
				canvas1.drawServerShape(m);
				
				
				//convert m to json string
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
	private void sendTakenUsernameMessage(DataOutputStream dout) {
		Message reply = new Message();
		reply.setConnectionDenied(true);
		reply.setDeniedMessage("username taken");
		String strReply = Message.toJson(reply);
		try {
			dout.writeUTF(strReply);
			conn.close();
			ActiveConnections.endPoints.remove(conn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
