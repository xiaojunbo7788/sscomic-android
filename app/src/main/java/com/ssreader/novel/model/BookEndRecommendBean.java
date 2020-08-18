package com.ssreader.novel.model;

import java.util.List;

public class BookEndRecommendBean {

    private String title, desc;

    private int comment_num;

    private BaseLabelBean guess_like;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public BaseLabelBean getGuess_like() {
        return guess_like;
    }

    public void setGuess_like(BaseLabelBean guess_like) {
        this.guess_like = guess_like;
    }
}
