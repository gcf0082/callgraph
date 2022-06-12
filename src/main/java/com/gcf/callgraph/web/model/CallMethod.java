package com.gcf.callgraph.web.model;

public class CallMethod {
    private String callerMehtod;
    private String callerClass;
    private int linenum;
    private String calleeMethod;

    public String getCalleeMethod() {
        return calleeMethod;
    }

    public void setCalleeMethod(String calleeMethod) {
        this.calleeMethod = calleeMethod;
    }

    public String getCallerMehtod() {
        return callerMehtod;
    }

    public void setCallerMehtod(String callerMehtod) {
        this.callerMehtod = callerMehtod;
    }

    public String getCallerClass() {
        return callerClass;
    }

    public void setCallerClass(String callerClass) {
        this.callerClass = callerClass;
    }

    public int getLinenum() {
        return linenum;
    }

    public void setLinenum(int linenum) {
        this.linenum = linenum;
    }
}
