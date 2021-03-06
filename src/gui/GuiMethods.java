package gui;

import java.util.HashMap;
import java.util.Stack;

import graph.MyEdge;
import graph.MyGraph;
import graph.MyNode;
import graph.QuadTree;
import util.DijkstraSP;

public class GuiMethods {
	private DrawGraph dg;
	private static GuiMethods instance;
	private static DijkstraSP dsp;
	private static QuadTree qt;
	private static MyGraph g;
	private static Stack<MyEdge> edgesOnPath = new Stack<MyEdge>();
	public static int nodeFromKDV = -1;
	public static int nodeToKDV = -1;

	public GuiMethods() {
		setDrawGraphInstance();
		this.g = MyGraph.getInstance();
		dsp = new DijkstraSP(g);
		
	}

	public void zoomOut() {
		dg.zoomOut();
	}

	public void setDrawGraphInstance() {
		dg = DrawGraph.getInstance();
	}

	public static GuiMethods getInstance() {
		if(instance != null) return instance;
		else return new GuiMethods();
	}

	public static DijkstraSP getDijekstraInstance() {
		return dsp;
	}


	public void findNodeKDV(String edgeRoadName, boolean from) {
		HashMap<String, MyEdge> possibleEdges = new HashMap<String, MyEdge>();
		for(MyNode n : g.getNodeArray())
			for(MyEdge e : n.getEdges()) {
				if(e.getRoadName().equals(edgeRoadName)) {
					possibleEdges.put(e.getRoadName() + " " + e.getPostalCode(), e);
				}
			}
		CreateGUI.returnInstance().showPossibleEdges(possibleEdges, from);
	}

	public void createPath(String textFromTextField, String textFromTextField2) {
		setDrawGraphInstance();
		findNodeKDV(textFromTextField, true);
		findNodeKDV(textFromTextField2, false);
	}
	
	public void searchForPath() {

		if(nodeFromKDV == -1 || nodeToKDV == -1) {
			System.out.println("edges not found");
			return;
		}

		for(MyEdge e : edgesOnPath) {
			e.removeFromPath();
		}
		edgesOnPath.clear();
		
		System.out.println("kdv from: " + nodeFromKDV + " kdv to: " + nodeToKDV);

		// print shortest path
		Iterable<MyEdge> path = dsp.calculateShortestPath(nodeFromKDV, nodeToKDV);
		if(path != null) {
			for (MyEdge e : path) {
				edgesOnPath.push(e);
				e.putOnPath();
			}
			dg.repaint();
		}
		else
			System.out.println("No path");
		System.out.println();
		
		nodeFromKDV = -1;
		nodeToKDV = -1;
	}
}
