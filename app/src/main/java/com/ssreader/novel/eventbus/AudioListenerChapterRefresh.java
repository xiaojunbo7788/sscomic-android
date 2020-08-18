package com.ssreader.novel.eventbus;

/**
 * 用于监听用户当前播放的章节
 */
public class AudioListenerChapterRefresh {

    private boolean isSound;

    private long audioId;

    private long chapterId;

    private String chapterName;

    public AudioListenerChapterRefresh(boolean isSound, long audioId, long chapterId, String chapterName) {
        this.isSound = isSound;
        this.audioId = audioId;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public long getAudioId() {
        return audioId;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
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
