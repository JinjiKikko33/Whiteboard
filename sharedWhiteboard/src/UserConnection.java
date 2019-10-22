import java.io.IOException;
import java.net.Socket;

public class UserConnection extends Socket {
	
	String username;
	
	public UserConnection(String host, int port) throws IOException {
		super(host, port);
		
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

}
