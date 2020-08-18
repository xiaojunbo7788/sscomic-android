package com.ssreader.novel.model;

import java.util.List;

public class ShelfComicBeen {

    public List<Comic> list;
    public List<Announce> announcement;
    public List<BaseBookComic> recommend_list;

    public List<Announce> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(List<Announce> announcement) {
        this.announcement = announcement;
    }

    public List<BaseBookComic> getRecommend_list() {
        return recommend_list;
    }

    public void setRecommend_list(List<BaseBookComic> recommend_list) {
        this.recommend_list = recommend_list;
    }

    @Override
    public String toString() {
        return "ShelfComicBeen{" +
                "list=" + list +
                ", announcement=" + announcement +
                ", recommend_list=" + recommend_list +
                '}';
    }
}
