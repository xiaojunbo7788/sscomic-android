package com.ssreader.novel.eventbus;

public class CommentRefresh {

    public long id;
    public int productType;

    public CommentRefresh(int productType, long id) {
        this.id = id;
        this.productType = productType;
    }
}
