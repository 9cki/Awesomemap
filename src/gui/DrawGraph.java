package gui;
import graph.Interval;
import graph.MyEdge;
import graph.MyInterval2D;
import graph.MyNode;
import graph.QuadTree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import util.GCThread;
import coastlines.CoastLines;

public class DrawGraph extends JComponent {
	private HashSet<MyEdge> edges;
	private QuadTree<Double> qt;
	private static DrawGraph instance;
	private double upperLeftX = 434168; //The upper left corner in UTM-coordinates
	private double upperLeftY = 6412239; //The upper left corner in UTM-coordinates
	public double width = 1.0; //The dimensions of the JFrame (overwritten in the constructor)
	public double height = 1.0; //The dimensions of the JFrame (overwritten in the constructor)
	private double utmWidth = 484790.0; //The width of entire map in UTM
	private double utmHeight = 379423.0; //The height of entire map in UTM
	private double preferredRatio = 1.27777032494; //484790.0/379423.0
	private double utmWidthRelation = utmWidth/1000; 
	private double utmHeightRelation = utmHeight/1000;
	private double x1, x2, y1, y2; //The two corners of the marked square when zooming (x1,y1) and (x2,y2)
	private double factor = 1.0; 
	private boolean dragging; //Is the mouse being dragged? (used for drawing the square)
	private double rectWidth, rectHeight; //The width and height of the marked square when zooming
	private double zoomLvl = 100;
	public static int garbageCollectorFlag = 0;
	private double pixelConstant = 150;
	private MyNode dirFromNode, dirToNode;
	private ImageIcon locationImg = createImageIcon("img/location.png");

	//Initializes the DrawGraph class
	private DrawGraph(QuadTree<Double> qt, int w, int h) {
		this.qt = qt;
		//Gets all the nodes in the given interval (All of Denmark)
		queryQT(new Interval<Double>(upperLeftX, upperLeftX+utmWidth), new Interval<Double>(upperLeftY-utmHeight, upperLeftY));
		edges = qt.getEdges();
		drawGraph(edges, w, h);
		addMouseListener(new MyMouseListener());
		addMouseMotionListener(new MyMouseAdapter());
		addMouseWheelListener(new MyMouseWheelListener());

	}

	public static DrawGraph getInstance(QuadTree<Double> qt, int w, int h) {
		if(instance != null) return instance;
		else {
			instance = new DrawGraph(qt, w, h);
			return instance;
		}
	}

