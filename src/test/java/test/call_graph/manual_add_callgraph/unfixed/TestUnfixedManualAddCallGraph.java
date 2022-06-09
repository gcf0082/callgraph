package test.call_graph.manual_add_callgraph.unfixed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author adrninistrator
 * @date 2022/4/16
 * @description:
 */
public class TestUnfixedManualAddCallGraph {
    public LinkedList test1a() {
        return new UnfixedService1a().invoke(null, null);
    }

    public LinkedList test1b() {
        return new UnfixedService1a().invoke(null, null);
    }

    public ArrayList test2() {
        return new AbstractUnFixedService1<Integer, ArrayList>() {
            @Override
            protected ArrayList execute(Integer integer, ArrayList list) {
                System.setProperty("", "");
                return null;
            }
        }.invoke(null, null);
    }

    public Set test3() {
        return new AbstractUnFixedService1<BigDecimal, Set>() {
            @Override
            protected Set execute(BigDecimal bigDecimal, Set set) {
                System.setIn(null);
                return null;
            }
        }.invoke(null, null);
    }

    public List test4() {
        return new AbstractUnFixedService1<String, List>() {
            @Override
            protected List execute(String str, List list) {
                System.setOut(null);
                return list;
            }
        }.invoke(null, null);
    }
}
