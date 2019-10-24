
import java.net.*;
import java.io.*;

public class Server implements Runnable {

	shape canvas1;
	PlayerList userPanel;
	int port;
	boolean active;
	ServerSocket server;
	//try make it static
	String username;
	ChatBox chatWindow;

	public Server(shape c, int port, PlayerList userPanel, String username, ChatBox chatWindow) {
		this.canvas1 = c;
		canvas1.clientMode = false;

		this.port = port;
		this.userPanel = userPanel;
		this.active = true;
		this.username = username;
		this.chatWindow = chatWindow;

	}

	public void setActive(boolean status) {
		this.active = status;
	}

	public void close() {
		try {
			this.server.close();
		} catch (IOException e) {
		}
	}

	public void startServer() throws IOException {
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
				System.out.println("New socket opened to " + conn.getInetAddress() + " on port " + port);

			} catch (IOException e) {
				System.out.println("ERROR: Cannot establish a connection");
				continue;

			}

			// Add the connection to the list of active connections
			ActiveConnections.endPoints.add(conn);
			new Thread(new ServerRunnable(conn, canvas1, userPanel, username, chatWindow)).start();

		}

	}

}
