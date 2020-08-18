package com.ssreader.novel.eventbus;

/**
 * 用于控制前台通知的显示
 */
public class AudioServiceRefresh {

    private boolean isShow, isCancel;

    public AudioServiceRefresh(boolean isShow, boolean isCancel) {
        this.isShow = isShow;
        this.isCancel = isCancel;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }
}
