package graph;

import java.util.HashSet;

public class QuadTree<Double extends Comparable> {
	private MyNode root; //The root of the quadtree
	private HashSet<MyEdge> edges = new HashSet<MyEdge>(); //Stores all the edges from and to the nodes in the given 2D interval

	public void insert(MyNode node) {
		root = insert(root, node);
	}
	
	//Inserts a node to the quadtree in the correct corner (SW, NW, NE or SE)
	private MyNode insert(MyNode root, MyNode node) {
		if(root == null) return node;
		else if(less(node.getX(), root.getX()) && less(node.getY(), root.getY())) root.SW = insert(root.SW, node);
		else if(less(node.getX(), root.getX()) && !less(node.getY(), root.getY())) root.NW = insert(root.NW, node);
		else if(!less(node.getX(), root.getX()) && !less(node.getY(), root.getY())) root.NE = insert(root.NE, node);
		else if(!less(node.getX(), root.getX()) && less(node.getY(), root.getY())) root.SE = insert(root.SE, node);
		return root;
	}

	//is x1 less than x2?
	private boolean less(double x1, double x2) {
		if(x1 < x2)
			return true;
		else
			return false;
	}

	public void query2D(MyInterval2D<Double> rect) {
		query2D(root, rect);
	}
	
	//Finds all the nodes in a given 2D interval and adds them to the nodeArrayList
	public void query2D(MyNode h, MyInterval2D<Double> rect) {
		if(h == null) return;
		double xmin = rect.intervalX.low;
		double ymin = rect.intervalY.low;
		double xmax = rect.intervalX.high;
		double ymax = rect.intervalY.high;
		if(rect.contains(h.getX(), h.getY())) edges.addAll(h.getEdges());
		if ( less(xmin, h.getX()) &&  less(ymin, h.getY())) query2D(h.SW, rect);
		if ( less(xmin, h.getX()) && !less(ymax, h.getY())) query2D(h.NW, rect);
		if (!less(xmax, h.getX()) && !less(ymax, h.getY())) query2D(h.NE, rect);
		if (!less(xmax, h.getX()) &&  less(ymin, h.getY())) query2D(h.SE, rect);
	}
	
	//Returns the nodeArrayList
	public HashSet<MyEdge> getEdges() {
		return edges;
	}
	
	//Clears the nodeArrayList
	public void clearEdges() {
		edges.clear();
	}
}
