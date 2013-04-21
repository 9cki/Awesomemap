package graph;

import java.util.ArrayList;

public class MyNode {
	private int kdv; //The kdv number of the node
	private double x; //The x coordinate of the node (UTM)
	private double y; //The y coordinate of the node (UTM)
	public MyNode NW, NE, SW, SE; //The four subtrees of the quadtree
	private ArrayList<MyEdge> edges = new ArrayList<MyEdge>(); //All edges from this node are stored here
	
	//Initializes the node
	public MyNode(int kdv, double xcoord, double ycoord) {
		this.kdv = kdv;
		x = xcoord;
		y = ycoord;
	}
	//Adds an edge to the ArrayList of type MyEdge
	public void addEdge(MyEdge e) {
		edges.add(e);
	}
	//Returns the ArrayList of type MyEdge
	public ArrayList<MyEdge> getEdges() {
		return edges;
	}
	//Returns the number of edges in the ArrayList
	public int getEdgeCount() {
		return edges.size();
	}
	//Getters for the private fields
	public int getKDV() {
		return kdv;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
