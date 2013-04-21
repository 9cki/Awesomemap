import util.GCThread;

import graph.MyGraph;
import graph.MyNode;
import graph.QuadTree;
import gui.CreateGUI;

public class Driver {
	public static boolean loading = true;

	public static void main(String[] args) {
		
		LoadingThread lt = new LoadingThread();
		lt.setPriority(Thread.MIN_PRIORITY);
		lt.start();
		GCThread gct = new GCThread();
		gct.setPriority(Thread.MIN_PRIORITY);
		gct.start();
		try {
			MyGraph mg = new MyGraph();
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
