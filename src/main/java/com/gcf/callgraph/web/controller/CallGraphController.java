package com.gcf.callgraph.web.controller;
import com.gcf.callgraph.jacg.conf.ConfInfo;
import com.gcf.callgraph.jacg.conf.ConfManager;
import com.gcf.callgraph.jacg.dboper.DbOperator;
import com.gcf.callgraph.jacg.runner.RunnerWriteDb;
import com.gcf.callgraph.web.model.CallMethod;
import com.gcf.callgraph.web.model.Project;
import com.gcf.callgraph.web.model.Result;
import com.gcf.callgraph.web.utils.HandleResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class CallGraphController {

    @RequestMapping("/test")
    public  List<String> test() {

        return Arrays.asList("xx", "yyy");
    }

    @PostMapping("/project")
    public Result createProject(@RequestBody Project project) {
        System.out.println(project.getName());
        DbOperator.getInstance().init(ConfManager.getConfInfo());
        String sql = "insert into project(name, jar_paths) values (?, ?)";
        List<Object[]> objectList = new ArrayList<>(1);
        Object[] object = new Object[]{project.getName(), project.getJar_paths()};
        objectList.add(object);
        if (DbOperator.getInstance().batchInsert(sql, objectList)) {
            Project proj = getProjectByName(project.getName());
            System.out.println(proj.getJar_paths());
            return HandleResult.buildOk();
        } else {
            return HandleResult.buildFailed();
        }
    }

    @PostMapping("/project/analysis")
    public Result anlysisProject(@RequestBody Project project) {
        ConfInfo confInfo = ConfManager.getConfInfo();
        confInfo.setAppName(project.getName());
        confInfo.setCallGraphJarList(getProjectByName(project.getName()).getJar_paths());
        new RunnerWriteDb().run();
        return HandleResult.buildOk();
    }

    @RequestMapping("/projects")
    public  List<Project> getProjects() {
        DbOperator.getInstance().init(ConfManager.getConfInfo());
        String sql = "select name, jar_paths from project";
        List<Map<String, Object>> list = DbOperator.getInstance().queryList(sql, null);
        List<Project> projects = new ArrayList<Project>();
        for(Map<String, Object> map : list) {
            String name = (String)map.get("name");
            String jar_paths = (String)map.get("jar_paths");
            Project project = new Project();
            project.setName(name);
            project.setJar_paths(jar_paths);
            projects.add(project);
        }
        DbOperator.getInstance().closeDs();
        return projects;
    }

    @RequestMapping("/callee")
    public List<CallMethod> getCallee(@RequestParam("project_name") String project_name) {
        String sql = String.format("SELECT caller_full_method,caller_full_class_name, callee_full_method,caller_line_num" +
                " from method_call_%s where callee_full_class_name not in" +
                "(SELECT full_name from handled_class_name_%s) order by callee_full_method", project_name, project_name);
        DbOperator.getInstance().init(ConfManager.getConfInfo());
        List<Map<String, Object>> list = DbOperator.getInstance().queryList(sql, null);
        List<CallMethod> callees = new ArrayList<CallMethod>();
        for(Map<String, Object> map : list) {
            String callerMethod = (String)map.get("caller_full_method");
            String callerClass = (String)map.get("caller_full_class_name");
            String calleeMethod = (String)map.get("callee_full_method");
            int lineNum = (Integer) map.get("caller_line_num");

            CallMethod callMehod = new CallMethod();
            callMehod.setCalleeMethod(calleeMethod);
            callMehod.setCallerClass(callerClass);
            callMehod.setCallerMehtod(callerMethod);
            callMehod.setLinenum(lineNum);
            callees.add(callMehod);
        }
        DbOperator.getInstance().closeDs();
        return callees;
    }

    @RequestMapping("/caller")
    public String getCaller() {
        return "";
    }

    private Project getProjectByName(String name) {
        DbOperator.getInstance().init(ConfManager.getConfInfo());
        String sql = "select name, jar_paths from project where name = '" + name + "'";
        Map<String, Object> map = DbOperator.getInstance().queryOneRow(sql, null);
        String project_name = (String)map.get("name");
        String project_jar_paths = (String)map.get("jar_paths");
        Project project = new Project();
        project.setName(project_name);
        project.setJar_paths(project_jar_paths);
        DbOperator.getInstance().closeDs();
        return project;
    }

}