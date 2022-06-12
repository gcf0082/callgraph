package com.gcf.callgraph;

import com.gcf.callgraph.jacg.runner.RunnerInitWebApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CallgraphApplication {

	public static void init(){
		new RunnerInitWebApp().run();
	}

	public static void main(String[] args) {
		init();
		SpringApplication.run(CallgraphApplication.class, args);
	}

}
