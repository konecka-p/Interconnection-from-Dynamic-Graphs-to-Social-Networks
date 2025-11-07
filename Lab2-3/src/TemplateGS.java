import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

public class TemplateGS {

    public TemplateGS(String[] args) {
        SingleGraph myGraph = new SingleGraph("first graph");
        myGraph.addNode("a");
        myGraph.addNode("b");
        myGraph.addNode("c");
        myGraph.addEdge("ab","a","b");
        myGraph.addEdge("a->c","a","c",true);
        myGraph.display();
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new TemplateGS(args);
    }

}