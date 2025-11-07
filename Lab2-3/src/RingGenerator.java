import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class RingGenerator {
    public static SingleGraph generateRing(String id, int n) {
        SingleGraph g = new SingleGraph(id);
        for (int i = 0; i < n; i++) {
            g.addNode(nodeId(i)).addAttribute("ui.label", nodeId(i));
        }
        for (int i = 0; i < n; i++) {
            String a = nodeId(i);
            String b = nodeId((i + 1) % n);
            g.addEdge(a + "--" + b, a, b);
        }
        return g;
    }

    private static String nodeId(int i) { return "r" + i; }

    public static void main(String[] args) {
        SingleGraph g = generateRing("ring10", 10);
        g.addAttribute("ui.stylesheet", "node {size: 20px;} edge {size:2px}");
        g.display();
    }
}
