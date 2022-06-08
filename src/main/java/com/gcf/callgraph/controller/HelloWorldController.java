package com.gcf.callgraph.controller;
import com.gcf.callgraph.jacg.runner.RunnerWriteDb;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String index() {
        new RunnerWriteDb().run();
        return "Hello World";
    }
}