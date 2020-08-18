package com.ssreader.novel.model;

import java.util.List;

public class BookComicStoare {

    private List<PublicIntent> banner;
    private List<BaseLabelBean> label;
    private List<String> hot_word;
    private List<BannerBottomItem> menus_tabs;

    public List<PublicIntent> getBanner() {
        return banner;
    }

    public void setBanner(List<PublicIntent> banner) {
        this.banner = banner;
    }

    public List<BaseLabelBean> getLabel() {
        return label;
    }

    public void setLabel(List<BaseLabelBean> label) {
        this.label = label;
    }

    public List<String> getHot_word() {
        return hot_word;
    }

    public void setHot_word(List<String> hot_word) {
        this.hot_word = hot_word;
    }

    public List<BannerBottomItem> getMenus_tabs() {
        return menus_tabs;
    }

    public void setMenus_tabs(List<BannerBottomItem> menus_tabs) {
        this.menus_tabs = menus_tabs;
    }
}
