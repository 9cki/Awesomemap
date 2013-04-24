import java.util.Iterator;

import graph.MyEdge;
import graph.MyGraph;
import graph.MyNode;
import graph.QuadTree;
import gui.CreateGUI;
import gui.DijkstraSP;
import util.GCThread;
import coastlines.CoastLines;

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
		try {
			MyGraph mg = new MyGraph();
			System.out.println("Nodes done");

			QuadTree<Double> qt = new QuadTree<Double>();
			for(MyNode n : mg.getNodeArray()) {
				qt.insert(n);
			}
			System.out.println("Nodes inserted");

			DijkstraSP sp = new DijkstraSP(mg);
			// print shortest path
			Iterable<MyEdge> path = sp.calculateShortestPath(298121, 441761);
			if(path != null) {
				for (MyEdge e : path) {
					System.out.println((e.getRoadName() + "   "));
					e.putOnPath();
				}
				System.out.println(sp.distTo(441761));
			}
			else
				System.out.println("No path");
			System.out.println();
		
			mg = null;
			loading = false;
			lt = null;
			CreateGUI.getInstance(qt);
			System.gc();
			System.out.println("Garbage collection done");
			System.out.println("Done drawing");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
