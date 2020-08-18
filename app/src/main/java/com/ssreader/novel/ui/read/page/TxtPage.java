package com.ssreader.novel.ui.read.page;

import java.util.List;

public class TxtPage {

    // 几页
    public int position;

    // 标题
    public String title;

    // 标题行数
    public int titleLines;

    // 内容
    public List<String> lines;

    // 0 = 正常；1 = 封面；2 = 全屏作家的话；3 = 广告，
    public int pageStyle = 0;

    public boolean pageAdLord;//4广告页面的下一页（用来缓存广告

    // 当前页是否是作家的话
    public boolean isAuthorPage = false;

    // 广告标识
    public String AdAddTime;

    // 广告标识
    public boolean pageAdOver;

    // 获取内容
    public String getLineTexts() {
        if (lines != null && !lines.isEmpty()) {
            String text = "";
            for (int i = titleLines == -1 ? 0 : titleLines; i < lines.size(); i++) {
                text += lines.get(i);
            }
            return text;
        } else {
            return "";
        }
    }
}
