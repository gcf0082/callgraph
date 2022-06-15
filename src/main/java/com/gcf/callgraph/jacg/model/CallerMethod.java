package com.gcf.callgraph.jacg.model;

public class CallerMethod {
    private Method caller;
    private int lineNum;

    public Method getCaller() {
        return caller;
    }

    public void setCaller(Method caller) {
        this.caller = caller;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
