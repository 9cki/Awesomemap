package graph;

import java.io.IOException;
import java.util.HashSet;

import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;


public class MyGraph {

	public static MyNode[] nodes;

	public MyGraph() throws NavException, XPathParseException, XPathEvalException, ParseException, IOException, InterruptedException {
		InitNodesThread nThread = new InitNodesThread();
		InitEdgesThread eThread = new InitEdgesThread();
		nThread.start();
		eThread.start();
		nThread.setPriority(Thread.MAX_PRIORITY);
		eThread.setPriority(Thread.MAX_PRIORITY);

		while(nThread.isAlive() || eThread.isAlive()) {
			Thread.sleep(10);
		}
	}

	//Initializes the nodes in an array
	public static void initNodes() throws NavException, XPathParseException, XPathEvalException, ParseException, IOException {
		String[] nodeArray = createNodeArray();
		nodes = new MyNode[nodeArray.length];
		int i = 0;
		while(i < nodeArray.length) {
			if(nodeArray[i] != null) {
				String[] lineArray = nodeArray[i].split("\\s&&&\\s");
				//Creates a new MyNode for each line in the nodeArray.
				nodes[i] = new MyNode(Integer.parseInt(lineArray[0]), Double.parseDouble(lineArray[1]), Double.parseDouble(lineArray[2]));
			}
			i++;
		}
		nodeArray = null;
	}

	//Initializes the edges in an array
	public static void initEdges() throws NavException, XPathParseException, XPathEvalException, ParseException, IOException {
		String[] edgeArray = createEdgeArray();
		String[] lineArray;
		for(int i = 0; i < edgeArray.length; i++) {
			if(edgeArray[i] != null) {
				String s = edgeArray[i];
				lineArray = s.split("\\s&&&\\s");
				nodes[Integer.parseInt(lineArray[0])-1].addEdge(
						new MyEdge(nodes[Integer.parseInt(lineArray[0])-1], nodes[Integer.parseInt(lineArray[1])-1], 
								Double.parseDouble(lineArray[2]), Integer.parseInt(lineArray[3]), lineArray[4], 
								Integer.parseInt(lineArray[5])));
				nodes[Integer.parseInt(lineArray[1])-1].addEdge(
						new MyEdge(nodes[Integer.parseInt(lineArray[1])-1], nodes[Integer.parseInt(lineArray[0])-1], 
								Double.parseDouble(lineArray[2]), Integer.parseInt(lineArray[3]), lineArray[4], 
								Integer.parseInt(lineArray[5])));
			}
		}
		edgeArray = null;
	}

	//Creates the node array by parsing the xml file
	public static String[] createNodeArray() throws NavException, XPathParseException, XPathEvalException, ParseException, IOException {
		MyNodeVTD nodeVTD = new MyNodeVTD();
		String[] nodeArray = null;
		try {
			nodeArray = nodeVTD.parse("kdv_node_unload.xml"); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		nodeVTD = null;
		return nodeArray;
	}

	//Creates the edge array by parsing the xml file
	public static String[] createEdgeArray() throws NavException, XPathParseException, XPathEvalException, ParseException, IOException {
		MyEdgeVTD edgeVTD = new MyEdgeVTD();
		String[] edgeArray = null;
		try {
			edgeArray = edgeVTD.parse("kdv_unload_Att.xml"); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		edgeVTD = null;
		return edgeArray;
	}

	public MyNode[] getNodeArray() {
		return nodes;
	}
	
	public HashSet<MyEdge> getEdges() {
		HashSet<MyEdge> set = new HashSet<MyEdge>();
		for(MyNode n : nodes) {
			set.addAll(n.getEdges());
		}
		return set;
	}


	public class InitNodesThread extends Thread {

		public void run() {
			try {
				MyGraph.initNodes();
			} catch (Exception e) {
				System.out.println("Error");
			}
		}
	}

	public class InitEdgesThread extends Thread {

		public void run() {
			try {
				MyGraph.initEdges();
			} catch (Exception e) {
				System.out.println("Error");
			}
		}
	}
}
