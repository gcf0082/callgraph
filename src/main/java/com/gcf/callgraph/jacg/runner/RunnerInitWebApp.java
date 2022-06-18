package com.gcf.callgraph.jacg.runner;

import com.gcf.callgraph.jacg.common.JACGConstants;
import com.gcf.callgraph.jacg.runner.base.AbstractRunner;
import com.gcf.callgraph.jacg.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RunnerInitWebApp extends AbstractRunner {
    private static final Logger logger = LoggerFactory.getLogger(RunnerInitWebApp.class);
    @Override
    public boolean preCheck() {
        if (confInfo.isDbUseH2() && !checkH2DbFile()) {
            // 使用H2数据库时，检查H2数据库文件
            return false;
        }

        return true;
    }

    private boolean checkH2DbFile() {
        File h2DbFile = getH2DbFile();
        if (!h2DbFile.exists()) {
            return true;
        }

        // 数据库文件存在
        if (!h2DbFile.isFile()) {
            logger.error("H2数据库文件不是文件 {}", FileUtil.getCanonicalPath(h2DbFile));
            return false;
        }

        // 检查H2数据库文件是否可写
        return checkH2DbFileWritable(h2DbFile);
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void operate() {
        createTables();
    }

    private boolean createTables() {
        String projectSql = readCreateTableSql(JACGConstants.DIR_SQL + File.separator + "project.sql");
        return dbOperator.createTable(projectSql);
    }

    private String readCreateTableSql(String sqlFilePath) {
        String sql = FileUtil.readFile2String(sqlFilePath);
        if (StringUtils.isBlank(sql)) {
            logger.error("文件内容为空 {}", sqlFilePath);
            return null;
        }

        if (confInfo.isDbUseH2()) {
            // 使用H2数据库时，对建表的SQL语句进行处理
            sql = sql.replace("ENGINE=InnoDB", "")
                    .replace("COLLATE=utf8_bin", "")
                    .replace(" text ", " varchar(3000) ");
        }

        logger.info("建表sql: {}", sql);
        return sql;
    }
}
