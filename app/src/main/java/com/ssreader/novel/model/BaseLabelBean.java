package com.ssreader.novel.model;

import java.util.List;

public class BaseLabelBean extends  BaseAd {

    private int recommend_id;
    private String label;
    private int style;
    private boolean can_more;
    private boolean can_refresh;
    private String total;
    public List<BaseBookComic> list;
    private List<BaseTag>tag;
    private int expire_time;

    public int getRecommend_id() {
        return recommend_id;
    }

    public void setRecommend_id(int recommend_id) {
        this.recommend_id = recommend_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isCan_more() {
        return can_more;
    }

    public void setCan_more(boolean can_more) {
        this.can_more = can_more;
    }

    public boolean isCan_refresh() {
        return can_refresh;
    }

    public void setCan_refresh(boolean can_refresh) {
        this.can_refresh = can_refresh;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<BaseBookComic> getList() {
        return list;
    }

    public void setList(List<BaseBookComic> list) {
        this.list = list;
    }

    public List<BaseTag> getTag() {
        return tag;
    }

    public void setTag(List<BaseTag> tag) {
        this.tag = tag;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }
}
