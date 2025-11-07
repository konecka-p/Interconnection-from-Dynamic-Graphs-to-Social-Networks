import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

import java.util.*;

public class Exercise2_BFS {
    private static final String GRAPH_FILE = "dgs/completegrid_10.dgs";
    private static final String STYLE_SHEET =
            "graph { fill-color: white; padding: 20px; }" +
                    "node { fill-color: red; size: 0px; shape: box; stroke-color: black; }" +
                    "edge { fill-color: lightgray; }";

    private static final int DELAY_MS = 200;
    private static final String INITIAL_NODE_STYLE = "fill-color: red; size: 0px; shape: box; stroke-color: black;";
    private static final String VISITED_NODE_STYLE = "fill-color: black; size: 6px; shape: box;";

    public static void main(String[] args) throws InterruptedException {
        SingleGraph graph = Tools.readGraph(GRAPH_FILE);
//        graph.addAttribute("ui.stylesheet", STYLE_SHEET);
        for (Node n : graph) {
            n.addAttribute("ui.style", "fill-color: red; size: 0px; shape: box; stroke-color: black;");
        }
        for (Node n : graph) {
            n.addAttribute("ui.style", INITIAL_NODE_STYLE);
        }


        Viewer viewer = graph.display();
        viewer.disableAutoLayout();

        Tools.pause(200);

        // start point
        bfs(graph, "0-5");

        Tools.hitakey("BFS completed.");
    }

    public static void bfs(SingleGraph graph, String startId) throws InterruptedException {
        Node start = graph.getNode(startId);
        if (start == null) {
            System.out.println("Start node not found: " + startId);
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

//            System.out.println("Visiting node: " + current.getId());
//            current.setAttribute("ui.style", "fill-color: black; size: 6px; shape: box;");
            current.setAttribute("ui.style", VISITED_NODE_STYLE);
            Tools.pause(200);

            // add neighbours
            for (Edge e : current.getEachEdge()) {
                Node neighbor = e.getOpposite(current);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }
}
