package com.ssreader.novel.model;

public class BaoyueUser {

    public String nickname;//": "小b", //昵称
    public String avatar;//": "http://img3.duitang.com/uploads/item/201505/26/20150526002859_c2yKG.thumb.700_0.jpeg", //头像
    public int baoyue_status;//": 1, //包月状态 0未开通包月 1已开通包月
    public String start_time;//": 1528381654, //包月有效期开始时间
    public String end_time;//": 1528481654, //包月有效期结束时间
    public String expiry_date;//": "2018年3月10~2018年5月20" //包月有效期
    public String vip_desc;

    @Override
    public String toString() {
        return "BaoyueUser{" +
                "nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", baoyue_status=" + baoyue_status +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", expiry_date='" + expiry_date + '\'' +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBaoyue_status() {
        return baoyue_status;
    }

    public void setBaoyue_status(int baoyue_status) {
        this.baoyue_status = baoyue_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }
}
