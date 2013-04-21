package graph;

public class MyInterval2D<Double extends Comparable> { 
    public final Interval<Double> intervalX;   // x-interval
    public final Interval<Double> intervalY;   // y-interval
   
    public MyInterval2D(Interval<Double> i1, Interval<Double> i2) {
    	intervalX = i1;
    	intervalY = i2;
    }
    

    // does this 2D interval a intersect b?
    public boolean intersects(MyInterval2D<Double> b) {
        if (intervalX.intersects(b.intervalX)) return true;
        if (intervalY.intersects(b.intervalY)) return true;
        return false;
    }

    // does this 2D interval contain (x, y)?
    public boolean contains(double x, double y) {
        return intervalX.contains(x) && intervalY.contains(y);
    }

    // return string representation
    public String toString() {
        return intervalX + " x " + intervalY;
    }

}