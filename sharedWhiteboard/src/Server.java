
import java.net.*;
import java.io.*;

public class Server implements Runnable{

	shape canvas1;
	PlayerList userPanel;
	int port;
	boolean active;
	ServerSocket server;
	String username;

	public Server(shape c, int port, PlayerList userPanel, String username) {
		this.canvas1 = c;
		this.port = port;
		this.userPanel = userPanel;
		this.active = true;
		this.username = username;
		
	}
	
	public void setActive(boolean status) {
		this.active = status;
	}
	
	
	public void close() {
		try {
			this.server.close();
		} catch (IOException e) {}
	}
	
	public void startServer() throws IOException{
		ServerSocket server = null;
		server = new ServerSocket(port);
		this.server = server;
		System.out.println("Running server on port: " + port);
	}
	

	@Override
	// Start the server
	public void run() {

		while (active) {
			Socket conn;
			try {
			conn = server.accept();
			//TODO: Accept or deny new user here and check username

			System.out.println("New socket opened to " + conn.getInetAddress() + " on port "
					+ port);


			} catch (IOException e) {
				System.out.println("ERROR: Cannot establish a connection");
				continue;

			}

			// Add the connection to the list of active connections
			// TODO: Delete if connection lost
			ActiveConnections.endPoints.add(conn);
			
			//TODO: Implement ConnectionRunnable to accept requests and draw on canvas
			new Thread(new ServerRunnable(conn, canvas1, userPanel, username)).start();


		}


	}



}
