import coastlines.CoastLines;
import util.GCThread;

import graph.MyGraph;
import graph.MyNode;
import graph.QuadTree;
import gui.CreateGUI;

public class Driver {
	public static boolean loading = true;

	public static void main(String[] args) {
		CoastLines cl = CoastLines.getInstance();
		cl.start();
		LoadingThread lt = new LoadingThread();
		lt.setPriority(Thread.MIN_PRIORITY);
		lt.start();
		GCThread gct = new GCThread();
		gct.setPriority(Thread.MIN_PRIORITY);
		gct.start();

		MyGraph mg = MyGraph.getInstance();
		MyNode[] nodes = mg.getNodeArray();
		System.out.println("Nodes done");

		QuadTree<Double> qt = new QuadTree<Double>();
		for(MyNode n : nodes) {
			qt.insert(n);
		}
		System.out.println("Nodes inserted");

		loading = false;
		lt = null;
		CreateGUI.getInstance(qt);
		System.gc();
		System.out.println("Garbage collection done");
	}
}
