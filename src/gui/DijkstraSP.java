package gui;

import java.util.Stack;

import graph.MyEdge;
import graph.MyGraph;

/*************************************************************************
 *  Compilation:  javac DijkstraSP.java
 *  Execution:    java DijkstraSP input.txt s
 *  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *  Data files:   http://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                http://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                http://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *  % java DijkstraSP tinyEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32   
 *  0 to 2 (0.26)  0->2  0.26   
 *  0 to 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39   
 *  0 to 4 (0.38)  0->4  0.38   
 *  0 to 5 (0.73)  0->4  0.38   4->5  0.35   
 *  0 to 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   
 *  0 to 7 (0.60)  0->2  0.26   2->7  0.34   
 *
 *  % java DijkstraSP mediumEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07   
 *  0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11   
 *  0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12   
 *  0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11   
 *  ...
 *
 *************************************************************************/

public class DijkstraSP {
	
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private MyEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private MyGraph G;

    public DijkstraSP(MyGraph G) {
    	this.G= G;
        for (MyEdge e : G.getEdges()) {
            if (e.getLength() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative length");
        }

        distTo = new double[G.getNodeArray().length];
        edgeTo = new MyEdge[G.getNodeArray().length];

    }
        
    public Iterable<MyEdge> calculateShortestPath(int from, int to) {
    	//Resets the arrays so previous searches results are removed
        for (int v = 0; v < G.getNodeArray().length; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
    		edgeTo[v] = null;
    }
        
    	distTo[from] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.getNodeArray().length);
        pq.insert(from, distTo[from]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (MyEdge e : G.nodes[v].getEdges())
                relax(e);
        }
        // check optimality conditions
        assert check(G, from);
        
        return pathTo(to);
    }

    // relax edge e and update pq if changed
    private void relax(MyEdge e) {
        int v = e.getFromNode().getKDV()-1, w = e.getToNode().getKDV()-1;
        if (distTo[w] > distTo[v] + e.getLength()) {
            distTo[w] = distTo[v] + e.getLength();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    // length of shortest path from s to v
    public double distTo(int v) {
        return distTo[v];
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // shortest path from s to v as an Iterable, null if no such path
    public Iterable<MyEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<MyEdge> path = new Stack<MyEdge>();
        for (MyEdge e = edgeTo[v]; e != null; e = edgeTo[e.getFromNode().getKDV()-1]) {
            path.push(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(MyGraph G, int s) {

        // check that edge weights are nonnegative
        for (MyEdge e : G.getEdges()) {
            if (e.getLength() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.getNodeArray().length; v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.getNodeArray().length; v++) {
            for (MyEdge e : G.nodes[v].getEdges()) {
                int w = e.getToNode().getKDV()-1;
                if (distTo[v] + e.getLength() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.getNodeArray().length; w++) {
            if (edgeTo[w] == null) continue;
            MyEdge e = edgeTo[w];
            int v = e.getFromNode().getKDV()-1;
            if (w != e.getToNode().getKDV()-1) return false;
            if (distTo[v] + e.getLength() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }
}