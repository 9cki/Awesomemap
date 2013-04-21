package graph;

public class MyEdge {
	private MyNode fromNode; //From node of the edge
	private MyNode toNode; //To node of the edge
	private double length; //Length of the edge
	private int roadType; //Road type of the edge
	private String roadName; //Road name of the edge
	private int speedLimit; //Speed limit of the edge
	
	//Initializes the edge
	public MyEdge(MyNode f, MyNode t, double l, int rt, String rn, int sl) {
		fromNode = f;
		toNode = t;
		length = l;
		roadType = rt;
		roadName = rn;
		speedLimit = sl;
	}
	
	//Getters for all the private fields
	public MyNode getFromNode() {
		return fromNode;
	}
	public MyNode getToNode() {
		return toNode;
	}
	public double getLength() {
		return length;
	}
	public int getRoadType() {
		return roadType;
	}
	public String getRoadName() {
		return roadName;
	}
	public int getSpeedLimit() {
		return speedLimit;
	}
	
}
