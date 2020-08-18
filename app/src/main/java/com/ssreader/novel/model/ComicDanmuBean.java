package com.ssreader.novel.model;

import java.util.List;

/**
 * 弹幕
 */
public class ComicDanmuBean extends PublicPage {

    private List<DanmuBean> list;

    public List<DanmuBean> getList() {
        return list;
    }

    public void setList(List<DanmuBean> list) {
        this.list = list;
    }

    public static class DanmuBean{

        private long uid;

        private long tucao_id;

        private String content;

        private String color;

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public long getTucao_id() {
            return tucao_id;
        }

        public void setTucao_id(long tucao_id) {
            this.tucao_id = tucao_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
