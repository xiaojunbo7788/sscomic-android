package com.ssreader.novel.model;

/**
 * Created by  on 2018/7/22.
 */
public class PayGoldDetail {

    /**
     * article : 购买章节
     * detail : -50元宝
     * date : 10月28日 15:08
     * detail_type : 2
     *
     * [{"article":"签到获取","detail":"+5书券","date":"06月19日 12:28","detail_type":1
     */
    public int log_id;
    public String title;
    public String desc;
    public String time;
    private String article;
    private String detail;
    private String date;
    private int detail_type;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDetail_type() {
        return detail_type;
    }

    public void setDetail_type(int detail_type) {
        this.detail_type = detail_type;
    }
}
