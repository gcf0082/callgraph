package test.run_by_code;

import org.apache.tinkerpop.gremlin.process.computer.traversal.step.map.ShortestPath;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLIo;
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
        g = graph.traversal().withComputer();
        List<Path> paths = g.V().has("name", "func1").repeat(out().simplePath()).until(outE().count().is(0)).path().toList();
        System.out.println(paths);
    }

}
