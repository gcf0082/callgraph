package com.gcf.callgraph.jacg.conf;

import com.gcf.callgraph.jacg.common.JACGConstants;
import com.gcf.callgraph.jacg.common.enums.ConfigKeyEnum;
import com.gcf.callgraph.jacg.common.enums.OutputDetailEnum;
import com.gcf.callgraph.jacg.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author adrninistrator
 * @date 2021/6/17
 * @description:
 */

public class ConfManager {

    public static final Logger logger = LoggerFactory.getLogger(ConfManager.class);

    private static ConfInfo CONF_INFO = new ConfInfo();

    private static final Pattern APP_NAME_PATTERN = Pattern.compile("[A-Za-z0-9_]*");

    private static boolean inited = false;

    public static boolean isInited() {
        return inited;
    }

    public static ConfInfo getConfInfo() {
        if (inited) {
            return CONF_INFO;
        }

        inited = true;

        String configFilePath = JACGConstants.DIR_CONFIG + File.separator + JACGConstants.FILE_CONFIG;
        try (Reader reader = new InputStreamReader(new FileInputStream(FileUtil.findFile(configFilePath)), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            String appName = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_APPNAME);
            if (checkBlank(appName, ConfigKeyEnum.CKE_APPNAME, configFilePath) || !checkAppName(appName)) {
                return null;
            }

            String callGraphJarList = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_CALL_GRAPH_JAR_LIST);
            if (checkBlank(callGraphJarList, ConfigKeyEnum.CKE_CALL_GRAPH_JAR_LIST, configFilePath)) {
                return null;
            }

            String inputIgnoreOtherPackage = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_INPUT_IGNORE_OTHER_PACKAGE);
            if (checkBlank(inputIgnoreOtherPackage, ConfigKeyEnum.CKE_INPUT_IGNORE_OTHER_PACKAGE, configFilePath)) {
                return null;
            }

