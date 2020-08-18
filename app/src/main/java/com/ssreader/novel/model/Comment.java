package com.ssreader.novel.model;

import java.io.Serializable;

/**
 * 作品详情中的Book评论
 */
public class Comment implements Serializable {

    public String comment_id;
    public String avatar;
    public String uid;
    public String time;
    public String nickname;
    public String like_num;
    public String content;
    public String reply_info;
    public String book_id;
    private int is_vip;
    public int comment_num;
    private String name_title;
    private int reply_num;

    public Comment() {

    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id='" + comment_id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", uid='" + uid + '\'' +
                ", time='" + time + '\'' +
                ", nickname='" + nickname + '\'' +
                ", like_num='" + like_num + '\'' +
                ", content='" + content + '\'' +
                ", reply_info='" + reply_info + '\'' +
                ", book_id='" + book_id + '\'' +
                ", is_vip='" + is_vip + '\'' +
                '}';
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply_info() {
        return reply_info;
    }

    public void setReply_info(String reply_info) {
        this.reply_info = reply_info;
    }

    public String getbook_id() {
        return book_id;
    }

    public void setbook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getName_title() {
        return name_title;
    }

    public void setName_title(String name_title) {
        this.name_title = name_title;
    }

    public int getReply_num() {
        return reply_num;
    }

    public void setReply_num(int reply_num) {
        this.reply_num = reply_num;
    }
}
