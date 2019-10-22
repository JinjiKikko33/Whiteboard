

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.google.gson.Gson;


class shape extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	private int shape = DRAW;
	private int startX = 0;
	private int startY = 0;
	private int endX = 0;
	private int endY = 0;
	private BufferedImage bufImage = null;
	private int WIDTH = 1000;
	private int HEIGHT = 600;
	private File attachedFile = null;

	private boolean clientMode = false;
	private DataOutputStream clientOutputStream;

	private ArrayList<point> eraser = new ArrayList<>();
	private HashSet<point> stop_eraser = new HashSet<>();
	private ArrayList<ArrayList<point>> undermouse = new ArrayList<>();

	private Color pen = Color.black;
	private Color bg = Color.white;
	//private ArrayList<Color> color_his = new ArrayList<>();
	private int eraserThickness = 3;
	private int penThickness = 3;
	private StringBuffer text = new StringBuffer();
	private boolean refresh = false;
	private Message toSend;
	//private boolean textDisplay = false;
	//private boolean start = true;

	private static Font sanSerifFont = new Font("SanSerif", Font.PLAIN, 22);

	Gson gson = new Gson();
	public static final int DRAW = 0;
	public static final int LINE = 1;
	public static final int RECTANGLE = 2;
	public static final int OVAL = 3;
	public static final int CIRCLE = 4;
	public static final int CLEAR = 5;
	public static final int TEXT = 6;
	public static final int CLEARCANVAS = 8;
	public static final int OPEN = 9;



	public shape() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(bg);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public void setPenColor(Color c) {
		this.pen = c;
	}

	public void setEraserThickness(int thickness) {
		if (thickness != -1) {
			this.eraserThickness = thickness;
		}
	}

	public void setPenThickness(int thickness) {
		if (thickness != -1) {
			this.penThickness = thickness;
		}
	}


	public int getEraserThickness() {
		return this.eraserThickness;
	}

	public int getPenThickness() {
		return this.penThickness;
	}

	public void refresh() {
		this.refresh = true;
		bufImage = null;
		shape = -1;
		this.repaint();
	}

	public void paint(Graphics g)

	{

		Graphics2D g2 = (Graphics2D) g;
		if (bufImage == null) {
			int width = this.getWidth();
			int height = this.getHeight();
			bufImage = (BufferedImage) this.createImage(width, height);
			Graphics2D gc = bufImage.createGraphics();
			gc.setColor(bg);
			gc.fillRect(0, 0, width, height);


		}
		g2.drawImage(bufImage, null, 0, 0);

		if (!this.refresh) {
			drawShape(g2);
		}
		this.refresh = false;

	}

	public void drawServerShape(Message m) {

		if (bufImage == null) {
			int width = this.getWidth();
			int height = this.getHeight();
			bufImage = (BufferedImage) this.createImage(width, height);
			Graphics2D gc = bufImage.createGraphics();
			gc.setColor(bg);
			gc.fillRect(0, 0, width, height);

		}

		Graphics2D g2 = bufImage.createGraphics();
		int type = m.getRequestType();
		//System.out.println("FROM CLIENT: " + m);
		switch (type) {
		case OPEN:
			this.refresh();
			BufferedImage img = decodeString(m.getImg());
			bufImage = img;
			shape = -1;
			//this.repaint();
			break;
		case DRAW:
			g2.setColor(new Color(m.getColor(), false));
			g2.setStroke(new BasicStroke(m.getPenThickness()));
			int[][] xy = m.getPointsXY();

			try {
			g2.drawLine(xy[0][0], xy[0][1], xy[1][0], xy[1][1]);
			} catch (NullPointerException e) {
				break;
			}

			break;
		case LINE:
			g2.setColor(new Color(m.getColor(), false));
			g2.drawLine(m.getStartX(), m.getStartY(), m.getEndX(), m.getEndY());

			break;
		case OVAL:
			g2.setColor(new Color(m.getColor(), false));
			g2.drawOval(m.getStartX(), m.getStartY(), m.getEndX() - m.getStartX(), m.getEndY() - m.getStartY());
			break;
		case RECTANGLE:
			g2.setColor(new Color(m.getColor(), false));
			g2.drawRect(m.getStartX(), m.getStartY(),  m.getEndX() - m.getStartX(), m.getEndY() - m.getStartY());
			break;

		case CLEAR:
			g2.setColor(bg);
			g2.setStroke(new BasicStroke(m.getEraserThickness()));
			int[][] xy2 = m.getPointsXY();
			try {
				g2.drawLine(xy2[0][0], xy2[0][1], xy2[1][0], xy2[1][1]);
			} catch (NullPointerException e) {
				break;
			}

			break;

		case CLEARCANVAS:
			// clear executes same code on all hosts, no need to gather data from message
			this.refresh();
			break;

		case CIRCLE:
			g2.setColor(new Color(m.getColor(), false));
			g2.drawArc(m.getStartX(), m.getStartY(), m.getEndX() - m.getStartX(),
					(m.getStartY() + m.getEndX() - m.getStartX()) - m.getStartY(), 0, 360);
			break;
		case TEXT:
			g2.setFont(this.sanSerifFont);
			g2.setColor(new Color(m.getColor(), false));
			g2.drawString(m.getMessageText(), m.getStartX(), m.getStartY());
			break;
		default:
			break;
		}
		this.repaint();


	}

	private void drawShape(Graphics2D g2) {
		Message m = new Message();
		boolean batchMessage = false;

		switch (shape) {

		case DRAW:
			batchMessage = true;
			m.setRequestType(DRAW);

			/*m.setColor(bg.getRGB());
			m.setPenThickness(this.eraserThickness);

			for (int i=1; i<eraser.size(); i++) {
				point point1 = eraser.get(i-1);
				point point2 = eraser.get(i);
				if (stop_eraser.contains(point1)) {
					continue;
				}
				g2.setColor(bg);
				g2.setStroke(new BasicStroke(this.eraserThickness));
				g2.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());

				m.setPointsXY(point1.getArray(), point2.getArray());
				setMessage(m);
				sendMessage();


			}*/

			m.setPenThickness(this.penThickness);
			m.setColor(pen.getRGB());

			for (int j=0; j<undermouse.size();j++) {
				ArrayList<point> curr = undermouse.get(j);

				for (int i=1; i<curr.size(); i++) {
					point point1 = curr.get(i-1);
					point point2 = curr.get(i);
					g2.setColor(this.pen);
					g2.setStroke(new BasicStroke(this.penThickness));
					g2.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());

					m.setPointsXY(point1.getArray(), point2.getArray());
					setMessage(m);
					sendMessage();

				}
			}

			break;

		case CLEAR:
			batchMessage = true;
			m.setRequestType(CLEAR);

			for (int i = 1; i < eraser.size(); i++) {
				point point1 = eraser.get(i - 1);
				point point2 = eraser.get(i);

				if (stop_eraser.contains(point1)) {
					continue;
				}
				g2.setColor(bg);
				g2.setStroke(new BasicStroke(this.eraserThickness));
				g2.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());

				m.setPointsXY(point1.getArray(), point2.getArray());
				m.setEraserThickness(this.eraserThickness);
				setMessage(m);
				sendMessage();


			}
			break;
		case LINE:
			g2.setColor(pen);
			g2.drawLine(startX, startY, endX, endY);

			m.setColor(pen.getRGB());
			m.setRequestType(LINE);
			m.setStartAndEndXY(startX, startY, endX, endY);
			break;

		case OVAL:
			g2.setColor(pen);
			g2.drawOval(startX, startY, endX - startX, endY - startY);

			m.setColor(pen.getRGB());
			m.setRequestType(OVAL);
			m.setStartAndEndXY(startX, startY, endX, endY);
			break;

		case RECTANGLE:
			g2.setColor(pen);
			g2.drawRect(startX, startY, endX - startX, endY - startY);

			m.setColor(pen.getRGB());
			m.setRequestType(RECTANGLE);
			m.setStartAndEndXY(startX, startY, endX, endY);
			break;

		case CIRCLE:
			g2.setColor(pen);
			g2.drawArc(startX, startY, endX - startX, (startY + endX - startX) - startY, 0, 360);

			m.setColor(pen.getRGB());
			m.setRequestType(CIRCLE);
			m.setStartAndEndXY(startX, startY, endX, endY);
			break;

		case TEXT:
			g2.setFont(this.sanSerifFont);
			g2.setColor(this.pen);
			g2.drawString(this.text.toString(), startX, startY);

			m.setRequestType(TEXT);
			m.setColor(pen.getRGB());
			m.setStartX(startX);
			m.setStartY(startY);
			m.setMessageText(text.toString());

			break;
		default:
			break;
		}
		if (!batchMessage) {
			toSend = m;

		}

	}

	public void mousePressed(MouseEvent e) {

		if (shape == 6) {
			this.requestFocus();
			startX = e.getX();
			startY = e.getY();
			this.text = new StringBuffer();

		}
		else {
			startX = e.getX();
			startY = e.getY();
			endX = startX;
			endY = startY;
			undermouse = new ArrayList<>();
			ArrayList<point> last = new ArrayList<>();
			undermouse.add(last);
			last.add(new point(e.getX(), e.getY()));
			//color_his.add(pen);
		}

	}



	public void mouseDragged(MouseEvent e) {

		if (shape != 6) {
			if (shape==0) {
					ArrayList<point> last = undermouse.get(undermouse.size()-1);
					last.add(new point(e.getX(), e.getY()));

			} if (shape==5) {
				eraser.add(new point(e.getX(), e.getY()));
			}else {
				endX = e.getX();
				endY = e.getY();

			}
		this.repaint();
		//sendMessage();

		}
	}

	public void mouseReleased(MouseEvent e) {
		if (shape != 6) {
			Graphics2D grafarea = bufImage.createGraphics();
			drawShape(grafarea);

			if(shape ==5) {
				stop_eraser.add(new point(e.getX(),e.getY()));
				eraser = new ArrayList<>();
			}else {
				endX = e.getX();
				endY = e.getY();

			}
		if (shape != DRAW) {
			this.repaint();
			sendMessage();

		} else {
			undermouse = new ArrayList<>();
		}

	}

	}

	public void setMessage (Message m) {
		toSend = m;
	}


	public void sendMessage() {
		String json = gson.toJson(toSend);
		if(clientMode) {
			try {
				clientOutputStream.writeUTF(json);
			} catch (IOException e) {
				System.err.println("Error: Cannot connect to server. Closing program...");
				clientMode = false;
				System.exit(1);
			}
		} else {
			// really messy :(

			for (Socket s : ActiveConnections.endPoints) {
				DataOutputStream dout = null;
				try {
					dout = new DataOutputStream(s.getOutputStream());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					dout.writeUTF(json);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Error: Could not write update to client " + s.getInetAddress().toString() + " disconnecting...");
					ActiveConnections.endPoints.remove(s);
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					continue;
				}
			}
		}
	}


	public void addOutputStream(DataOutputStream dout) {
		this.clientMode = true;
		this.clientOutputStream = dout;

	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}


	@Override
	public void keyTyped(KeyEvent e) {
		if (shape == 6) {
			this.text.append(e.getKeyChar());

			Graphics2D grafarea = bufImage.createGraphics();
			drawShape(grafarea);

			this.repaint();// need this
			sendMessage();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}


	public void clientOpen(byte[] img, int height, int width) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(img);

		this.refresh();
		bufImage = ImageIO.read(bis);
	}

	public void open() {
		byte[] imgBytes=null;
		boolean success = false;
		this.refresh();

		try {
			bufImage = (BufferedImage) ImageIO.read(attachedFile);
			success = true;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		shape = -1;
		this.repaint();

		//get the current canvas and send to clients if successful
		if (success) {
			Message m = new Message();
			m.setRequestType(OPEN);
			m.setImg(encodeString(bufImage));
			toSend = m;
			sendMessage();
		}

	}

	public void save() {
		try {
			ImageIO.write(bufImage, "png", attachedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFile(File f) {
		this.attachedFile = f;
	}



	public String encodeString(BufferedImage img) {
		String imgString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(img, "png",bos);
			byte[] imgBytes = bos.toByteArray();
			imgString = Base64.getEncoder().encodeToString(imgBytes);
			bos.close();
			return imgString;
		} catch (IOException e) {
			return "";
		}

	}


	public BufferedImage decodeString(String buffer) {
		BufferedImage image = null;
		byte[] imgByte;
		try {
			imgByte = Base64.getDecoder().decode(buffer);
			ByteArrayInputStream bis = new ByteArrayInputStream(imgByte);
			image = ImageIO.read(bis);
			bis.close();
			return image;

		} catch (IOException e) {
			return null;
		}
	}

	public BufferedImage getBufImage() {
		return this.bufImage;
	}


}
