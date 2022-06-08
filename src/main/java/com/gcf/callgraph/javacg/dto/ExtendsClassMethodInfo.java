package com.gcf.callgraph.javacg.dto;

import com.gcf.callgraph.javacg.dto.MethodAttribute;

import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/6/25
 * @description:
 */

public class ExtendsClassMethodInfo {

    private boolean abstractClass;

    private String superClassName;

    private Map<String, com.gcf.callgraph.javacg.dto.MethodAttribute> methodAttributeMap;

    public boolean isAbstractClass() {
        return abstractClass;
    }

    public void setAbstractClass(boolean abstractClass) {
        this.abstractClass = abstractClass;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }

    public Map<String, com.gcf.callgraph.javacg.dto.MethodAttribute> getMethodAttributeMap() {
        return methodAttributeMap;
    }

    public void setMethodAttributeMap(Map<String, MethodAttribute> methodAttributeMap) {
        this.methodAttributeMap = methodAttributeMap;
    }
}
