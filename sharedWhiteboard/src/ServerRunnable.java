import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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


				//check whether manager kick this client out
				boolean isKickout = false;
				if(isKickout) {
					try {
						m.setConnectionDenied(false);
						m.setDeniedMessage("kick out");
						dout.writeUTF(m.toString());
						conn.close();
						ActiveConnections.endPoints.remove(conn);
					}
					catch (IOException e2) {
						e2.printStackTrace();
					}
					break;
				}

				// check username
				if (m.getRequestType() == 10) {
					Message reply = new Message();
					String desiredName = m.getUsername();


					for (String s : ActiveConnections.SocketUsernameMap.keySet()) {
						if (s.equals(desiredName)){
							reply.setConnectionDenied(true);
							reply.setDeniedMessage("username taken");
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
					}
					// username free
					ActiveConnections.SocketUsernameMap.put(desiredName, conn);
					reply.setConnectionDenied(false);

					// get bufImage and send
					BufferedImage img = canvas1.getBufImage();
					reply.setImg(canvas1.encodeString(img));
					reply.setRequestType(shape.OPEN);

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
