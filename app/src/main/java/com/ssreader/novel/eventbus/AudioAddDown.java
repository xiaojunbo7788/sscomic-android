package com.ssreader.novel.eventbus;

public class AudioAddDown {

    public int status;    //0 添加下载队列，1，下载完成
    public long chapter_id;//章节ID
    public String path;
    public boolean isFromDownActivity;

    public AudioAddDown(boolean isFromDownActivity,int status) {
        this.status = status;
        this.isFromDownActivity = isFromDownActivity;
    }

    public AudioAddDown(int status, long chapter_id, String path) {
        this.status = status;
        this.chapter_id = chapter_id;
        this.path = path;
    }
}
