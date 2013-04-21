package graph;

public class Interval<Double extends Comparable> { 
    public final double low;      // left endpoint
    public final double high;     // right endpoint
   
    public Interval(double low, double high) {
        if (less(high, low)) { //if low is higher than high then switch
        	double temp = low;
        	low = high;
        	high = temp;
        }
        this.low  = low;
        this.high = high;
    }

    // is x between low and high
    public boolean contains(double x) {
        return !less(x, low) && !less(high, x);
    }

    // does this interval a intersect interval b?
    public boolean intersects(Interval<Double> b) {
        Interval<Double> a  = this;
        if (less(a.high, b.low)) return false;
        if (less(b.high, a.low)) return false;
        return true;
    }

    // does this interval a equal interval b?
    public boolean equals(Interval<Double> b) {
        Interval<Double> a  = this;
        return a.low == b.low && a.high == b.high;
    }


    // comparison helper functions
    private boolean less(double x, double y) {
        if(x < y)
        	return true;
        else
        	return false;
    }

    // return string representation
    public String toString() {
        return "[" + low + ", " + high + "]";
    }
}