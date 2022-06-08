package com.gcf.callgraph.jacg.extensions.find_filter;

/**
 * @author adrninistrator
 * @date 2021/11/5
 * @description:
 */
public class BaseFindKeywordFilter {

    /**
     * 判断通过关键字keyword在方法向上/向下的完整调用链找到的当前行line是否需要显示
     *
     * @param keyword
     * @param line
     * @return true: 显示；false: 不显示
     */
    public boolean filter(String keyword, String line) {
        return true;
    }
}
