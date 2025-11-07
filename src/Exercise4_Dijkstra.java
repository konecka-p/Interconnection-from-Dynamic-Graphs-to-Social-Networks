import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.ArrayList;

public class Exercise4_Dijkstra {
//    private static final String GRAPH_FILE = "dgs/gridvaluated_10_12.dgs";
    private static final String GRAPH_FILE = "dgs/gridvaluated_10_220.dgs";
    private static final boolean AUTO_LAYOUT = false;
    private static final int DELAY_MS = 100;

    private static final String STYLE_SHEET =
            "graph { fill-color: white; padding: 20px; }" +
                    "node { shape: box; stroke-color: black; text-alignment: under; }" +
                    "edge { fill-color: lightgrey; text-size: 10px; text-alignment: along; }";

    private static final String INITIAL_NODE_STYLE = "fill-color: blue; size: 8px;";
    private static final String VISITED_NODE_STYLE = "fill-color: red; size: 10px;";
    private static final String CURRENT_NODE_STYLE = "fill-color: green; size: 12px;";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        SingleGraph graph = Tools.readGraph(GRAPH_FILE);
        graph.addAttribute("ui.stylesheet", STYLE_SHEET);
        graph.addAttribute("ui.antialias");

        for (Node n : graph) {
            n.setAttribute("ui.style", INITIAL_NODE_STYLE);
        }

        Viewer viewer = graph.display(AUTO_LAYOUT);
        Tools.pause(500);

        dijkstra(graph);

        Tools.hitakey("Dijkstra completed. Press any key to exit...");
    }

    public static void dijkstra(SingleGraph graph) throws InterruptedException {
        Node source = Toolkit.randomNode(graph);
        System.out.println("Starting Dijkstra from node: " + source.getId());

        for (Node n : graph) {
            n.setAttribute("distance", Double.POSITIVE_INFINITY);
            n.setAttribute("visited", false);
        }
        source.setAttribute("distance", 0.0);

        ArrayList<Node> queue = new ArrayList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            Node current = getMinDistanceNode(queue);
            queue.remove(current);

            current.setAttribute("visited", true);
            current.setAttribute("ui.style", CURRENT_NODE_STYLE);
            Tools.pause(DELAY_MS);

            double currentDist = current.getAttribute("distance");

            for (Edge e : current.getEachEdge()) {
                Node neighbor = e.getOpposite(current);
                if ((boolean) neighbor.getAttribute("visited")) continue;

                double weight = getEdgeWeight(e);
                double newDist = currentDist + weight;
                double oldDist = neighbor.getAttribute("distance");

                if (newDist < oldDist) {
                    neighbor.setAttribute("distance", newDist);
                    if (!queue.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }

            current.setAttribute("ui.style", VISITED_NODE_STYLE);
            Tools.pause(DELAY_MS);
        }

        for (Node n : graph) {
            double d = n.getAttribute("distance");
            if (Double.isInfinite(d))
                n.setAttribute("ui.label", n.getId() + " (∞)");
            else
                n.setAttribute("ui.label", n.getId() + " (" + String.format("%.1f", d) + ")");
        }
    }

    private static Node getMinDistanceNode(ArrayList<Node> list) {
        Node minNode = list.get(0);
        double minDist = minNode.getAttribute("distance");

        for (Node n : list) {
            double dist = n.getAttribute("distance");
            if (dist < minDist) {
                minDist = dist;
                minNode = n;
            }
        }
        return minNode;
    }

    private static double getEdgeWeight(Edge e) {
        if (e.hasAttribute("weight")) return e.getAttribute("weight");
        if (e.hasAttribute("length")) return e.getAttribute("length");
        return 1.0; //default
    }
}
