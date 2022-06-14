package test.run_by_code;

import org.apache.tinkerpop.gremlin.process.computer.traversal.step.map.ShortestPath;
import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLIo;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.apache.tinkerpop.gremlin.structure.io.IoCore.graphml;

public class TestTemp {
    private static final Logger logger = LoggerFactory.getLogger(TestTemp.class);

    @Test
    public void test3() {
        Graph graph = TinkerGraph.open();
        GraphTraversalSource g = graph.traversal();
        Vertex v1 = graph.addVertex(T.label, "method", T.id, 1, "name", "func1"); //2
        Vertex v2 = graph.addVertex(T.label, "method", T.id, 2, "name", "func2");
        Vertex v3 = graph.addVertex(T.label, "method", T.id, 3, "name", "func3");
        Vertex v4 = graph.addVertex(T.label, "method", T.id, 4, "name", "func4");


        v1.addEdge("call", v2);
        v2.addEdge("call", v3);
        v1.addEdge("call", v4);

        GraphTraversal tmp = g.V().hasId(4);
        if (tmp.hasNext()) {
            List<Vertex> tmp2 = tmp.toList();
            (tmp2.get(0)).addEdge("call", v3);
            System.out.println(tmp2.get(0));
        }

        g.io("extract.graphml").with(IO.writer, IO.graphml).write().iterate();
        List<Path> paths = g.V().has("name", "func1").repeat(out().simplePath()).until(outE().count().is(0)).path().toList();
        System.out.println(paths);
    }

    @Test
    public void test_read() throws IOException {
        Graph graph = TinkerGraph.open(); graph.io(graphml()).readGraph("extract2.graphml");
        GraphTraversalSource g = graph.traversal();
        GraphTraversal tree = g.V().has("name", "test.call_graph.method_call.TestMCCaller:test4h()").repeat(out().simplePath()).until(outE().count().is(0)).tree().by("name");
        System.out.println(tree.next());
    }
    @Test
    public void test4() {
        Graph graph = TinkerFactory.createModern();
        GraphTraversalSource g = graph.traversal();
        System.out.println(g.V(1).values("xname"));
    }



}
