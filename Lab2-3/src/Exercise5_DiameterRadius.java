import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.*;

public class Exercise5_DiameterRadius {
    private static final String GRAPH_FILE = "dgs/gridvaluated_30_120.dgs";
    private static final boolean AUTO_LAYOUT = false;
    private static final int DELAY_MS = 20;

    private static final String STYLE_SHEET =
            "graph { fill-color: white; padding: 20px; }" +
                    "node { shape: box; stroke-color: black; text-alignment: under; text-size: 10px; }" +
                    "edge { fill-color: lightgrey; }";

    private static final String INITIAL_NODE_STYLE = "fill-color: grey; size: 10px;";

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
        Tools.pause(500);

        computeDiameterAndRadius(graph);

        Tools.hitakey("Press any key to exit...");
    }

    public static void computeDiameterAndRadius(SingleGraph graph) throws InterruptedException {
        Map<Node, Double> eccentricities = new HashMap<>();

        for (Node n : graph) {
            double ecc = computeEccentricity(graph, n);
            eccentricities.put(n, ecc);
            n.setAttribute("eccentricity", ecc);
            System.out.println("Eccentricity(" + n.getId() + ") = " + ecc);
        }

        double diameter = Collections.max(eccentricities.values());
        double radius = Collections.min(eccentricities.values());
        System.out.println("\nGraph radius = " + radius);
        System.out.println("Graph diameter = " + diameter);

        for (Node n : graph) {
            double ecc = eccentricities.get(n);
            String color = getColorForEccentricity(ecc, radius, diameter);
            n.setAttribute("ui.style", "fill-color: " + color + "; size: 10px;");
            n.setAttribute("ui.label", String.format("%s (%.1f)", n.getId(), ecc));
        }
    }

    private static double computeEccentricity(SingleGraph graph, Node source) throws InterruptedException {
        Map<Node, Double> dist = new HashMap<>();
        for (Node n : graph) {
            dist.put(n, Double.POSITIVE_INFINITY);
        }
        dist.put(source, 0.0);

        ArrayList<Node> queue = new ArrayList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            Node u = getMinDistanceNode(queue, dist);
            queue.remove(u);

            double du = dist.get(u);

            for (Edge e : u.getEachEdge()) {
                Node v = e.getOpposite(u);
                double weight = getEdgeWeight(e);
                double newDist = du + weight;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    if (!queue.contains(v)) queue.add(v);
                }
            }
        }

        double ecc = 0.0;
        for (double d : dist.values()) {
            if (d != Double.POSITIVE_INFINITY && d > ecc) {
                ecc = d;
            }
        }

        return ecc;
    }

    private static Node getMinDistanceNode(ArrayList<Node> list, Map<Node, Double> dist) {
        Node minNode = list.get(0);
        double minDist = dist.get(minNode);

        for (Node n : list) {
            double d = dist.get(n);
            if (d < minDist) {
                minDist = d;
                minNode = n;
            }
        }
        return minNode;
    }

    private static double getEdgeWeight(Edge e) {
        if (e.hasAttribute("weight")) return e.getAttribute("weight");
        if (e.hasAttribute("length")) return e.getAttribute("length");
        return 1.0;
    }

    private static String getColorForEccentricity(double ecc, double radius, double diameter) {
        if (diameter == radius) return "gray";

        double t = (ecc - radius) / (diameter - radius);
        int r = (int) (255 * t);
        int b = (int) (255 * (1 - t));
        return String.format("rgb(%d,0,%d)", r, b);
    }
}
