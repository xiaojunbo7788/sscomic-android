package com.ssreader.novel.eventbus;

/**
 * 用于记录用户切换了播放模式后
 * 选择的章节信息
 */
public class AudioSwitchRefresh {

    private boolean isSound;

    private long id, chapterId;

    private String chapterName;

    public AudioSwitchRefresh(boolean isSound, long id, long chapterId, String chapterName) {
        this.isSound = isSound;
        this.id = id;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}
