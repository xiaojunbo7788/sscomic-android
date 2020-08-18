package com.ssreader.novel.eventbus;

/**
 * 刷新个人资料
 */
public class RefreshUserInfo {

    public boolean isRefresh;

    public RefreshUserInfo(Boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}
