package com.ssreader.novel.model;

import java.util.List;

public class DiscoverComicData {

    public List<PublicIntent> banner;
    public DiscoverComicBean item_list;

    public List<PublicIntent> getBanner() {
        return banner;
    }

    public void setBanner(List<PublicIntent> banner) {
        this.banner = banner;
    }

    public DiscoverComicBean getItem_list() {
        return item_list;
    }

    public void setItem_list(DiscoverComicBean item_list) {
        this.item_list = item_list;
    }
}
