import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class MooreGridGenerator {
    public static SingleGraph generateMooreGrid(String id, int rows, int cols) {
        SingleGraph g = new SingleGraph(id);
        // create nodes
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String nid = idNode(r, c);
                Node n = g.addNode(nid);
                n.addAttribute("ui.label", r + "-" + c);
            }
        }
        // connect More neighbours (8 neighborhood)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String a = idNode(r, c);
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                            String b = idNode(nr, nc);
                            String eid = a + "--" + b;

                            if (g.getEdge(eid) == null && g.getEdge(b + "--" + a) == null) {
                                g.addEdge(eid, a, b);
                            }
                        }
                    }
                }
            }
        }
        return g;
    }

    private static String idNode(int r, int c){ return "v" + r + "-" + c; }

    public static void main(String[] args) {
        SingleGraph g = generateMooreGrid("moore", 8, 8);
        g.addAttribute("ui.stylesheet", "node {size:8px;} edge{size:1px}");
        g.display();
    }
}
