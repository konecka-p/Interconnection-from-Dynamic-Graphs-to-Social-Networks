import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;

public class KaryTreeGenerator {
    public static SingleGraph generateKaryTree(String id, int k, int height) {
        SingleGraph g = new SingleGraph(id);
        int counter = 0;
        Node root = g.addNode("n" + (counter++));
        root.addAttribute("ui.label", root.getId());

        List<Node> currentLevel = new ArrayList<>();
        currentLevel.add(root);

        for (int level = 1; level <= height; level++) {
            List<Node> nextLevel = new ArrayList<>();
            for (Node parent : currentLevel) {
                for (int i = 0; i < k; i++) {
                    Node child = g.addNode("n" + (counter++));
                    child.addAttribute("ui.label", child.getId());
                    g.addEdge(parent.getId() + "--" + child.getId(), parent.getId(), child.getId());
                    nextLevel.add(child);
                }
            }
            currentLevel = nextLevel;
        }
        return g;
    }

    public static void main(String[] args) {
        SingleGraph g = generateKaryTree("kary", 3, 3); // k=3, height=3
        g.addAttribute("ui.stylesheet", "node {size:20px;} edge {size:1px}");
        g.display();
    }
}
