package coastlines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class CoastLines extends Thread {

	private static MyUTMPoint[][] utmPoints;
	public static CoastLines instance = null;

	public void run() {
		MyCoastLines mcl = new MyCoastLines();
		utmPoints = mcl.getUTMPoints();
	}

	public static CoastLines getInstance() {
		if(instance == null) return new CoastLines();
		else return instance;
	}
	
	public void paintComponent(Graphics2D g, double upperLeftX, double upperLeftY, double pixelFactor) {
		//Draw coastLines
		g.setColor(Color.DARK_GRAY);
		g.setStroke(new BasicStroke(0.8f));
		for(MyUTMPoint[] utmPointArray : utmPoints) {
			for(int i = 0; i < utmPointArray.length; i++) {
				if(utmPointArray[i] != null && utmPointArray[i+1] != null) {
					g.draw(new Line2D.Double(((utmPointArray[i].getX()-upperLeftX)/pixelFactor), 
							((upperLeftY-utmPointArray[i].getY())/pixelFactor), ((utmPointArray[i+1].getX()-upperLeftX)/pixelFactor), 
							((upperLeftY-utmPointArray[i+1].getY())/pixelFactor)));
				}
			}
		}
	}
}