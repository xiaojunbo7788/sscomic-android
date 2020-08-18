package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.BookChapter;

/**
 * 用于在播放界面打开购买弹窗
 */
public class AudioPurchaseRefresh {

    private boolean isSound, isDown;

    private BookChapter bookChapter;

    private AudioChapter audioChapter;

    public AudioPurchaseRefresh(boolean isSound, BookChapter bookChapter, boolean isDown) {
        this.isSound = isSound;
        this.bookChapter = bookChapter;
        this.isDown = isDown;
    }

    public AudioPurchaseRefresh(boolean isSound, AudioChapter audioChapter, boolean isDown) {
        this.isSound = isSound;
        this.audioChapter = audioChapter;
        this.isDown = isDown;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setDown(boolean down) {
        isDown = down;
    }

    public BookChapter getBookChapter() {
        return bookChapter;
    }

    public void setBookChapter(BookChapter bookChapter) {
        this.bookChapter = bookChapter;
    }

    public AudioChapter getAudioChapter() {
        return audioChapter;
    }

    public void setAudioChapter(AudioChapter audioChapter) {
        this.audioChapter = audioChapter;
    }
}
