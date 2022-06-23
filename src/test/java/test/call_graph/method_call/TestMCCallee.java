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
    }

    public static void test3(String str1, String str2, String str3) {
    }

    public static String run(int a, String b, BigDecimal c) {
        String s1 = "";
        String s2;
        System.out.println(s1);
        return "";
    }

    public static void test_gcf_1() {
        test_gcf_4();
    }

    public static void test_gcf_2() {
        test_gcf_1();
    }

    public static void test_gcf_3() {
        test_gcf_1();
        test_gcf_5();
    }

    public static void test_gcf_4() {
        test_gcf_3();
    }

    public static void test_gcf_5() {
        test_gcf_4();
    }

    public static void test_gcf_6() {
        test_gcf_5();
    }
}
