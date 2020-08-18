package com.ssreader.novel.model;

import java.util.List;

/**
 * Created by  on 2018/10/27.
 */

public class SigninSuccess {

    private String sign_days;//连续签到的天数
    private String award;//获取的奖励
    private String tomorrow_award;//明日签到奖励
    private List<Recommend.RecommendProduc> book;
    private List<Recommend.RecommendProduc> comic;
    private List<Recommend.RecommendProduc> audio;

    public List<Recommend.RecommendProduc> getComic() {
        return comic;
    }

    public void setComic(List<Recommend.RecommendProduc> comic) {
        this.comic = comic;
    }

    public String getSign_days() {
        return sign_days;
    }

    public List<Recommend.RecommendProduc> getBook() {
        return book;
    }

    public void setBook(List<Recommend.RecommendProduc> book) {
        this.book = book;
    }

    public List<Recommend.RecommendProduc> getAudio() {
        return audio;
    }

    public void setAudio(List<Recommend.RecommendProduc> audio) {
        this.audio = audio;
    }

    public void setSign_days(String sign_days) {
        this.sign_days = sign_days;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getTomorrow_award() {
        return tomorrow_award;
    }

    public void setTomorrow_award(String tomorrow_award) {
        this.tomorrow_award = tomorrow_award;
    }
}
