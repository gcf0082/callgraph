package com.gcf.callgraph.jacg.model;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private String fullMethod;
    private List<CallerMethod> callerMethods;
    private List<CalleeMethod> calleeMethods;

    public Method() {

    }

    public Method(String fullMethod) {
        this.fullMethod = fullMethod;
    }

    public String getFullMethod() {
        return fullMethod;
    }

    public void setFullMethod(String fullMethod) {
        this.fullMethod = fullMethod;
    }

    public List<CallerMethod> getCallerMethods() {
        return callerMethods;
    }

    public void setCallerMethods(List<CallerMethod> callerMethods) {
        this.callerMethods = callerMethods;
    }

    public List<CalleeMethod> getCalleeMethods() {
        return calleeMethods;
    }

    public void setCalleeMethods(List<CalleeMethod> calleeMethods) {
        this.calleeMethods = calleeMethods;
    }

    public void addCalleeMethod(Method method, int lineNum) {
        if (calleeMethods == null) {
            calleeMethods = new ArrayList<CalleeMethod>();
        }
        CalleeMethod calleeMethod = new CalleeMethod();
        calleeMethod.setCallee(method);
        calleeMethod.setLineNum(lineNum);
        calleeMethods.add(calleeMethod);
    }

    public void addCallerMethod(Method method, int lineNum) {
        if (callerMethods == null) {
            callerMethods = new ArrayList<CallerMethod>();
        }
        CallerMethod calleeMethod = new CallerMethod();
        calleeMethod.setCaller(method);
        calleeMethod.setLineNum(lineNum);
        callerMethods.add(calleeMethod);
    }
}