            String genCombinedOutput = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_GEN_COMBINED_OUTPUT);
            if (checkBlank(genCombinedOutput, ConfigKeyEnum.CKE_GEN_COMBINED_OUTPUT, configFilePath)) {
                return null;
            }

            // ??????????????????????????????????????????
            String showCallerLineNum = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_SHOW_CALLER_LINE_NUM);
            if (checkBlank(showCallerLineNum, ConfigKeyEnum.CKE_SHOW_CALLER_LINE_NUM, configFilePath)) {
                return null;
            }

            // ??????????????????????????????????????????????????????????????????????????????????????????????????????
            String ignoreDupCalleeInOneCaller = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_IGNORE_DUP_CALLEE_IN_ONE_CALLER);
            if (StringUtils.isBlank(ignoreDupCalleeInOneCaller)) {
                // ????????????????????????
                ignoreDupCalleeInOneCaller = String.valueOf(false);
            }

            // ?????????????????????????????????
            String callGraphOutputDetail = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_CALL_GRAPH_OUTPUT_DETAIL);
            if (checkBlank(callGraphOutputDetail, ConfigKeyEnum.CKE_CALL_GRAPH_OUTPUT_DETAIL, configFilePath)) {
                return null;
            }

            OutputDetailEnum outputDetailEnum = OutputDetailEnum.getFromDetail(callGraphOutputDetail);
            if (OutputDetailEnum.ODE_ILLEGAL == outputDetailEnum) {
                logger.error("???????????????????????????????????? {}", ConfigKeyEnum.CKE_CALL_GRAPH_OUTPUT_DETAIL);
                for (OutputDetailEnum outputDetailEnum1 : OutputDetailEnum.values()) {
                    logger.info("{}", outputDetailEnum1.getDetail());
                }
                return null;
            }

            String strThreadNum = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_THREAD_NUM);
            if (checkBlank(strThreadNum, ConfigKeyEnum.CKE_THREAD_NUM, configFilePath)) {
                return null;
            }
            int threadNum = handleThreadNum(strThreadNum);
            if (threadNum == 0) {
                return null;
            }

            String showMethodAnnotation = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_SHOW_METHOD_ANNOTATION);
            if (checkBlank(showMethodAnnotation, ConfigKeyEnum.CKE_SHOW_METHOD_ANNOTATION, configFilePath)) {
                return null;
            }

            String strDbUseH2 = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_DB_USE_H2);
            if (checkBlank(strDbUseH2, ConfigKeyEnum.CKE_DB_USE_H2, configFilePath)) {
                return null;
            }

            CONF_INFO.setDbUseH2(Boolean.parseBoolean(strDbUseH2));
            if (CONF_INFO.isDbUseH2()) {
                logger.info("??????H2?????????");
                if (!handleH2Db(properties, configFilePath)) {
                    return null;
                }
            } else {
                logger.info("?????????H2?????????");
                if (!handleNonH2Db(properties, configFilePath)) {
                    return null;
                }
            }

            CONF_INFO.setAppName(appName);
            CONF_INFO.setCallGraphJarList(callGraphJarList);
            CONF_INFO.setInputIgnoreOtherPackage(Boolean.parseBoolean(inputIgnoreOtherPackage));
            CONF_INFO.setCallGraphOutputDetail(callGraphOutputDetail);
            CONF_INFO.setThreadNum(threadNum);
            CONF_INFO.setOriginalThreadNum(threadNum);
            CONF_INFO.setShowMethodAnnotation(Boolean.parseBoolean(showMethodAnnotation));
            CONF_INFO.setGenCombinedOutput(Boolean.parseBoolean(genCombinedOutput));
            CONF_INFO.setShowCallerLineNum(Boolean.parseBoolean(showCallerLineNum));
            CONF_INFO.setIgnoreDupCalleeInOneCaller(Boolean.parseBoolean(ignoreDupCalleeInOneCaller));
            if (System.getProperty(JACGConstants.PROPERTY_WRITE_CONFIG_IN_RESULT) != null) {
                CONF_INFO.setWriteConf(true);
            }

            return CONF_INFO;
        } catch (Exception e) {
            logger.error("error: ", e);
            return null;
        }
    }

    private static boolean checkAppName(String appName) {
        if (!APP_NAME_PATTERN.matcher(appName).matches()) {
            logger.error("{} ?????????????????????????????????????????? {}", ConfigKeyEnum.CKE_APPNAME, appName);
            return false;
        }
        return true;
    }

    // ????????????????????????0????????????
    private static int handleThreadNum(String strThreadNum) {
        int threadNum;
        try {
            threadNum = Integer.parseInt(strThreadNum);
        } catch (NumberFormatException e) {
            logger.error("??????????????? {} {}", ConfigKeyEnum.CKE_THREAD_NUM, strThreadNum);
            return 0;
        }

        if (threadNum <= 0) {
            logger.error("??????????????? {} {}", ConfigKeyEnum.CKE_THREAD_NUM, strThreadNum);
            return 0;
        }
        if (threadNum > JACGConstants.MAX_THREAD_NUM) {
            logger.error("??????????????? {} {}", ConfigKeyEnum.CKE_THREAD_NUM, strThreadNum);
            return 0;
        }

        return threadNum;
    }

    private static boolean checkBlank(String value, ConfigKeyEnum configKeyEnum, String configFilePath) {
        String key = configKeyEnum.getKey();
        if (StringUtils.isBlank(value)) {
            logger.error("?????????????????????????????? {} {}", configFilePath, key);
            return true;
        }

        logger.info("????????????????????? [{}] [{}]", key, value);

        return false;
    }

    private static boolean handleH2Db(Properties properties, String configFilePath) {
        String dbH2FilePath = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_DB_H2_FILE_PATH);
        if (checkBlank(dbH2FilePath, ConfigKeyEnum.CKE_DB_H2_FILE_PATH, configFilePath)) {
            return false;
        }

        if (StringUtils.endsWithIgnoreCase(dbH2FilePath, JACGConstants.H2_FILE_EXT)) {
            logger.error("{} ?????????????????????H2?????????????????? {} {}", ConfigKeyEnum.CKE_DB_H2_FILE_PATH, JACGConstants.H2_FILE_EXT, dbH2FilePath);
            return false;
        }

        CONF_INFO.setDbH2FilePath(dbH2FilePath);

        return true;
    }

    private static boolean handleNonH2Db(Properties properties, String configFilePath) {
        String dbDriverName = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_DB_DRIVER_NAME);
        if (checkBlank(dbDriverName, ConfigKeyEnum.CKE_DB_DRIVER_NAME, configFilePath)) {
            return false;
        }

        String dbUrl = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_DB_URL);
        if (checkBlank(dbUrl, ConfigKeyEnum.CKE_DB_URL, configFilePath)) {
            return false;
        }

        String dbUsername = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_DB_USERNAME);
        if (checkBlank(dbUsername, ConfigKeyEnum.CKE_DB_USERNAME, configFilePath)) {
            return false;
        }

        String dbPassword = ConfigureWrapper.getConfig(properties, ConfigKeyEnum.CKE_DB_PASSWORD);
        if (checkBlank(dbPassword, ConfigKeyEnum.CKE_DB_PASSWORD, configFilePath)) {
            return false;
        }

        CONF_INFO.setDbDriverName(dbDriverName);
        CONF_INFO.setDbUrl(dbUrl);
        CONF_INFO.setDbUsername(dbUsername);
        CONF_INFO.setDbPassword(dbPassword);

        return true;
    }

    private ConfManager() {
        throw new IllegalStateException("illegal");
    }
}
