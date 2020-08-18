package com.ssreader.novel.model;

import java.util.List;

public class ShelfAudioBeen {

    public List<Audio> list;
    public List<Announce> announce;
    public List<BaseBookComic> recommend;

    public List<Announce> getAnnouncement() {
        return announce;
    }

    public void setAnnouncement(List<Announce> announcement) {
        this.announce = announcement;
    }

    public List<BaseBookComic> getRecommend_list() {
        return recommend;
    }

    public void setRecommend_list(List<BaseBookComic> recommend_list) {
        this.recommend = recommend_list;
    }

    @Override
    public String toString() {
        return "ShelfComicBeen{" +
                "list=" + list +
                ", announcement=" + announce +
                ", recommend_list=" + recommend +
                '}';
    }
}
