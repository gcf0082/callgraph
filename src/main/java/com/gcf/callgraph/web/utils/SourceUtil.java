package com.gcf.callgraph.web.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class SourceUtil {
    public static String getSourcePathByClassName(String projectName, String className) {
        File root = new File("code" + File.separator + projectName);
        String filePath = className.split("\\$")[0].replaceAll("\\.", "/") + ".java";
        try {

            Collection files = FileUtils.listFiles(root, null, true);
            for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                File file = (File) iterator.next();
                if (file.getAbsolutePath().endsWith((new File(filePath)).getPath())) {
                    System.out.println(file.getAbsolutePath());
                    return file.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSourceContentByClassName(String projectName, String className) {

        try {
            String filePath = getSourcePathByClassName(projectName, className);
            File file = new File(filePath);
            String content = FileUtils.readFileToString(file, "UTF-8");
            return content;
        } catch (IOException e) {
            return "####read file exception";
        }
    }
}
