package com.ssreader.novel.model;

import java.util.List;

public class FeedBackAnswerNameBean {
    public String name;
    public List<FeedBackAnswerListBean> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FeedBackAnswerListBean> getList() {
        return list;
    }

    public void setList(List<FeedBackAnswerListBean> list) {
        this.list = list;
    }
}
