import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

public class RandomWalk {
    public static List<Node> randomWalk(SingleGraph g, String sourceId, String targetId) {
        Node cur = g.getNode(sourceId);
        Node target = g.getNode(targetId);
        if (cur == null || target == null) return Collections.emptyList();

        List<Node> path = new ArrayList<>();
        Random rnd = new Random();

        while (!cur.equals(target)) {
            path.add(cur);
            // choose random neighbor
            List<Node> neigh = new ArrayList<>();
            Iterator<Node> it = cur.getNeighborNodeIterator();
            while (it.hasNext()) neigh.add(it.next());
            if (neigh.isEmpty()) break; // no way
            cur = neigh.get(rnd.nextInt(neigh.size()));
            if (path.size() > g.getNodeCount() * 100) break;
        }
        path.add(target);
        return path;
    }

    // demo
    public static void main(String[] args) {
        SingleGraph g = MazeGenerator.generateVonNeumannGrid("grid4", 10, 10);
        g.addAttribute("ui.stylesheet","node{size:8px;}edge{size:1px}");
        g.display();
        List<Node> walk = randomWalk(g, "m0-0", "m9-9");
        // highlight path
        for (Node n : walk) n.setAttribute("ui.style", "fill-color: yellow; size: 10px;");
        for (int i = 0; i < walk.size() - 1; i++) {
            Edge e = walk.get(i).getEdgeBetween(walk.get(i + 1));
            if (e != null) e.setAttribute("ui.style", "fill-color: orange; size:3px;");
        }
    }
}
