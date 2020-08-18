package com.ssreader.novel.model;

import java.util.List;

public class AnswerFeedBackBean extends PublicPage {

    public List<AnswerFeedBackListBean> list;

    public List<AnswerFeedBackListBean> getList() {
        return list;
    }

    public void setList(List<AnswerFeedBackListBean> list) {
        this.list = list;
    }
}
