package com.ssreader.novel.eventbus;

/**
 * 用于刷新进度
 */
public class AudioProgressRefresh {

    // 播放状态
    public boolean isPlayer, isSound;
    // 最大值
    public int duration;
    // 当前进度
    public int progress;

    public AudioProgressRefresh(boolean isPlayer, boolean isSound, int duration, int progress) {
        this.isPlayer = isPlayer;
        this.isSound = isSound;
        this.duration = duration;
        this.progress = progress;
    }
}
