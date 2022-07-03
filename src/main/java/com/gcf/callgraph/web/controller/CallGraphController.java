package com.gcf.callgraph.web.controller;
import com.gcf.callgraph.jacg.common.enums.OtherConfigFileUseSetEnum;
import com.gcf.callgraph.jacg.conf.ConfInfo;
import com.gcf.callgraph.jacg.conf.ConfManager;
import com.gcf.callgraph.jacg.conf.ConfigureWrapper;
import com.gcf.callgraph.jacg.dboper.DbOperator;
import com.gcf.callgraph.jacg.runner.RunnerGenAllGraph4Callee;
import com.gcf.callgraph.jacg.runner.RunnerGenAllGraph4Caller;
import com.gcf.callgraph.jacg.runner.RunnerWriteDb;
import com.gcf.callgraph.web.model.*;
import com.gcf.callgraph.web.utils.HandleResult;
import com.gcf.callgraph.web.utils.SourceUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    //获取某个函数向下调用链
    @RequestMapping("/caller_graph")
    public  String getCallerGraph(@RequestParam("project_name") String project_name, @RequestParam("method") String method){
        System.out.println("caller_graph");
        ConfInfo confInfo = ConfManager.getConfInfo();
        confInfo.setAppName(project_name);
        confInfo.setCallGraphJarList(getProjectByName(project_name).getJar_paths());
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_ENTRY_METHOD, new HashSet<String>(Arrays.asList(
                method
        )));
        RunnerGenAllGraph4Caller runner = new RunnerGenAllGraph4Caller();
        runner.setSupportIgnore(true);
        runner.run();
        return runner.getCallerGraphJson();
    }

    //获取单个函数向上调用链
    @RequestMapping("/callee_graph")
    public String getCalleeGraph(@RequestParam("project_name") String project_name, @RequestParam("method") String method){
        System.out.println("callee_graph");
        ConfInfo confInfo = ConfManager.getConfInfo();
        confInfo.setAppName(project_name);
        confInfo.setCallGraphJarList(getProjectByName(project_name).getJar_paths());
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLEE_CLASS_NAME, new HashSet<String>(Arrays.asList(
                method
        )));
        RunnerGenAllGraph4Callee runner = new RunnerGenAllGraph4Callee();
        runner.run();
        System.out.println(runner.getCalleeGraphJson());
        return runner.getCalleeGraphJson();
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
    public  @ResponseBody List<Project> getProjects() {
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

    @RequestMapping("/source_file")
    public SourceFile getSourceFile(@RequestParam("project_name") String projectName,
                                    @RequestParam("class") String className,
                                    @RequestParam("linenum") int lineNumber) {
        String content = SourceUtil.getSourceContentByClassName(projectName, className);
        SourceFile sourceFile = new SourceFile();
        sourceFile.setFile_content(content);
        sourceFile.setLineNum(lineNumber);
        return sourceFile;
    }

    //
    @RequestMapping(value = "/callees_method", produces = MediaType.APPLICATION_JSON_VALUE)
    public  List<String> getAllCallees(@RequestParam("project_name") String project_name) {
        String sql = String.format("SELECT DISTINCT callee_full_method" +
                " from method_call_%s where callee_full_class_name not in" +
                "(SELECT full_name from handled_class_name_%s) order by callee_full_method", project_name, project_name);
        DbOperator.getInstance().init(ConfManager.getConfInfo());
        List<Map<String, Object>> list = DbOperator.getInstance().queryList(sql, null);
        Set<String> ignoreFullMethodPrefixSet = ConfigureWrapper.getOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_IGNORE_FULL_METHOD_PREFIX);
        List<String> callees = new ArrayList<String>();
        for(Map<String, Object> map : list) {
            String calleeMethod = (String)map.get("callee_full_method");


            boolean found = false;
            for (String ignoreMehod : ignoreFullMethodPrefixSet) {
                if (calleeMethod.startsWith(ignoreMehod)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            callees.add(calleeMethod);
        }
        DbOperator.getInstance().closeDs();
        return callees;
    }


    //获取所有的调用外部函数列表
    @RequestMapping("/callee")
    public List<CallMethod> getCallee(@RequestParam("project_name") String project_name) {
        String sql = String.format("SELECT caller_full_method,caller_full_class_name, callee_full_method,caller_line_num" +
                " from method_call_%s where callee_full_class_name not in" +
                "(SELECT full_name from handled_class_name_%s) order by callee_full_method", project_name, project_name);
        DbOperator.getInstance().init(ConfManager.getConfInfo());
        List<Map<String, Object>> list = DbOperator.getInstance().queryList(sql, null);
        Set<String> ignoreFullMethodPrefixSet = ConfigureWrapper.getOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_IGNORE_FULL_METHOD_PREFIX);
        List<CallMethod> callees = new ArrayList<CallMethod>();
        for(Map<String, Object> map : list) {
            String callerMethod = (String)map.get("caller_full_method");
            String callerClass = (String)map.get("caller_full_class_name");
            String calleeMethod = (String)map.get("callee_full_method");
            int lineNum = (Integer) map.get("caller_line_num");

            boolean found = false;
            for (String ignoreMehod : ignoreFullMethodPrefixSet) {
                if (calleeMethod.startsWith(ignoreMehod)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }

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