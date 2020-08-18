package com.ssreader.novel.model;

import java.util.List;

public class AnswerFeedBackListBean {

    /**
     * content : 这是内容
     * images : ["http://ssreader.oss-cn-beijing.aliyuncs.com/feedback/7712d4840c39de205b4e7423ff5a3aa1.jpg"]
     * reply :
     * created_at : 2019-12-24 16:57:58
     */

    private String content;
    private String reply;
    private String created_at;
    private List<String> images;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
