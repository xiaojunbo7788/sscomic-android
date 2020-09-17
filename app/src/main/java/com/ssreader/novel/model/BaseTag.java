package com.ssreader.novel.model;

import java.io.Serializable;

public class BaseTag implements Serializable {

    /**
     * tab : 现代爱情
     * color : #71c5fb
     */

    private String tab;
    private String color;
    private String title;
    private String id;
    private boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseTag{" +
                "tab='" + tab + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
