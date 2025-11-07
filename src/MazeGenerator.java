import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

public class MazeGenerator {
    public static SingleGraph generateVonNeumannGrid(String id, int rows, int cols) {
        SingleGraph g = new SingleGraph(id);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                g.addNode(nodeId(r,c)).addAttribute("ui.label", r + "-" + c);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String a = nodeId(r,c);
                if (r + 1 < rows) addIfAbsent(g, a, nodeId(r+1,c));
                if (c + 1 < cols) addIfAbsent(g, a, nodeId(r,c+1));
            }
        }
        return g;
    }

    private static void addIfAbsent(Graph g, String a, String b) {
        if (g.getEdge(a + "--" + b) == null && g.getEdge(b + "--" + a) == null) {
            g.addEdge(a + "--" + b, a, b).addAttribute("ui.style", "fill-color: lightgray; size:1px;");
        }
    }

    // randomized DFS to create spanning tree (maze)
    public static void carveMazeByRandomDFS(SingleGraph g, int startR, int startC, int delay) throws InterruptedException {
        Node start = g.getNode(nodeId(startR, startC));
        Stack<Node> st = new Stack<>();
        Set<Node> visited = new HashSet<>();
        st.push(start);
        Random rand = new Random();

        while (!st.isEmpty()) {
            Node u = st.pop();
            if (!visited.contains(u)) {
                visited.add(u);
                u.setAttribute("ui.style", "fill-color: white; size:8px;");
                Thread.sleep(delay);

                // gather unvisited neighbors
                List<Node> neigh = new ArrayList<>();
                Iterator<Node> it = u.getNeighborNodeIterator();
                while (it.hasNext()) {
                    Node v = it.next();
                    if (!visited.contains(v)) neigh.add(v);
                }
                Collections.shuffle(neigh, rand);
                for (Node v : neigh) {
                    if (!visited.contains(v)) {
                        st.push(v);
                        // mark edge as corridor (part of spanning tree)
                        Edge e = u.getEdgeBetween(v);
                        if (e != null) e.setAttribute("ui.style", "size:3px; fill-color: black;");
                    }
                }
            }
        }
    }

    private static String nodeId(int r, int c) { return "m" + r + "-" + c; }

    public static void main(String[] args) throws InterruptedException {
        SingleGraph g = generateVonNeumannGrid("mazeGrid", 20, 20);
        g.addAttribute("ui.stylesheet", "node {size:6px; fill-color: gray;} edge{size:1px; fill-color:lightgray}");
        g.display();
        Thread.sleep(300);
        carveMazeByRandomDFS(g, 0, 0, 10);
    }
}
