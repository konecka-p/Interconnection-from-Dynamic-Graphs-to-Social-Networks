import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.Iterator;
import java.util.Stack;

public class Exercise3_DFS {
    private static final String GRAPH_FILE = "dgs/completegrid_30.dgs";
    private static final boolean AUTO_LAYOUT = false;
    private static final boolean SPANNING_TREE_VISU = true;
    private static final int DELAY_MS = 50;

    // styles
    private static final String STYLE_SHEET =
            "graph { fill-color: white; padding: 20px; }" +
                    "node { shape: box; stroke-color: black; text-alignment: under; }" +
                    "edge { fill-color: lightgrey; }";

    private static final String INITIAL_NODE_STYLE = "fill-color: blue; size: 5px;";
    private static final String VISITED_NODE_STYLE = "fill-color: red; size: 10px;";
    private static final String IN_MEMORY_NODE_STYLE = "fill-color: green; size: 7px;";
    private static final String SPANNING_TREE_EDGE_STYLE = "size: 4px; fill-color: red;";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        SingleGraph graph = Tools.readGraph(GRAPH_FILE);
        graph.addAttribute("ui.stylesheet", STYLE_SHEET);
        graph.addAttribute("ui.antialias");

        for (Node n : graph) {
            n.addAttribute("ui.style", INITIAL_NODE_STYLE);
        }

        Viewer viewer = graph.display(AUTO_LAYOUT);
        Tools.pause(300);

        dfs(graph);

        Tools.hitakey("DFS completed. Press any key to exit...");
    }

    public static void dfs(SingleGraph graph) throws InterruptedException {
        Node start = Toolkit.randomNode(graph);
        System.out.println("Starting DFS from node: " + start.getId());

        Stack<Node> memory = new Stack<>();
        memory.push(start);

        while (!memory.isEmpty()) {
            Node u = memory.pop();

            if (!u.hasAttribute("visited")) {
                u.addAttribute("visited", true);
                u.addAttribute("ui.style", VISITED_NODE_STYLE);
                Tools.pause(DELAY_MS);

                Iterator<Node> neighbors = u.getNeighborNodeIterator();
                while (neighbors.hasNext()) {
                    Node w = neighbors.next();

                    if (!w.hasAttribute("visited") && !memory.contains(w)) {
                        memory.push(w);

                        if (SPANNING_TREE_VISU) {
                            Edge e = u.getEdgeBetween(w);
                            e.addAttribute("ui.style", SPANNING_TREE_EDGE_STYLE);
                        }

                        w.addAttribute("ui.style", IN_MEMORY_NODE_STYLE);
                        Tools.pause(DELAY_MS);
                    }
                }
            }
        }
    }
}
