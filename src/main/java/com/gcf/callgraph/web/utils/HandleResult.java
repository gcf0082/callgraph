package com.gcf.callgraph.web.utils;

import com.gcf.callgraph.web.model.Result;

public class HandleResult {
    public static Result buildOk() {
        Result result = new Result();
        result.setStatus(0);
        result.setMsg("success");
        return result;
    }

    public static Result buildFailed() {
        Result result = new Result();
        result.setStatus(1);
        result.setMsg("failed");
        return result;
    }
}
