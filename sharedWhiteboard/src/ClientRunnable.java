import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// We listen for updates from the host owner in a separate thread

public class ClientRunnable implements Runnable {
	Socket conn;
	shape canvas1;
	DataInputStream din;
	
	public ClientRunnable(Socket conn, shape canvas1, String username) {
		this.conn = conn;
		this.canvas1 = canvas1;
		try {
			din = new DataInputStream(conn.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			//TODO: Implement server error
			System.err.println("ERROR: Could not establish connection with server");
			System.exit(1);
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
			return false;
		} else {
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
				// TODO Auto-generated catch block
				System.err.println("ERROR: Could not get request from server");
				e.printStackTrace();
				// attempt reconnection?
				
				//TODO: Implement server error
				System.exit(1);
			}
			

			Message m = Message.makeMessageFromJson(request);
			// draw locally
			//System.out.println("REQ:" + m.getRequestType());
			canvas1.drawServerShape(m);	

			
			
		}
		
		
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
