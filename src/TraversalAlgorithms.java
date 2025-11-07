//package y2025_2026.uksw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class TraversalAlgorithms {

	public final static String dgsFile = "dgs/firstgraphlab2.dgs";
	public final static String dgsFile2 = "dgs/randomgnp_50_0.05.dgs";
	public final static String dgsFile3 = "dgs/grid:vonneumann_30.dgs";

	public final static boolean autolayout = false;
	
	public final static String styleSheet = 
			"graph {fill-color:white; padding: 20px; }" 
			+ "node {size:20px; shape:box; fill-color:blue; text-alignment: under;}" 
			+ "edge {fill-color: lightgrey; } ";
	
	// for visualization purpose we add a delay and styles
	public int delay = 50;
	public final static String initialNodeStyle = "fill-color:blue;size:5px;";
	public final static String visitedNodeStyle = "fill-color:red;size:10px;";
	public final static String inMemoryNodeStyle = "fill-color:green;size:7px;";
	public final static String spanningTreeEdge = "size:4px;fill-color:red;";
	public boolean spanningTreeVisu = false;
	
	public final static int DFS_METHOD = 1;
	public final static int BFS_METHOD = 2;
	
	protected SingleGraph myGraph;
	
	public TraversalAlgorithms() {
		initGraph(dgsFile3);
		myGraph.display(autolayout);
		System.out.println("average degree is equal to: "+averageDegree());
		for(Node z:myGraph.getNodeSet()) 
			z.addAttribute("ui.style",initialNodeStyle); // visu
		Tools.hitakey("Start the visit");
		System.out.println("is the graph connected: "+isConnected(DFS_METHOD));
	}
	
	/**
	 * algorithm from the lecture
	 * 
		v randomly chosen vertex
		push v on the Stack S
		while S ̸= ∅do
			u ←pop
			mark u as visited
			Lu ←set of neighbors of u
			for each neighbor w of u do
				if w has not been visited yet then
					push w
				endIf
			endFor
		endWhile
	 * 
	 * We add into the code some instructions for 
	 * visualizing the visit and visualizing the edges of the 
	 * spanning tree built by the visit
	 */
	public void DFS() {
		Node v = Toolkit.randomNode(myGraph);
		Stack<Node> memory = new Stack<>();
		memory.push(v);
		while(!memory.isEmpty()) {
			Node u = memory.pop();
			u.addAttribute("visited",true);
			u.addAttribute("ui.style",visitedNodeStyle); // visu
			Tools.pause(delay);
			Iterator<Node> neighbors = u.getNeighborNodeIterator();
			while(neighbors.hasNext()) {
				Node w = neighbors.next();
				if(!w.hasAttribute("visited") && (!memory.contains(w))) {
					memory.push(w);	
					if(spanningTreeVisu) {
						Edge e = u.getEdgeBetween(w); // spanning tree visu
						e.addAttribute("ui.style",spanningTreeEdge); // spanning tree
					}
					w.addAttribute("ui.style",inMemoryNodeStyle); // visu
					Tools.pause(delay); // visu
				}
			}
		}
	}
	

	/**
	 * algorithm from the lecture
	 * 
		v randomly chosen vertex
		add v to the Queue Q
		while Q ̸= ∅do
			remove u, first element of the Q
			mark u as visited
			Lu ←set of neighbors of u
			for each neighbor w of u do
				if w has not been visited yet then
					add w to Q
				endIf
			endFor
		endWhile
	 * 
	 * We add into the code some instructions for 
	 * visualizing the visit
	 */
	public void BFS() {
		Node v = Toolkit.randomNode(myGraph);
		ArrayList<Node> memory = new ArrayList<>();
		memory.add(v);
		while(!memory.isEmpty()) {
			Node u = memory.remove(0);
			u.addAttribute("visited",true);
			u.addAttribute("ui.style",visitedNodeStyle); // visu
			Tools.pause(delay);
			Iterator<Node> neighbors = u.getNeighborNodeIterator();
			while(neighbors.hasNext()) {
				Node w = neighbors.next();
				if(!w.hasAttribute("visited") && (!memory.contains(w))) {
					memory.add(w);	
					if(spanningTreeVisu) {
						Edge e = u.getEdgeBetween(w); // spanning tree visu
						e.addAttribute("ui.style",spanningTreeEdge); // spanning tree
					}
					w.addAttribute("ui.style",inMemoryNodeStyle); // visu
					Tools.pause(delay); // visu
				}
			}
		}
	}
	
	public boolean isConnected(int method) {
		if(method == DFS_METHOD) DFS();
		else if(method == BFS_METHOD) BFS();
		
		int nbVisitedNodes = 0;
		for(Node u:myGraph.getNodeSet()) {
			if(u.hasAttribute("visited"))
				nbVisitedNodes++;
		}
		return (nbVisitedNodes == myGraph.getNodeCount());
	}
	
	/** 
	 * simple method for computing the average degree of the graph
	 * @return
	 */
	public float averageDegree() {
		float sum = 0;
		for(Node u:myGraph.getNodeSet()) {
			sum+=u.getDegree();
		}
		return sum/myGraph.getNodeCount();
	}

    public void initGraph(String filename) {
        //myGraph = new SingleGraph("first graph");
    	myGraph = Tools.readGraph(filename);
        myGraph.addAttribute("ui.stylesheet",styleSheet);
        myGraph.addAttribute("ui.antialias");
    }
	
	public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", 
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new TraversalAlgorithms();
	}

}
