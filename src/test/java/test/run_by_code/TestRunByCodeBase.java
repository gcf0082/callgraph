package test.run_by_code;

import com.gcf.callgraph.jacg.common.enums.ConfigKeyEnum;
import com.gcf.callgraph.jacg.common.enums.OtherConfigFileUseListEnum;
import com.gcf.callgraph.jacg.common.enums.OtherConfigFileUseSetEnum;
import com.gcf.callgraph.jacg.common.enums.OutputDetailEnum;
import com.gcf.callgraph.jacg.conf.ConfigureWrapper;
import org.junit.Before;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author adrninistrator
 * @date 2022/4/20
 * @description:
 */
public abstract class TestRunByCodeBase {
    @Before
    public void init() {
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_APPNAME, "test_rbc");
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_CALL_GRAPH_JAR_LIST, "build/libs/test.jar");
        //ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_CALL_GRAPH_JAR_LIST, "C:/temp/log4j/log4j-core-2.14.1.jar");
        //ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_CALL_GRAPH_JAR_LIST, "C:/temp/log4j/log4j-api-2.14.1.jar");
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_INPUT_IGNORE_OTHER_PACKAGE, Boolean.TRUE.toString());
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_CALL_GRAPH_OUTPUT_DETAIL, OutputDetailEnum.ODE_2.getDetail());
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_THREAD_NUM, "20");
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_SHOW_METHOD_ANNOTATION, Boolean.TRUE.toString());
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_GEN_COMBINED_OUTPUT, Boolean.TRUE.toString());
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_SHOW_CALLER_LINE_NUM, Boolean.TRUE.toString());
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_IGNORE_DUP_CALLEE_IN_ONE_CALLER, Boolean.FALSE.toString());

        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_DB_USE_H2, Boolean.TRUE.toString());
        ConfigureWrapper.addConfig(ConfigKeyEnum.CKE_DB_H2_FILE_PATH, "./build/jacg_h2db_rbc");

        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_IN_ALLOWED_CLASS_PREFIX, new HashSet(Arrays.asList(
                "test.call_graph.method_call",
                "test.call_graph.argument",
                "test",
                "java.")));
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLEE_CLASS_NAME, new HashSet(Arrays.asList(
                "test.call_graph.method_call.TestMCCallee:test_func_4"
                )));
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_ENTRY_METHOD, new HashSet(Arrays.asList(
                "test.call_graph.method_call.TestMCCallee:test_func_1")));
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_ENTRY_METHOD_IGNORE_PREFIX, new HashSet(Arrays.asList(
                "test4i",
                "test5")));
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_IGNORE_CLASS_KEYWORD, new HashSet(Arrays.asList("TestArgument1")));
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_IGNORE_FULL_METHOD_PREFIX, new HashSet(Arrays.asList(
                "test.call_graph.argument.TestArgument2")));
        ConfigureWrapper.addOtherConfigSet(OtherConfigFileUseSetEnum.OCFUSE_OUT_GRAPH_FOR_CALLER_IGNORE_METHOD_PREFIX, new HashSet(Arrays.asList("test1")));

        ConfigureWrapper.addOtherConfigList(OtherConfigFileUseListEnum.OCFULE_FIND_KEYWORD_4CALLEE, Arrays.asList(
                "!entry!",
                "<init>"));
        ConfigureWrapper.addOtherConfigList(OtherConfigFileUseListEnum.OCFULE_FIND_KEYWORD_4CALLER, Arrays.asList(
                "System",
                "Deprecated"));
    }
}
