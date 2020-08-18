package com.ssreader.novel.model;

public class Announce {

    /**
     * title : 每日更新好书，等你来读
     * content : 每日更新好书，等你来读。
     */
    private String title;
    private String content;

    public Announce(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Announce{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
