import java.io.IOException;

import org.graphstream.graph.implementations.SingleGraph;

public class Tools {

	/**
	 * read the dgs file and create the SingleGraph
	 * and return this graph to the calling process
	 * @param filename
	 * @return
	 */
	public static SingleGraph readGraph(String filename) {
		SingleGraph graph = new SingleGraph(""+filename);
		try { 
			graph.read(filename);
		} catch(Exception e) { 
			System.out.println("Error while reading savedgraph.dgs\n"+e.toString());
		}
		return graph;
	}

    /**
     * add a delay during the evolution of the graph, 
     * mainly for visualization purpose
     * @param delay
     */
    public final static void pause(long delay) {
            try {
                    Thread.sleep(delay);
            } catch(InterruptedException ie) {}
    }
    

	/**
	 * This method stops the execution of the process 
	 * (any algorithm operating on the graph), and asks
	 * the user to hit a key in order for the process
	 * to go on. 
	 * @param message
	 */
	public final static void hitakey(String message) {
        System.out.println("-----------------------");
        System.out.println(message);
        System.out.println("----- Hit a Key ------");
        try {
                System.in.read();
        } catch(IOException ioe) {}
	}
}
