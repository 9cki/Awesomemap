import coastlines.CoastLines;
import coastlines.MyCoastLines;
import coastlines.MyUTMPoint;
import util.GCThread;
import gui.DijkstraSP;

import graph.MyEdge;
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
		try {
			MyGraph mg = new MyGraph();
			MyNode[] nodes = mg.getNodeArray();
			System.out.println("Nodes done");
			
			QuadTree<Double> qt = new QuadTree<Double>();
			for(MyNode n : nodes) {
				qt.insert(n);
			}
			System.out.println("Nodes inserted");
			
			DijkstraSP sp = new DijkstraSP(mg, 441761);
	        // print shortest path
			int t = 443225;
	            if (sp.hasPathTo(t)) {
	                System.out.printf("%d to %d (%.2f)  ", 1, t, sp.distTo(t));
	                if (sp.hasPathTo(t)) {
	                    for (MyEdge e : sp.pathTo(t)) {
	                        StdOut.print(e.getRoadName() + "   ");
	                    }
	                }
	                System.out.println();
	            }
	            else {
	                System.out.printf("%d to %d         no path\n", 1, t);
	           }
			
			nodes = null;
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
