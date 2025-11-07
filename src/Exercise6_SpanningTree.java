import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

import java.util.*;

public class Exercise6_SpanningTree {
    private static final String GRAPH_FILE = "dgs/completegrid_10.dgs";

    private static final String STYLE_SHEET =
            "graph { fill-color: white; padding: 20px; }" +
                    "node { shape: box; size: 8px; fill-color: blue; stroke-color: black; }" +
                    "edge { fill-color: lightgray; size: 1px; }";

    private static final int DELAY_MS = 100;
    private static final String VISITED_NODE_STYLE = "fill-color: red; size: 10px; shape: box;";
    private static final String IN_STACK_NODE_STYLE = "fill-color: green; size: 8px; shape: box;";
    private static final String SPANNING_EDGE_STYLE = "size: 3px; fill-color: red;";

    public static void main(String[] args) throws InterruptedException {
        SingleGraph graph = Tools.readGraph(GRAPH_FILE);
        graph.addAttribute("ui.stylesheet", STYLE_SHEET);
        graph.addAttribute("ui.antialias");

        Viewer viewer = graph.display();
        viewer.disableAutoLayout();

        Tools.pause(200);

        dfsWithSpanningTree(graph);
        Tools.hitakey("DFS Spanning Tree completed!");
    }

    public static void dfsWithSpanningTree(SingleGraph graph) throws InterruptedException {
        Node start = getRandomNode(graph);

        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (!visited.contains(current)) {
                visited.add(current);
                current.setAttribute("ui.style", VISITED_NODE_STYLE);
                Tools.pause(DELAY_MS);

                for (Edge e : current.getEachEdge()) {
                    Node neighbor = e.getOpposite(current);
                    if (!visited.contains(neighbor) && !stack.contains(neighbor)) {
                        e.setAttribute("ui.style", SPANNING_EDGE_STYLE);
                        neighbor.setAttribute("ui.style", IN_STACK_NODE_STYLE);

                        stack.push(neighbor);
                        Tools.pause(DELAY_MS);
                    }
                }
            }
        }
    }

    private static Node getRandomNode(Graph g) {
        List<Node> nodes = new ArrayList<>();
        for (Node n : g) {
            nodes.add(n);
        }
        Random r = new Random();
        return nodes.get(r.nextInt(nodes.size()));
    }
}
