package test.jacg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gcf.callgraph.jacg.runner.RunnerGenAllGraph4Caller;

import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/6/23
 * @description: 生成向下的方法完整调用链
 */

public class TestRunnerGenAllGraph4Caller {


    public static void main(String[] args) {
        RunnerGenAllGraph4Caller runnerGenAllGraph4Caller =  new RunnerGenAllGraph4Caller();
        runnerGenAllGraph4Caller.run();
        String json = runnerGenAllGraph4Caller.getCalleeGraphJson();
        System.out.print(json);
    }
}