	public static DrawGraph getInstance() {
		if(instance != null) return instance;
		else return null;
	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private void drawGraph(HashSet<MyEdge> e, double w, double h){
		edges = e;
		width = w;
		height = h;
		calculateFactor();
		double actualWidth = (utmWidthRelation*factor);
		double actualHeight = (utmHeightRelation*factor);
		setPreferredSize(new Dimension((int)actualWidth, (int)actualHeight));
	}

	//Finds all the nodes in the given interval
	private void queryQT(Interval<Double> xInterval, Interval<Double> yInterval) {
		MyInterval2D<Double> rect = new MyInterval2D<Double>(xInterval, yInterval);
		qt.clearEdges();
		qt.query2D(rect);
	}

	//Calculates the factor 
	public void calculateFactor() {
		factor = ((width-utmWidthRelation) >= (height-utmHeightRelation)) ? (height/utmHeightRelation) : (width/utmWidthRelation);
	}

	public void calculateZoomLvl() {
		zoomLvl = ((484790.0/utmWidth*100 >= 379423.0/utmHeight*100)) ? (484790.0/utmWidth*100) : (379423.0/utmHeight*100);

	}

	private double pixelToUTMConverter(double d) {
		return d * (1000/factor); //Calculates the UTM coordinates instead of pixel coordinates
	}

	/*
	 * Pan the map in the given direction
	 * @param int direction 1 = up, 2 = down, 3 = left, 4 = right
	 */
	public void pan(int direction) {
		GCThread.increaseGCFlag(5);
		switch(direction) {
		case 1: zoom(upperLeftX, (upperLeftY+(utmHeight*0.1)), utmWidth, utmHeight, false, 0, 0, 0, 0);
		break;
		case 2: zoom(upperLeftX, (upperLeftY-(utmHeight*0.1)), utmWidth, utmHeight, false, 0, 0, 0, 0);
		break;
		case 3: zoom((upperLeftX-(utmWidth*0.1)), upperLeftY, utmWidth, utmHeight, false, 0, 0, 0, 0);
		break;
		case 4: zoom((upperLeftX+(utmWidth*0.1)), upperLeftY, utmWidth, utmHeight, false, 0, 0, 0, 0);
		break;
		}
	}
	public void zoomOut(){
		if(utmWidth >= 364790 || utmHeight >= 269423) { //You can't zoom more out than this
			//If the upper left corner is not in the right position, reset the map
			if(upperLeftX != 434168 && upperLeftY != 6412239) { 
				resetMap(); 
			}
			return;
		}
		zoom(upperLeftX-(utmWidth*0.1), upperLeftY+(utmHeight*0.1),utmWidth*1.2,utmHeight*1.2, false, 0,0,0,0);
		//		zoom(upperLeftX-(pixelConstant*(2500/factor)), upperLeftY+(pixelConstant*(2500/factor)),utmWidth+(2*preferredRatio*pixelConstant*(2500/factor)),utmHeight+(2*pixelConstant*(2500/factor)), false, 0,0,0,0);
	}

	//Called when zooming with the button
	public void zoomIn() {
		zoom(0,0,0,0, true, pixelToUTMConverter((width-250)*0.1), pixelToUTMConverter((width-250)*0.9), pixelToUTMConverter(height*0.1), pixelToUTMConverter(height*0.9));
	}
	//Zooms in on the map
	private void zoom(double upperLeftX2, double upperLeftY2, double utmWidth2, double utmHeight2, boolean zoomIn,
			double x1utm2, double x2utm2, double y1utm2, double y2utm2) {

		if(zoomIn) {
			if(x1utm2 > x2utm2) { //If x1 > x2 then switch
				double temp = x1utm2;
				x1utm2 = x2utm2;
				x2utm2 = temp;
			}
			if(y1utm2 > y2utm2) { //If y1 > y1 then switch
				double temp = y1utm2;
				y1utm2 = y2utm2;
				y2utm2 = temp;
			}


			if(x2utm2-x1utm2 < 1500 || y2utm2-y1utm2 < 1500) { //You can't zoom more than this (1500m in either width or height)
				x1 = 0; y1 = 0; x2 = 0; y2 = 0;
				repaint();
				return;
			}

			utmWidth = x2utm2-x1utm2;
			utmHeight = y2utm2-y1utm2;
			upperLeftX += x1utm2;
			upperLeftY -= y1utm2;

		}
		else {

			upperLeftX = upperLeftX2;
			upperLeftY = upperLeftY2;
			utmWidth = utmWidth2;
			utmHeight = utmHeight2;
		}


		if(utmWidth/utmHeight < preferredRatio) {
			this.utmWidth = utmHeight * preferredRatio;
		}
		else if(utmWidth/utmHeight > preferredRatio) {
			this.utmHeight = utmWidth / preferredRatio;
		}
		utmWidthRelation = utmWidth/1000;
		utmHeightRelation = utmHeight/1000;

		calculateZoomLvl();
		calculateFactor();
		//A new query with the new interval is made and all the nodes in this interval is redrawn with the new factor
		queryQT(new Interval<Double>(upperLeftX, upperLeftX+utmWidth), new Interval<Double>(upperLeftY, upperLeftY-utmHeight));
		edges = qt.getEdges();
		drawGraph(edges, width, height);
		repaint();
	}

	//Resets the map to the initial state
	public void resetMap() {
		zoom(434168, 6412239, 484790.0, 379423.0, false, 0, 0, 0, 0);
		GCThread.increaseGCFlag(40);
	}

	//Draws all the edges
	public void paintComponent(Graphics2D g) {
		super.paintComponents(g);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.clearRect(0,0,getWidth(), getHeight());
		drawGraph(edges, (int)CreateGUI.returnInstance().getFrameWidth(), (int)CreateGUI.returnInstance().getFrameHeight());

		BasicStroke sizeTwo = new BasicStroke(2);
		BasicStroke sizeOnePointTwo = new BasicStroke(1.2f);
		double pixelFactor = 1000/factor;
		try {
			for(MyEdge e : edges) {
				g.setStroke(new BasicStroke(1));
				int roadType = e.getRoadType();

				if((roadType == 1 || roadType == 2 || roadType == 3 || roadType == 80 || roadType == 41 || 
						(roadType == 4 && zoomLvl >= 300) || (roadType == 5 && zoomLvl >= 900) || (roadType == 8 && zoomLvl >= 3500)
						||	(roadType != 8 && zoomLvl >= 2000)) || e.isOnPath()) { // filters different road types depending on the zoom level

					if(e.isOnPath()) {
						Color pathColor = new Color(255,218,69, 100);
						g.setColor(pathColor);
						g.setStroke(new BasicStroke(10));

						double fromX = ((e.getFromNode().getX() - upperLeftX) / pixelFactor);
						double fromY = ((upperLeftY - e.getFromNode().getY()) / pixelFactor);
						double toX = ((e.getToNode().getX() - upperLeftX) / pixelFactor);
						double toY = ((upperLeftY - e.getToNode().getY()) / pixelFactor);
						g.draw(new Line2D.Double(fromX, fromY, toX, toY));
					}

					g.setStroke(new BasicStroke(1));

					if(roadType == 1) {
						g.setColor(Color.RED);
						g.setStroke(sizeTwo);
					}
					else if(roadType == 3) {
						g.setColor(Color.BLUE);
						g.setStroke(sizeOnePointTwo);
					}
					else if(roadType == 8) {
						g.setColor(Color.GREEN);
					}
					else if(roadType == 80) {
						g.setColor(Color.MAGENTA);
					}
					else {
						g.setColor(Color.BLACK);
					}

					double fromX = ((e.getFromNode().getX() - upperLeftX) / pixelFactor);
					double fromY = ((upperLeftY - e.getFromNode().getY()) / pixelFactor);
					double toX = ((e.getToNode().getX() - upperLeftX) / pixelFactor);
					double toY = ((upperLeftY - e.getToNode().getY()) / pixelFactor);
					g.draw(new Line2D.Double(fromX, fromY, toX, toY));
				}

			}


			CoastLines.getInstance().paintComponent(g, upperLeftX, upperLeftY, pixelFactor);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		paintComponent(g2);
		if(dragging) {
			g2.setColor(Color.RED);
			if(rectWidth/rectHeight < preferredRatio) {
				rectWidth = rectHeight * preferredRatio;
			}
			else if(rectWidth/rectHeight > preferredRatio) {
				rectHeight = rectWidth / preferredRatio;
			}
			g2.draw(new Rectangle2D.Double(x2, y2, rectWidth, rectHeight));
		}
		if(dirFromNode != null) {
			int size = 32;
			Image locImg = locationImg.getImage();
			Image newImg = locImg.getScaledInstance(size, size, 0);
			locationImg = new ImageIcon(newImg);
			locationImg.paintIcon(this, g2, (int)((dirFromNode.getX() - upperLeftX) / (1000/factor))-size/2, (int)((upperLeftY - dirFromNode.getY()) / (1000/factor)-(size*0.875)));
		}
		if(dirToNode != null) {
			int size = 32;
			Image locImg = locationImg.getImage();
			Image newImg = locImg.getScaledInstance(size, size, 0);
			locationImg = new ImageIcon(newImg);
			locationImg.paintIcon(this, g2, (int)((dirToNode.getX() - upperLeftX) / (1000/factor))-size/2, (int)((upperLeftY - dirToNode.getY()) / (1000/factor)-(size*0.875)));
		}
		dragging = false;
	}

	private void doPop(MouseEvent e){
		PopUp menu = new PopUp(this);
		x1 = e.getX();
		y1 = e.getY();
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	public void findNearestNode(int type) {
		double x = upperLeftX + (x1 * (1000/factor)); 
		double y = upperLeftY - (y1 * (1000/factor));
		HashSet<MyEdge> nearestEdges = new HashSet<MyEdge>();
		int range = 100;
		for(MyEdge edge : edges) {
			if(((edge.getFromNode().getX() - range) < x && x < (edge.getFromNode().getX() + range) &&
					(edge.getFromNode().getY() - range) < y && y < (edge.getFromNode().getY() + range)) || 
					(edge.getToNode().getX() - range) < x && x < (edge.getToNode().getX() + range) &&
					(edge.getToNode().getY() - range) < y && y < (edge.getToNode().getY() + range)) {
				nearestEdges.add(edge);
			}
		}
		MyEdge nearest = null;
		for(MyEdge ne : nearestEdges) {
			if(nearest == null) { nearest = ne; }
			else if((Math.abs((x - ne.getFromNode().getX()) + (y - ne.getFromNode().getY())) <
					Math.abs((x - nearest.getFromNode().getX()) + (y - nearest.getFromNode().getY())))) {
				nearest = ne;
			}
		}
		if(nearest != null) {
			String roadname = nearest.getRoadName();
			switch(type) {
			case 1: System.out.println("Show directions from: " + roadname);
			dirFromNode = nearest.getFromNode();
			break;
			case 2: System.out.println("Show directions to: " + roadname);
			dirToNode = nearest.getFromNode();
			break;
			case 3: System.out.println("Nearest edge: " + roadname);
			break;				
			}
		}
	}

	private class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) { 
			if(e.getClickCount() == 2 && !e.isConsumed()) {
				e.consume();
				findNearestNode(3);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {	}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent e) { //The upper left corner of the marked square (pixel coordinates) 
			x1 = e.getX();
			y1 = e.getY();

			if (e.isPopupTrigger()){
				doPop(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) { //The lower right corner of the marked square (pixel coordinates)
			if(e.isShiftDown()) {
				x2 = e.getX();
				y2 = e.getY();
				zoom(0,0,0,0,true, pixelToUTMConverter(x1), pixelToUTMConverter(x2), pixelToUTMConverter(y1), pixelToUTMConverter(y2));
				GCThread.increaseGCFlag(10);
			}

			if (e.isPopupTrigger()){
				doPop(e);
			}
		}

	}

	private class MyMouseAdapter extends MouseAdapter {

		@Override
		public void mouseDragged(MouseEvent e) {	
			GCThread.increaseGCFlag(5);	
			if (SwingUtilities.isLeftMouseButton(e) && !e.isShiftDown()) {
				x2 = e.getX();
				y2 = e.getY();
				zoom(upperLeftX-((x2-x1)*(75/factor)), upperLeftY+((y2-y1)*(75/factor)), utmWidth, utmHeight, false, 0, 0, 0, 0);
			}
			if(e.isShiftDown()) {
				x2 = Math.min(e.getX(), x1);
				y2 = Math.min(e.getY(), y1);
				rectWidth = (int) Math.abs(x1 - e.getX());
				rectHeight = (int) Math.abs(y1 - e.getY());
				dragging = true;
				repaint();
			}
		}
	}

	private class MyMouseWheelListener implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notches = e.getWheelRotation();
			//A pixel constant used for setting the interval when only given one point (pixel coordinates of the mouse when scrolling)
			if(notches < 0) { //Zoom in
				if(utmWidth < 1500 || utmHeight < 1500) { //You can't zoom more than this (1500m in either width or height)
					return;
				}
				x1 = e.getX()-pixelConstant*preferredRatio;
				y1 = e.getY()+pixelConstant;
				x2 = e.getX()+pixelConstant*preferredRatio;
				y2 = e.getY()-pixelConstant;
				zoom(0,0,0,0, true, pixelToUTMConverter(x1), pixelToUTMConverter(x2), pixelToUTMConverter(y1), pixelToUTMConverter(y2));
			}
			else { //Zoom out
				zoomOut();
			}	
			GCThread.increaseGCFlag(10);
		}
	}
}