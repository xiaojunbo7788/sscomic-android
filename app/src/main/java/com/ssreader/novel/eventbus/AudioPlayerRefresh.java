package com.ssreader.novel.eventbus;

/**
 * 用于更改播放或暂停的状态
 */
public class AudioPlayerRefresh {

    private boolean isSound;

    private boolean isPause;

    public AudioPlayerRefresh(boolean isSound, boolean isPause) {
        this.isSound = isSound;
        this.isPause = isPause;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
