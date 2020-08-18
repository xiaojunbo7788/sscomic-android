package com.ssreader.novel.ui.push;

public class PushManager {

    /**
     * label : 作品更新提醒
     * push_key : chapter_update
     * status : 1
     */
    private String label;
    private String push_key;
    private int status;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPush_key() {
        return push_key;
    }

    public void setPush_key(String push_key) {
        this.push_key = push_key;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
