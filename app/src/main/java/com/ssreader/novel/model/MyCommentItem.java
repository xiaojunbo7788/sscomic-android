package com.ssreader.novel.model;

import java.util.List;

public class MyCommentItem extends PublicPage {

    public List<Comment> list;

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }
}
