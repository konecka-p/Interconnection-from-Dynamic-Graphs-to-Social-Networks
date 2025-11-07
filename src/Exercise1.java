import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class Exercise1 {
    private static final String GRAPH_FILE = "dgs/firstgraphlab2.dgs";
    private static final int THRESHOLD = 30;

    private static final String STYLE_SHEET =
            "node {" +
                    "   text-size: 14px;" +
                    "   text-alignment: above;" +
                    "   text-color: black;" +
                    "}";

    public static void main(String[] args) {
        SingleGraph graph = Tools.readGraph(GRAPH_FILE);
        graph.display();

        // set styles for all nodels
        graph.setAttribute("ui.stylesheet", STYLE_SHEET);

        // compute the average degree of graph
        double sumDegrees = 0;
        for (Node node : graph) {
            sumDegrees += node.getDegree();
        }
        double avgDegree = sumDegrees / graph.getNodeCount();
        System.out.println("Average degree of the graph: " + avgDegree);


        for (Node node : graph) {
            int neighborCostSum = 0;
            for (Edge edge : node.getEachEdge()) {
                Node neighbor = edge.getOpposite(node);
                int neighborCost = neighbor.getAttribute("cost");
                neighborCostSum += neighborCost;
            }

//            System.out.println(node.getId() + ": neighbor cost sum = " + neighborCostSum);

            // set styles (red or blue nodes)
            if (neighborCostSum > THRESHOLD) {
                node.setAttribute("ui.style", "fill-color: red; size: 30px;");
            } else {
                node.setAttribute("ui.style", "fill-color: blue; size: 15px;");
            }

            int cost = node.getAttribute("cost");
            node.setAttribute("ui.label", node.getId() + " (cost=" + cost + ")");
        }
        Tools.hitakey("");
    }
}
