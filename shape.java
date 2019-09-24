package sharedWhiteboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

class shape extends JPanel implements MouseListener, MouseMotionListener {

	private int shape = NONE;
	private int startX = 0; 
	private int startY = 0;
	private int endX = 0; 
	private int endY = 0;
	private BufferedImage bufImage = null;
	private int WIDTH = 1000;
	private int HEIGHT = 600;
	public static final int NONE = 0;
	public static final int LINE = 1;
	public static final int RECTANGLE = 2;
	public static final int OVAL = 3;
	public static final int CIRCLE = 4;

	public shape() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.white);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public void paint(Graphics g)

	{
		Graphics2D g2 = (Graphics2D) g; 
		if (bufImage == null) {
			int width = this.getWidth();
			int height = this.getHeight();
			bufImage = (BufferedImage) this.createImage(width, height);
			Graphics2D gc = bufImage.createGraphics();
			gc.setColor(Color.white);
			gc.fillRect(0, 0, width, height); 
		}

		g2.drawImage(bufImage, null, 0, 0); 
		drawShape(g2);
	}

	private void drawShape(Graphics2D g2) {
		switch (shape) {
		case NONE:
			break;
		case LINE:
			g2.drawLine(startX, startY, endX, endY);
			break;
		case OVAL:
			g2.drawOval(startX, startY, endX - startX, endY - startY);
			break;
		case RECTANGLE:
			g2.drawRect(startX, startY, endX - startX, endY - startY);
			break;
		case CIRCLE:
			g2.drawArc(startX, startY, endX - startX, (startY + endX - startX) - startY, 0, 360);
			break;

		default:
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		startX = e.getX(); 
		startY = e.getY(); 
		endX = startX;
		endY = startY;
	}

	public void mouseDragged(MouseEvent e) {
		endX = e.getX(); 
		endY = e.getY();
		this.repaint(); 
	}

	public void mouseReleased(MouseEvent e) {
		endX = e.getX(); 
		endY = e.getY();
		Graphics2D grafarea = bufImage.createGraphics();
		drawShape(grafarea);
		this.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

}

