package com.ssreader.novel.model;

import java.util.List;

/**
 * 用于存储热词数据
 */
public class StoreHotWordBean {

    private List<String> hotWordList;

    public List<String> getHotWordList() {
        return hotWordList;
    }

    public void setHotWordList(List<String> hotWordList) {
        this.hotWordList = hotWordList;
    }
}
