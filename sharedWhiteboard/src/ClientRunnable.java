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
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	
	public ClientRunnable(Socket conn, shape canvas1) {
		this.conn = conn;
		this.canvas1 = canvas1;
	}
	
	@Override
	public void run() {
		DataInputStream din = null;
		try {
			din = new DataInputStream(conn.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			//TODO: Implement server error
			System.err.println("ERROR: Could not establish connection with server");
			System.exit(1);
		}
		
		
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
			//check whether server refuse connection
			if (!m.getConnection()) {
				System.out.println(m.getRefuseMsg());
				break;
			}
			
			// draw locally
			//System.out.println("REQ:" + m.getRequestType());
			canvas1.drawServerShape(m);	

		}
		
		
	}

}
