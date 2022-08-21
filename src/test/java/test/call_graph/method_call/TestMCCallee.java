package test.call_graph.method_call;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/8/9
 * @description:
 */
public class TestMCCallee {

    public static final String TEST_STRING = "test_string";

    @Deprecated
    public static void test1(String str1) {
    }

    public void test2(String str1, String str2) {
        int a;
        int b = 3;
        System.out.println(b);
        TestMCCallee.test1("");
    }

    public static void test3(String str1, String str2, String str3) {
    }

    public static String run(int a, String b, BigDecimal c) {
        String s1 = "";
        String s2;
        System.out.println(s1);
        return "";
    }

    public static void test_func_1() {
        test_func_4();
    }

    public static void test_func_2() {
        test_func_1();
    }

    public static void test_func_3() {
        test_func_1();
        test_func_5();
    }

    public static void test_func_4() {
        test_func_3();
    }

    public static void test_func_5() {

        //test_func_4();
    }

    /*public static void test_func_6() {
        test_func_5();
    }*/
}
