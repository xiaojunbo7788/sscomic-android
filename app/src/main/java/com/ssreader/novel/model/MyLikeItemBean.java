package com.ssreader.novel.model;

import java.io.Serializable;

public class MyLikeItemBean implements Serializable {

    private String sinici;
    private int from_channel;
    private String author;
    private String original;
    private String icon;
    private String count;

    public String getIcon() {
        if (icon == null) {
            return "no";
        }
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCount() {
        if (count == null) {
            return "0";
        }
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSinici() {
        return sinici;
    }

    public void setSinici(String sinici) {
        this.sinici = sinici;
    }

    public int getFrom_channel() {
        return from_channel;
    }

    public void setFrom_channel(int from_channel) {
        this.from_channel = from_channel;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

}
