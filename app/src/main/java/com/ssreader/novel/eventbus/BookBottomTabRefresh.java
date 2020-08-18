package com.ssreader.novel.eventbus;

public class BookBottomTabRefresh {

    private int type;

    private String content;

    private long chapterId;

    public BookBottomTabRefresh(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public BookBottomTabRefresh(int type, String content, long chapterId) {
        this.type = type;
        this.content = content;
        this.chapterId = chapterId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }
}
