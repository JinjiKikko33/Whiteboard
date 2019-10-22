
import java.net.*;
import java.io.*;

public class Server implements Runnable{

	shape canvas1;
	int port;

	public Server(shape c, int port) {
		this.canvas1 = c;
		this.port = port;
	}

	@Override
	// Start the server
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("Running server on port: " + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("ERROR: Could not start server");
			e.printStackTrace();
		}

		while (true) {
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
			new Thread(new ServerRunnable(conn, canvas1)).start();


		}


	}



}
