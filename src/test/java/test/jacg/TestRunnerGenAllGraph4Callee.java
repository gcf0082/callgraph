package test.jacg;

import com.gcf.callgraph.jacg.runner.RunnerGenAllGraph4Callee;

/**
 * @author adrninistrator
 * @date 2021/6/23
 * @description: 生成向上的方法完整调用链
 */

public class TestRunnerGenAllGraph4Callee {

    public static void main(String[] args) {
        RunnerGenAllGraph4Callee runnerGenAllGraph4Callee = new RunnerGenAllGraph4Callee();
        runnerGenAllGraph4Callee.run();
        String json = runnerGenAllGraph4Callee.getCalleeGraphJson();
        System.out.println(json);
    }
}
