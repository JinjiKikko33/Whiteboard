

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JPanel;


class shape extends JPanel implements MouseListener, MouseMotionListener {

	private int shape = DRAW;
	private int startX = 0; 
	private int startY = 0;
	private int endX = 0; 
	private int endY = 0;
	private BufferedImage bufImage = null;
	private int WIDTH = 1000;
	private int HEIGHT = 600;		
	
	private ArrayList<point> eraser = new ArrayList<>();
	private HashSet<point> stop_eraser = new HashSet<>();
	private ArrayList<ArrayList<point>> undermouse = new ArrayList<>();
	
	private Color pen = Color.black;
	private Color bg = Color.white;
	//private ArrayList<Color> color_his = new ArrayList<>();
	private int eraserThickness = 3;
	private int penThickness = 3; 
	
	public static final int DRAW = 0;
	public static final int LINE = 1;
	public static final int RECTANGLE = 2;
	public static final int OVAL = 3;
	public static final int CIRCLE = 4;
	public static final int CLEAR = 5;
	
	

	public shape() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(bg); 
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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
		drawShape(g2);
	}

	private void drawShape(Graphics2D g2) {
		switch (shape) {
		case DRAW:
			
			for (int i=1; i<eraser.size(); i++) {
				point point1 = eraser.get(i-1);
				point point2 = eraser.get(i);
				if (stop_eraser.contains(point1)) {
					continue;
				}
				g2.setColor(bg);
				g2.setStroke(new BasicStroke(this.eraserThickness));
				g2.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
			}
			
			for (int j=0; j<undermouse.size();j++) {
				ArrayList<point> curr = undermouse.get(j);
				
				//Color c = color_his.get(j);
				for (int i=1; i<curr.size(); i++) {
					point point1 = curr.get(i-1);
					point point2 = curr.get(i);
					g2.setColor(this.pen);
					g2.setStroke(new BasicStroke(this.penThickness));
					g2.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
				}
			}
		

			break;
		case LINE:
			g2.setColor(pen);
			g2.drawLine(startX, startY, endX, endY);
			break;
		case OVAL:
			g2.setColor(pen);
			g2.drawOval(startX, startY, endX - startX, endY - startY);
			break;
		case RECTANGLE:
			g2.setColor(pen);
			g2.drawRect(startX, startY, endX - startX, endY - startY);
			break;
		case CIRCLE:
			g2.setColor(pen);
			g2.drawArc(startX, startY, endX - startX, (startY + endX - startX) - startY, 0, 360);
			break;
		case CLEAR:					
			
			for (int i=1; i<eraser.size(); i++) {
				point point1 = eraser.get(i-1);
				point point2 = eraser.get(i);
				if (stop_eraser.contains(point1)) {
					continue;
				}
				g2.setColor(bg);
				g2.setStroke(new BasicStroke(this.eraserThickness));
				g2.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
			}
			

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
		undermouse = new ArrayList<>();
		ArrayList<point> last = new ArrayList<>();
		undermouse.add(last);
		last.add(new point(e.getX(), e.getY()));
		//color_his.add(pen);		
	}

	public void mouseDragged(MouseEvent e) {
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
	}

	public void mouseReleased(MouseEvent e) {
		Graphics2D grafarea = bufImage.createGraphics();
		drawShape(grafarea);
		if(shape==5) {
			stop_eraser.add(new point(e.getX(),e.getY()));
			eraser = new ArrayList<>();
		}else {
			endX = e.getX(); 
			endY = e.getY();
		}
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

