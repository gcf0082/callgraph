package com.gcf.callgraph.jacg.model;

public class CalleeMethod {
    private Method callee;
    private int lineNum;

    public Method getCallee() {
        return callee;
    }

    public void setCallee(Method callee) {
        this.callee = callee;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
