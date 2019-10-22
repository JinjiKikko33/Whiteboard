import java.awt.image.BufferedImage;

import com.google.gson.Gson;

public class Message {

	private int startX;
	private int startY;
	private int endX;
	private int endY;

// Corresponds to Shape.SHAPE (e.g. draw is 0)
// REQUEST TYPES:

// Draw 0
// Line 1
// RECT 2
// Oval 3
// Circle 4
// Clear (eraser) 5
// Text 6
// Chat 7
// Clear canvas 8
// Open image 9


	private int requestType;
	private int penThickness;
	private int eraserThickness;
	private String username;
	private boolean refresh;
	private String messageText;
	private String img;
	private int imgHeight;
	private int imgWidth;

	// x and y coordinate of point
	private int[][] pointsXY;
	
	// color components
	// can obtain with Color.getRGB()
	// convert from int to Color with constructor
	// Color c = new Color(colorInt, false)
	private int color;
	
	//set to False if server refuse connection; set to True otherwise
	private boolean connection;
	//message when server refuse connection
	private String refuseMsg;
	
	// no constructors since we'd need to make lots of them depending
	// on the instruction
	// instead, we just set the relevant variables


	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static String toJson(Message msg) {
		Gson gson = new Gson();
		return gson.toJson(msg);
	}


	public static Message makeMessageFromJson(String s) {
		Gson gson = new Gson();
		return gson.fromJson(s, Message.class);
	}


	public static void main(String[] args) {
		byte[] b = {6,5,4,3};
		
		Message m = new Message();
		m.setRequestType(0);
		m.setPenThickness(30);
		m.setColor(14443);

		// Convert to Json
		System.out.println(Message.toJson(m));
		System.out.println(m);


		// Convert back
		String q = Message.toJson(m);
		Message m1 = Message.makeMessageFromJson(q);

	}

	public String getImg(){
		return this.img;
	}

	public void setImg(String seq){
		this.img = seq;
	}

	public int getHeight() {
		return this.imgHeight;
	}
	
	public int getWidth() {
		return this.imgWidth;
	}


	public int getColor() {
		return color;
	}


	public void setColor(int color) {
		this.color = color;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStartX() {
		return startX;
	}
	public void setStartX(int startX) {
		this.startX = startX;
	}
	public int getStartY() {
		return startY;
	}
	public void setStartY(int startY) {
		this.startY = startY;
	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	public int getPenThickness() {
		return penThickness;
	}
	public void setPenThickness(int penThickness) {
		this.penThickness = penThickness;
	}
	public int getEraserThickness() {
		return eraserThickness;
	}
	public void setEraserThickness(int eraserThickness) {
		this.eraserThickness = eraserThickness;
	}
	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	
	public void setStartAndEndXY(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	public int getEndX() {
		return endX;
	}


	public void setEndX(int endX) {
		this.endX = endX;
	}


	public int getEndY() {
		return endY;
	}


	public void setEndY(int endY) {
		this.endY = endY;
	}

	public int[][] getPointsXY() {
		return pointsXY;
	}

	public void setPointsXY(int[] pointX, int[] pointY) {
		int[][] xy = new int[2][2];
		xy[0] = pointX;
		xy[1] = pointY;
		this.pointsXY = xy;
	}
	
	//set and retrieve connection status
	public boolean getConnection() {
		return connection;
	}

	public void setConnection(boolean status) {
		connection = status;
	}
	
	//get and set refuse message for socket connection
	public String getRefuseMsg() {
		return refuseMsg;
	}
	
	public void setRefuseMsg(String msg) {
		refuseMsg = msg;
	}
	
}