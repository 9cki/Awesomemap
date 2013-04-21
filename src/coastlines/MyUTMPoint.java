package coastlines;

public class MyUTMPoint {
	private double xcoord;
	private double ycoord;
	
	/*
	 * Represents a UTM point
	 * @param x The x-coordinate of the UTM point
	 * @param y The y-coordinate of the UTM point
	 */
	public MyUTMPoint(double x, double y) {
		xcoord = x;
		ycoord = y;
	}
	
	/*
	 * @return Returns the x-coordinate of the UTM point
	 */
	public double getX() {
		return xcoord;
	}
	
	/*
	 * @return Returns the y-coordinate of the UTM point
	 */
	public double getY() {
		return ycoord;
	}
}
