package test.run_by_code;

import com.gcf.callgraph.web.utils.SourceUtil;
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

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.apache.tinkerpop.gremlin.structure.io.IoCore.graphml;
import static org.mockito.AdditionalMatchers.gt;

public class TestTemp {
    private static final Logger logger = LoggerFactory.getLogger(TestTemp.class);

    @Test
    public void test_caller_graph() {
        Graph graph = TinkerGraph.open();
        GraphTraversalSource g = graph.traversal();
        Vertex v1 = graph.addVertex(T.label, "method", T.id, 1, "name", "func1"); //2
        Vertex v2 = graph.addVertex(T.label, "method", T.id, 2, "name", "func2");
        Vertex v3 = graph.addVertex(T.label, "method", T.id, 3, "name", "func3");
        Vertex v4 = graph.addVertex(T.label, "method", T.id, 4, "name", "func4");
        Vertex v5 = graph.addVertex(T.label, "method", T.id, 5, "name", "func5");


        v1.addEdge("call", v4);
        v2.addEdge("call", v1);
        v3.addEdge("call", v1);
        v3.addEdge("call", v5);
        v4.addEdge("call", v3);
        v4.addEdge("call", v1);
        //v5.addEdge("call", v4);
        //v3.addEdge("call", v1);

        /*GraphTraversal tmp = g.V().hasId(4);
        if (tmp.hasNext()) {
            List<Vertex> tmp2 = tmp.toList();
            (tmp2.get(0)).addEdge("call", v3);
            System.out.println(tmp2.get(0));
        }*/

        g.io("extract.graphml").with(IO.writer, IO.graphml).write().iterate();
        List<Path> paths1 = g.V().has("name", "func1").repeat(out().simplePath()).until(outE().count().is(0)).path().toList();
        System.out.println(paths1);
        List<Path> paths2 = g.V().as("a").has("name", "func1").
                repeat(outE().inV().simplePath()).emit().
                //times(2000).
                outE().inV().where(eq("a")).
                path().toList();
        System.out.println(paths2);

        GraphTraversal tree = g.V().as("a").has("name", "func1").
                repeat(outE().inV().simplePath()).emit().
                //times(2000).
                        outE().inV().where(eq("a"))
                .tree().by("name");
        System.out.println(tree.next());

        List<Path>  paths4 = g.V().has("name", "func1").out().has("name", "func6").path().toList();
        System.out.println(paths4);
    }

    @Test
    public void test_callee_graph() {
        Graph graph = TinkerGraph.open();
        GraphTraversalSource g = graph.traversal();
        Vertex v1 = graph.addVertex(T.label, "method", T.id, 1, "name", "func1"); //2
        Vertex v2 = graph.addVertex(T.label, "method", T.id, 2, "name", "func2");
        Vertex v3 = graph.addVertex(T.label, "method", T.id, 3, "name", "func3");
        Vertex v4 = graph.addVertex(T.label, "method", T.id, 4, "name", "func4");
        Vertex v5 = graph.addVertex(T.label, "method", T.id, 5, "name", "func5");


        v1.addEdge("call", v2);
        v2.addEdge("call", v3);
        v3.addEdge("call", v1);

        List<Path> paths1 = g.V().has("name", "func3").repeat(in().simplePath()).until(inE().count().is(0)).path().toList();
        System.out.println(paths1);
        List<Path> paths2 = g.V().as("a").has("name", "func3").
                repeat(inE().outV().simplePath()).emit().
                //times(2000).
                        inE().outV().where(eq("a")).
                path().toList();
        System.out.println(paths2);

        GraphTraversal tree = g.V().as("a").has("name", "func3").
                repeat(inE().outV().simplePath()).emit().
                //times(2000).
                        inE().outV().where(eq("a"))
                .tree().by("name");
        System.out.println(tree.next());
    }

    @Test
    public void test_read() throws IOException {
        Graph graph = TinkerGraph.open(); graph.io(graphml()).readGraph("extract2.graphml");
        GraphTraversalSource g = graph.traversal();
        GraphTraversal tree = g.V().has("name", "test.call_graph.method_call.TestMCCaller:test4h()").repeat(out().simplePath()).until(outE().count().is(0)).tree().by("name");
        System.out.println(tree.next());
    }

    @Test
    public void test_log4j_2() throws IOException {
        Graph graph = TinkerGraph.open(); graph.io(graphml()).readGraph("extract2.graphml");
        GraphTraversalSource g = graph.traversal();
        GraphTraversal tree = g.V().has("name", "org.apache.logging.log4j.core.appender.FileManager:getName()").repeat(out().simplePath()).until(outE().count().is(0)).tree().by("name");
        System.out.println(tree.next());
    }

    @Test
    public void test_log4j() throws IOException {
        Graph graph = TinkerGraph.open(); graph.io(graphml()).readGraph("C:\\temp\\callgraph\\output_dir\\extract2.graphml");
        GraphTraversalSource g = graph.traversal();
        String caleeMethod = "java.io.File:exists()";
        GraphTraversal tree = g.V().has("name", caleeMethod).repeat(timeLimit(50).in().simplePath()).until(inE().count().is(0)).tree().by("name");
        System.out.println(tree.next());
        /*List<Path> paths2 = g.V().as("a").has("name", caleeMethod).
                repeat(inE().outV().simplePath()).emit().
                //times(2000).
                        inE().outV().where(eq("a")).
                path().toList();
        System.out.println(paths2);

        GraphTraversal tree = g.V().as("a").has("name", caleeMethod).
                repeat(inE().outV().simplePath()).emit().
                //times(2000).
                        inE().outV().where(eq("a"))
                .tree().by("name");
        System.out.println(tree.next());*/
    }
    @Test
    public void test4() {
        Graph graph = TinkerFactory.createModern();
        GraphTraversalSource g = graph.traversal();
        System.out.println(g.V(1).values("xname"));
    }

    @Test
    public void test_getSoruce() {
        String content = SourceUtil.getSourceContentByClassName("log4j", "org.apache.logging.log4j.core.filter.BurstFilter");
        System.out.println(content);
    }

}
