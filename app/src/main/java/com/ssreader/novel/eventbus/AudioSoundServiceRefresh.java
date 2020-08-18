package com.ssreader.novel.eventbus;

/**
 * 用于向有声服务传递数据
 */
public class AudioSoundServiceRefresh {

    private long chapterId;

    private String path;

    public AudioSoundServiceRefresh(long chapterId, String path) {
        this.chapterId = chapterId;
        this.path = path;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
