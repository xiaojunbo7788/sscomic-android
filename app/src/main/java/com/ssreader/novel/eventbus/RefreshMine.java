package com.ssreader.novel.eventbus;

/**
 * 用于刷新用户状态
 */
public class RefreshMine {

    public boolean isPayVip;
    public int type;   // 1 = 福利

    public RefreshMine() {

    }

    public RefreshMine(int type) {
        this.type = type;
    }

    public RefreshMine(boolean isPayVip) {
        this.isPayVip = isPayVip;
    }
}
