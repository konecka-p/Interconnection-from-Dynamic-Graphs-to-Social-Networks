import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class HoneycombGenerator {
    // even-q vertical layout
    public static SingleGraph generateHoneycomb(String id, int rows, int cols) {
        SingleGraph g = new SingleGraph(id);
        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                String nid = nodeId(r, q);
                g.addNode(nid).addAttribute("ui.label", r + "-" + q);
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int q = 0; q < cols; q++) {
                String a = nodeId(r, q);
                // neighbours in even-q axial offset
                int[][] neighEven = { {0, -1}, {0, 1}, {-1, 0}, {1, 0}, {-1, 1}, {1, 1} };
                int[][] neighOdd  = { {0, -1}, {0, 1}, {-1, 0}, {1, 0}, {-1, -1}, {1, -1} };
                int[][] use = (q % 2 == 0) ? neighEven : neighOdd;
                for (int[] d : use) {
                    int nr = r + d[0], nq = q + d[1];
                    if (nr >= 0 && nr < rows && nq >= 0 && nq < cols) {
                        String b = nodeId(nr, nq);
                        if (g.getEdge(a + "--" + b) == null && g.getEdge(b + "--" + a) == null) {
                            g.addEdge(a + "--" + b, a, b);
                        }
                    }
                }
            }
        }
        return g;
    }

    private static String nodeId(int r, int c){ return "h" + r + "-" + c; }

    public static void main(String[] args) {
        SingleGraph g = generateHoneycomb("hex", 8, 8);
        g.addAttribute("ui.stylesheet", "node {size:6px;} edge{size:1px}");
        g.display();
    }
}
