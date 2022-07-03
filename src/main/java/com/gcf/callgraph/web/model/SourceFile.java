package com.gcf.callgraph.web.model;

public class SourceFile {
    int lineNum;

    public String getFile_content() {
        return file_content;
    }

    public void setFile_content(String file_content) {
        this.file_content = file_content;
    }

    String file_content;

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
