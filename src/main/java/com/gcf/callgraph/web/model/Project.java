package com.gcf.callgraph.web.model;

public class Project {
    private String name;

    private String jar_paths;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setJar_paths(String jar_paths) {
        this.jar_paths = jar_paths;
    }

    public String getJar_paths() {
        return jar_paths;
    }
}
