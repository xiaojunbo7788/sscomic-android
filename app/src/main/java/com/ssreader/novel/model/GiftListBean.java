package com.ssreader.novel.model;

public class GiftListBean {
    public int gift_id;
    public String title;
    public String icon;
    public String gift_price;
    public String flag;
    public boolean chose;
    public int getGift_id() {
        return gift_id;
    }

    public void setGift_id(int gift_id) {
        this.gift_id = gift_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAward_money() {
        return gift_price;
    }

    public void setAward_money(String award_money) {
        this.gift_price = award_money;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
